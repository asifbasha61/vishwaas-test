package com.vishwaas;


import java.util.Collections;
import java.util.logging.Logger;

import org.apache.commons.text.StringEscapeUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Component
public class Verify 
{
	//Parameter for RC verify certificate
	 	@Value("${baseURL}")
	    private String baseURL;

	    @Value("${rc.port}")
	    private String port;

	    @Value("${rc.Entity}")
	    private String entity;
	    @Value("${rc.Verify}")
	    private String ver;
	    @Autowired
	    private Token tokenTest;
	    private static final Logger logger = Logger.getLogger(Verify.class.getName());

	    //Verify certificate is called to verify the certificate
	    public ResponseEntity<String> verifyCertificate(String osid) throws  JsonProcessingException {

	        String certificateUrl = baseURL + ":" + port + entity + "/" + osid;
	        String postUrl = baseURL + ":" + port + ver;
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.setBearerAuth(tokenTest.getToken());
	        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

	        RestTemplate restTemplate = new RestTemplate();

	        HttpEntity<CertificateData> requestEntity = new HttpEntity<>(headers);

	        ResponseEntity<String> getResponse = restTemplate.exchange(
	                certificateUrl,
	                HttpMethod.GET,
	                requestEntity,
	                String.class
	        );
	        String responseBody = getResponse.getBody();

	        ObjectMapper objectMapper = new ObjectMapper();

	        if (getResponse.getStatusCode() == HttpStatus.OK) {
	            JsonNode jsonNode = objectMapper.readTree(responseBody);
	            String signedData = jsonNode.get("_osSignedData").asText();
	     

	            String unescapedString = StringEscapeUtils.unescapeJava(signedData);

	            JsonNode signedCredentials = objectMapper.readTree(unescapedString);

	            ObjectNode updatedJsonNode = objectMapper.createObjectNode();
	            updatedJsonNode.set("signedCredentials", signedCredentials);

	            String updatedResponseBody = objectMapper.writeValueAsString(updatedJsonNode);

	            HttpHeaders postHeaders = new HttpHeaders();
	            postHeaders.setContentType(MediaType.APPLICATION_JSON);
	            postHeaders.setBearerAuth(tokenTest.getToken());
	            postHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

	            HttpEntity<String> postRequestEntity = new HttpEntity<>(updatedResponseBody, postHeaders);

	            ResponseEntity<String> postResponse = restTemplate.exchange(
	                    postUrl,
	                    HttpMethod.POST,
	                    postRequestEntity,
	                    String.class
	            );

	            if (postResponse.getStatusCode() == HttpStatus.OK) {
	                return new ResponseEntity<>(postResponse.getBody(), HttpStatus.OK);
	            } else {
	                logger.severe("Failed to create certificate. Status code: " + postResponse.getStatusCode());
	                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	            }
	        } else if (getResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
	            logger.warning("Failed to retrieve certificate. Status code: " + getResponse.getStatusCode());
	        }

	        return new ResponseEntity<>(responseBody, getResponse.getStatusCode());
	    }

		

		

}