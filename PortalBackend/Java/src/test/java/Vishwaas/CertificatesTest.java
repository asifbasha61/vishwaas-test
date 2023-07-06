package Vishwaas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vishwaas.CertificateData;
import com.vishwaas.Certificates;
import com.vishwaas.Token;
import com.vishwaas.ValidateCertificate;
@ExtendWith(MockitoExtension.class)
 class CertificatesTest {
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
