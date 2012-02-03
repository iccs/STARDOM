import time
from datetime import datetime
import sys
import MySQLdb



def get_connection():
    conn = MySQLdb.connect (
                                host = 'localhost',
                                port = 8889,
                                user = 'alert',
                                passwd = '1234',
                                db  = 'alert_historical')
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

    identities = []

    for i in range(1,7):
        identities.append(i)

    tables = [
        "scm_activity_metric",
        "scm_api_introduced_metric",
        "its_activity_metric",
        "mailing_list_activity_metric"
    ]


    result={}

    for value in tables:

        print "Looking for metric %s " % value

        rankings_query = """
        select identity_id, count(id) as activity
        from %s join metric using(id) group by identity_id order by activity DESC""" % value


        cursor.execute(rankings_query)
        results = cursor.fetchall()


        for row in results:

            identity_id = row[0]
            metric_value = row[1]

            if not result.has_key(identity_id):
                result[identity_id]={}

            if not result[identity_id].has_key(value):
                result[identity_id][value] = []

            result[identity_id][value].append(metric_value)


        time_query = """
                select identity_id, max(UNIX_TIMESTAMP(created_at)) max_date from %s
                join metric using(id)
                group by identity_id
                order by max_date asc """ % value

        cursor.execute(time_query)
        results= cursor.fetchall()
        for row in results:

            identity_id = row[0]

            datetime_value = datetime.fromtimestamp(row[1])
            time_value = float(1) / (datetime.now()-datetime_value).days

            print "%s - %s = %s" %(datetime.now(),datetime_value,time_value)

            if not result.has_key(identity_id):
                result[identity_id]={}

            if not result[identity_id].has_key(value):
                result[identity_id][value] = []

            result[identity_id][value].append(time_value)



        #create the file
    output_file="%s/%s.arff" % (file,"rankings-"+datetime.now().strftime("%Y%m%d%H%M%S"))
    print "Creating output file %s" % output_file

    bugAWriter = open(output_file, 'wb')
    bugAWriter.writelines((
                              """ % 1.  Title: Bug Solution Dataset Numeric
                            %
                            % 2. Sources:
                            %(a) Creator: ALERT Project
                            %(b) Date: """+(datetime.now().strftime("%d %b %Y - %H:%M:%S")))+"""\n%\n\n""")

    bugAWriter.writelines("@RELATION bug\n")
    bugAWriter.writelines("\n\n\n")
    bugAWriter.writelines("@ATTRIBUTE identity_id  NUMERIC\n")


    # Go over the enabled metrics and create the heading
    for value in tables:
        bugAWriter.writelines("@ATTRIBUTE %s NUMERIC\n" % value)
        bugAWriter.writelines("@ATTRIBUTE %s_time NUMERIC\n" % value)



    bugAWriter.writelines("@ATTRIBUTE relevant {TRUE,FALSE}\n")
    bugAWriter.writelines("\n@DATA\n")


    #print result
    for identity_id, metrics in result.iteritems():

        row = [identity_id]

        for value in tables:

            value = metrics[value] if metrics.has_key(value) else [0,0]
            row.extend(value)


        row.append(("TRUE" if identity_id in identities else "FALSE"))


        bugAWriter.writelines(', '.join(str(x) for x in row)+"\n")


    bugAWriter.close()
    conn.close()

if __name__ == '__main__':




    if len(sys.argv) != 2:
        print """
            You need to specify a directory as an argument
        """
        exit(1)


    print  "Extracting information to %s " % sys.argv[1]

    create_rankings(sys.argv[1])


