package com.payline.payment.tsi.request;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class TsiSealedJsonRequestTest {

    private TsiSealedJsonRequest.Builder builder;

    @Before
    public void setup(){
        this.builder = new TsiSealedJsonRequest.Builder();
    }

    @Test
    public void testBuilder_formatTransactionId_shorter() throws NoSuchAlgorithmException {
        // given: a transaction id shorter than 32 characters
        String transactionId = "TSI4567890123456";

        // when: formatting the transaction id
        String formatted = this.builder.formatTransactionId( transactionId );

        // then: result is not null and is 32 characters long
        Assert.assertNotNull( formatted );
        Assert.assertEquals( 32, formatted.length() );
    }

    @Test
    public void testBuilder_formatTransactionId_rightLength() throws NoSuchAlgorithmException {
        // given: a transaction id 32 characters long
        String transactionId = "TSI45678901234567890123456789012";

        // when: formatting the transaction id
        String formatted = this.builder.formatTransactionId( transactionId );

        // then: result is not null and is 32 characters long
        Assert.assertNotNull( formatted );
        Assert.assertEquals( 32, formatted.length() );
    }

    @Test
    public void testBuilder_formatTransactionId_longer() throws NoSuchAlgorithmException {
        // given: a transaction id longer than 32 characters
        String transactionId = "TSI45678901234567890123456789012345678901234567890";

        // when: formatting the transaction id
        String formatted = this.builder.formatTransactionId( transactionId );

        // then: result is not null and is 32 characters long
        Assert.assertNotNull( formatted );
        Assert.assertEquals( 32, formatted.length() );
    }

}
