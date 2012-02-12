import os
import shutil
import subprocess
import sys

repos={
    "kdelibs":{
        "url":"git://anongit.kde.org/kdelibs",
        "dirs":{
            "solid":"Solid"
        }
    },
    "kde-workspace":{
        "url":"git://anongit.kde.org/kde-workspace",
        "dirs":{
            "solid":"Solid",
            "powerdevil":"powerdevil",
            "lib-solid":"lib/solid",
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

    if len(sys.argv) != 2:
        print """
            You need to specify a file as an argument
        """
        exit(1)

    root_dir = os.path.abspath(sys.argv[1])

    original = root_dir+"/originals"
    destination = root_dir+"/dest"

    #create originals if it doesn't exist
    if not os.path.exists(original):
        os.mkdir(original)

    #remove dest if it exists
    if os.path.exists(destination):
        shutil.rmtree(destination)

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

                branch_dir = destination+"/"+repo+"-"+branch_name
                print "Creating branch directory %s " % branch_dir

                if os.path.exists(branch_dir):
                    print "Branch directory exists so we are deleting it '%s'" % branch_dir
                    shutil.rmtree(branch_dir)

                print "Copying '%s' into '%s'" % (repo_dir, branch_dir)
                subprocess.call(['cp',"-r",repo_dir, branch_dir],cwd=root_dir)
                #shutil.copytree(repo_dir,branch_dir)



                #git checkout -b "nameofthebranch"
                subprocess.call(['git','checkout',"-b",branch_name],cwd=branch_dir)

                #git reset --hard master
                subprocess.call(['git','reset',"--hard","master"],cwd=branch_dir)

                #git filter-branch --subdirectory-filter
                subprocess.call(['git','filter-branch',"--subdirectory-filter",directory],cwd=branch_dir)
        else:
            branch_dir = destination+"/"+repo+"-"+repo
            shutil.copytree(repo_dir,branch_dir)

    dest_repos = os.listdir(destination)

    for r in dest_repos:
        print "Run cvsanaly for %s " % destination+"/"+r

