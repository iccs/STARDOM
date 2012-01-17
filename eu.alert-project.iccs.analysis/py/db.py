import MySQLdb
import config

__author__ = 'fotis'

def get_alert_connection():
    return MySQLdb.connect (
                            host = config.alert_db_host,
                            port = config.alert_db_port,
                            user = config.alert_db_user,
                            passwd = config.alert_db_password,
                            db  = config.alert_db_name)

def get_cvsanaly_connection():
    return MySQLdb.connect (
                            host = config.cvsanaly_db_host,
                            port = config.cvsanaly_db_port,
                            user = config.cvsanaly_db_user,
                            passwd = config.cvsanaly_db_password,
                            db = config.cvsanaly_db_name)
