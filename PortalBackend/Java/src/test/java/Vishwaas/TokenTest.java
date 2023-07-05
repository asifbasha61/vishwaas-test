package Vishwaas;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class TokenTest {
    @Mock
    private RestTemplate restTemplate;
    @Autowired
    private Token token;

    @SuppressWarnings("deprecation")
	@Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        token = new Token();
        token.setRestTemplate(restTemplate);
        token.setKeycloakUrl("http://keycloak:8080");
        token.setKeycloakendpoint("/auth/realms/sunbird-rc/protocol/openid-connect/token");
        token.setClientId("clientId");
        token.setGrantType("grantType");
        token.setClientSecret("clientSecret");
    }


    @Test
    public void testGetToken_SuccessfulRequest_ReturnsAccessToken() {
       
        String accessToken = "testAccessToken";
        String response = "{\"access_token\": \"" + accessToken + "\"}";
        ResponseEntity<String> mockResponse = new ResponseEntity<>(response, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);
      
      
     
        String result = token.getToken();

       
        verify(restTemplate).exchange(eq("http://keycloak:8080/auth/realms/sunbird-rc/protocol/openid-connect/token"), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));

     
        assertEquals(accessToken, result);
    }

    @Test
    public void testGetToken_InvalidClientId_ReturnsNull() {
     
        ResponseEntity<String> mockResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);
       
       
        String result = token.getToken();

       
        verify(restTemplate).exchange(eq("http://keycloak:8080/auth/realms/sunbird-rc/protocol/openid-connect/token"), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));

      
        assertNull(result);
    }

    @Test
    public void testGetToken_InvalidGrantType_ReturnsNull() {
       
        ResponseEntity<String> mockResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

   
        String result = token.getToken();

        verify(restTemplate).exchange(eq("http://keycloak:8080/auth/realms/sunbird-rc/protocol/openid-connect/token"), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));

        
        assertNull(result);
    }

    @Test
    public void testGetToken_WrongEndpoint_ReturnsNull() {
  
        ResponseEntity<String> mockResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

    
        String result = token.getToken();

      
        verify(restTemplate).exchange(eq("http://keycloak:8080/auth/realms/sunbird-rc/protocol/openid-connect/token"), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));

     
        assertNull(result);
    }


	public String getToken() {
		// TODO Auto-generated method stub
		return null;
	}
}

