package com.payline.payment.tsi.service;

import com.payline.pmapi.bean.paymentform.bean.PaymentFormLogo;
import com.payline.pmapi.bean.paymentform.request.PaymentFormConfigurationRequest;
import com.payline.pmapi.bean.paymentform.request.PaymentFormLogoRequest;
import com.payline.pmapi.bean.paymentform.response.configuration.PaymentFormConfigurationResponse;
import com.payline.pmapi.bean.paymentform.response.configuration.PaymentFormConfigurationResponseProvided;
import com.payline.pmapi.bean.paymentform.response.logo.PaymentFormLogoResponse;
import com.payline.pmapi.service.PaymentFormConfigurationService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class PaymentFormConfigurationServiceImpl implements PaymentFormConfigurationService {

    @Override
    public PaymentFormConfigurationResponse getPaymentFormConfiguration( PaymentFormConfigurationRequest paymentFormConfigurationRequest ) {
        return PaymentFormConfigurationResponseProvided.PaymentFormConfigurationResponseBuilder.aPaymentFormConfigurationResponse()
                .withContextPaymentForm(new HashMap<>())
                .build();
    }

    @Override
    public PaymentFormLogoResponse getPaymentFormLogo( PaymentFormLogoRequest paymentFormLogoRequest ) {
        // TODO !
        return null;
    }

    @Override
    public PaymentFormLogo getLogo( Locale locale ) throws IOException {
        // TODO !
        return null;
    }
}
