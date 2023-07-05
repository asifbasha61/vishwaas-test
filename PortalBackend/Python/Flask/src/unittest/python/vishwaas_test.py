from src.main.python.vishwaas import get_access_token, add_token_json
import requests
from configparser import ConfigParser
from constant_test import request_body, osid

config = ConfigParser()
config.read('config.ini')
certificate_endpoint = config.get('API', 'certificate_endpoint')
registry_ip = config.get('HOST', 'registry_ip')
application_port = config.get('PORT', 'application_port')
verify_test_url = config.get('TEST', 'verify_test_url')
create_entity_url = config.get('API', 'create_entity_url')
get_entity_url = config.get('API', 'get_entity_url')
verification_url = config.get('API', 'verification_url')


def test_endpoint_validation():
    token = get_access_token(client_id="registry-frontend", username="9876543210",
                             password="abcd@123", grant_type="password").get("access_token")
    header = add_token_json(token)
    response = requests.post(registry_ip+application_port+certificate_endpoint, json=request_body,
                             headers=header)
    assert response.status_code == 200


def test_token_generate():
    response = get_access_token(client_id="registry-frontend", username="9876543210",
                                password="abcd@123", grant_type="password")
    assert response.get("token_type") == "Bearer"


def test_exists_certificate():
    token = get_access_token(client_id="registry-frontend", username="9876543210",
                             password="abcd@123", grant_type="password").get("access_token")
    header = add_token_json(token)
    response = requests.get(get_entity_url+osid, headers=header)
    assert response.status_code == 200


def test_certificate_creation():
    token = get_access_token(client_id="registry-frontend", username="9876543210",
                             password="abcd@123", grant_type="password").get("access_token")
    header = add_token_json(token)
    response = requests.post(create_entity_url, json=request_body, headers=header)
    assert response.status_code == 200


def test_certificate_verify():
    token = get_access_token(client_id="registry-frontend", username="9876543210",
                             password="abcd@123", grant_type="password").get("access_token")
    header = add_token_json(token)
    response = requests.get(registry_ip + application_port + verify_test_url + osid, headers=header)
    assert response.status_code == 200
