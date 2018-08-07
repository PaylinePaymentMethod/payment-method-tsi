package com.payline.payment.tsi.service;

import com.payline.payment.tsi.exception.InvalidRequestException;
import com.payline.payment.tsi.request.TsiStatusCheckRequest;
import com.payline.payment.tsi.response.TsiStatusCheckResponse;
import com.payline.payment.tsi.utils.config.ConfigEnvironment;
import com.payline.payment.tsi.utils.config.ConfigProperties;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.common.Message;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import com.payline.pmapi.bean.payment.request.TransactionStatusRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.PaymentResponseSuccess;
import com.payline.pmapi.bean.payment.response.buyerpaymentidentifier.impl.Email;
import com.payline.pmapi.service.PaymentWithRedirectionService;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class PaymentWithRedirectionServiceImpl extends AbstractPaymentHttpService<RedirectionPaymentRequest> implements PaymentWithRedirectionService {

    private static final Logger logger = LogManager.getLogger( PaymentWithRedirectionServiceImpl.class );

    private TsiStatusCheckRequest.Builder requestBuilder;

    public PaymentWithRedirectionServiceImpl(){
        super();
        this.requestBuilder = new TsiStatusCheckRequest.Builder();
    }

    @Override
    public PaymentResponse finalizeRedirectionPayment( RedirectionPaymentRequest redirectionPaymentRequest ) {
        return processRequest( redirectionPaymentRequest );
    }

    @Override
    public Response createSendRequest( RedirectionPaymentRequest redirectionPaymentRequest )
            throws IOException, InvalidRequestException {
        // Create StatusCheck request from Payline input
        TsiStatusCheckRequest statusCheckRequest = requestBuilder.fromRedirectionPaymentRequest( redirectionPaymentRequest );

        // Call StatusCheck to recover transaction info
        ConfigEnvironment env = redirectionPaymentRequest.getPaylineEnvironment().isSandbox() ? ConfigEnvironment.TEST : ConfigEnvironment.PROD;
        String scheme = ConfigProperties.get( "tsi.scheme", env );
        String host = ConfigProperties.get( "tsi.host", env );
        String path = ConfigProperties.get( "tsi.statusCheck.path", env );
        return httpClient.doPost( scheme, host, path, statusCheckRequest.buildBody() );
    }

    @Override
    public PaymentResponse processResponse( Response response ) throws IOException {
        // Parse response
        TsiStatusCheckResponse statusCheck = (new TsiStatusCheckResponse.Builder()).fromJson( response.body().string() );

        // Status = "OK" and no error : transaction is a success
        if( "OK".equals( statusCheck.getStatus() ) && !statusCheck.isError() ){
            return PaymentResponseSuccess.PaymentResponseSuccessBuilder.aPaymentResponseSuccess()
                    .withMessage( new Message( Message.MessageType.SUCCESS, statusCheck.getMessage() ) )
                    .withStatusCode( statusCheck.getErCode() )
                    .withTransactionIdentifier( statusCheck.getTid() )
                    // TODO: replace the fake email by another solution (waiting for another release from PM-API)
                    // TODO: make the fake email configurable
                    .withTransactionDetails( Email.EmailBuilder.anEmail().withEmail( "fake.address@tsi.fake.fr" ).build() )
                    .withTransactionAdditionalData( statusCheck.getResume() )
                    .build();
        }
        // no valid transaction was found or an error occurred
        else {
            logger.error( "TSI Status Check request returned an error: " + statusCheck.getMessage() + "(" + statusCheck.getErCode() + ")" );
            return buildPaymentResponseFailure( statusCheck.getMessage(), FailureCause.PAYMENT_PARTNER_ERROR );
        }
    }

    @Override
    public PaymentResponse handleSessionExpired( TransactionStatusRequest transactionStatusRequest ) {
        return buildPaymentResponseFailure( "timeout", FailureCause.SESSION_EXPIRED );
    }
}
