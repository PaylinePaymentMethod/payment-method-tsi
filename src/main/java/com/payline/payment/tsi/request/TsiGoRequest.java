package com.payline.payment.tsi.request;

import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class TsiGoRequest {

    // Mandatory fields
    private String mac;
    private int merchantId;
    private String transactionId;
    private String amount;
    private String currency;
    private int keyId;
    private String productDescription;
    private String urlOk;
    private String urlNok;
    private String urlS2s;
    private boolean debitAll;
    private boolean isTest;

    // Non mandatory fields
    private Map<String, Object> s2sRequestParameters;

    public TsiGoRequest( int merchantId, String transactionId, String amount, String currency, int keyId,
                         String productDescription, String urlOk, String urlNok, String urlS2s, boolean debitAll,
                         boolean isTest, Map<String, Object> s2sRequestParameters ){
        this.merchantId = merchantId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.currency = currency;
        this.keyId = keyId;
        this.productDescription = productDescription;
        this.urlOk = urlOk;
        this.urlNok = urlNok;
        this.urlS2s = urlS2s;
        this.debitAll = debitAll;
        this.isTest = isTest;
        this.s2sRequestParameters = s2sRequestParameters;
    }

    /**
     * Constructs the string message that will be used to calculate the request's HMAC seal.
     * @return The message
     */
    public String buildSealMessage(){
        return this.merchantId + "|"
                + this.transactionId + "|"
                + this.amount + "|"
                + this.currency + "|"
                + this.keyId + "|"
                + this.productDescription + "|"
                + this.urlOk + "|"
                + this.urlNok + "|"
                + this.urlS2s + "|"
                + (this.debitAll ? "Y" : "N") + "|"
                + (this.isTest ? "Y" : "N");

    }

    /**
     * Build the map representing the body of the request. It can then be converted into JSON.
     * keys are strings, values can be of different types.
     *
     * @return The key-value map containing the content of the request.
     */
    public Map<String, Object> buildBodyMap(){
        Map<String, Object> bodyMap = new HashMap<>();

        bodyMap.put( "mac", this.mac );
        bodyMap.put( "mid", this.merchantId ); // TODO: attention au format ! (int demandé)
        bodyMap.put( "tid", this.transactionId );
        bodyMap.put( "amount", this.amount ); // TODO: attention au format ! (float demandé)
        bodyMap.put( "currency", this.currency );
        bodyMap.put( "key_id", this.keyId ); // TODO: attention au format ! (int demandé)
        bodyMap.put( "product_desc", this.productDescription );
        bodyMap.put( "url_ok", this.urlOk );
        bodyMap.put( "url_nok", this.urlNok );
        bodyMap.put( "url_s2s", this.urlS2s );
        bodyMap.put( "debit_all", (this.debitAll ? "Y" : "N") );
        bodyMap.put( "th", (this.isTest ? "Y" : "N") );
        if( this.s2sRequestParameters != null ){
            bodyMap.put( "custom", this.s2sRequestParameters );
        }

        return bodyMap;
    }

    public void setMac( String mac ){
        this.mac = mac;
    }
}
