package com.payline.payment.tsi.request;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.tsi.TsiConstants;
import com.payline.payment.tsi.exception.InvalidRequestException;
import com.payline.pmapi.bean.payment.ContractConfiguration;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import com.payline.pmapi.bean.payment.request.TransactionStatusRequest;

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

        public TsiStatusCheckRequest fromTransactionStatusRequest(final TransactionStatusRequest transactionStatusRequest) throws InvalidRequestException {
            this.checkInputRequest(transactionStatusRequest.getContractConfiguration());

            final TsiStatusCheckRequest request = new TsiStatusCheckRequest(
                    transactionStatusRequest.getTransactionId(),
                    Integer.parseInt(transactionStatusRequest.getContractConfiguration().getContractProperties().get( TsiConstants.CONTRACT_KEY_ID ).getValue())
            );

            return build(transactionStatusRequest.getContractConfiguration(), request);
        }

        public TsiStatusCheckRequest fromRedirectionPaymentRequest(final  RedirectionPaymentRequest redirectionPaymentRequest ) throws InvalidRequestException {
            this.checkInputRequest(redirectionPaymentRequest.getContractConfiguration());

            if( redirectionPaymentRequest.getRequestContext().getRequestData() == null || !redirectionPaymentRequest.getRequestContext().getRequestData().containsKey(TsiConstants.REQUEST_CONTEXT_KEY_TID) ){
                throw new InvalidRequestException( "Redirection context (containing the tid) is required" );
            }

            final TsiStatusCheckRequest request = new TsiStatusCheckRequest(
                    redirectionPaymentRequest.getRequestContext().getRequestData().get(TsiConstants.REQUEST_CONTEXT_KEY_TID),
                    Integer.parseInt(redirectionPaymentRequest.getContractConfiguration().getContractProperties().get( TsiConstants.CONTRACT_KEY_ID ).getValue())
            );

            return build(redirectionPaymentRequest.getContractConfiguration(), request);
        }

        private TsiStatusCheckRequest build(final ContractConfiguration contractConfiguration, final TsiStatusCheckRequest request) throws InvalidRequestException {

            // Seal the request with HMAC algorithm
            final String secretKey = contractConfiguration.getContractProperties().get( TsiConstants.CONTRACT_KEY_VALUE ).getValue();
            request.seal(secretKey);

            return request;
        }

        /**
         * Verifies that the input request contains all the required fields.
         *
         * @param contractConfiguration
         * @throws InvalidRequestException If recovering the field value would result in a NPE or if the value is null or empty.
         */
        protected void checkInputRequest(final ContractConfiguration contractConfiguration)
                throws InvalidRequestException {

            if( contractConfiguration == null
                    || contractConfiguration.getContractProperties() == null  ){
                throw new InvalidRequestException( "Contract configuration properties object must not be null" );
            }
            if( contractConfiguration.getContractProperties().get( TsiConstants.CONTRACT_KEY_VALUE ) == null ){
                throw new InvalidRequestException( "Missing contract configuration property: secret key" );
            }
            if( contractConfiguration.getContractProperties().get( TsiConstants.CONTRACT_KEY_ID ) == null ){
                throw new InvalidRequestException( "Missing contract configuration property: key id" );
            }
        }

    }
}
