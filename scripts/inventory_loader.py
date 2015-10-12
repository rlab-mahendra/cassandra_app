__author__ = 'pigol'

"""
Script for loading the inventory information
from the InTouch schema into the cassandra
schema
"""

import MySQLdb
import MySQLdb.cursors
from cassandra.cluster import Cluster

idb = MySQLdb.connect("localhost", "root", "root", "user_management", cursorclass=MySQLdb.cursors.DictCursor)
cluster = Cluster(["192.168.56.101"])
sess = cluster.connect("mykeyspace")
inv_prep_insert = sess.prepare("INSERT INTO inventory(org_id, sku, attributes, price) VALUES(?, ?, ?, ?)")
invprices_prep_insert = sess.prepare("INSERT INTO inventory_prices(org_id, price, sku) VALUES (?, ?, ?)")
invattributes_prep_insert = sess.prepare("""
                    INSERT INTO inventory_attributes(attribute_name, attribute_value, org_id, sku)
                    VALUES (?, ?, ?, ?)
                    """)


cursor = idb.cursor()
for o in xrange(138, 1000):
    print("Loading for org: " + str(o))
    done = False;
    id = 0;
    while not done:

        sql = "SELECT id, item_sku FROM inventory_masters im WHERE org_id = %s and id > %s ORDER BY id ASC LIMIT 500" % (o, id)
        cursor.execute(sql)
        rows = cursor.fetchall()
        if not len(rows) > 0:
            print("Done for org: " + str(o))
            done = True
            break

        sku_list = []
        sku_dict = {}
        for r in rows:
            sku_list.append(r['item_sku'])
            tdict = {}
            for k,v in r.items():
                tdict[k] = v
            sku_dict[r['item_sku']] = tdict
            id = r['id']

        sku_list = ["'" + s + "'" for s in sku_list]
        print(sku_list)
        skustring = ",".join(sku_list)

        if not len(sku_list) > 0:
            pass

        sql = """
              select im.item_sku as sku, im.org_id as org_id,
              group_concat(concat(iia.name, '::::', iiav.value_name) separator ',') as attributes,
              im.price as price
              from inventory_masters im, inventory_item_attributes iia, inventory_item_attribute_values iiav,
              inventory_items ii
              where im.org_id = %s
              and ii.org_id = im.org_id and ii.item_id = im.id and ii.attribute_id = iia.id and
              ii.org_id = iia.org_id and ii.attribute_value_id = iiav.id and ii.org_id = iiav.org_id
              and ii.org_id = %s and iia.org_id = %s and im.item_sku in (%s) group by im.item_sku, im.org_id
            """ % (o, o, o, skustring)
        print("Query: " + str(sql))
        cursor.execute(sql)
        rows = cursor.fetchall()
        for r in rows:
            attr_list = [s for s in str(r['attributes']).split(",")]
            attr_dict = {}
            for s in attr_list:
                if s.__contains__("::::"):
                    (k, v) = s.split("::::")
                    attr_dict[k] = v
                    invattributes_bounded_sql = invattributes_prep_insert.bind((k, v, r['org_id'], r['sku']))
                    sess.execute(invattributes_bounded_sql)

            inv_bounded_sql = inv_prep_insert.bind((r['org_id'], r['sku'], attr_dict, r['price']))
            sess.execute(inv_bounded_sql)

            invprices_bounded_sql = invprices_prep_insert.bind((r['org_id'], r['price'], r['sku']))
            sess.execute(invprices_bounded_sql)



