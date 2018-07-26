package com.payline.payment.tsi.request;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TsiGoRequestTest {

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
                false,
                true,
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

}
