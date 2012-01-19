import re
import db

QRY_DROP_TABLE="DROP TABLE IF EXISTS `identity_csvid` ; ";
QRY_CREATE_TABLE= "CREATE TABLE `identity_csvid` (  " \
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

def people_mapping_init():

    user_to_identity_dict={}

    alert_conn = db.get_alert_connection()

    cvsanaly_conn = db.get_cvsanaly_connection()

    # Apparently this is a compound statement and
    # we should close and reopen the cursor
    alert_cursor=alert_conn.cursor ()
    alert_cursor.execute (QRY_DROP_TABLE)
    alert_cursor.execute (QRY_CREATE_TABLE)
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

        alert_cursor.close();
        alert_cursor = alert_conn.cursor();
        rows = alert_cursor.execute (u"""insert into identity_csvid VALUES(%s,%s,%s,%s) ;""", (identity_id,user_id,name,email,))

        print "Created %s rows " % rows
        user_to_identity_dict[user_id]=identity_id
        alert_cursor.close ()

    csv_cursor.close()
    alert_conn.commit()
    alert_conn.close()
    cvsanaly_conn.close()


    return user_to_identity_dict


if __name__ == '__main__':
    people_mapping_init()