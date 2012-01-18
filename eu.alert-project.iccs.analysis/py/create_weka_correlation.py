#!/usr/bin/env python
from datetime import date, datetime

import MySQLdb
import csv
import re
import time
import config


QRY_DROP_TABLE="DROP TABLE IF EXISTS `identity_csvid` ; " \
               "CREATE TABLE `identity_csvid` (  " \
                "`identity_id` INT  NOT NULL,  " \
                "`user_id` INT  NOT NULL,  " \
                "`name` TEXT  NOT NULL,  " \
                "`email` TEXT  NOT NULL) ENGINE = InnoDB;"

QRY_SELECT_ALL_PEOPLE = "select id,name, email from people"

QRY_SELECT_IDENTITY = """select distinct identity_id
                        from profile p
                        join identity_is_profile i on p.id=i.profile_id
                        where email=%s;"""

QRY_INSERT_CSVID = """insert into identity_csvid VALUES(%s,%s,%s,%s) ;"""




def date_distance(date1):
    fmt = '%Y-%m-%d %H:%M:%S'
    d1 = datetime.strptime('2012-01-01 17:31:22', fmt)
    d2 = datetime.strptime(date1, fmt)
    return (d2-d1).days

def get_alert_connection():
    return MySQLdb.connect (
                            host = config.alert_db_host,
                            port = config.alert_db_port,
                            user = config.alert_db_user,
                            passwd = config.alert_db_password,
                            db  = config.alert_db_name)




def people_mapping_init():


    user_to_identity_dict={}

    alert_conn = get_alert_connection()

    cvsanaly_conn = MySQLdb.connect (
                        host = config.cvsanaly_db_host,
                        port = config.cvsanaly_db_port,
                        user = config.cvsanaly_db_user,
                        passwd = config.cvsanaly_db_password,
                        db = config.cvsanaly_db_name)


    # Apparently this is a compound statement and
    # we should close and reopen the cursor
    alert_cursor=alert_conn.cursor ()
    alert_cursor.execute (QRY_DROP_TABLE)
    alert_cursor.close()

    csv_cursor = cvsanaly_conn.cursor ()
    all_cvsanaly_people = csv_cursor.execute (QRY_SELECT_ALL_PEOPLE)

    counter = 0
    for x in range(all_cvsanaly_people):
        row=csv_cursor.fetchone ()
        user_id=row[0]
        name=row[1]
        email = row[2]
        repl=' '
        email = re.sub(r"[-_\+\.@]", repl, email)
        name = re.sub(r"[\']", repl, name)

        print "Processing %s => %s " % (user_id,email,)

        alert_cursor=alert_conn.cursor ()
        alert_cursor.execute (QRY_SELECT_IDENTITY, (email,))
        alert_row=alert_cursor.fetchone ()

        if alert_row is None:
            print '%s NOT FOUND!!! ' % email
            continue
        identity_id=alert_row[0]
        print "Identity id %s found for %s " % (identity_id,email,)
        alert_cursor.execute (QRY_INSERT_CSVID, (identity_id,user_id,name,email,))

        user_to_identity_dict[user_id]=identity_id
        alert_cursor.close ()

    csv_cursor.close()
    alert_conn.close()
    cvsanaly_conn.close()

    return user_to_identity_dict


def get_metric(connection, query, identity_id):
    print "Executing "+query
    cursor = connection.cursor()
    cursor.execute (
        query,
        (identity_id,)
    )

    ret = 0
    row=cursor.fetchone ()
    if row is not None:
        ret =row[1]

    cursor.close()

    return ret

def get_quantitative_metric(connection, identity_id , metric_name):

    qry = """select m.id,quantity,m.identity_id,m.created_at
                        from %s as scm
                        inner join metric_quantitative as mq on scm.id=mq.id
                        inner join metric as m on scm.id=m.id
                        where m.identity_id=%s
                        limit 1;""" % (metric_name,"%s",)

    return get_metric(connection,qry,identity_id)


def get_temporal_metric(connection, identity_id , metric_name):
    qry = """select m.id,temporal,identity_id,created_at
            from %s as scm
            inner join metric_temporal as mt on scm.id=mt.id
            inner join metric as m on scm.id=m.id
            where identity_id=%s
            limit 1;""" % (metric_name,"%s",)
    return date_distance(get_metric(connection,qry,metric_name,identity_id))


if __name__ == '__main__':

    metrics = {
        "scm_activity_metric"           :[1, "q"],
        "its_activity_metric"           :[1, "q"],
        "mailing_list_activity_metric"  :[1, "q"],
        "scm_api_introduced_metric"     :[1, "q"],
        "scm_api_usage_count_metric"    :[1, "q"],

        "scm_temporal_metric"           :[1, "t"],
        "its_temporal_metric"           :[1, "t"],
        "mailing_list_temporal_metric"  :[1, "t"],
    }

    # You propably shouldnt bother with what is under this line
    #
    # ---------------------------------------------------------
    #



    #create the file
    output_file=config.io_results_file+"/%s.arff" % ("correlation-"+datetime.now().strftime("%Y%m%d%H%M%S"))
    print "Creating output file %s" % output_file;

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
    for metric_name, values in metrics.iteritems():

        if values[0] >= 1:
            bugAWriter.writelines("@ATTRIBUTE %s NUMERIC\n" % metric_name)



    bugAWriter.writelines("@ATTRIBUTE betweeness NUMERIC\n")
    bugAWriter.writelines("\n@DATA\n")



    people_map_dict={}
    people_map_dict = people_mapping_init()

    alert_conn = get_alert_connection()
    spamReader = csv.reader(open(config.io_betweenness_file, 'rb'),delimiter='\t', quotechar='|')

    for row in spamReader:

        identity_id=people_map_dict[int(row[0])]
        btness=row[1]

        #tdesc = "select m.id,quantity,m.identity_id,created_at from scm_activity_metric as scm inner join metric_quantitative as mq on scm.id=mq.id inner join metric as m on scm.id=m.id where m.identity_id='"+str(identity_id)+"' order by quantity desc limit 1;"

        result =[]
        result.append(identity_id)

        for metric_name, values in metrics.iteritems():

            if values[0] >= 1:

                if values[1] == 'q':
                    result.append(get_quantitative_metric(alert_conn,identity_id,metric_name))
                elif values[1] == 't':
                    result.append(get_temporal_metric(alert_conn,identity_id,metric_name))


        result.append(btness)
        bugAWriter.writelines(', '.join(str(x) for x in result)+"\n")


    bugAWriter.close()
    alert_conn.close ()
