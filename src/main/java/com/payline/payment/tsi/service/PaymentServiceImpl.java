package com.payline.payment.tsi.service;

import com.payline.payment.tsi.utils.HttpClient;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.service.PaymentService;

public class PaymentServiceImpl implements PaymentService {

    private HttpClient httpClient;

    public PaymentServiceImpl(){
        this.httpClient = new HttpClient();
    }

    @Override
    public PaymentResponse paymentRequest( PaymentRequest paymentRequest ){
        // Send the Go request
        // If everything is alright, return a PaymentResponseRedirect instance containing the token returned by TSI
        // If something goes wrong, return a PaymentResponseFailure instance with the error code and failure cause

        return null;
    }
}
