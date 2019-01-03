package com.payline.payment.tsi.service;

import com.google.gson.JsonObject;
import com.payline.payment.tsi.response.TsiStatusCheckResponse;
import com.payline.payment.tsi.utils.http.StringResponse;
import com.payline.pmapi.bean.common.TransactionCorrelationId;
import com.payline.pmapi.bean.notification.request.NotificationRequest;
import com.payline.pmapi.bean.notification.response.NotificationResponse;
import com.payline.pmapi.bean.notification.response.impl.IgnoreNotificationResponse;
import com.payline.pmapi.bean.notification.response.impl.PaymentResponseByNotificationResponse;
import com.payline.pmapi.bean.payment.request.NotifyTransactionStatusRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.service.NotificationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class NotificationServiceImpl implements NotificationService {

    private ResponseProcessor responseProcessor;

    public NotificationServiceImpl() {
        responseProcessor = ResponseProcessor.getInstance();
    }

    @Override
    public NotificationResponse parse( NotificationRequest notificationRequest ){
        StringResponse stringResponse = notificationToJson(notificationRequest);
        final TsiStatusCheckResponse statusCheck = (new TsiStatusCheckResponse.Builder()).fromJson(stringResponse.getContent());
        PaymentResponse paymentResponse = responseProcessor.processResponseStatusCheckResponse(statusCheck);

        final TransactionCorrelationId transactionCorrelationId = TransactionCorrelationId.TransactionCorrelationIdBuilder.aCorrelationIdBuilder()
                .withType(TransactionCorrelationId.CorrelationIdType.PARTNER_TRANSACTION_ID)
                .withValue(statusCheck.getTid())
                .build();

        return PaymentResponseByNotificationResponse.PaymentResponseByNotificationResponseBuilder.aPaymentResponseByNotificationResponseBuilder()
                .withPaymentResponse(paymentResponse)
                .withHttpBody("ACC=OK")
                .withHttpStatus(200)
                .withTransactionCorrelationId(transactionCorrelationId)
                .build();
    }

    @Override
    public void notifyTransactionStatus( NotifyTransactionStatusRequest notifyTransactionStatusRequest ){
        // Nothing to do.
    }

    private StringResponse notificationToJson(NotificationRequest notificationRequest) {
        String content = getContentFromNotification(notificationRequest);

        Map<String, String> parameters = getParametersFromString(content);

        String json = convertParametersMapToJson(parameters);

        StringResponse stringResponse = new StringResponse();
        stringResponse.setContent(json);

        return stringResponse;
    }

    private String getContentFromNotification(NotificationRequest notificationRequest) {
        StringBuilder content = new StringBuilder();
        String thisLine;

        try {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(notificationRequest.getContent()));
            while ((thisLine = buffer.readLine()) != null) {
                content.append(thisLine);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while reading inputstream from notification", e);
        }

        return content.toString();
    }

    private Map<String, String> getParametersFromString(String content) {
        Map<String, String> query_pairs = new HashMap<>();
        String[] pairs = content.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(pair.substring(0, idx), pair.substring(idx + 1));
        }
        return query_pairs;
    }

    private String convertParametersMapToJson(Map<String, String> parameters) {
        JsonObject jsonObject = new JsonObject();
        for(Map.Entry<String, String> entry : parameters.entrySet()) {
            jsonObject.addProperty(entry.getKey(), entry.getValue());
        }

        return jsonObject.toString();
    }
}
