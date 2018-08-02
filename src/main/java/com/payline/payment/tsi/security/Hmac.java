package com.payline.payment.tsi.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Digests a Hmac seal with the given algorithm, using javax.crypto classes.
 * @see Mac
 */
public class Hmac {

    private static final Logger logger = LogManager.getLogger( Hmac.class );

    private String algorithm;
    private String key;

    public Hmac( String key, HmacAlgorithm algorithm ){
        this.algorithm = algorithm.toString();
        this.key = key;
    }

    public String digest( String message ){
        String seal = null;

        try {
            // Init Mac instance
            SecretKeySpec key = new SecretKeySpec( this.key.getBytes("UTF-8"), this.algorithm );
            Mac mac = Mac.getInstance( this.algorithm );
            mac.init(key);

            // Process the message and finishes MAC operation
            byte[] bytes = mac.doFinal( message.getBytes( "UTF-8" ) );

            // Convert result to a readable string
            StringBuilder hash = new StringBuilder();
            for( int i = 0; i < bytes.length; i++ ){
                String hex = Integer.toHexString( 0xFF & bytes[ i ] ); // Conversion to hexa using a mask
                if( hex.length() == 1 ){
                    hash.append( '0' );
                }
                hash.append( hex );
            }
            seal = hash.toString();

        } catch( InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException e ){
            logger.error( "An unexpected error occured during the HMAC seal digest : " + e.getMessage(), e );
        }

        return seal;
    }
}
