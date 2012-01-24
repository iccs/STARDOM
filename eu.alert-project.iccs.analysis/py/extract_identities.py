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
                                db  = 'alert_work')
    return conn



#
# Creates the analogous ranking for the developer in the identity
# mpa
#
def create_queries():

    IDENTITY_QUERY="SELECT * FROM identity WHERE id=%s"
    PROFILE_QUERY="SELECT * FROM profile WHERE id=%s"
    IDENTITY_IS_PROFILE_QUERY="SELECT * FROM identity_is_profile WHERE identity_id=%s ORDER BY profile_id"

    conn = get_connection()
    cursor = conn.cursor()

    identities = [
        1,
        74,
        87,
        124,
        532,
        2893
    ]


    identity_id=1
    profile_id=1

    for value in identities:


        cursor.execute(IDENTITY_QUERY,value)
        row = cursor.fetchone()


        print "INSERT INTO identity VALUES(%s,'%s');\n" % (identity_id,row[1])


        # extract the metrics from each view ranked by quantity
        cursor.execute(IDENTITY_IS_PROFILE_QUERY,value)

        results = cursor.fetchall()
        for profileIdentity in results:


            profileCursor = conn.cursor()
            profileCursor.execute(PROFILE_QUERY,profileIdentity[1])

            profileResults = profileCursor.fetchall()
            for profile in profileResults:

                print "INSERT INTO profile VALUES(%s,%s,%s,%s,%s,%s,%s);\n" % (
                                                                                            profile_id,
                                                                                            trim_to_empty(profile[1]),
                                                                                            trim_to_empty(profile[2]),
                                                                                            trim_to_empty(profile[3]),
                                                                                            trim_to_empty(profile[4]),
                                                                                            trim_to_empty(profile[5]),
                                                                                            '\''+profile[6]+'\'')

                print "INSERT INTO identity_is_profile VALUES (%s,%s);\n" % (identity_id,profile_id)
                profile_id += 1


            profileCursor.close()


        identity_id +=1



    print "UPDATE sequence SET sequence_index=%s WHERE sequence_name='identity_sequence';\n" % identity_id
    print "UPDATE sequence SET sequence_index=%s WHERE sequence_name='profile_sequence';\n" % profile_id


    cursor.close()
    conn.close()


def trim_to_empty(string):
    if string is None :
        return 'null'
    elif string.strip() == 'none':
        return 'null'
    elif string.strip == '':
        return 'null'
    else:
        return '\''+string.strip()+'\''



if __name__ == '__main__':

    sys.argv
    create_queries()





