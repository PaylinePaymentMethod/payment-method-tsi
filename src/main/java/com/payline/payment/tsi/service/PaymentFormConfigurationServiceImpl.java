package com.payline.payment.tsi.service;

import com.payline.pmapi.bean.paymentForm.PaymentFormConfigurationRequest;
import com.payline.pmapi.bean.paymentForm.PaymentFormConfigurationResponse;
import com.payline.pmapi.service.PaymentFormConfigurationService;

import java.util.HashMap;

public class PaymentFormConfigurationServiceImpl implements PaymentFormConfigurationService {

    @Override
    public PaymentFormConfigurationResponse getPaymentFormConfiguration( PaymentFormConfigurationRequest paymentFormConfigurationRequest ) {
        return PaymentFormConfigurationResponse.PaymentFormConfigurationResponseBuilder.aPaymentFormConfigurationResponse()
                .withContextPaymentForm(new HashMap<>())
                .build();
    }
}
