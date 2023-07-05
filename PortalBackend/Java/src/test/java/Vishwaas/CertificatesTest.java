package Vishwaas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

 class CertificatesTest extends VishwaasApplicationTests{
    @Mock
    private Token token;

    @Mock
    private ValidateCertificate certificate;

    @InjectMocks
    private Certificates certificateAPI;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testCertificateAPI_Success() {
        CertificateData certificateData = new CertificateData();  

       
        when(token.getToken()).thenReturn("validToken");

     
        ObjectNode expectedResponse = createResponseObject(HttpStatus.OK, "Success");
        when(certificate.checkCertificate(certificateData)).thenReturn(expectedResponse);

        ObjectNode actualResponse = certificateAPI.certificateAPI(certificateData);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
     void testCertificateAPI_InvalidEndpoint() {
        CertificateData certificateData = new CertificateData();  

        
        when(token.getToken()).thenReturn("validToken");

         HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        when(certificate.checkCertificate(certificateData)).thenThrow(exception);

        assertThrows(HttpClientErrorException.class, () -> {
            certificateAPI.certificateAPI(certificateData);
        });
    }

    @Test
     void testCertificateAPI_InvalidToken() {
        CertificateData certificateData = new CertificateData(); 

        when(token.getToken()).thenReturn("invalidToken");

        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        when(certificate.checkCertificate(certificateData)).thenThrow(exception);

        assertThrows(HttpClientErrorException.class, () -> {
            certificateAPI.certificateAPI(certificateData);
        });
    }

    private ObjectNode createResponseObject(HttpStatus status, String message) {
        ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
        response.put("status", status.value());
        response.put("message", message);
        return response;
    }
}
