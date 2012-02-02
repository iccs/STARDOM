import csv
from math import sqrt
import operator
import sys
import MySQLdb
from commons import lang


__author__ = 'fotis'



def get_connection():
    conn = MySQLdb.connect (
                                host = 'localhost',
                                port = 8889,
                                user = 'alert',
                                passwd = '1234',
                                db  = 'alert_dev')
    return conn



#
# Creates the analogous ranking for the developer in the identity
# mpa
#
def calculate_distances(file):

    conn = get_connection()
    cursor = conn.cursor()


    tables = [
        "scm_activity_metric",
        "scm_api_introduced_metric",
        "its_activity_metric",
        "mailing_list_activity_metric"
    ]


    metrics={}

    ranks = {}




    mean=[1363.27272727,
          1062.18181818,
          248.54545455,
          52.24940191]


    #get the values of all the identities
    cursor.execute("SELECT id FROM identity")
    identity_ids = cursor.fetchall()
    for ids in identity_ids:
        identity_id = ids[0]
        metrics[identity_id] = []

        #for each id get all of the possible metrics
        for metric_name in tables:

            query = "select COUNT(*) from %s join metric using(id) where identity_id = %s " % (metric_name, identity_id)

            cursor.execute(query)
            result=cursor.fetchone()

            if result is None:
                metrics[identity_id].append(0)
            else:
                metrics[identity_id].append(result[0])

#            print "%s => %s "% (query,metrics[identity_id])
        ranks[identity_id] = calculate_euclidean_distance(mean, metrics[identity_id])


    sorted_ranks = sorted(ranks.items(), key=operator.itemgetter(1))

     ## write to resulting string
    spamWriter = csv.writer(open(file, 'w'),delimiter=',', quotechar='"')

    for sorts in sorted_ranks:
        spamWriter.writerow([sorts[0],sorts[1],lang.get_name_and_lastname(conn,sorts[0])])

    for i in range(1,5):
        spamWriter.writerow([])


    conn.close()




def calculate_euclidean_distance(array1,  array2):

    if len(array1) != len(array2) or len(array1)< 2 :
        return -1


    dist = 0
    for i in range(0,len(array1)):

        dist += pow(array1[i]-array2[i],2)

#    dist = sqrt(dist)

    print " %s vs %s = %s" % (array1,array2,dist)
    return sqrt(dist)




if __name__ == '__main__':

    if len(sys.argv) != 2:
        print """
            You need to specify a file as an argument
        """
        exit(1)


    print  "Extracting information to %s " % sys.argv[1]

    calculate_distances(sys.argv[1])


