package com.payline.payment.tsi.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class TsiGoResponse {

    /** The control code returned for a initialization transaction request */
    private int status;
    /** The description of the status */
    private String message;
    /** The url to redirect the customer to the payment panel (if status = 1) */
    private String url;
    /** Unique transaction identifier */
    private String tid;
    /** The key id */
    @SerializedName( "keyid" )
    private String keyId;

    protected TsiGoResponse( int status, String message ){
        this.status = status;
        this.message = message;
    }

    public int getStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }

    public String getUrl(){
        return url;
    }

    public String getTid(){
        return tid;
    }

    public String getKeyId(){
        return keyId;
    }

    /**
     * Implements the builder pattern to instantiate {@link TsiGoResponse} from a JSON string content.
     */
    public static class Builder {

        public TsiGoResponse fromJson( String jsonContent ){
            Gson gson = new Gson();
            return gson.fromJson( jsonContent, TsiGoResponse.class );
        }

    }
}
