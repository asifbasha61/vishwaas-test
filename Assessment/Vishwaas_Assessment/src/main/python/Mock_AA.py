from flask import Flask, request, jsonify
import requests
import json
from MockAAGetSet import *
from Database import DbConnection as db

app = Flask(__name__)

conn = db.get_connection()
cur = conn.cursor()


@app.route('/consent', methods=['POST'])
def consent_request():
    request_body = request.get_json()

    # Extracting required data from the request body
    ver = request_body['ver']
    customer_id = request_body['ConsentDetail']['Customer']['id']
    timestamp = request_body['timestamp']
    txnid = request_body['txnid']

    response_body = {
        "ver": ver,
        "Customer": {
            "id": customer_id
        },
        "ConsentHandle": "39e108fe-9243-11e8-b9f2-0256d88baae8",
        "timestamp": timestamp,
        "txnid": txnid
    }

    import requests
    import json

    url = "http://127.0.0.1:8082/consent/request"

    payload = json.dumps({
        "ver": "1.0",
        "timestamp": "2023-05-29T07:48:51.054Z",
        "txnid": "4a4adbbe-29ae-11e8-a8d7-0289437bf331",
        "ConsentDetail": {
            "consentStart": "2019-12-06T11:39:57.153Z",
            "consentExpiry": "2019-12-06T11:39:57.153Z",
            "consentMode": "VIEW",
            "fetchType": "ONETIME",
            "consentTypes": [
                "PROFILE"
            ],
            "fiTypes": [
                "DEPOSIT"
            ],
            "DataConsumer": {
                "id": "fiu7465374537id"
            },
            "Customer": {
                "id": "customer_identifier@AA_identifier"
            },
            "Purpose": {
                "code": "101",
                "refUri": "https://api.rebit.org.in/aa/purpose/101.xml",
                "text": "Wealth management service",
                "Category": {
                    "type": "string"
                }
            },
            "FIDataRange": {
                "from": "2018-12-06T11:39:57.153Z",
                "to": "2019-12-06T11:39:57.153Z"
            },
            "DataLife": {
                "unit": "MONTH",
                "value": 0
            },
            "Frequency": {
                "unit": "HOUR",
                "value": 1
            },
            "DataFilter": [
                {
                    "type": "TRANSACTIONAMOUNT",
                    "operator": ">=",
                    "value": "20000"
                }
            ]
        }
    })
    headers = {
        'Content-Type': 'application/json'
    }

    response = requests.request("POST", url, headers=headers, data=payload)
    print(response.json())

    return jsonify(response_body)


@app.route('/consent/handle/<consent_handle>', methods=['GET'])
def get_consent_handle(consent_handle):

    url = "http://127.0.0.1:8082/consent/handle/request"

    payload = json.dumps({
        "ver": "1.0",
        "timestamp": "2023-05-29T07:48:51.054Z",
        "txnid": "4a4adbbe-29ae-11e8-a8d7-0289437bf331",
        "ConsentDetail": {
            "consentStart": "2019-12-06T11:39:57.153Z",
            "consentExpiry": "2019-12-06T11:39:57.153Z",
            "consentMode": "VIEW",
            "fetchType": "ONETIME",
            "consentTypes": [
                "PROFILE"
            ],
            "fiTypes": [
                "DEPOSIT"
            ],
            "DataConsumer": {
                "id": "fiu7465374537id"
            },
            "Customer": {
                "id": "customer_identifier@AA_identifier"
            },
            "Purpose": {
                "code": "101",
                "refUri": "https://api.rebit.org.in/aa/purpose/101.xml",
                "text": "Wealth management service",
                "Category": {
                    "type": "string"
                }
            },
            "FIDataRange": {
                "from": "2018-12-06T11:39:57.153Z",
                "to": "2019-12-06T11:39:57.153Z"
            },
            "DataLife": {
                "unit": "MONTH",
                "value": 0
            },
            "Frequency": {
                "unit": "HOUR",
                "value": 1
            },
            "DataFilter": [
                {
                    "type": "TRANSACTIONAMOUNT",
                    "operator": ">=",
                    "value": "20000"
                }
            ]
        }
    })
    headers = {
        'Content-Type': 'application/json'
    }

    Status = mockAaResponseObj.getResponse("status")

    response = requests.request("POST", url, headers=headers, data=payload)

    print(response)
    print(response.json())

    response = {
        "ver": "1.0",
        "timestamp": "2018-12-06T11:39:57.153Z",
        "txnid": "795038d3-86fb-4d3a-a681-2d39e8f4fc3c",
        "ConsentHandle": consent_handle,
        "ConsentStatus": {
            "id": "654024c8-29c8-11e8-8868-0289437bf3x31",
            "status": Status
        }
    }

    return response

@app.route('/Consent/Notification/Trigger', methods=['POST'])
def trigger_consent_notification():
    """
    Triggers the /consent/notification
    :returns /consent/notification response.
    """

    import requests
    import json

    url = "http://127.0.0.1:5000/consent/notification"

    payload = json.dumps({
        "ver": "1.0",
        "timestamp": "2018-12-06T11:39:57.153Z",
        "txnid": "0b811819-9044-4856-b0ee-8c88035f8858",
        "Notifier": {
            "type": "AA",
            "id": "AA-1"
        },
        "ConsentStatusNotification": {
            "consentId": "XXXX0-XXXX-XXXX",
            "consentHandle": "39e108fe-9243-11e8-b9f2-0256d88baae8",
            "consentStatus": "PAUSED"
        }
    })
    headers = {
        'Content-Type': 'application/json'
    }

    response = requests.request("POST", url, headers=headers, data=payload)
    print(response.text)
    return response.json()


if __name__ == '__main__':
    app.run(port=5001)
