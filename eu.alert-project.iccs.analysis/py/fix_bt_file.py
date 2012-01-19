import csv
import identity
import config
import db

__author__ = 'fotis'


def fix_file():
    people_map_dict={}
    people_map_dict = identity.people_mapping_init()

    spamReader = csv.reader(open(config.io_betweenness_file, 'rb'),delimiter='\t', quotechar='|')
    spamWriter = csv.writer(open(config.io_betweeness_output, 'w'),delimiter=',', quotechar='"')

    conn = db.get_alert_connection()
    cursor=conn.cursor()

    first = False

    for row in spamReader:
#
#        if not first:
#            first = True
#            continue

        edge_identity_id=people_map_dict[int(row[0])]
        no_edge_identity_id=people_map_dict[int(row[2])]

        edge_btness=row[1]
        no_edge_btness=row[3]


        cursor.execute("SELECT * FROM identity_csvid WHERE identity_id=%s",edge_identity_id)
        edge_row = cursor.fetchone()

        cursor.execute("SELECT * FROM identity_csvid WHERE identity_id=%s",no_edge_identity_id)
        no_edge_row = cursor.fetchone()

        spamWriter.writerow([
            edge_row[0],
            edge_row[1],
            edge_btness,
            edge_row[2],
            edge_row[3],
            no_edge_row[0],
            no_edge_row[1],
            no_edge_btness,
            no_edge_row[2],
            no_edge_row[3]
            ])

    cursor.close()
    conn.close()

if __name__ == '__main__':
    fix_file()