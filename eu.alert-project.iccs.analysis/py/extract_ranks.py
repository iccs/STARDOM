import csv
import sys
import MySQLdb
import config

__author__ = 'fotis'



def get_connection():
    conn = MySQLdb.connect (
                                host = 'localhost',
                                port = 8889,
                                user = 'alert',
                                passwd = '1234',
                                db  = 'alert_work_after_2003')
    return conn


#
# Attemps to return the most suitable name
# for printing, which includes both
# the first name and the lastname
#
#
def get_name_and_lastname(id):

    conn = get_connection()

    cursor = conn.cursor()

    cursor.execute("SELECT * FROM identity_profile_view WHERE id=%s",id)
    results= cursor.fetchall()

    ret = ''

    for row in results:

        name =      row[3].strip() if row[3] is not None else ""
        lastname =  row[4].strip() if row[4] is not None else ""
        username =  row[5].strip() if row[5] is not None else ""
        email =     row[6].strip() if row[6] is not None else ""

        if name != '' and lastname != '' :
            ret  = name+" "+lastname
            break
        elif lastname == '' and  name !='' and email !='' :
            ret = name+" "+email
        elif lastname == '' and name == '' and email !='' and username !='' :
            ret = email +" "+username
        elif lastname == '' and name == '' and email =='' and username !='' :
            ret = username
        else:
            ret = email

    conn.close()
    return ret


#
# Creates the analogous ranking for the developer in the identity
# mpa
#
def create_rankings(file):

    conn = get_connection()
    cursor = conn.cursor()

    identities = [
        1,
        2,
        3,
        4,
        5,
        6
    ]

    tables = [
        "scm_activity_metric_view",
        "scm_api_introduced_metric_view",
        "its_activity_metric_view",
        "mailing_list_activity_metric_view"
    ]

    rankings={}
    list= {}

    for value in tables:

        # extract the metrics from each view ranked by quantity
        cursor.execute("""
                    select quantity,identity_id from %s order by quantity desc limit %s;
                       """ % (value, config.extract_max_search_results))

        results = cursor.fetchall()

        #use as a row number
        counter = 1

        for row in results:

            #
            # We want to create a single row for printing
            # there for the values will be appened on each row
            #
            if not list.has_key(counter) :
                list[counter] = []
			

            name_last = get_name_and_lastname(row[1])
            identity_id = row[1]
            metric_value = row[0]


            if identity_id in identities:
                if not rankings.has_key(identity_id):
                    rankings[identity_id] = {}

                rankings[identity_id].update({value : [metric_value,counter]})

            list[counter].extend([metric_value,identity_id,name_last])

            counter+=1


    conn.close()

    ## write to resulting string
    spamWriter = csv.writer(open(file, 'w'),delimiter=',', quotechar='"')

    printrows = {'Metrics': []}

    for key, values in rankings.iteritems():
        printrows['Metrics'].extend([get_name_and_lastname(key)+" ("+str(key)+")",'Rank'])


    for key, values in rankings.iteritems():

        for k, v in values.iteritems():

            if not printrows.has_key(k):
                printrows[k] = []

            printrows[k].extend(v)


    for key, values in printrows.iteritems():
        spamWriter.writerow([key]+values)


    for i in range(1,5):
        spamWriter.writerow([])


    header = [""]+tables
    spamWriter.writerow(header)
    for i in identities:
        row = [get_name_and_lastname(i)]
        for t in tables:
            row.append(rankings[i][t][1])

        spamWriter.writerow(row)

    for i in range(1,5):
        spamWriter.writerow([])


    wr = 0
    for key, values in list.iteritems():
        spamWriter.writerow([key]+ values)
        if wr > config.extract_max_print_results:
            break
        wr+=1



if __name__ == '__main__':

    if len(sys.argv) != 2:
        print """
            You need to specify a file as an argument
        """
        exit(1)


    print  "Extracting information to %s " % sys.argv[1]

    create_rankings(sys.argv[1])





