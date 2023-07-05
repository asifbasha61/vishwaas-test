package Vishwaas;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class ValidateCertificate {
    @Value("${baseURL}")
    private String baseURL;

    @Value("${rc.port}")
    private String port;

    @Value("${rc.Entity}")
    private String entity;

  

    @Value("${certificateApiUrl}")
    private String certificateApiUrl;

    @Autowired
    private Token tokenTest;

    @Autowired
    private Create create;

	private RestTemplate setRestTemplate;

	private Create setCreateCertificate;
   

    public ObjectNode checkCertificate(@RequestBody CertificateData certificateData) {
        ObjectNode jsonObject = new ObjectMapper().createObjectNode();
        boolean certificateExists = false;
       

        try {
            String accessToken = tokenTest.getToken();
            String certificateUrl = baseURL + ":" + port + entity;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

           RestTemplate restTemplate = new RestTemplate();

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    certificateUrl,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );
            String responseBody = response.getBody();

            if (response.getStatusCode() == HttpStatus.OK && responseBody != null) {
                JsonNode responseJson = new ObjectMapper().readTree(responseBody);

                for (JsonNode certificateJson : responseJson) {
                    if (certificateJson.has("name") && certificateJson.has("entitytype") &&
                            certificateJson.has("rebitversion") && certificateJson.has("purposecode") &&
                            certificateJson.has("fitype") && certificateJson.has("expired")) {

                        if (certificateJson.get("name").asText().equals(certificateData.getName()) &&
                                certificateJson.get("entitytype").asText().equals(certificateData.getEntitytype()) &&
                                certificateJson.get("rebitversion").asText().equals(certificateData.getRebitversion()) &&
                                certificateJson.get("purposecode").asInt() == certificateData.getPurposecode() &&
                                certificateJson.get("fitype").asText().equals(certificateData.getFitype()) &&
                                certificateJson.get("expired").asText().equals(certificateData.getExpired())) {
                            certificateExists = true;
                            break;
                        }
                    }
                }
            }
            	if (certificateExists) {
                jsonObject.put("status", "Certificate  already exists");
                return jsonObject;
            } else {
               // jsonObject.put("status", "Certificate does not exist");
                ResponseEntity<String> createResponse = create.createCertificate(certificateData);
               // System.out.println(createResponse.getBody());
               
                jsonObject = (ObjectNode) new ObjectMapper().readTree(createResponse.getBody());
                
            }
        } catch (Exception e) {
            ResponseEntity<String> createResponse = create.createCertificate(certificateData);
          e.printStackTrace();
        }
		return jsonObject;

        
    }


	public void setRestTemplate(RestTemplate restTemplate) {
		// TODO Auto-generated method stub
		
	}


	


	

	
}