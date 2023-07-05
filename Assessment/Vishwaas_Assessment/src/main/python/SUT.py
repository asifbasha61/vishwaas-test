from Database import DbConnection as db
import Constants as cons


class Sut():

    @classmethod
    def get_sut(self, sutid):
        conn = db.get_connection()
        cur = conn.cursor()
        # Execute query to select data from table
        cur.execute(f"SELECT * FROM {cons.Constants.SUT_TABLE} where sutid='{sutid}'")
        rows = cur.fetchall()

        if rows is not None:
            # rows = cur.fetchall()
            # print(rows)
            data = [{'sutid': row[0], 'entitytype': row[1], 'ipaddress': row[2],
                     'fitype': row[3], 'rebitapistandardversion': row[4], 'fistandardversion': row[5]} for row in rows]
            # Close the cursor and the database connection
            cur.close()
            conn.close()

            return data
