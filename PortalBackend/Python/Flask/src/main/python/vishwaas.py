from flask import Flask, request, jsonify
import requests
import uuid
import json
from configparser import ConfigParser
from constant import token_generate_header, header_json, certificate_exists_failure_response_code, \
    token_failure_response_code, verification_failure_response_code, signedCredentials_key, osid_key, \
    entity_exists_or_not_failure_response_code, name_key, entity_failure_response_code

app = Flask(__name__)

config = ConfigParser()
config.read('config.ini')
certificate_endpoint = config.get('API', 'certificate_endpoint')
verification_endpoint = config.get('API', 'verification_endpoint')
token_url = config.get('API', 'token_url')
create_entity_url = config.get('API', 'create_entity_url')
get_entity_url = config.get('API', 'get_entity_url')
verification_url = config.get('API', 'verification_url')
entity_verify_keys = config.get('ENTITY', 'entity_verify_keys')


def unique_osid():
    """
    Generates a unique identifier (OSID) using UUID.

    Input: None

    Output: str (unique OSID)
    """
    osid = str(uuid.uuid4())
    return osid


def add_token_json(token):
    """
    Adds an authorization token to the header JSON.

    Input:
    - token: str (access token)

    Output: dict (updated header JSON with authorization token)
    """
    if token != None:
        newdata = {"Authorization": "Bearer " + token}
        header_json.update(newdata)
    return header_json


@app.route(certificate_endpoint, methods=['POST'])
def check_certificate():
    """
    Validates the certificate data and creates a new certificate if it doesn't already exist.
    Returns appropriate response based on the existence of the certificate.

    Input: JSON
    The data given in input contains certificate information

    Output: JSON response
    """

    token = get_access_token(client_id="registry-frontend", username="9876543210",
                             password="abcd@123", grant_type="password").get("access_token")
    header = add_token_json(token)
    certificate_data = request.json
    entity_exists = entity_exists_or_not(certificate_data, header)

    if entity_exists.get("responseCode") != entity_failure_response_code:
        response = entity_exists
    else:
        response = create_certificate(certificate_data, header)
    return jsonify(response), 200


def get_access_token(**kwargs):
    """
    Retrieves an access token for authentication using the provided parameters.

    Input:
    - client_id: str (client ID for authentication)
    - username: str (username for authentication)
    - password: str (password for authentication)
    - grant_type: str (type of grant)

    Output: dict (access token data)
    """
    response = requests.post(token_url, data=kwargs, headers=token_generate_header)
    if response.status_code == 200:
        data = response.json()
        return data

    else:
        response_code = token_failure_response_code
        response = {
            "responseCode": response_code
        }
        return response


def entity_exists_or_not(certificate_data, header):
    """
    Checks if an entity with the given certificate_data already exists in the registry.

    :param:
    - certificate_data: dict (entity data)
    - header: dict (header JSON with authorization token)

    Output: dict or None (entity data if found, None if not found)
    """
    matching_count = 0
    entity_keys = entity_verify_keys.split('|')
    response = requests.get(get_entity_url, headers=header)
    entities = json.loads(response.text)
    for entity in entities:
        if entity[name_key] == certificate_data[name_key]:
            for key in entity_keys:
                if entity[key] == certificate_data[key]:
                    matching_count += 1
            break

    if matching_count == len(entity_keys):
        response_code = certificate_exists_failure_response_code
        response = {
            "responseCode": response_code
        }
        return response

    elif matching_count > 0:
        response_code = entity_exists_or_not_failure_response_code
        response = {
            "responseCode": response_code
        }
        return response
    else:
        response_code = entity_failure_response_code
        response = {
            "responseCode": response_code
        }
        return response


def entity(key, value, header):
    """
    Whatever key and value we are giving it will fetch that particular entity and return.

    :param:
    - key: str (entity property key)
    - value: str (entity property value)
    - header: dict (header JSON with authorization token)

    Output: dict or None (entity data if found, None if not found)
    """
    response = requests.get(get_entity_url, headers=header)
    entities = json.loads(response.text)
    for entity in entities:
        if entity[key] == value:
            return entity


def create_certificate(certificate_data, header):
    """
    Creates a new certificate entity in the registry using the provided data.

    Input:
    - certificate_data: dict (certificate information)
    - header: dict (header JSON with authorization token)

    Output: dict (response data)
    """

    keys = ['name', 'entitytype', 'rebitversion', 'purposecode', 'fitype', 'expired']
    certificate_data = {key: certificate_data.get(key) for key in keys}

    response = requests.post(create_entity_url, json=certificate_data, headers=header)
    return response.json()


@app.route(verification_endpoint, methods=['GET'])
def verify_certificate(osid):
    """
    Verifies the certificate using its OSID (unique identifier).

    Input:
    - osid: str (unique OSID of the certificate)

    Output: JSON response
    """
    result = certification_verify(osid)
    return result


def certification_verify(osid):
    """
    Performs the verification of a certificate using its OSID.

    Input:
    Escape the characters from the osSignedData and add signedCredentials as
    key and osSignedData as value to the verify API

    Output: JSON response
    """
    token = get_access_token(client_id="registry-frontend", username="9876543210",
                             password="abcd@123", grant_type="password").get("access_token")
    header = add_token_json(token)
    get_entity = entity(osid_key, osid, header)
    unescaped_data = {
        signedCredentials_key: json.loads(get_entity['_osSignedData'])
    }
    payload = json.dumps(unescaped_data)
    response = requests.post(verification_url, data=payload, headers=header)
    if response.status_code == 200:
        return response.json()
    else:
        response_code = verification_failure_response_code
        response = {
            "responseCode": response_code
        }
        return response, 400


if __name__ == '__main__':
    app.run()
