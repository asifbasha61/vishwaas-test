package com.vishwaas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpHeaders;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class Verification 
{
	//Parameter to Verification API
	@Autowired
	private Verify verify;
	@Autowired
	private CertificateData certificatedata;
	@Autowired
	private Token tokentest;
	@Value("${verifyApiUrl}")
    private String verifyApiUrl;
	
	//Verification API is called to verify the certificate
	@GetMapping(path="/api/v1/Verification/{osid}")
	public ResponseEntity<String> verifyAPI(@PathVariable String osid) throws  JsonProcessingException 
	{ 
	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Authorization", "Bearer " + tokentest.getToken());

	    return verify.verifyCertificate(osid);
	}

}
