package Vishwaas;

import org.junit.jupiter.api.BeforeEach;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ValidateCertificateTest {

    @InjectMocks
    private ValidateCertificate validateCertificate;

    @Mock
    private Token tokenTest;

    @Mock
    private Create create;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckCertificate_ExistingCertificate() {
        CertificateData certificateData = new CertificateData();
        certificateData.setName("Test Certificate");
        certificateData.setEntitytype("Entity Type");
        certificateData.setRebitversion("1.0");
        certificateData.setPurposecode(1);
        certificateData.setFitype("FIT");
        certificateData.setExpired("false");

        RestTemplate restTemplate = mock(RestTemplate.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("mocked_access_token");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(new ResponseEntity<>("[{\"name\":\"Test Certificate\",\"entitytype\":\"Entity Type\",\"rebitversion\":\"1.0\",\"purposecode\":1,\"fitype\":\"FIT\",\"expired\":\"false\"}]", HttpStatus.OK));

        when(tokenTest.getToken()).thenReturn("mocked_access_token");
        when(create.createCertificate(any(CertificateData.class))).thenReturn(ResponseEntity.ok("{\"status\":\"Certificate created\"}"));
        validateCertificate.setRestTemplate(restTemplate);

        ObjectNode result = validateCertificate.checkCertificate(certificateData);

        verify(tokenTest).getToken();
        verify(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        );
        verify(create, never()).createCertificate(any(CertificateData.class));

        assertEquals("Certificate already exists", result.get("status").asText());
    }

    @Test
    void testCheckCertificate_NonExistingCertificate() {
        CertificateData certificateData = new CertificateData();
        certificateData.setName("Test Certificate");
        certificateData.setEntitytype("Entity Type");
        certificateData.setRebitversion("1.0");
        certificateData.setPurposecode(1);
        certificateData.setFitype("FIT");
        certificateData.setExpired("false");

        RestTemplate restTemplate = mock(RestTemplate.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("mocked_access_token");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(new ResponseEntity<>("[]", HttpStatus.OK));

        ResponseEntity<String> createResponse = ResponseEntity.ok("{\"status\":\"Certificate created\"}");
        when(tokenTest.getToken()).thenReturn("mocked_access_token");
        when(create.createCertificate(any(CertificateData.class))).thenReturn(createResponse);
        validateCertificate.setRestTemplate(restTemplate);

        ObjectNode result = validateCertificate.checkCertificate(certificateData);

        verify(tokenTest).getToken();
        verify(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        );
        verify(create).createCertificate(any(CertificateData.class));

        assertEquals("Certificate created", result.get("status").asText());
    }
}




