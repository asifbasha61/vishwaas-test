package com.vishwaas;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Component
public class DatabaseCertificateDataService implements Service{
	 @Value("${certificateApiUrl}")
	    private String certificateApiUrl;	   
	 @ResponseBody
	    @Override
	    public CertificateData getCertificateData() 
	    {
	    	RestTemplate restTemplate = new RestTemplate();
	        ResponseEntity<CertificateData> response = restTemplate.exchange(
	                certificateApiUrl,
	                HttpMethod.GET,
	                null,
	                CertificateData.class
	        );

	        if (response.getStatusCode().is2xxSuccessful()) {
	            return response.getBody();
	        } else {
	            throw new RuntimeException("Failed to fetch certificate data from the API");
	        }
	    }
}
