package com.payline.payment.tsi.request;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.tsi.security.Hmac;
import com.payline.payment.tsi.security.HmacAlgorithm;

public class TsiStatusCheckRequest extends TsiSealedJsonRequest {

    /** The transaction identifier, 32 characters long exactly */
    @SerializedName( "tid" )
    private String transactionId;
    /** The identifier of the key, provided by TSI */
    @SerializedName( "id" )
    private int keyId;

    public TsiStatusCheckRequest( String transactionId, int keyId ) {
        this.transactionId = transactionId;
        this.keyId = keyId;
    }

    @Override
    public String buildSealMessage(){
        return this.transactionId + "|" + this.keyId;
    }

    public TsiStatusCheckRequest sealIt(){
        // TODO: externalize key definition in a properties file
        Hmac hmac = new Hmac( "45f3bcf660df19f8364c222e887300fa", HmacAlgorithm.MD5 );
        this.setMac( hmac.digest( this.buildSealMessage() ) );
        return this;
    }
}
