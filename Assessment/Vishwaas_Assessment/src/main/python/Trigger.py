import Database.DbConnection as db
import requests as rq
import json
import logging

# from Assessment import  logger
logger = logging.getLogger('Assessment-logger')
# Create a file handler to write logs to a file
handler = logging.FileHandler('assessment.log', mode='a')
# Set the logging level to INFO
logger.setLevel(logging.INFO)
# Create a formatter to format the log messages
formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')

# Set the formatter for the handler
handler.setFormatter(formatter)

# Add the handler to the logger
logger.addHandler(handler)


# from flask import jsonify
class Trigger():
    """Call the trigger API of SUT"""

    # call the sut trigger api
    # url, endpoint,
    def Trigger(self, sut, stepConf):
        print("Inside trigger Function: ", sut, stepConf)
        # SUT[FIU]      # Consent                 #Vishwaas
        logger.info(f"Triggering {stepConf['to']} {stepConf['endpoint']} from {stepConf['from']}")
        if stepConf['action'].lower() == 'trigger':
            ipaddress = sut[0]['ipaddress']
            source = stepConf['from']
            destination = stepConf['to']
            endpoint = stepConf['endpoint']
            request_method = stepConf['method']
            validationResponseCode = stepConf['validationResponseCode']
            port = ":5000"
            url = ipaddress + port + endpoint
            headers = {
                'Content-Type': 'application/json',
            }

            request_body = json.dumps({
                "ver": "1.0",
                "timestamp": "2018-12-06T11:39:57.153Z"
            })
            print("Trigger details::", "URL: " + url + " Headers: " + str(headers) +" \nrequest_method: " + request_method +
                  "\nrequest_body: " + str(request_body))
            trigger_res = Trigger.call_api(Trigger(), url, headers, request_method, request_body)
            if trigger_res.status_code == validationResponseCode:
                return trigger_res
            return trigger_res

    def callback(self, stepConf):
        print("inside call back")
        logger.info(f"Callback {stepConf['to']} /consent/notification from {stepConf['from']}")
        if stepConf['action'].lower() == 'callback':
            # ipaddress = sut[0]['ipaddress']
            ipaddress = "127.0.0.1"
            source = stepConf['from']
            destination = stepConf['to']
            endpoint = stepConf['endpoint']
            request_method = stepConf['method']
            validationResponseCode = stepConf['validationResponseCode']
            port = ":5001"
            url = ipaddress + port + endpoint
            headers = {
                'x-jws-signature': 'Duis et cillum velit',
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }

            request_body = json.dumps({
                "ver": "1.0",
                "timestamp": "2018-12-06T11:39:57.153Z"
            })

            callback_res = Trigger.call_api(Trigger(), url, headers, request_method, body=request_body)
            return callback_res

    def call_api(self, url, header, request_method, body):
        # print("Request method::", request_method)
        if request_method == 'GET':
            get_response = rq.get("http://" + url, headers=header)
            return get_response
        elif request_method == 'POST':
            print("inside post")
            post_response = rq.post("http://" + url, headers=header, data=body)
            return post_response
        return {"error": "method not supported"}


if __name__ == '__main__':
    Trigger.getTrigger(Trigger())
