package com.payline.payment.tsi.service;

import com.payline.pmapi.bean.common.Message;
import com.payline.pmapi.bean.notification.request.NotificationRequest;
import com.payline.pmapi.bean.notification.response.NotificationResponse;
import com.payline.pmapi.bean.notification.response.impl.PaymentResponseByNotificationResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseSuccess;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.util.Collections;

@RunWith( MockitoJUnitRunner.class )
public class NotificationServiceImplTest {

    @InjectMocks
    private NotificationServiceImpl service;

    @Test
    public void parseOKTest() {
        String content = "authid=9365493&mac=6e32f602c8397699584009f2b8ec651f&mid=806&tid=f553173bbfccb1be2a02885727c99c20&status=OK&ercode=0&message=OK&amount=0.10&currency=EUR&product_desc=Ticket Premium&pin_type=T&pin_info=na";

        NotificationRequest notificationRequest = NotificationRequest.NotificationRequestBuilder.aNotificationRequest()
                .withContent( new ByteArrayInputStream(content.getBytes()))
                .withHttpMethod("myHttpMethod")
                .withPathInfo("http://monsite.fr")
                .withHeaderInfos(Collections.emptyMap())
                .build();


        NotificationResponse notificationResponse = service.parse(notificationRequest);

        Assert.assertEquals(PaymentResponseByNotificationResponse.class, notificationResponse.getClass());

        Assert.assertEquals(new Integer(200), ((PaymentResponseByNotificationResponse)notificationResponse).getHttpStatus());
        Assert.assertEquals("ACC=OK", ((PaymentResponseByNotificationResponse)notificationResponse).getHttpBody());

        Assert.assertEquals(PaymentResponseSuccess.class, ((PaymentResponseByNotificationResponse)notificationResponse).getPaymentResponse().getClass());
        PaymentResponseSuccess paymentResponseSuccess = (PaymentResponseSuccess) ((PaymentResponseByNotificationResponse)notificationResponse).getPaymentResponse();
        Assert.assertEquals("OK", paymentResponseSuccess.getMessage().getMessage());
        Assert.assertEquals(Message.MessageType.SUCCESS, paymentResponseSuccess.getMessage().getType());
        Assert.assertEquals("0", paymentResponseSuccess.getStatusCode());
        Assert.assertEquals("f553173bbfccb1be2a02885727c99c20", paymentResponseSuccess.getPartnerTransactionId());
    }
}
