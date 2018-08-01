package com.payline.payment.tsi.request;

import com.payline.pmapi.bean.common.Amount;
import com.payline.pmapi.bean.payment.*;
import com.payline.pmapi.bean.payment.request.PaymentRequest;

import java.math.BigInteger;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PaymentRequestMock {

    private BigInteger amount;
    private Currency currency;
    protected Map<String, ContractProperty> contractProperties;
    private String successUrl;
    private String cancelUrl;
    private String notificationUrl;
    private String transactionId;
    private String softDescriptor;

    public PaymentRequestMock(){
        reset();
    }

    // TODO: Doc
    public PaymentRequest mock(){
        return PaymentRequest.builder()
                .withAmount( new Amount( this.amount, this.currency ) )
                .withBrowser( new Browser( "", Locale.FRANCE ) )
                .withContractConfiguration( new ContractConfiguration( "", this.contractProperties ) )
                .withPaylineEnvironment( new PaylineEnvironment( this.notificationUrl, this.successUrl, this.cancelUrl, true ) )
                .withTransactionId( this.transactionId )
                .withOrder( Order.OrderBuilder.anOrder().withReference( this.transactionId ).build() )
                .withSoftDescriptor( this.softDescriptor )
                .build();
    }

    // TODO: Doc
    public PaymentRequestMock reset(){
        this.amount = BigInteger.TEN;
        this.currency = Currency.getInstance( "EUR" );
        this.successUrl = "https://succesurl.com/";
        this.cancelUrl = "http://localhost/cancelurl.com/";
        this.notificationUrl = "http://google.com/";
        this.transactionId = "1234567890";
        this.softDescriptor = "softDescriptor";
        this.contractProperties = new HashMap<>();
        return this;
    }

    public PaymentRequestMock withAmount( BigInteger amount ){
        this.amount = amount;
        return this;
    }

    public PaymentRequestMock withCurrency( Currency currency ){
        this.currency = currency;
        return this;
    }

    public PaymentRequestMock withContractProperties( Map<String, ContractProperty> contractProperties ){
        this.contractProperties = contractProperties;
        return this;
    }

    public PaymentRequestMock withSuccessUrl( String successUrl ){
        this.successUrl = successUrl;
        return this;
    }

    public PaymentRequestMock withCancelUrl( String cancelUrl ){
        this.cancelUrl = cancelUrl;
        return this;
    }

    public PaymentRequestMock withNotificationUrl( String notificationUrl ){
        this.notificationUrl = notificationUrl;
        return this;
    }

    public PaymentRequestMock withTransactionId( String transactionId ){
        this.transactionId = transactionId;
        return this;
    }

    public PaymentRequestMock withSoftDescriptor( String softDescriptor ){
        this.softDescriptor = softDescriptor;
        return this;
    }
}
