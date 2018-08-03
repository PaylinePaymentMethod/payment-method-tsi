package com.payline.payment.tsi.service;

import com.payline.pmapi.bean.paymentForm.PaymentFormConfigurationRequest;
import com.payline.pmapi.bean.paymentForm.PaymentFormConfigurationResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

@RunWith( MockitoJUnitRunner.class )
public class PaymentFormConfigurationServiceImplTest {

    @InjectMocks
    private PaymentFormConfigurationServiceImpl service;

    @Test
    public void testGetPaymentFormConfiguration_notNull(){
        // when: getPaymentFormConfiguration is called
        PaymentFormConfigurationResponse response = service.getPaymentFormConfiguration( mock( PaymentFormConfigurationRequest.class ) );

        // then: returned object is not null
        Assert.assertNotNull( response );
    }

}
