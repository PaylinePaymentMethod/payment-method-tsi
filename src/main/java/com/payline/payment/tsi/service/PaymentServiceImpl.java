package com.payline.payment.tsi.service;

import com.payline.payment.tsi.exception.InvalidRequestException;
import com.payline.payment.tsi.request.TsiGoRequest;
import com.payline.payment.tsi.response.TsiGoResponse;
import com.payline.payment.tsi.utils.JsonHttpClient;
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
import java.security.NoSuchAlgorithmException;

import static com.payline.pmapi.bean.payment.response.PaymentResponseRedirect.RedirectionRequest;

public class PaymentServiceImpl extends AbstractPaymentHttpService implements PaymentService {

    private static final Logger logger = LogManager.getLogger( PaymentServiceImpl.class );

    private TsiGoRequest.Builder requestBuilder;

    public PaymentServiceImpl(){
        super();
        this.requestBuilder = new TsiGoRequest.Builder();
    }

    @Override
    public PaymentResponse paymentRequest( PaymentRequest paymentRequest ) {
        return this.processRequest( paymentRequest );
    }

    @Override
    public Response createSendRequest( PaymentRequest paymentRequest ) throws IOException, InvalidRequestException, NoSuchAlgorithmException {
        // Create Go request from Payline request
        TsiGoRequest tsiGoRequest = requestBuilder.fromPaymentRequest( paymentRequest );

        // Send Go request
        // TODO: externalize scheme, host and path definitions!
        return httpClient.doPost( "https", "sandbox-voucher.tsiapi.com", "/context", tsiGoRequest.buildBody() );
    }

    @Override
    public PaymentResponse processResponse( Response response ) throws IOException {
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
}
