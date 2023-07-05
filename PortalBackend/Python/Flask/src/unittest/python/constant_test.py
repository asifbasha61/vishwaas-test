certificate_exists_failure_response_code = "CERTIFICATE_EXISTS"
token_failure_response_code = "FAILED TO GET ACCESS TOKEN"
verification_failure_response_code = "SOMETHING WENT WRONG"
entity_exists_or_not_failure_response_code = "PROVIDE VALID ENTITY DATA"
entity_failure_response_code = "CREATE CERTIFICATE"

token_generate_header = {"Content-Type": "application/x-www-form-urlencoded"}
header_json = {
        "content-type": "application/json"
    }

osid = "1-630804af-b749-4cb5-8f7c-d7c080257027"

request_body = {
        "name": "aaa",
        "entitytype": "FIU",
        "rebitversion": "1.1.2v",
        "purposecode": 103,
        "expired": "2023-12-12",
        "fitype": "fip"
    }

signedCredentials_key = 'signedCredentials'
osid_key = "osid"
name_key = "name"
