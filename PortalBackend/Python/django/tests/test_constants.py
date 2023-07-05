generate_token_payload = {
    "client_id": "",
    "username": "",
    "password": "",
    "grant_type": ""
}

header = {'content-type': "application/x-www-form-urlencoded"}
certificate_header = {"Content-Type": "application/json"}
generate_token_url = "http://keycloak:8080/auth/realms/sunbird-rc/protocol/openid-connect/token"

get_request_method = 'GET'
post_request_method = 'POST'

create_certificate_body = {
    "name":"Pavankumar",
    "certificateid":"3",
    "entitytype":"Bank",
    "rebitversion":"1.1.2v",
    "purposecode":103,
    "fitype":"fip",
    "expired":"2023-06-06"
}

verify_certificate_payload = {"signedCredentials": {"@context": ["https://www.w3.org/2018/credentials/v1", {"@context": {"@version": 1.1, "@protected": True, "SkillCertificate": {"@id": "https://github.com/sunbird-specs/vc-specs#SkillCertificate", "@context": {"id": "@id", "@version": 1.1, "@protected": True, "skills": "schema:Text"}}, "Person": {"@id": "https://github.com/sunbird-specs/vc-specs#Person", "@context": {"name": "schema:Text"}}, "entitytype": {"@id": "https://github.com/sunbird-specs/vc-specs#entitytype", "@context": {"name": "schema:Text"}}, "purposecode": {"@id": "https://github.com/sunbird-specs/vc-specs#purposecode", "@context": {"name": "schema:Text"}}, "rebitversion": {"@id": "https://github.com/sunbird-specs/vc-specs#rebitversion", "@context": {"name": "schema:Text"}}, "fitype": {"@id": "https://github.com/sunbird-specs/vc-specs#fitype", "@context": {"name": "schema:Text"}}, "expired": {"@id": "https://github.com/sunbird-specs/vc-specs#fitype", "@context": {"name": "schema:Date"}}}}], "type": ["VerifiableCredential"], "issuanceDate": "2021-08-27T10:57:57.237Z", "credentialSubject": {"type": "Person", "name": "Pavankumar1234", "entitytype": "Bank", "purposecode": "103", "rebitversion": "1.1.2v", "fitype": "fip", "expired": ""}, "issuer": "did:web:sunbirdrc.dev/vc/skill", "proof": {"type": "RsaSignature2018", "created": "2023-06-07T11:15:13Z", "verificationMethod": "did:india", "proofPurpose": "assertionMethod", "jws": "eyJhbGciOiJQUzI1NiIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il19..atZzhO8l5BWFZSrd9OMWUx8yxJlNW7Xj_IhfDjkDIjWx6oTCfIqxhYa4asjgN281i6Svd9FvBuESWMEVLKSpEKbPAPQb7abovPFAiiYQ3BGCLh6RE1sQKJ6tl-IWJqIiPP2o9L4SyNZykVbItAbPciS13euENPcIEo1K413MKm5aUlZ2s9ydUudNv1Go7oQOsiTc_eGPXWDeqwrbKNctCH_WnvxKS-T6FpwetDJcS0-P-SS2CitPJ0tQoqvfn1KOx0JB19say0mdxz4FkWgPlogOh3J1cSqXAEcVmZsvfNr07AdgAQPdACEwzcLpQGgLi-9q_0AZmIb99wqaePQZZQ"}}}
data = "data"
access_token = "access_token"
Authorization = "Authorization"
Bearer = "Bearer "
data = "data"
signedCredentials = "signedCredentials"
_osSignedData = "_osSignedData"
status_code = "status_code"
test_config_file_path = "/home/pavan/projects/Testing/vishwaas/tests/test_config.ini"
