package com.payline.payment.tsi.utils;

import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;

public class PaymentResponseUtil {

    private PaymentResponseUtil() {

    }

    public static PaymentResponseUtil getInstance() {
        return PaymentResponseUtilHolder.INSTANCE;
    }

    /**
     * Utility method to instantiate {@link PaymentResponseFailure} objects, using the class' builder.
     *
     * @param errorCode The error code
     * @param failureCause The failure cause
     * @return The instantiated object
     */
    public PaymentResponseFailure buildPaymentResponseFailure(String errorCode, FailureCause failureCause, final String tid){
        return PaymentResponseFailure.PaymentResponseFailureBuilder.aPaymentResponseFailure()
                .withFailureCause( failureCause )
                .withErrorCode( errorCode )
                .withPartnerTransactionId(tid)
                .build();
    }

    private static class PaymentResponseUtilHolder {
        private static final PaymentResponseUtil INSTANCE = new PaymentResponseUtil();
    }
}
