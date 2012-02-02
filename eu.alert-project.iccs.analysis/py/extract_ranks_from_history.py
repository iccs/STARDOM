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
                                db  = 'alert_dev')
    return conn


#
# Attemps to return the most suitable name
# for printing, which includes both
# the first name and the lastname
#
#
def get_name_and_lastname(id):

    print "Looking for name by id = %s " % str(id)
    conn = get_connection()

    cursor = conn.cursor()

    name_query = "SELECT * FROM identity_profile_view WHERE id=%s" % id

    cursor.execute(name_query)

    print name_query

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

    print "Found %s = %s " % (ret,str(id))
    return "%s (%s)" % (ret,str(id))



#
# Creates the analogous ranking for the developer in the identity
# mpa
#
def create_rankings(file):

    conn = get_connection()
    cursor = conn.cursor()

    identities = []

    for i in range(1,32):
        identities.append(i);

    tables = [
        "scm_activity_metric",
        "scm_api_introduced_metric",
        "its_activity_metric",
        "mailing_list_activity_metric"
    ]


    rankings={}
    overall={}

    start_date = 1997
    end_date = 2011


    titles = ["Activity"]
    events = {}

    for metric_name in tables:

        print "Looking for metric %s " % metric_name

        current_date = start_date



        while current_date <= end_date:
            if not current_date in titles:
                titles.extend([current_date])

            query = "select COUNT(*) from %s join metric using(id) where DATE(created_at) >= '%s-01-01' " % (metric_name, current_date)

            cursor.execute(query)
            result=cursor.fetchone()

            if not events.has_key(metric_name) :
                events[metric_name] = []

            events[metric_name].append(str(result[0]))




            ranknigs_query = """
            select identity_id, count(id) as activity
            from %s join metric using(id)
            WHERE DATE(created_at) >='%s-01-01' group by identity_id order by activity DESC;""" % (metric_name,current_date)


            cursor.execute(ranknigs_query)
            metrics_values = cursor.fetchall()

            ranking = 1
            for metric in metrics_values:

                identity_id = metric[0]
                activity_metric = metric[1]


                #look for the developer ranking
                if identity_id in identities:
                    if not rankings.has_key(metric_name):
                        rankings[metric_name] = {}

                    if not rankings[metric_name].has_key(identity_id):
                        rankings[metric_name][identity_id] = []

                    rankings[metric_name][identity_id].extend([activity_metric])

                ranking += 1

            current_date +=1


        overall_query = """
                    select identity_id, count(id) as activity
                    from %s join metric using(id)
                    group by identity_id order by activity DESC ;""" % (metric_name)


        cursor.execute(overall_query)
        top30 = cursor.fetchall()

        if not overall.has_key(metric_name):
            overall[metric_name] = []

        for row in top30:

            if row[0] in identities :
                overall[metric_name].append({'id':row[0],'metric':row[1]})






    ## write to resulting string
    spamWriter = csv.writer(open(file, 'w'),delimiter=',', quotechar='"')


    spamWriter.writerow(titles)

    for key,metric_name in events.iteritems():
        spamWriter.writerow([key]+metric_name)

    for i in range(1,5):
        spamWriter.writerow([])

    spamWriter.writerow(titles)
    for metric_label, metric_per_identity in rankings.iteritems():
        spamWriter.writerow([metric_label])
        for id, ranks in metric_per_identity.iteritems():

            spamWriter.writerow([get_name_and_lastname(id)]+ranks)

        for i in range(1,5):
            spamWriter.writerow([])

    for i in range(1,5):
        spamWriter.writerow([])

    for name, values in overall.iteritems():
        spamWriter.writerow([name])
        spamWriter.writerow([])
        for v in values:
            spamWriter.writerow([get_name_and_lastname(v['id']),v['metric']])
        spamWriter.writerow([])
        spamWriter.writerow([])



if __name__ == '__main__':

    if len(sys.argv) != 2:
        print """
            You need to specify a file as an argument
        """
        exit(1)


    print  "Extracting information to %s " % sys.argv[1]

    create_rankings(sys.argv[1])


