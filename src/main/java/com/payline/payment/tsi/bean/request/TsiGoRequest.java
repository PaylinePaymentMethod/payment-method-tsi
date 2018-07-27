package com.payline.payment.tsi.bean.request;

import com.payline.pmapi.bean.payment.request.PaymentRequest;

import java.util.HashMap;
import java.util.Map;

public class TsiGoRequest {

    // Mandatory fields
    /** The HMAC seal for the request (generated from the other request fields) */
    private String mac;
    /** The merchant identifier. */
    private int merchantId;
    /** The transaction identifier, 32 characters long exactly */
    private String transactionId;
    /** The requested amount as a float without useless zeros */
    private String amount;
    /** The ISO4217 currency code. */
    private String currency;
    /** The identifier of the key, provided by TSI */
    private int keyId;
    /** Description of the product, 64 characters max */
    private String productDescription;
    /** URL by which the purchaser resturns to the merchant website after a payment validation */
    private String urlOk;
    /** URL by which the purchaser returns to the merchant website after a payment failure */
    private String urlNok;
    /** Automatic confirmation URL, its role is to receive the payment confirmation message transmitted by our platform */
    private String urlS2s;
    /** Should TSI debit all the ticket (Y) or just the order amount (N) ? */
    private String debitAll;
    /** Is it a test request (Y) or a production request (N) ? */
    private String th;

    // Non mandatory fields
    private Map<String, Object> s2sRequestParameters;

    /**
     * Instantiates a {@link TsiGoRequest} from a {@link PaymentRequest}. Performs the mapping between the 2 objects.
     *
     * @param paymentRequest The request instance transmitted by PM-API.
     * @return The equivalent TSI Go request
     */
    public static TsiGoRequest getInstanceFromPaymentRequest( PaymentRequest paymentRequest ){

        // TODO: make it 32 characters long
        String transactionId = paymentRequest.getTransactionId();
        // TODO: convert into plain currency (not cents) and format it
        String amount = paymentRequest.getAmount().getAmountInSmallestUnit().toString();
        // TODO: map it
        String productionDescription = "";
        // TODO: map it
        boolean debitAll = false;

        return new TsiGoRequest(
                Integer.parseInt( paymentRequest.getContractConfiguration().getContractProperties().get("mid").getValue() ),
                transactionId,
                amount,
                paymentRequest.getAmount().getCurrency().getCurrencyCode(),
                Integer.parseInt( paymentRequest.getContractConfiguration().getContractProperties().get("keyId").getValue() ),
                productionDescription,
                paymentRequest.getPaylineEnvironment().getRedirectionReturnURL(),
                paymentRequest.getPaylineEnvironment().getRedirectionCancelURL(),
                paymentRequest.getPaylineEnvironment().getNotificationURL(),
                debitAll ? "Y" : "N",
                paymentRequest.getPaylineEnvironment().isSandbox() ? "Y" : "N",
                null
        );
    }

    /**
     * Public standard constructor.
     */
    public TsiGoRequest( int merchantId, String transactionId, String amount, String currency, int keyId,
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
        bodyMap.put( "amount", this.amount ); // TODO: attention au format ! (float demandé, sans 0 en trop)
        bodyMap.put( "currency", this.currency );
        bodyMap.put( "key_id", this.keyId ); // TODO: attention au format ! (int demandé)
        bodyMap.put( "product_desc", this.productDescription );
        bodyMap.put( "url_ok", this.urlOk );
        bodyMap.put( "url_nok", this.urlNok );
        bodyMap.put( "url_s2s", this.urlS2s );
        bodyMap.put( "debit_all", this.debitAll );
        bodyMap.put( "th", this.th );
        if( this.s2sRequestParameters != null ){
            bodyMap.put( "custom", this.s2sRequestParameters );
        }

        return bodyMap;
    }

    public void setMac( String mac ){
        this.mac = mac;
    }
}
