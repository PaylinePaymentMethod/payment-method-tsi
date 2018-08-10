package com.payline.payment.tsi.security;

import org.junit.Assert;
import org.junit.Test;

public class HmacTest {

    /**
     * Uses the example given in the RFC2104 specification to validate the implementation.
     * @see <a href="https://tools.ietf.org/html/rfc2104">https://tools.ietf.org/html/rfc2104 (p.9)</a>
     */
    @Test
    public void testSeal_rfc2104(){
        String message = "what do ya want for nothing?";
        String key = "Jefe";
        String result = "750c783e6ab0b503eaa86e310a5db738";

        Hmac hmac = new Hmac( key, HmacAlgorithm.MD5 );
        Assert.assertEquals( result, hmac.digest( message ) );
    }

    /**
     * Uses the example given on the HMAC wikipedia page to validate the implementation.
     * @see <a href="https://en.wikipedia.org/wiki/HMAC">https://en.wikipedia.org/wiki/HMAC</a>
     */
    @Test
    public void testSeal_wikipedia(){
        String message = "The quick brown fox jumps over the lazy dog";
        String key = "key";
        String result = "80070713463e7749b90c2dc24911e275";

        Hmac hmac = new Hmac( key, HmacAlgorithm.MD5 );
        Assert.assertEquals( result, hmac.digest( message ) );
    }

}
