package com.payline.payment.tsi;

import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import com.payline.pmapi.bean.payment.request.TransactionStatusRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.service.PaymentWithRedirectionService;

public class PaymentWithRedirectionServiceImpl implements PaymentWithRedirectionService {

    @Override
    public PaymentResponse finalizeRedirectionPayment( RedirectionPaymentRequest redirectionPaymentRequest ){
        // TODO
        return null;
    }

    @Override
    public PaymentResponse handleSessionExpired( TransactionStatusRequest transactionStatusRequest ){
        // TODO
        return null;
    }
}
