package com.payline.payment.tsi.service;

import com.payline.payment.tsi.response.TsiStatusCheckResponse;
import com.payline.payment.tsi.utils.PaymentResponseUtil;
import com.payline.payment.tsi.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.common.Message;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.buyerpaymentidentifier.impl.EmptyTransactionDetails;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseSuccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResponseProcessor {

    private PaymentResponseUtil paymentResponseUtil;

    private static final Logger LOGGER = LogManager.getLogger(ResponseProcessor.class);

    private ResponseProcessor () {
        this.paymentResponseUtil = PaymentResponseUtil.getInstance();
    }

    public static ResponseProcessor getInstance() {
        return ResponseProcessorHolder.INSTANCE;
    }

    public PaymentResponse processResponseStatusCheckResponse(StringResponse response) {
        // Parse response
        final TsiStatusCheckResponse statusCheck = (new TsiStatusCheckResponse.Builder()).fromJson(response.getContent());

        return processResponseStatusCheckResponse(statusCheck);
    }

    public PaymentResponse processResponseStatusCheckResponse(TsiStatusCheckResponse statusCheck) {
        String tid = statusCheck.getTid();

        // Status = "OK" and no error : transaction is a success
        if( "OK".equals( statusCheck.getStatus() ) && !statusCheck.isError() ){
            return PaymentResponseSuccess.PaymentResponseSuccessBuilder.aPaymentResponseSuccess()
                    .withMessage( new Message( Message.MessageType.SUCCESS, statusCheck.getMessage() ) )
                    .withStatusCode( statusCheck.getErCode() )
                    .withPartnerTransactionId( statusCheck.getTid() )
                    .withTransactionDetails( new EmptyTransactionDetails() )
                    .withTransactionAdditionalData( statusCheck.getResume() )
                    .build();
        } else if ("NO SUCCESSFUL TRANSACTIONS FOUND WITHIN 6 MONTHS".equals(statusCheck.getMessage())) {
            LOGGER.info("TSI Status Check request returned something equals to an expiration: {} ({})", statusCheck.getMessage(), statusCheck.getErCode());
            return paymentResponseUtil.buildPaymentResponseFailure( statusCheck.getMessage(), FailureCause.SESSION_EXPIRED, tid);
        } else { // no valid transaction was found or an error occurred
            LOGGER.info("TSI Status Check request returned an error: {} ({})", statusCheck.getMessage(), statusCheck.getErCode());
            return paymentResponseUtil.buildPaymentResponseFailure( statusCheck.getMessage(), FailureCause.PAYMENT_PARTNER_ERROR, tid);
        }
    }

    private static class ResponseProcessorHolder {
        private final static ResponseProcessor INSTANCE = new ResponseProcessor();
    }
}
