package com.payline.payment.tsi.request;

import com.google.gson.Gson;
import com.payline.payment.tsi.security.Hmac;
import com.payline.payment.tsi.security.HmacAlgorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Abstract class for a sealed JSON request (containing a HMAC seal calculated from the request's fields)
 */
public abstract class TsiSealedJsonRequest {

    /** The HMAC seal for the request (generated from the other request fields) */
    private String mac;

    /**
     * Builds the request body.
     *
     * @return a JSON formatted string.
     */
    public String buildBody(){
        return (new Gson()).toJson( this );
    }

    /**
     * Builds the message that will be digested by HMAC algorithm to calculate the request's seal.
     *
     * @return a string containing the request's fields
     */
    public abstract String buildSealMessage();

    protected String getMac(){
        return mac;
    }

    public void seal( String secretKey ){
        Hmac hmac = new Hmac( secretKey, HmacAlgorithm.MD5 );
        this.mac = hmac.digest( this.buildSealMessage() );
    }

    public static class Builder {

        /**
         * Hashes the input transaction id with MD5 to generate a 32-characters-long unique
         * transaction identifier according to TSI API specifications.
         *
         * @param transactionId The input transaction id
         * @return A 32-characters-long transaction id
         */
        public String formatTransactionId(String transactionId) throws NoSuchAlgorithmException {
            // Generate the MD5 hash
            byte[] hashBytes = MessageDigest.getInstance( "MD5" ).digest( transactionId.getBytes() );

            // Convert it in a readable string
            StringBuilder hash = new StringBuilder();
            for( int i = 0; i < hashBytes.length; i++ ){
                String hex = Integer.toHexString( 0xFF & hashBytes[ i ] ); // Conversion to hexa using a mask
                if( hex.length() == 1 ){
                    hash.append( '0' );
                }
                hash.append( hex );
            }

            return hash.toString();
        }

    }

}
