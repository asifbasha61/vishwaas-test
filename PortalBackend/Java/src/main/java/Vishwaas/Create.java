package Vishwaas;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;



@Component
public class Create {
    @Value("${baseURL}")
    private String baseURL;

    @Value("${rc.port}")
    private String port;

    @Value("${rc.Entity}")
    private String entity;

   @Autowired
   private Token tokenTest;
   
   @Autowired
   private CertificateData certificateData;

   

    public ResponseEntity<String> createCertificate(@RequestBody CertificateData certificatedata) {
        try {
            String certificateUrl = baseURL + ":" + port + entity;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(tokenTest.getToken());
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            RestTemplate restTemplate = new RestTemplate();

            HttpEntity<CertificateData> requestEntity = new HttpEntity<>(certificatedata, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    certificateUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
          

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Certificate created"); 
                return new ResponseEntity<String>(response.getBody(),HttpStatus.OK);
               		
                
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                System.out.println("Failed to create certificate. Status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
        	
            System.out.println("An error occurred while creating certificate: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create certificate");
    }
}