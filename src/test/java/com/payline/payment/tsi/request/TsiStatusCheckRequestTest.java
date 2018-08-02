package com.payline.payment.tsi.request;

import org.junit.Assert;
import org.junit.Test;

public class TsiStatusCheckRequestTest {

    /**
     * Uses the example given in TSI's Merchant API integration documentation to validate the construction of the message
     * used to seal the request.
     */
    @Test
    public void testSealMessage(){
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
    public void testSealIt(){
        // given: the example request and message
        TsiStatusCheckRequest exampleRequest = new TsiStatusCheckRequest(
                "43b3a1b952dc5c1f2fd2a46162b3cbee",
                441
        );

        // when: sealing the request
        exampleRequest.sealIt();

        // then: the mac field is not null and 32 characters long
        Assert.assertNotNull( exampleRequest.getMac() );
        Assert.assertEquals( 32, exampleRequest.getMac().length() );
    }

}
