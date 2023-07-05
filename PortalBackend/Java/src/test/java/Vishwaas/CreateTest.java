package Vishwaas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class CreateTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Token tokenTest;

    @InjectMocks
    private Create create;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateCertificate_Success() {
        CertificateData certificateData = new CertificateData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("valid_token");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<CertificateData> requestEntity = new HttpEntity<>(certificateData, headers);
        ResponseEntity<String> successResponse = new ResponseEntity<>("Certificate created successfully", HttpStatus.OK);

        when(tokenTest.getToken()).thenReturn("valid_token");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(successResponse);

        ResponseEntity<String> response = create.createCertificate(certificateData);

        assertEquals(OK, response.getStatusCode());
        assertEquals("Certificate created successfully", response.getBody());
    }

    @Test
    public void testCreateCertificate_InvalidEndpoint() {
        CertificateData certificateData = new CertificateData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("valid_token");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<CertificateData> requestEntity = new HttpEntity<>(certificateData, headers);
        ResponseEntity<String> invalidEndpointResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        when(tokenTest.getToken()).thenReturn("valid_token");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(invalidEndpointResponse);

        ResponseEntity<String> response = create.createCertificate(certificateData);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCreateCertificate_InvalidToken() {
        CertificateData certificateData = new CertificateData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("invalid_token");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<CertificateData> requestEntity = new HttpEntity<>(certificateData, headers);
        ResponseEntity<String> invalidTokenResponse = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        when(tokenTest.getToken()).thenReturn("invalid_token");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

        ResponseEntity<String> response = create.createCertificate(certificateData);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
  

}
