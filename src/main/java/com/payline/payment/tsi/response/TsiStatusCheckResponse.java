package com.payline.payment.tsi.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class TsiStatusCheckResponse {

    /** Authorization id */
    private String authId;
    /** Transaction identifier */
    private String tid;
    /** Transaction status: OK, NOK or ER */
    private String status;
    /** Error code: 0 or 1 */
    @SerializedName( "ercode" )
    private String erCode;
    /** Return message. "SUCCESSFUL TRANSACTION FOUND' in case of success. */
    private String message;
    /** Transaction's amount (format: XX,XX) */
    private String amount;
    /** Is there more than one voucher used for the transaction: 't' or 'f' */
    private String multi;
    /** Transaction timestamp (YYYY-MM-DD HH24:MI:SS) */
    private String dtime;
    /** List of ISO3 voucher's country, separated by a pipe */
    private String country;

    public TsiStatusCheckResponse( String authId, String tid, String status, String erCode, String message,
                                   String amount, String multi, String dtime, String country ) {
        this.authId = authId;
        this.tid = tid;
        this.status = status;
        this.erCode = erCode;
        this.message = message;
        this.amount = amount;
        this.multi = multi;
        this.dtime = dtime;
        this.country = country;
    }

    public String getAuthId() {
        return authId;
    }

    public String getTid() {
        return tid;
    }

    public String getStatus() {
        return status;
    }

    public String getErCode() {
        return erCode;
    }

    public boolean isError(){
        return this.erCode == "1";
    }

    public String getMessage() {
        return message;
    }

    public String getAmount() {
        return amount;
    }

    public String getMulti() {
        return multi;
    }

    public String getDtime() {
        return dtime;
    }

    public String getCountry() {
        return country;
    }

    public String getResume(){
        return new GsonBuilder().create().toJson(this);
    }

    /**
     * Implements the builder pattern to instantiate {@link TsiStatusCheckResponse} from a JSON string content.
     */
    public static class Builder {

        public TsiStatusCheckResponse fromJson( String jsonContent ){
            Gson gson = new Gson();
            return gson.fromJson( jsonContent, TsiStatusCheckResponse.class );
        }

    }
}
