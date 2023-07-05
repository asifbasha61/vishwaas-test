import json

from Database import DbConnection as db
from utils import uuidGenerator as ug
from datetime import datetime, timezone
import logging
import Constants as cons
from SUT import Sut
import psycopg2
import pytz
from Trigger import Trigger

# Create a logger
logger = logging.getLogger('Assessment-logger')
# Create a file handler to write logs to a file
handler = logging.FileHandler('assessment.log', mode='w')
# Set the logging level to INFO
logger.setLevel(logging.INFO)
# Create a formatter to format the log messages
formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')

# Set the formatter for the handler
handler.setFormatter(formatter)

# Add the handler to the logger
logger.addHandler(handler)


class Assessment():
    def assessment(self, sut):
        logger.info(f"Asessment started for: {sut}")

        conn = db.get_connection()
        cur = conn.cursor()
        try:
            """ insert a new assessment into the assessment table """
            insert_assessment = f"""INSERT INTO {cons.Constants.ASSESSMENT_TABLE}("assessmentId", "sutId", rebitapistandardversion, fistandardversion)
                        VALUES(%s,%s,%s,%s) ;"""
            assessment_id = ug.getUUID()
            # execute the INSERT statement
            cur.execute(insert_assessment,
                        (assessment_id, sut['sutid'], sut['rebitapistandardversion'], sut['fistandardversion']))
            # commit the changes to the database
            conn.commit()
            # print("Inserted a record into assessment table")

            res = Assessment.assessmentlog(self, assessment_id)
            if res:
                return res
            else:
                return False

        except (Exception, psycopg2.Error) as error:

            logger.error(f"Failed to insert record into {cons.Constants.ASSESSMENT_TABLE} table")
            logger.error(error)
        finally:
            # close the database connection
            if conn:
                cur.close()
                conn.close()

    def assessmentlog(self, assessment_id):
        """ insert a new assessment into the assessmentlogs table """
        try:
            conn = db.get_connection()
            cur = conn.cursor()
            conn.autocommit = True
            insert_assessment_log = f"""INSERT INTO {cons.Constants.ASSESSMENT_LOGS}
            (assessmentid, starttime, status, id)
                                    VALUES(%s,%s,%s,%s) """

            status = "started"
            # Get the current timestamp

            dt = datetime.now(timezone.utc)

            # execute the INSERT statement
            cur.execute(insert_assessment_log, (assessment_id, dt, status, ug.getUUID()))

            # commit the changes to the database
            conn.commit()
            print(f"Inserted a record into {cons.Constants.ASSESSMENT_LOGS} table")
            # Close the cursor and the database connection
            cur.close()
            return True
        except (Exception, psycopg2.Error) as error:

            logger.error(f"Failed to insert record into {cons.Constants.ASSESSMENT_LOGS} table")
            logger.error(error)
        finally:
            # close the database connection
            if conn:
                cur.close()
                conn.close()

    def test_suite(self, testsuitId):
        """Get the all  features in the testsuite
        :returns list of featureId
        """
        # "0ec36d3b-b02c-4c69-9dce-f0dbddc06a01"
        try:
            logger.info(f"TestSuite selected: {testsuitId}")
            conn = db.get_connection()
            cur = conn.cursor()
            conn.autocommit = True

            # Execute query to select data from table

            cur.execute(f'SELECT * FROM "{cons.Constants.TEST_SUITE_TABLE}" where testsuiteid=\'{testsuitId}\'')
            rows = cur.fetchall()
            if rows is not None:
                data = [{'testsuiteid': row[0], 'featureId': row[1], 'entitytype': row[2],
                         'rebitapistandardversion': row[3],
                         'fistandardversion': row[4]} for row in rows]

                return data

        except (Exception, psycopg2.Error) as error:
            print(f"Failed to query record into {cons.Constants.TEST_SUITE_TABLE} table", error)
            logger.error(f"Failed to query record into {cons.Constants.TEST_SUITE_TABLE} table")
            logger.error(error)
        finally:
            # close the database connection
            if conn:
                cur.close()
                conn.close()

    def scenario(self, featureId):
        try:

            conn = db.get_connection()
            cur = conn.cursor()
            conn.autocommit = True

            # Execute query to select data from table
            cur.execute(f'SELECT * FROM {cons.Constants.SCENARIO_TABLE} where featureid=\'{featureId}\'')
            rows = cur.fetchall()
            if rows is not None:
                data = [{'scenarioId': row[0], 'functionunderstest': row[1], 'description': row[2],
                         'entitytype': row[3], 'rebitapistandardversion': row[4], 'fistandardversion': row[5],
                         'featureid': row[6]} for row in rows]

                logger.info(f"Running the featureid: {featureId} ")
                return data

        except (Exception, psycopg2.Error) as error:
            logger.error(f"Failed to query record into {cons.Constants.SCENARIO_TABLE} table")
            logger.error(error)

        finally:
            # close the database connection
            if conn:
                cur.close()
                conn.close()

    def step(self, scenarioId):
        try:
            logger.info(f"Running  the scenarioId: {scenarioId}")
            conn = db.get_connection()
            cur = conn.cursor()
            conn.autocommit = True

            # Execute query to select data from table
            cur.execute(f'SELECT * FROM {cons.Constants.STEP_TABLE} where scenarioid=\'{scenarioId}\' order by stepid')
            rows = cur.fetchall()
            if rows is not None:
                data = [{'stepid': row[0], 'scenarioid': row[1], 'functionundertest': row[2],
                         'auto': row[3], 'config': row[4]} for row in rows]

                return data

        except (Exception, psycopg2.Error) as error:
            logger.error(f"Failed to query record into {cons.Constants.STEP_TABLE} table")
            logger.error(error)
        finally:
            # close the database connection
            if conn:
                cur.close()
                conn.close()

    def start_assessment(self, sutid):
        """ 1. Get the sut details
         2. insert record in assessment table
         3. insert record in assessment log table
         4. Get the Testpackage details
         5. Get the feature details
         6. Get the scenario details
         7. Get the step details
         8. Run the step
         """
        pass


