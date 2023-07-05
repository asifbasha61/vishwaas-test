package Vishwaas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class Certificates {
    @Value("${baseURL}")
    private String baseURL;

    @Value("${rc.port}")
    private String port;

    @Value("${certificateApiUrl}")
    private String certificateApiUrl;

    @Autowired
    private Token token;

    public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	@Autowired
    private ValidateCertificate certificate;

    @Autowired
    private CertificateData certificateData;

    @PostMapping(path = "/api/v1/Certificates", consumes = "application/json")
    public ObjectNode certificateAPI(@RequestBody CertificateData certificateData) {
		

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token.getToken());
        HttpEntity<CertificateData> requestEntity = new HttpEntity<>(certificateData, headers);

        return certificate.checkCertificate(certificateData);
    }

	
}

	


	
	  

