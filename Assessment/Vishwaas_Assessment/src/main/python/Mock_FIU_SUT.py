import requests
from flask import Flask, request, jsonify

app = Flask(__name__)


@app.route('/consent/notification', methods=['POST'])
def consent_notification():
    request_body = request.get_json()
    # print(request_body)

    # Extracting required data from the request body
    ver = request_body['ver']
    timestamp = request_body['timestamp']
    txnid = request_body['txnid']
    consent_id = request_body['ConsentStatusNotification']['consentId']
    consent_handle = request_body['ConsentStatusNotification']['consentHandle']
    consent_status = request_body['ConsentStatusNotification']['consentStatus']

    # Creating the response body
    response_body = {
        "ver": ver,
        "timestamp": timestamp,
        "txnid": txnid,
        "response": "OK",
        "consent_handle": consent_handle
    }

    import requests
    import json

    url = "http://127.0.0.1:8082/consent/notification/response"

    data = request.get_json()

    headers = {
        'Content-Type': 'application/json'
    }

    response = requests.request("POST", url, headers=headers, data=json.dumps(data))

    print(response.text)

    return jsonify(response_body)


@app.route('/Consent/trigger', methods=['POST'])
def trigger_consent():
    """
    Trigger api to call /consent
    :return: /consent response is returned
    """

    # data = request.get_json()
    # FIU to AA
    api_url = 'http://127.0.0.1:5001/consent'
    #
    request_body = {
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
    }
    #
    response = requests.post(api_url, json=request_body)
    # print(response.text)
    return response.json()
    # return {"result":"OK"}


@app.route('/Consent/handle/trigger', methods=['POST'])
def trigger_consent_handle():
    """
    Trigger the /consent/handle
    return: /consent/handle response
    """
    consent_handle = "39e108fe-9243-11e8-b9f2-0256d88baae8"
    url = f"http://localhost:5001/consent/handle/{consent_handle}"
    # print(url)
    consent_handle_res = requests.get(url)
    print(consent_handle_res)

    return consent_handle_res.json()


if __name__ == '__main__':
    app.run(port=5000)
