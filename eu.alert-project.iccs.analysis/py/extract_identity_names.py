import csv
import string
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
    return string.replace(ret,"'","''")


if __name__ == '__main__':

    conn = get_connection()
    cursor= conn.cursor()
    cursor.execute("select * from identity")
    identities = cursor.fetchall()


    print "set names utf8;"
    for row in identities:
        print "insert into uuid_name values('%s','%s');" % (row[1], get_name_and_lastname(row[0]))

