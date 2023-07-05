package Vishwaas;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import com.fasterxml.jackson.databind.node.ObjectNode;

class YourTestClass extends VishwaasApplicationTests {
    private static final String APPLICATION_PROPERTIES_FILE = "application.properties";

    @Value("${keycloak.clientId}")
    private String clientId;

    @Value("${keycloak.grantType}")
    private String grantType;

    @Value("${keycloak.clientSecret}")
    private String clientSecret;

    @Value("${keycloak.baseurl}")
    private String keycloakUrl;

    @Value("${keycloak.endpoint}")
    private String keycloakEndpoint;

    
    void testGetToken() {
      
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String encodedClientId = UriUtils.encode(clientId, StandardCharsets.UTF_8);
        String encodedGrantType = UriUtils.encode(grantType, StandardCharsets.UTF_8);
        String encodedClientSecret = UriUtils.encode(clientSecret, StandardCharsets.UTF_8);

        StringBuilder sb = new StringBuilder();
        sb.append("client_id=").append(encodedClientId)
                .append("&grant_type=").append(encodedGrantType)
                .append("&client_secret=").append(encodedClientSecret);

        HttpEntity<String> request = new HttpEntity<>(sb.toString(), headers);
        ResponseEntity<String> response = restTemplate.exchange(keycloakUrl + keycloakEndpoint, HttpMethod.POST, request, String.class);

        HttpStatus statusCode = (HttpStatus) response.getStatusCode();
        String responseBody = response.getBody();

        if (statusCode.is2xxSuccessful()) {
            // Success status codes (2xx)
            assertEquals(HttpStatus.OK, statusCode);
            assertNotNull(responseBody);
            System.out.println("Pass: Request successful. Response: " + responseBody);
        } else if (statusCode.is4xxClientError()) {
            // Client error status codes (4xx)
            if (statusCode == HttpStatus.BAD_REQUEST) {
                // Invalid client ID
                assertEquals("Provide a valid client ID.", responseBody);
                System.out.println("Pass: Invalid client ID. Response: " + responseBody);
            } else {
                fail("Unexpected client error: " + statusCode);
                System.out.println("Fail: Unexpected client error. Status code: " + statusCode);
            }
        } else if (statusCode.is5xxServerError()) {
          
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, statusCode);
           
            assertNotNull(responseBody);
            System.out.println("Pass: Internal server error. Response: " + responseBody);
        } else {
          
            fail("Unexpected status code: " + statusCode);
            System.out.println("Fail: Unexpected status code. Status code: " + statusCode);
        }
    }

    
    public ObjectNode certificateAPi(CertificateData certificatedata) {
       
        return null;
    }
}
