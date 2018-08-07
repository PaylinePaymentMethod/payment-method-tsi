package com.payline.payment.tsi.request;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.tsi.TsiConstants;
import com.payline.payment.tsi.exception.InvalidRequestException;
import com.payline.payment.tsi.security.Hmac;
import com.payline.payment.tsi.security.HmacAlgorithm;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class TsiStatusCheckRequest extends TsiSealedJsonRequest {

    /** The transaction identifier, 32 characters long exactly */
    @SerializedName( "tid" )
    private String transactionId;
    /** The identifier of the key, provided by TSI */
    @SerializedName( "id" )
    private int keyId;

    protected TsiStatusCheckRequest( String transactionId, int keyId ) {
        this.transactionId = transactionId;
        this.keyId = keyId;
    }

    @Override
    public String buildSealMessage(){
        return this.transactionId + "|" + this.keyId;
    }

    public static class Builder extends TsiSealedJsonRequest.Builder {

        public TsiStatusCheckRequest fromRedirectionPaymentRequest( RedirectionPaymentRequest redirectionPaymentRequest )
                throws InvalidRequestException {

            // Check the input request for NPEs and mandatory fields
            this.checkInputRequest( redirectionPaymentRequest );

            // Instantiate the TsiStatusCheckRequest from input request
            TsiStatusCheckRequest request = new TsiStatusCheckRequest(
                    redirectionPaymentRequest.getRedirectionContext().toString(), // Should contain the tid
                    Integer.parseInt( redirectionPaymentRequest.getContractConfiguration().getContractProperties().get( TsiConstants.CONTRACT_KEY_ID ).getValue() )
            );

            // Seal the request with HMAC algorithm
            String secretKey = redirectionPaymentRequest.getContractConfiguration().getContractProperties().get( TsiConstants.CONTRACT_KEY_VALUE ).getValue();
            this.sealRequest( request, secretKey );

            return request;
        }

        /**
         * Verifies that the input request contains all the required fields.
         *
         * @param redirectionPaymentRequest The input request
         * @throws InvalidRequestException If recovering the field value would result in a NPE or if the value is null or empty.
         */
        protected void checkInputRequest( RedirectionPaymentRequest redirectionPaymentRequest )
                throws InvalidRequestException {

            if( redirectionPaymentRequest == null ){
                throw new InvalidRequestException( "Request must not be null" );
            }
            if( redirectionPaymentRequest.getPaylineEnvironment() == null ){
                throw new InvalidRequestException( "PaylineEnvironment request property must not be null" );
            }

            if( redirectionPaymentRequest.getContractConfiguration() == null
                    || redirectionPaymentRequest.getContractConfiguration().getContractProperties() == null  ){
                throw new InvalidRequestException( "Contract configuration properties object must not be null" );
            }
            if( redirectionPaymentRequest.getContractConfiguration().getContractProperties().get( TsiConstants.CONTRACT_KEY_VALUE ) == null ){
                throw new InvalidRequestException( "Missing contract configuration property: secret key" );
            }
            if( redirectionPaymentRequest.getContractConfiguration().getContractProperties().get( TsiConstants.CONTRACT_KEY_ID ) == null ){
                throw new InvalidRequestException( "Missing contract configuration property: key id" );
            }

            if( redirectionPaymentRequest.getRedirectionContext() == null || redirectionPaymentRequest.getRedirectionContext().toString().isEmpty() ){
                throw new InvalidRequestException( "Redirection context (containing the tid) is required" );
            }
        }

    }
}
