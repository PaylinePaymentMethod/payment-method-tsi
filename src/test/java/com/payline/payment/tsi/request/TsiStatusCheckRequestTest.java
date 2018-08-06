package com.payline.payment.tsi.request;

import com.payline.payment.tsi.exception.InvalidRequestException;
import com.payline.payment.tsi.request.mock.TsiRedirectionPaymentRequestMock;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class TsiStatusCheckRequestTest {

    private TsiStatusCheckRequest.Builder builder;

    @Before
    public void setup() {
        this.builder = new TsiStatusCheckRequest.Builder();
    }

    /**
     * Uses the example given in TSI's Merchant API integration documentation to validate the construction of the message
     * used to seal the request.
     */
    @Test
    public void testSealMessage() {
        // given: the example request and message
        TsiStatusCheckRequest exampleRequest = new TsiStatusCheckRequest(
                "43b3a1b952dc5c1f2fd2a46162b3cbee",
                441
        );
        String exampleMessage = "43b3a1b952dc5c1f2fd2a46162b3cbee|441";

        // when: building the message from the request
        String sealMessage = exampleRequest.buildSealMessage();

        // then: get the same message as the example
        Assert.assertEquals( exampleMessage, sealMessage );
    }

    @Test
    public void testBuilder_checkInputRequest_ok() throws InvalidRequestException {
        // given: a valid RedirectionPaymentRequest
        RedirectionPaymentRequest redirectionPaymentRequest = ( new TsiRedirectionPaymentRequestMock() ).mock();

        // when: checking the request validity,  then: no exception is thrown
        builder.checkInputRequest( redirectionPaymentRequest );
    }

    /*
    For each case in which checkInputRequest should throw an exception,
    check the TsiStatusCheckRequestCheckInputTest class.
     */

    @Test
    public void testBuilder_fromPaymentRequest() throws InvalidRequestException {
        // given: a valid RedirectionPaymentRequest
        RedirectionPaymentRequest redirectionPaymentRequest = (new TsiRedirectionPaymentRequestMock()).mock();

        // when: instantiating the TSI request
        TsiStatusCheckRequest request = builder.fromRedirectionPaymentRequest( redirectionPaymentRequest );

        // then: request has a mac
        Assert.assertNotNull( request.getMac() );
        Assert.assertFalse( request.getMac().isEmpty() );
    }

    public static TsiStatusCheckRequest sample(){
        return new TsiStatusCheckRequest(
                "1234567890",
                123
        );
    }

}