if __name__ == '__main__':
    # sut id = 2d8724da-c4b4-4d0c-9e19-80a54141c01e
    sut_id = "2d8724da-c4b4-4d0c-9e19-80a54141c01e"
    # Get the sut details
    sut = Sut.get_sut(sut_id)

    # start the assessment
    assessment_res = Assessment.assessment(Assessment(), sut[0])
    print(assessment_res)
    if assessment_res:
        """Get the all  features in the testsuite"""
        # "0ec36d3b-b02c-4c69-9dce-f0dbddc06a01"
        test_suite_id = "4va28d3b-b02c-4c69-9dce-f0dbddc06a01"
        test_suite_res = Assessment.assessment(Assessment(), test_suite_id)
        print(test_suite_res)

    """Get the list of feature id from testsuite"""
    test_suite_id = "4va28d3b-b02c-4c69-9dce-f0dbddc06a01"
    print("Get ")
    test_suite_res = Assessment.test_suite(Assessment(), test_suite_id)
    if test_suite_res:
        """Get the list of scenario id for the featureId"""
        scenario_res = Assessment.scenario(Assessment(), test_suite_res[0]['featureId'])
        # print(scenario_res)
        if scenario_res:
            """Get the list of steps  for the scenarioId"""
            step_res = Assessment.step(Assessment(), scenario_res[0]['scenarioId'])
            print(step_res)
            if step_res:
                logger.info("Parsing the step configuration")
                for i in step_res:
                    # print(i['config'])
                    if i['config']['action'] == 'Trigger':

                        """call the sut's trigger"""
                        # sutid should be dynamic
                        sut_id = "2d8724da-c4b4-4d0c-9e19-80a54141c01e"
                        sut_details = Sut.get_sut(sut_id)
                        # print(sut_details)
                        logger.info(f"Running the step: {i} ")

                        trigger_res = Trigger.Trigger(Trigger(), sut_details, i['config'])
                        print(trigger_res.text)

                    elif i['config']['action'] == 'Request':
                        """wait for the request from sut"""
                        pass
                    elif i['config']['action'] == 'Callback':
                        """make a request to sut"""
                        logger.info(f"Running the step: {i} ")
                        callback_res = Trigger.callback(Trigger(), i['config'])
                        print(callback_res)
