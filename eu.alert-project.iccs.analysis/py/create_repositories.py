#!/usr/bin/python
import argparse
import shutil
import os
import sys
import subprocess
from config import preferences

repos={
    "kdelibs":{
        "url":"git://anongit.kde.org/kdelibs",
        "dirs":{
            "solid":"solid"
                    }
            },
    "kde-workspace":{
        "url":"git://anongit.kde.org/kde-workspace",
        "dirs":{
            "solid":"solid",
            "powerdevil":"powerdevil",
            "lib-solid":"libs/solid",
            "plasma-generic-dataengines-powermanagement": "plasma/generic/dataengines/powermanagement",
            "plasma-generic-dataengines-soliddevice": "plasma/generic/dataengines/soliddevice",
            "plasma-generic-dataengines-hotplug": "plasma/generic/dataengines/hotplug",
            "kcontrol-hardware": "kcontrol/hardware",
                    }
            },
    "kde-runtime":{
        "url":"git://anongit.kde.org/kde-runtime",
        "dirs":{
            "solid-device-automounter":  "solid-device-automounter",
            "solid-hardware":            "solid-hardware",
            "solid-networkstatus":       "solid-networkstatus",
            "solidautoeject":            "solidautoeject",
            "soliduiserver":             "soliduiserver",
                    }
            },
    "bluedevil":{
        "url":"git://anongit.kde.org/bluedevil",
        "dirs":None
            },
    "networkmanagement":{
        "url":"git://anongit.kde.org/networkmanagement",
        "dirs":None
            }
    }


if __name__ == '__main__':

    parser = argparse.ArgumentParser(description='Clone the directories and filter directories')

    parser.add_argument('--repos', action="store", dest="repos", nargs=1)
    parser.add_argument('--skip', action="store", dest="skip")

    options = parser.parse_args()

    try:
        root_dir = os.path.abspath(options.repos[0])
    except:
        parser.print_help()
        sys.exit(1)    

    original = root_dir+"/originals"
    intermediate = root_dir+"/intermediate"
    destination = root_dir+"/dest"

    if not options.skip:

        #create originals if it doesn't exist
        if not os.path.exists(original):
            os.mkdir(original)

        #remove intermediate if it exists
        if os.path.exists(intermediate):
            shutil.rmtree(intermediate)        

        #remove dest if it exists
        if os.path.exists(destination):
            shutil.rmtree(destination)

        os.mkdir(intermediate)
        os.mkdir(destination)

        print "Cloning repos into "+original

        for repo,items in repos.iteritems():

            repo_dir = original+"/"+repo+"/"

            print "Fetching or updating %s " % repo
            if os.path.exists(repo_dir):
                subprocess.call(['git','pull'],cwd=repo_dir)
            else:
                subprocess.call(['git','clone',items['url']],cwd=original)

            if items['dirs'] is not None:
                for branch_name, directory in items['dirs'].iteritems():

                    branch_dir = intermediate+"/"+repo+"-"+branch_name
                    print "Creating branch directory %s " % branch_dir
                    
                    #final_dir = destination+"/"+repo+"-"+branch_name
                    #print "Creating final directory %s " % final_dir                    
                    
                    if os.path.exists(branch_dir):
                        print "Branch directory exists so we are deleting it '%s'" % branch_dir
                        shutil.rmtree(branch_dir)

                    print "Copying '%s' into '%s'" % (repo_dir, branch_dir)
                    subprocess.call(['cp',"-r",repo_dir, branch_dir],cwd=root_dir)
                    #shutil.copytree(repo_dir,branch_dir)
                    
                    #git checkout -b "nameofthebranch"
                    print "%s: git checkout -b %s" % (branch_dir, branch_name)
                    subprocess.call(['git','checkout',"-b",branch_name],cwd=branch_dir)
                    
                    #git reset --hard master
                    print "%s: git reset --hard --master" % branch_dir                    
                    subprocess.call(['git','reset',"--hard","master"],cwd=branch_dir)
                    
                    #git filter-branch --subdirectory-filter
                    print "%s: git filter-branch --subdirectory-filter %s " % (branch_dir, directory)
                    subprocess.call(['git','filter-branch',"--subdirectory-filter",directory],cwd=branch_dir)

                    #delete master
                    print "%s: git branch -d master" % branch_dir
                    subprocess.call(['git','branch','-d','master'],cwd=branch_dir)

                    #git mv -b whatever solid
                    print "%s: git branch -m %s master" % (branch_dir,branch_name)
                    subprocess.call(['git','branch','-m',branch_name,'master'],cwd=branch_dir)

                    #git clone
                    print "%s: git clone %s " % (destination,branch_dir)
                    subprocess.call(['git','clone',branch_dir],cwd=destination)

            else:
                branch_dir = destination+"/"+repo
                shutil.copytree(repo_dir,branch_dir)

    else:
        print "Skiped repository creation"

            
    #reset the database
    subprocess.call([
        "mysqladmin",
        "-u",preferences.cvsanaly_db_user,
        "-p"+preferences.cvsanaly_db_password,
        "--force","drop",
        preferences.cvsanaly_db_name        
    ])

    subprocess.call([
        "mysqladmin",
        "-u",preferences.cvsanaly_db_user,
        "-p"+preferences.cvsanaly_db_password,
        "create",
        preferences.cvsanaly_db_name
        ])
    
    dest_repos = os.listdir(destination)
    
    for r in dest_repos:
        cvsanaly_repo =destination+"/"+r
        cvsanaly_log =root_dir+"/cvsanaly_log/"
        
        print "Run cvsanaly for %s " % cvsanaly_repo
        
        #preferences.cvsanaly_bin -u alert -p 1234 -d cvsanaly_kde_solid -H localhost --metrics-all --profile --debug --extensions=Metrics -s /tmp/cvsanaly_kde_solid /home/fotis/repos/dest/bluedevil
        subprocess.call([preferences.cvsanaly_bin,
                         "-u",preferences.cvsanaly_db_user,
                         "-p",preferences.cvsanaly_db_password,
                         "-d",preferences.cvsanaly_db_name,
                         "-H",preferences.cvsanaly_db_host,
                         "--metrics-all",
                         "--profile",
                         "--extensions=Metrics",
                         cvsanaly_repo
                         ])
        


