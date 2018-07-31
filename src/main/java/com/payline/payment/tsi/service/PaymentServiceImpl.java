package com.payline.payment.tsi.service;

import com.payline.payment.tsi.exception.InvalidRequestException;
import com.payline.payment.tsi.request.TsiGoRequest;
import com.payline.payment.tsi.response.TsiGoResponse;
import com.payline.payment.tsi.utils.HttpClient;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.PaymentResponseFailure;
import com.payline.pmapi.bean.payment.response.PaymentResponseRedirect;
import com.payline.pmapi.service.PaymentService;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import static com.payline.pmapi.bean.payment.response.PaymentResponseRedirect.RedirectionRequest;

public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LogManager.getLogger( PaymentServiceImpl.class );

    private TsiGoRequest.Builder requestBuilder;
    private HttpClient httpClient;

    public PaymentServiceImpl(){
        this.httpClient = new HttpClient();
        this.requestBuilder = new TsiGoRequest.Builder();
    }

    @Override
    public PaymentResponse paymentRequest( PaymentRequest paymentRequest ){
        try {
            // Create Go request from Payline request and build the HTTP request body
            TsiGoRequest tsiGoRequest = requestBuilder.fromPaymentRequest( paymentRequest );
            Map<String, String> requestBody = tsiGoRequest.buildBodyMap();

            // Send Go request
            // TODO: externalize scheme, host and path definitions!
            Response response = httpClient.doPost( "https", "sandbox-voucher.tsiapi.com", "/context", requestBody, "application/json" );

            if( response != null && response.code() == 200 && response.body() != null ){
                // Parse response
                TsiGoResponse tsiGoResponse = (new TsiGoResponse.Builder()).fromJson( response.body().string() );

                // If status == 1, proceed with the redirection
                if( tsiGoResponse.getStatus() == 1 ){
                    String redirectUrl = tsiGoResponse.getUrl();

                    RedirectionRequest redirectionRequest = new RedirectionRequest( new URL( redirectUrl ) );
                    return PaymentResponseRedirect.PaymentResponseRedirectBuilder.aPaymentResponseRedirect()
                            .withRedirectionRequest( redirectionRequest )
                            .build();
                }
                else {
                    logger.error( "TSI Go request returned an error: " + tsiGoResponse.getMessage() + "(" + Integer.toString( tsiGoResponse.getStatus() ) + ")" );
                    return buildPaymentResponseFailure( tsiGoResponse.getMessage(), FailureCause.PAYMENT_PARTNER_ERROR );
                }
            }
            else if( response == null || response.body() == null ){
                logger.error( "The HTTP response or its body is null and should not be" );
                return buildPaymentResponseFailure( "no code transmitted", FailureCause.INTERNAL_ERROR );
            }
            else {
                logger.error( "An HTTP error occurred while sending the request: " + response.message() );
                return buildPaymentResponseFailure( Integer.toString( response.code() ), FailureCause.COMMUNICATION_ERROR );
            }
        }
        catch( InvalidRequestException e ){
            logger.error( "PaymentRequest is invalid: " + e.getMessage() );
            return buildPaymentResponseFailure( "no code transmitted", FailureCause.INVALID_DATA );
        }
        catch( IOException e ){
            logger.error( "An IOException occurred while sending the HTTP request: " + e.getMessage() );
            return buildPaymentResponseFailure( "no code transmitted", FailureCause.COMMUNICATION_ERROR );
        }
    }

    private PaymentResponseFailure buildPaymentResponseFailure( String errorCode, FailureCause failureCause ){
        return PaymentResponseFailure.PaymentResponseFailureBuilder.aPaymentResponseFailure()
                .withFailureCause( failureCause )
                .withErrorCode( errorCode )
                .build();
    }
}
