package com.payline.payment.tsi.service;

import com.payline.pmapi.bean.notification.request.NotificationRequest;
import com.payline.pmapi.bean.notification.response.NotificationResponse;
import com.payline.pmapi.bean.notification.response.impl.IgnoreNotificationResponse;
import com.payline.pmapi.bean.payment.request.NotifyTransactionStatusRequest;
import com.payline.pmapi.service.NotificationService;

public class NotificationServiceImpl implements NotificationService {

    @Override
    public NotificationResponse parse( NotificationRequest notificationRequest ){
        return IgnoreNotificationResponse.IgnoreNotificationResponseBuilder.aIgnoreNotificationResponseBuilder()
                .withHttpBody("ACC=OK")
                .withHttpStatus(200)
                .build();
    }

    @Override
    public void notifyTransactionStatus( NotifyTransactionStatusRequest notifyTransactionStatusRequest ){
        // Nothing to do.
    }
}
