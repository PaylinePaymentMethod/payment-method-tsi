package com.payline.payment.tsi.service;

import com.payline.payment.tsi.exception.InvalidRequestException;
import com.payline.payment.tsi.request.TsiGoRequest;
import com.payline.payment.tsi.response.TsiGoResponseTest;
import com.payline.payment.tsi.utils.JsonHttpClient;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.PaymentResponseFailure;
import com.payline.pmapi.bean.payment.response.PaymentResponseRedirect;
import okhttp3.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.mockito.Mockito.*;

@RunWith( MockitoJUnitRunner.class )
public class PaymentServiceImplTest {

    private static final Protocol TEST_HTTP_PROTOCOL = Protocol.HTTP_1_1;

    @Mock private TsiGoRequest.Builder requestBuilder;
    @Mock private JsonHttpClient httpClient;

    @InjectMocks
    private PaymentServiceImpl service;

    @Before
    public void mockRequestBuilder() throws InvalidRequestException, NoSuchAlgorithmException {
        // In most cases, the PaymentRequest-to-TsiGoRequest mapping is not what we want to test. So we mock it for every test.
        when( requestBuilder.fromPaymentRequest( any( PaymentRequest.class ) ) )
                .thenReturn( mock( TsiGoRequest.class ) );
    }

    @Test
    public void testPaymentRequest_ok() throws IOException {
        // when: the HTTP call is a success
        Response response = this.mockResponse( 200, "OK", 1, "OK", "http://redirect-url.com" );
        when( httpClient.doPost( anyString(), anyString(), anyString(), any() ) )
                .thenReturn( response );
        PaymentResponse paymentResponse = service.paymentRequest( mock( PaymentRequest.class ) );

        // then: returned object is an instance of PaymentResponseRedirect
        Assert.assertTrue( paymentResponse instanceof PaymentResponseRedirect );
    }

    @Test
    public void testPaymentRequest_invalidRequest() throws InvalidRequestException, NoSuchAlgorithmException {
        // when: the PaymentRequest is invalid, i.e. the builder throws an exception
        when( requestBuilder.fromPaymentRequest( any( PaymentRequest.class ) ) )
                .thenThrow( InvalidRequestException.class );
        PaymentResponse paymentResponse = service.paymentRequest( mock( PaymentRequest.class ) );

        // then: returned object is an instance of PaymentResponseFailure
        Assert.assertTrue( paymentResponse instanceof PaymentResponseFailure );
    }

    @Test
    public void testPaymentRequest_businessError() throws IOException {
        // when: the HTTP call returns a business error (wrong HMAC for example)
        Response response = this.mockResponse( 200, "OK", 15, "WRONG HMAC", null );
        when( httpClient.doPost( anyString(), anyString(), anyString(), any() ) )
                .thenReturn( response );
        PaymentResponse paymentResponse = service.paymentRequest( mock( PaymentRequest.class ) );

        // then: returned object is an instance of PaymentResponseFailure
        Assert.assertTrue( paymentResponse instanceof PaymentResponseFailure );
    }

    @Test
    public void testPaymentRequest_noResponseBody() throws IOException {
        // when: the HTTP call returns a response without body
        Response response = this.mockResponse( 200, "OK", null, null, null );
        when( httpClient.doPost( anyString(), anyString(), anyString(), any() ) )
                .thenReturn( response );
        PaymentResponse paymentResponse = service.paymentRequest( mock( PaymentRequest.class ) );

        // then: returned object is an instance of PaymentResponseFailure
        Assert.assertTrue( paymentResponse instanceof PaymentResponseFailure );
    }

    @Test
    public void testPaymentRequest_httpError() throws IOException {
        // when: the HTTP call returns a HTTP error (503 Service Unavailable par exemple)
        Response response = this.mockResponse( 503, "Service Unavailable", null, null, null );
        when( httpClient.doPost( anyString(), anyString(), anyString(), any() ) )
                .thenReturn( response );
        PaymentResponse paymentResponse = service.paymentRequest( mock( PaymentRequest.class ) );

        // then: returned object is an instance of PaymentResponseFailure
        Assert.assertTrue( paymentResponse instanceof PaymentResponseFailure );
    }

    @Test
    public void testPaymentRequest_ioException() throws IOException {
        // when: the HTTP call returns an exception
        when( httpClient.doPost( anyString(), anyString(), anyString(), any() ) )
                .thenThrow( IOException.class );
        PaymentResponse paymentResponse = service.paymentRequest( mock( PaymentRequest.class ) );

        // then: returned object is an instance of PaymentResponseFailure
        Assert.assertTrue( paymentResponse instanceof PaymentResponseFailure );
    }

    private Response mockResponse( int httpCode, String httpMessage, Integer bodyStatus, String bodyMessage, String bodyUrl ){
        String jsonBody = null;
        if( bodyStatus != null && bodyMessage != null ){
            jsonBody = TsiGoResponseTest.mockJson( bodyStatus, bodyMessage, bodyUrl, null, null );
        }
        return ( new Response.Builder() )
                .code( httpCode )
                .message( httpMessage )
                .body( jsonBody == null ? null : ResponseBody.create( MediaType.parse( "application/json" ), jsonBody ) )
                .request( (new Request.Builder()).url("http://fake.fr").build() )
                .protocol( TEST_HTTP_PROTOCOL )
                .build();
    }

}
