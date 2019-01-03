package com.payline.payment.tsi.service;

import com.payline.payment.tsi.exception.ExternalCommunicationException;
import com.payline.payment.tsi.exception.InvalidRequestException;
import com.payline.payment.tsi.request.TsiStatusCheckRequest;
import com.payline.payment.tsi.response.TsiStatusCheckResponse;
import com.payline.payment.tsi.utils.PaymentResponseUtil;
import com.payline.payment.tsi.utils.config.ConfigEnvironment;
import com.payline.payment.tsi.utils.config.ConfigProperties;
import com.payline.payment.tsi.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.common.Message;
import com.payline.pmapi.bean.payment.Environment;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import com.payline.pmapi.bean.payment.request.TransactionStatusRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.buyerpaymentidentifier.impl.EmptyTransactionDetails;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseSuccess;
import com.payline.pmapi.service.PaymentWithRedirectionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;

public class PaymentWithRedirectionServiceImpl extends AbstractPaymentHttpService<RedirectionPaymentRequest> implements PaymentWithRedirectionService {

    private static final Logger logger = LogManager.getLogger( PaymentWithRedirectionServiceImpl.class );

    private TsiStatusCheckRequest.Builder requestBuilder;
    private PaymentResponseUtil paymentResponseUtil;
    private ResponseProcessor responseProcessor;

    public PaymentWithRedirectionServiceImpl() {
        super();
        this.requestBuilder = new TsiStatusCheckRequest.Builder();
        this.paymentResponseUtil = PaymentResponseUtil.getInstance();
        this.responseProcessor = ResponseProcessor.getInstance();
    }

    @Override
    public PaymentResponse finalizeRedirectionPayment( RedirectionPaymentRequest redirectionPaymentRequest ) {
        return processRequest(redirectionPaymentRequest);
    }

    @Override
    public StringResponse createSendRequest(RedirectionPaymentRequest redirectionPaymentRequest )
            throws IOException, InvalidRequestException, URISyntaxException, ExternalCommunicationException {
        // Create StatusCheck request from Payline input
        final TsiStatusCheckRequest statusCheckRequest = requestBuilder.fromRedirectionPaymentRequest( redirectionPaymentRequest );

        return postCheckstatus(redirectionPaymentRequest.getEnvironment(), statusCheckRequest.buildBody());
    }

    @Override
    public PaymentResponse processResponse(StringResponse response, final String tid) throws IOException {
        return responseProcessor.processResponseStatusCheckResponse(response);
    }

    @Override
    public PaymentResponse handleSessionExpired(final TransactionStatusRequest transactionStatusRequest) {
        final String tid = transactionStatusRequest.getTransactionId();
        try {
            final TsiStatusCheckRequest statusCheckRequest = requestBuilder.fromTransactionStatusRequest(transactionStatusRequest);
            final StringResponse response = postCheckstatus(transactionStatusRequest.getEnvironment(), statusCheckRequest.buildBody());
            return processResponse(response, tid);
        } catch (InvalidRequestException e) {
            logger.error( "TSI handleSessionExpired, the TransactionStatusRequest is invalid", e);
            return paymentResponseUtil.buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.INVALID_DATA, tid);
        } catch (IOException | URISyntaxException e) {
            logger.error("TSI handleSessionExpired, postCheckstatus error", e);
            return paymentResponseUtil.buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.COMMUNICATION_ERROR, tid);
        } catch( Exception e ){
            logger.error("An unexpected error occurred", e);
            return paymentResponseUtil.buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR, tid);
        }
    }

    /**
     * Call StatusCheck to recover transaction info
     *
     * @param environment
     * @return
     */
    private StringResponse postCheckstatus(final Environment environment, final String body) throws IOException, URISyntaxException, ExternalCommunicationException {
        final ConfigEnvironment env = Boolean.FALSE.equals(environment.isSandbox()) ? ConfigEnvironment.PROD : ConfigEnvironment.TEST;

        final String scheme = ConfigProperties.get("tsi.scheme", env);
        final String host = ConfigProperties.get("tsi.host", env);
        final String path = ConfigProperties.get("tsi.statusCheck.path", env);
        return getHttpClient().doPost(scheme, host, path, body);
    }
}
