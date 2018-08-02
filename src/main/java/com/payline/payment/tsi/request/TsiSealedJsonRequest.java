package com.payline.payment.tsi.request;

import com.google.gson.Gson;

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

    public abstract String buildSealMessage();


    protected String getMac(){
        return mac;
    }

    protected void setMac( String mac ){
        this.mac = mac;
    }

}
