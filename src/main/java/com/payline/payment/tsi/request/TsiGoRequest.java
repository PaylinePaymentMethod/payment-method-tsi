package com.payline.payment.tsi.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.payline.payment.tsi.TsiConstants;
import com.payline.payment.tsi.exception.InvalidRequestException;
import com.payline.payment.tsi.security.Hmac;
import com.payline.payment.tsi.security.HmacAlgorithm;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.request.PaymentRequest;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class TsiGoRequest {

    // Mandatory fields
    /** The HMAC seal for the request (generated from the other request fields) */
    private String mac;
    /** The merchant identifier. */
    @SerializedName( "mid" )
    private int merchantId;
    /** The transaction identifier, 32 characters long exactly */
    @SerializedName( "tid" )
    private String transactionId;
    /** The requested amount as a float without useless zeros */
    private String amount;
    /** The ISO4217 currency code. */
    private String currency;
    /** The identifier of the key, provided by TSI */
    @SerializedName( "key_id" )
    private int keyId;
    /** Description of the product, 64 characters max */
    @SerializedName( "product_desc" )
    private String productDescription;
    /** URL by which the purchaser resturns to the merchant website after a payment validation */
    @SerializedName( "url_ok" )
    private String urlOk;
    /** URL by which the purchaser returns to the merchant website after a payment failure */
    @SerializedName( "url_nok" )
    private String urlNok;
    /** Automatic confirmation URL, its role is to receive the payment confirmation message transmitted by our platform */
    @SerializedName( "url_s2s" )
    private String urlS2s;
    /** Should TSI debit all the ticket (Y) or just the order amount (N) ? */
    @SerializedName( "debit_all" )
    private String debitAll;
    /** Is it a test request (Y) or a production request (N) ? */
    private String th;

    // Non mandatory fields
    @SerializedName( "custom" )
    private Map<String, Object> s2sRequestParameters;

    protected TsiGoRequest( int merchantId, String transactionId, String amount, String currency, int keyId,
                         String productDescription, String urlOk, String urlNok, String urlS2s, String debitAll,
                         String th, Map<String, Object> s2sRequestParameters ){
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
        this.th = th;
        this.s2sRequestParameters = s2sRequestParameters;
    }

    /**
     * Constructs the string message that will be used to calculate the request's HMAC seal.
     *
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
                + this.debitAll + "|"
                + this.th ;
    }

    /**
     * Builds the request body.
     *
     * @return a JSON formatted string.
     */
    public String buildBody(){
        return (new Gson()).toJson( this );
    }

    protected String getMac(){
        return mac;
    }

    protected void setMac( String mac ){
        this.mac = mac;
    }

    /**
     * Implements the builder pattern to instantiate {@link TsiGoRequest} from a {@link PaymentRequest} object.
     */
    public static class Builder {

        public TsiGoRequest fromPaymentRequest( PaymentRequest paymentRequest )
                throws InvalidRequestException, NoSuchAlgorithmException {
            // Check the input request for NPEs and mandatory fields
            this.checkInputRequest( paymentRequest );

            // Instantiate the TsiGoRequest from input request
            TsiGoRequest request = new TsiGoRequest(
                    Integer.parseInt( paymentRequest.getContractConfiguration().getContractProperties().get( TsiConstants.CONTRACT_MERCHANT_ID ).getValue() ),
                    this.formatTransactionId( paymentRequest.getTransactionId() ),
                    this.formatAmount( paymentRequest.getAmount().getAmountInSmallestUnit() ),
                    paymentRequest.getAmount().getCurrency().getCurrencyCode(),
                    Integer.parseInt( paymentRequest.getContractConfiguration().getContractProperties().get( TsiConstants.CONTRACT_KEY_ID ).getValue() ),
                    "", // TODO: map it!
                    paymentRequest.getPaylineEnvironment().getRedirectionReturnURL(),
                    paymentRequest.getPaylineEnvironment().getRedirectionCancelURL(),
                    paymentRequest.getPaylineEnvironment().getNotificationURL(),
                    "N", // TODO: map it?
                    paymentRequest.getPaylineEnvironment().isSandbox() ? "Y" : "N",
                    null // TODO: put something inside ?
            );

            // Seal the request with HMAC algorithm
            // TODO: externalize key definition in a properties file
            Hmac hmac = new Hmac( request.buildSealMessage(), "45f3bcf660df19f8364c222e887300fa", HmacAlgorithm.MD5 );
            request.setMac( hmac.seal() );

            return request;
        }

        /**
         * Verifies that the input request contains all the required fields.
         *
         * @param paymentRequest The input request
         * @throws InvalidRequestException If recovering the field value would result in a NPE or if the value is null or empty.
         */
        protected void checkInputRequest( PaymentRequest paymentRequest ) throws InvalidRequestException {
            if( paymentRequest == null ){
                throw new InvalidRequestException( "Request must not be null" );
            }

            if( paymentRequest.getContractConfiguration() == null
                    || paymentRequest.getContractConfiguration().getContractProperties() == null  ){
                throw new InvalidRequestException( "Contract configuration properties object must not be null" );
            }
            Map<String, ContractProperty> contractProperties = paymentRequest.getContractConfiguration().getContractProperties();
            if( contractProperties.get( TsiConstants.CONTRACT_MERCHANT_ID ) == null ){
                throw new InvalidRequestException( "Missing contract configuration property: merchant id" );
            }
            if( contractProperties.get( TsiConstants.CONTRACT_KEY_ID ) == null ){
                throw new InvalidRequestException( "Missing contract configuration property: key id" );
            }

            if( paymentRequest.getTransactionId() == null || paymentRequest.getTransactionId().isEmpty() ){
                throw new InvalidRequestException( "Transaction id is required" );
            }

            if( paymentRequest.getAmount() == null || paymentRequest.getAmount().getAmountInSmallestUnit() == null ){
                throw new InvalidRequestException( "Transaction amount is required" );
            }
            if( paymentRequest.getAmount().getCurrency() == null
                    || paymentRequest.getAmount().getCurrency().getCurrencyCode() == null ){
                throw new InvalidRequestException( "Transaction currency with a valid ISO 4217 code is required" );
            }
            
            if( paymentRequest.getPaylineEnvironment() == null ){
                throw new InvalidRequestException( "PaylineEnvironment request property must not be null" );
            }
            if( paymentRequest.getPaylineEnvironment().getRedirectionReturnURL() == null
                    || paymentRequest.getPaylineEnvironment().getRedirectionReturnURL().isEmpty() ){
                throw new InvalidRequestException( "Redirection return URL is required" );
            }
            if( paymentRequest.getPaylineEnvironment().getRedirectionCancelURL() == null
                    || paymentRequest.getPaylineEnvironment().getRedirectionCancelURL().isEmpty() ){
                throw new InvalidRequestException( "Redirection cancel URL is required" );
            }
            if( paymentRequest.getPaylineEnvironment().getNotificationURL() == null
                    || paymentRequest.getPaylineEnvironment().getNotificationURL().isEmpty() ){
                throw new InvalidRequestException( "Notification URL is required" );
            }
        }

        /**
         * Formats the input amount according to TSI Go request specifications.
         *
         * @param paymentRequestAmount The input amount
         * @return A string-formatted float amount
         */
        protected String formatAmount( BigInteger paymentRequestAmount ){
            double amount = paymentRequestAmount.doubleValue() / 100;
            if( amount == (long)amount ){
                return String.format( "%d", (long)amount );
            } else {
                return String.format( "%s", amount );
            }
        }

        /**
         * Hashes the input transaction id with MD5 to genreate a 32-characters-long unique
         * transaction identifier according to TSI Go request specifications.
         *
         * @param transactionId The input transaction id
         * @return A 32-characters-long transaction id
         */
        protected String formatTransactionId( String transactionId ) throws NoSuchAlgorithmException {
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
