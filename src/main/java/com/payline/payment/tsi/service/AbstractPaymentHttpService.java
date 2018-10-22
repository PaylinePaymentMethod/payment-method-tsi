package com.payline.payment.tsi.service;

import com.payline.payment.tsi.exception.ExternalCommunicationException;
import com.payline.payment.tsi.exception.InvalidRequestException;
import com.payline.payment.tsi.request.TsiSealedJsonRequest;
import com.payline.payment.tsi.utils.http.JsonHttpClient;
import com.payline.payment.tsi.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

/**
 * This abstract service handles the common issues encountered when sending, receiving and processing a {@link PaymentRequest} (or subclass)
 * It delegates the specific parts to the classes that will extends it, through the abstract methods.
 * This way, most of the exception handling can be done here, once.
 */
public abstract class AbstractPaymentHttpService<T extends PaymentRequest> {

    private static final Logger logger = LogManager.getLogger( AbstractPaymentHttpService.class );

    protected static final String DEFAULT_ERROR_CODE = "no code transmitted";

    private JsonHttpClient httpClient;

    /**
     * Late initialization of httpClient to work with batch
     *
     * @return
     */
    protected JsonHttpClient getHttpClient() {
        if (httpClient == null) {
            this.httpClient = JsonHttpClient.getInstance();
        }
        return httpClient;
    }

    /**
     * Builds the request, sends it through HTTP using the httpClient and recovers the response.
     *
     * @param paymentRequest The input request provided by Payline
     * @return The {@link HttpResponse} from the HTTP call
     * @throws IOException
     * @throws InvalidRequestException
     * @throws GeneralSecurityException
     * @throws URISyntaxException
     * @throws ExternalCommunicationException
     */
    public abstract StringResponse createSendRequest(T paymentRequest ) throws IOException, InvalidRequestException, GeneralSecurityException, URISyntaxException, ExternalCommunicationException;

    /**
     * Process the response from the HTTP call.
     * It focuses on business aspect of the processing : the technical part has already been done by {@link #processRequest(PaymentRequest)} .
     *
     * @param response The {@link StringResponse} from the HTTP call, which HTTP code is 200 and which body is not null.
     * @return The {@link PaymentResponse}
     * @throws IOException Can be thrown while reading the response body
     */
    public abstract PaymentResponse processResponse( StringResponse response, final String tid) throws IOException;

    /**
     * Process a {@link PaymentRequest} (or subclass), handling all the generic error cases
     *
     * @param paymentRequest The input request from Payline
     * @return The corresponding {@link PaymentResponse}
     */
    protected PaymentResponse processRequest( T paymentRequest){
        String tid = null;
        try {
            final TsiSealedJsonRequest.Builder builder = new TsiSealedJsonRequest.Builder();
            if (null != paymentRequest.getTransactionId()) {
                tid = builder.formatTransactionId(paymentRequest.getTransactionId());
            }
            logger.info("Payline transaction Id: {}, Partner transaction ID: {}", paymentRequest.getTransactionId(), tid);

            // Mandate the child class to create and send the request (which is specific to each implementation)
            final StringResponse response = this.createSendRequest( paymentRequest );

            if( response != null && response.getCode() == 200 && response.getContent() != null ){
                // Mandate the child class to process the request when it's OK (which is specific to each implementation)
                return this.processResponse( response, tid);
            }
            else if( response != null && response.getCode() != 200 && response.getContent() != null ){
                logger.error( "An HTTP error occurred while sending the request: " + response.getContent() );
                return buildPaymentResponseFailure( Integer.toString(response.getCode()), FailureCause.COMMUNICATION_ERROR, tid);
            }
            else {
                logger.error( "The HTTP response or its body is null and should not be" );
                return buildPaymentResponseFailure( DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR, tid);
            }
        }
        catch( InvalidRequestException e ){
            logger.error( "The input payment request is invalid: ", e);
            return buildPaymentResponseFailure( DEFAULT_ERROR_CODE, FailureCause.INVALID_DATA, tid);
        }
        catch( ExternalCommunicationException e ){
            logger.error( "An error occurred while sending the HTTP request or receiving the response: ", e);
            return buildPaymentResponseFailure( DEFAULT_ERROR_CODE, FailureCause.COMMUNICATION_ERROR, tid);
        }
        catch( Exception e ){
            logger.error( "An unexpected error occurred: ", e );
            return buildPaymentResponseFailure( DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR, tid);
        }
    }

    /**
     * Utility method to instantiate {@link PaymentResponseFailure} objects, using the class' builder.
     *
     * @param errorCode The error code
     * @param failureCause The failure cause
     * @return The instantiated object
     */
    protected PaymentResponseFailure buildPaymentResponseFailure(String errorCode, FailureCause failureCause, final String tid){
        return PaymentResponseFailure.PaymentResponseFailureBuilder.aPaymentResponseFailure()
                .withFailureCause( failureCause )
                .withErrorCode( errorCode )
                .withPartnerTransactionId(tid)
                .build();
    }
}
