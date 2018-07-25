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

        Hmac hmac = new Hmac( message, key, HmacAlgorithm.MD5 );
        Assert.assertEquals( result, hmac.seal() );
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

        Hmac hmac = new Hmac( message, key, HmacAlgorithm.MD5 );
        Assert.assertEquals( result, hmac.seal() );
    }

    /**
     * Uses the example given on TSI's Merchant API integration documentation to validate the implementation.
     */
    @Test
    public void testSeal_tsiApiDoc(){
        String message = "430|43b3a1b952dc5c1f2fd2a46162b3aaaa|20|EUR|430|Produit de" +
                "testun|http://boutique.com/returnOK.php|http://boutique.com/returnNOK.php|http://" +
                "boutique.com/returnS2S.php|N|Y";
        String key = "88765cf86c5a9c8d8b6382c1d89afa34";
        String result = "28610617d6b40d85f1ea734226f8b2f5";

        Hmac hmac = new Hmac( message, key, HmacAlgorithm.MD5 );
        Assert.assertEquals( result, hmac.seal() );
    }

}
