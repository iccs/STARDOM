import MySQLdb
from config import preferences


__author__ = 'fotis'

def get_alert_connection():
    return MySQLdb.connect (
                            host = preferences.alert_db_host,
                            port = preferences.alert_db_port,
                            user = preferences.alert_db_user,
                            passwd = preferences.alert_db_password,
                            db  = preferences.alert_db_name)

def get_cvsanaly_connection():
    return MySQLdb.connect (
                            host = preferences.cvsanaly_db_host,
                            port = preferences.cvsanaly_db_port,
                            user = preferences.cvsanaly_db_user,
                            passwd = preferences.cvsanaly_db_password,
                            db = preferences.cvsanaly_db_name)


