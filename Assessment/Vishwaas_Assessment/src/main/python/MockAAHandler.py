from flask import Flask, request, jsonify
import logging
from MockAAGetSet import *
import requests


app = Flask(__name__)
# Set the log level to DEBUG
app.logger.setLevel(logging.DEBUG)

# Create a file handler
handler = logging.FileHandler(
    '/home/amith/Desktop/Vishwass/Runner/vishwaas-final-poc-1/vishwaas/src/main/python/assessment.log',
    mode='a')
handler.setLevel(logging.DEBUG)

# Create a formatter and add it to the handler
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
handler.setFormatter(formatter)

# Add the handler to the app logger
app.logger.addHandler(handler)


@app.route('/consent/request', methods=['POST'])
def consent_request():
    app.logger.info("Consent request received from SUT")

    # Extract data from request
    ver = request.json['ver']
    txnid = request.json['txnid']

    # Construct response
    response_data = {
        'ver': ver,
        'txnid': txnid,
        'status': 'ok',
        "message": "from Mock-AA Consent request is received"
    }
    return jsonify(response_data)


@app.route('/consent/handle/request', methods=['Post'])
def consent_handle_request():
    app.logger.info("Consent handle request received from SUT")

    # Extract data from request
    ver = request.json['ver']
    txnid = request.json['txnid']

    # Construct response
    response_data = {
        'ver': ver,
        'txnid': txnid,
        'status': "OK",
        "message": "from Mock-AA Consent Handler request"
    }

    mockAaResponseObj.setResponse("Pending")

    return jsonify(response_data)


@app.route('/consent/notification/response', methods=['POST'])
def consent_notification_response():
    app.logger.info("Consent notification response received from MOCK-AA")

    ver = request.json['ver']
    txnid = request.json['txnid']

    # Construct response
    response_data = {
        'ver': ver,
        'txnid': txnid,
        'status': 'ok',
        "message": "Consent notification response received from Mock AA, ready for validation"
    }

    app.logger.info("Validation started for consent notification response")
    app.logger.info("Parsing the validation json specification file")

    res = jsonify(response_data)

    assert res.status_code == 200
    print(f"Response code: {res.status_code}")

    return res

if __name__ == '__main__':
    app.run(port=8082)
