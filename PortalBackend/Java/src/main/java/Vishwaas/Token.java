package Vishwaas;



import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

@Component
@ComponentScan(basePackages = "com.example.Vishwaas")
public class Token {
    @Value("${keycloak.baseurl}")
    private String keycloakUrl;

    @Value("${keycloak.endpoint}")
    private String keycloakendpoint;

    @Value("${keycloak.clientId}")
    private String clientId;

    @Value("${keycloak.grantType}")
    private String grantType;

    @Value("${keycloak.clientSecret}")
    private String clientSecret;

    @Autowired
    private RestTemplate restTemplate;
    public String getKeycloakUrl() {
		return keycloakUrl;
	}

	public void setKeycloakUrl(String keycloakUrl) {
		this.keycloakUrl = keycloakUrl;
	}

	public String getKeycloakendpoint() {
		return keycloakendpoint;
	}

	public void setKeycloakendpoint(String keycloakendpoint) {
		this.keycloakendpoint = keycloakendpoint;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

//	private RestTemplate restTemplate;

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String urlEncoded = urlEncode(clientId, grantType, clientSecret);
        HttpEntity<String> request = new HttpEntity<>(urlEncoded, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                keycloakUrl + keycloakendpoint, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String token = response.getBody();
            JSONObject json = new JSONObject(token);
            String accessToken = json.getString("access_token");

            return accessToken;
        } else {
            System.out.println("Failed to retrieve token. Status code: " + response.getStatusCodeValue());
            
        }
		return null;
		
    }

    private String urlEncode(String clientId, String grantType, String clientSecret) {
        String encodedClientId = UriUtils.encode(clientId, StandardCharsets.UTF_8);
        String encodedGrantType = UriUtils.encode(grantType, StandardCharsets.UTF_8);
        String encodedClientSecret = UriUtils.encode(clientSecret, StandardCharsets.UTF_8);

        StringBuilder sb = new StringBuilder();
        sb.append("client_id=").append(encodedClientId)
                .append("&grant_type=").append(encodedGrantType)
                .append("&client_secret=").append(encodedClientSecret);

        return sb.toString();
    }

	
}
