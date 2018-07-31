package com.payline.payment.tsi.request;

import com.payline.payment.tsi.exception.InvalidRequestException;
import com.payline.payment.tsi.security.Hmac;
import com.payline.payment.tsi.security.HmacAlgorithm;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigInteger;
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

    // TODO: Test checkInputRequest method

    //@Test
    public void testBuilder_fromPaymentRequest() throws InvalidRequestException {
        // given: a valid PaymentRequest
        // TODO: Mock a valid PaymentRequest and enable this test
        PaymentRequest paymentRequest = Mockito.mock( PaymentRequest.class );

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

    @Test
    public void testBuilder_formatTransactionId_shorter(){
        // given: a transaction id shorter than 32 characters
        String transactionId = "TSI4567890123456";

        // when: formatting the transaction id
        String formatted = this.builder.formatTransactionId( transactionId );

        // then: result is not null and is 32 characters long
        Assert.assertNotNull( formatted );
        Assert.assertEquals( 32, formatted.length() );
    }

    @Test
    public void testBuilder_formatTransactionId_rightLength(){
        // given: a transaction id 32 characters long
        String transactionId = "TSI45678901234567890123456789012";

        // when: formatting the transaction id
        String formatted = this.builder.formatTransactionId( transactionId );

        // then: result is not null and is 32 characters long
        Assert.assertNotNull( formatted );
        Assert.assertEquals( 32, formatted.length() );
    }

    @Test
    public void testBuilder_formatTransactionId_longer(){
        // given: a transaction id longer than 32 characters
        String transactionId = "TSI45678901234567890123456789012345678901234567890";

        // when: formatting the transaction id
        String formatted = this.builder.formatTransactionId( transactionId );

        // then: result is not null and is 32 characters long
        Assert.assertNotNull( formatted );
        Assert.assertEquals( 32, formatted.length() );
    }

}
