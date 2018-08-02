package com.payline.payment.tsi.request;

import com.payline.payment.tsi.exception.InvalidRequestException;
import com.payline.payment.tsi.request.mock.TsiPaymentRequestMock;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class TsiGoRequestTest {

    private TsiGoRequest.Builder builder;

    @Before
    public void setup(){
        this.builder = new TsiGoRequest.Builder();
    }

    /**
     * Uses the example given in TSI's Merchant API integration documentation to validate the construction of the message
     * used to seal the request.
     */
    @Test
    public void testSealMessage(){
        // given: the example request and message
        Map<String, Object> custom = new HashMap<>();
        custom.put( "ref", "123456789" );
        TsiGoRequest exampleRequest = new TsiGoRequest(
                430,
                "43b3a1b952dc5c1f2fd2a46162b3aaaa",
                "20",
                "EUR",
                430,
                "Produit de testun",
                "http://boutique.com/returnOK.php",
                "http:// boutique.com/returnNOK.php",
                "http:// boutique.com /returnS2S.php",
                "N",
                "Y",
                custom
        );
        String exampleMessage = "430|43b3a1b952dc5c1f2fd2a46162b3aaaa|20|EUR|430|Produit de " +
                "testun|http://boutique.com/returnOK.php|http:// boutique.com/returnNOK.php|http:// " +
                "boutique.com /returnS2S.php|N|Y";

        // when: building the message from the request
        String sealMessage = exampleRequest.buildSealMessage();

        // then: get the same message as the example
        Assert.assertEquals( exampleMessage, sealMessage );
    }

    @Test
    public void testBuilder_checkInputRequest_ok() throws InvalidRequestException {
        // given: a valid PaymentRequest
        PaymentRequest paymentRequest = (new TsiPaymentRequestMock()).mock();

        // when: checking the request validity,  then: no exception is thrown
        builder.checkInputRequest( paymentRequest );
    }

    /*
    For each case in which checkInputRequest should throw an exception,
    check the TsiGoRequestCheckInputTest class.
     */


    @Test
    public void testBuilder_fromPaymentRequest() throws InvalidRequestException, NoSuchAlgorithmException {
        // given: a valid PaymentRequest
        PaymentRequest paymentRequest = (new TsiPaymentRequestMock()).mock();

        // when: instantiating the TSI request
        TsiGoRequest request = builder.fromPaymentRequest( paymentRequest );

        // then: request has a mac
        Assert.assertNotNull( request.getMac() );
        Assert.assertFalse( request.getMac().isEmpty() );
    }

    @Test
    public void testBuilder_formatAmount_integer(){
        // given: a cents amount with no cents
        BigInteger amount = BigInteger.valueOf( 100 );

        // when: formatting amount, then: result has no separator
        Assert.assertEquals( "1", this.builder.formatAmount( amount ) );
    }

    @Test
    public void testBuilder_formatAmount_noTrailingZero(){
        // given: a cents amount with no cents
        BigInteger amount = BigInteger.valueOf( 102 );

        // when: formatting amount, then: result has a separator and a decimal part
        Assert.assertEquals( "1.02", this.builder.formatAmount( amount ) );
    }

    @Test
    public void testBuilder_formatAmount_trailingZero(){
        // given: a cents amount with no cents
        BigInteger amount = BigInteger.valueOf( 110 );

        // when: formatting the amount, then: result has no trailing zero on the decimal part
        Assert.assertEquals( "1.1", this.builder.formatAmount( amount ) );
    }

}
