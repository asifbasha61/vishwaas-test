import json
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from requests import request as rq
from .constants import header, generate_token_payload, generate_token_url, certificate_header, get_request_method, post_request_method, data, Authorization, Bearer, signedCredentials, _osSignedData, access_token
from .config_reader import read_config

config = read_config()
generate_token_url = config.get('URL', 'generate_token_url')
certificate_url = config.get('URL', 'certificate_url')
verify_certificate_url = config.get('URL', 'verify_certificate_url')


class ResponseHandler:
    def __init__(self):
        pass

    @staticmethod
    def handle_response(status_code, response_data):
        if status_code == 200:
            message = "Success: OK"
        elif status_code == 400:
            message = "Error: Bad Request"
        elif status_code == 401:
            message = "Error: Unauthorized access"
        elif status_code == 404:
            message = "Error: Not Found"
        elif status_code == 500:
            message = "Error: Internal Server Error"
        else:
            status_code == status_code
            message = "Error: Something went wrong"

        response = {
            "status_code": status_code,
            "data": response_data,
            "message": message
        }

        return json.dumps(response)


def call_api(**kwargs):
    '''
    Make an API call using the provided parameters.
    '''
    http_method = kwargs.get('http_method')
    http_url = kwargs.get('http_url')
    http_headers = kwargs.get('http_header')
    http_payload = kwargs.get('http_payload')

    # Make the API call
    api_response = rq(http_method, http_url, headers=http_headers, data=http_payload)
    status_code = api_response.status_code
    response_data = api_response.json()

    # Handle the response using ResponseHandler
    handler = ResponseHandler()
    response = handler.handle_response(status_code, response_data)
    return response


def generate_tokens():
    '''
    Generate tokens by making an API call.
    '''
    api_response = call_api(http_method=post_request_method, http_url=generate_token_url, http_header=header,
                            http_payload=generate_token_payload)
    response = json.loads(api_response)
    return response


def get_entity(access_token):
    '''
    Get entity information using the provided access token.
    '''
    header[Authorization] = Bearer + access_token
    certificate_response = call_api(http_method=get_request_method, http_url=certificate_url, http_header=header)
    response = json.loads(certificate_response)
    return response


def create_certificate(data, access_token):
    '''
    Create a certificate using the provided data and access token.
    '''
    certificate_header[Authorization] = Bearer + access_token
    certificate_response = call_api(http_method=post_request_method, http_url=certificate_url,
                                    http_header=certificate_header, http_payload=data)
    response = json.loads(certificate_response)
    return response


class Certificate(APIView):
    def post(self, request):
        '''
        Handle the POST request to create a certificate.
        '''
        get_token = generate_tokens()  # generate a token
        token = get_token[data][access_token]
        get_the_entity = get_entity(token)  # get the entity information

        if isinstance(get_the_entity[data], list):
            flag = False
            for i in get_the_entity[data]:
                if i['name'] == request.data['name'] and i['certificateid'] == request.data['certificateid'] and \
                        i['entitytype'] == request.data['entitytype'] and i['rebitversion'] == request.data[
                    'rebitversion'] and i['purposecode'] == request.data['purposecode'] and i['fitype'] == request.data[
                    'fitype'] and i['expired'] == request.data['expired']:
                    flag = True
                    break
            if flag:
                return Response({'message': "Certificate already exists"}, status=status.HTTP_409_CONFLICT)
            elif not flag:
                response = create_certificate(json.dumps(request.data), token)
                return Response({"message": "Certificate created", "response": response}, status=status.HTTP_200_OK)
        elif get_the_entity['status_code'] == 404:
            response = create_certificate(json.dumps(request.data), token)
            return Response({"message": "Certificate created", "response": response}, status=status.HTTP_200_OK)


def get_entity_by_id(certificate_id, access_token):
    '''
    Get entity information for a specific certificate ID using the provided access token.
    '''
    certificate_header[Authorization] = Bearer + access_token
    entity_response = call_api(http_method=get_request_method, http_url=certificate_url + "/" + certificate_id,
                               http_header=certificate_header)
    response = json.loads(entity_response)
    return response


def verify_certificate(access_token, payload):
    '''
    Verify a certificate using the provided access token and payload.
    '''
    certificate_header[Authorization] = Bearer + access_token
    verified_response = call_api(http_method=post_request_method, http_url=verify_certificate_url,
                                 http_header=certificate_header, http_payload=payload)
    response = json.loads(verified_response)
    return response


class Verification(APIView):

    def get(self, request, certificate_id):
        '''
        Handle the GET request to verify a certificate.
        '''
        get_token = generate_tokens()
        token = get_token[data][access_token]
        get_the_entity = get_entity_by_id(certificate_id, token)
        unescaped_data = {
            signedCredentials: json.loads(get_the_entity[data][_osSignedData])
        }
        payload = json.dumps(unescaped_data)
        response = verify_certificate(token, payload)
        return Response({"Message:": "Certificate verified", "response": response[data]}, status=status.HTTP_200_OK)

