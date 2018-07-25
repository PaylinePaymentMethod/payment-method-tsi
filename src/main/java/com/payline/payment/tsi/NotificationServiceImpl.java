package com.payline.payment.tsi;

import com.payline.pmapi.bean.notification.request.NotificationRequest;
import com.payline.pmapi.bean.notification.response.NotificationResponse;
import com.payline.pmapi.bean.payment.request.NotifyTransactionStatusRequest;
import com.payline.pmapi.service.NotificationService;

public class NotificationServiceImpl implements NotificationService {

    @Override
    public NotificationResponse parse( NotificationRequest notificationRequest ){
        // TODO
        return null;
    }

    @Override
    public void notifyTransactionStatus( NotifyTransactionStatusRequest notifyTransactionStatusRequest ){
        // TODO
    }
}
