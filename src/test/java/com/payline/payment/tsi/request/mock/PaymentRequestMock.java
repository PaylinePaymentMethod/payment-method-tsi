package com.payline.payment.tsi.request.mock;

import com.payline.pmapi.bean.common.Amount;
import com.payline.pmapi.bean.common.Buyer;
import com.payline.pmapi.bean.configuration.PartnerConfiguration;
import com.payline.pmapi.bean.payment.*;
import com.payline.pmapi.bean.payment.request.PaymentRequest;

import java.math.BigInteger;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Generates PaymentRequest mock objects.
 */
public class PaymentRequestMock {

    protected BigInteger amount;
    protected Currency currency;
    protected Map<String, ContractProperty> contractProperties;
    protected String successUrl;
    protected String cancelUrl;
    protected String notificationUrl;
    protected String transactionId;
    protected String softDescriptor;
    protected String orderReference;

    public PaymentRequestMock(){
        reset();
    }

    /**
     * Instanciate a {@link PaymentRequest} object with the values from this instance of the mocker.
     *
     * @return A {@link PaymentRequest} instance.
     */
    public PaymentRequest mock(){
        return PaymentRequest.builder()
                .withAmount( new Amount( this.amount, this.currency ) )
                .withBrowser( new Browser( "", Locale.FRANCE ) )
                .withContractConfiguration( new ContractConfiguration( "", this.contractProperties ) )
                .withEnvironment( new Environment( this.notificationUrl, this.successUrl, this.cancelUrl, true ) )
                .withTransactionId( this.transactionId )
                .withOrder( Order.OrderBuilder.anOrder().withReference( this.orderReference ).build() )
                .withSoftDescriptor( this.softDescriptor )
                .withBuyer(Buyer.BuyerBuilder.aBuyer().build())
                .withPartnerConfiguration(new PartnerConfiguration(new HashMap<>(),new HashMap<>()))
                .build();
    }

    /**
     * Reinitialize this mocker instance with default values.
     *
     * @return the mocker instance, to allow chained calls.
     */
    public PaymentRequestMock reset(){
        this.amount = BigInteger.TEN;
        this.currency = Currency.getInstance( "EUR" );
        this.successUrl = "https://succesurl.com/";
        this.cancelUrl = "http://localhost/cancelurl.com/";
        this.notificationUrl = "http://google.com/";
        this.transactionId = "1234567890";
        this.softDescriptor = "softDescriptor";
        this.contractProperties = new HashMap<>();
        this.orderReference = "orderReference";
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

    public PaymentRequestMock withOrderReference( String orderReference ){
        this.orderReference = orderReference;
        return this;
    }
}
