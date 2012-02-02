import db


#
# Attemps to return the most suitable name
# for printing, which includes both
# the first name and the lastname
#
#
def get_name_and_lastname(conn, id):

#    print "Looking for name by id = %s " % str(id)

    cursor = conn.cursor()

    name_query = "SELECT * FROM identity_profile_view WHERE id=%s" % id

    cursor.execute(name_query)

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

#    print "Found %s = %s " % (ret,str(id))
    return "%s (%s)" % (ret,str(id))
