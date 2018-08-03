package com.payline.payment.tsi.service;

import com.payline.payment.tsi.exception.InvalidRequestException;
import com.payline.payment.tsi.request.TsiStatusCheckRequest;
import com.payline.pmapi.bean.common.Message;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import com.payline.pmapi.bean.payment.request.TransactionStatusRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.PaymentResponseSuccess;
import com.payline.pmapi.service.PaymentWithRedirectionService;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class PaymentWithRedirectionServiceImpl extends AbstractPaymentHttpService<RedirectionPaymentRequest> implements PaymentWithRedirectionService {

    private static final Logger logger = LogManager.getLogger( PaymentWithRedirectionServiceImpl.class );

    private TsiStatusCheckRequest.Builder requestBuilder;

    public PaymentWithRedirectionServiceImpl(){
        super();
        this.requestBuilder = new TsiStatusCheckRequest.Builder();
    }

    @Override
    public PaymentResponse finalizeRedirectionPayment( RedirectionPaymentRequest redirectionPaymentRequest ) {
        // Example :
        return PaymentResponseSuccess.PaymentResponseSuccessBuilder.aPaymentResponseSuccess()
                .withMessage( new Message( Message.MessageType.SUCCESS, "" ) )
                .withStatusCode( "0" )
                .withTransactionIdentifier( "transactionId" )
                .withTransactionDetails( null )
                .build();
    }

    @Override
    public Response createSendRequest( RedirectionPaymentRequest redirectionPaymentRequest )
            throws IOException, InvalidRequestException, NoSuchAlgorithmException {
        // Create StatusCheck request from Payline input
        TsiStatusCheckRequest statusCheckRequest = requestBuilder.fromRedirectionPaymentRequest( redirectionPaymentRequest );

        // Call StatusCheck to recover transaction info
        // TODO: externalize scheme, host and path definitions!
        return httpClient.doPost( "https", "sandbox-voucher.tsiapi.com", "/checkstatus", statusCheckRequest.buildBody() );
    }

    @Override
    public PaymentResponse processResponse( Response response ) throws IOException {
        // TODO
        return null;
    }

    @Override
    public PaymentResponse handleSessionExpired( TransactionStatusRequest transactionStatusRequest ) {
        // TODO
        return null;
    }
}
