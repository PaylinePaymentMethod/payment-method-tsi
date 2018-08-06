package com.payline.payment.tsi.request.mock;

import com.payline.pmapi.bean.common.Amount;
import com.payline.pmapi.bean.payment.Browser;
import com.payline.pmapi.bean.payment.ContractConfiguration;
import com.payline.pmapi.bean.payment.Order;
import com.payline.pmapi.bean.payment.PaylineEnvironment;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;

import java.util.Locale;

/**
 * Generates RedirectionPaymentRequest mock objects.
 */
public class RedirectionPaymentRequestMock extends PaymentRequestMock {

    protected String redirectionContext;

    @Override
    public RedirectionPaymentRequest mock(){
        return RedirectionPaymentRequest.builder()
                .withRedirectionContext( this.redirectionContext )
                .withAmount( new Amount( this.amount, this.currency ) )
                .withBrowser( new Browser( "", Locale.FRANCE ) )
                .withContractConfiguration( new ContractConfiguration( "", this.contractProperties ) )
                .withPaylineEnvironment( new PaylineEnvironment( this.notificationUrl, this.successUrl, this.cancelUrl, true ) )
                .withTransactionId( this.transactionId )
                .withOrder( Order.OrderBuilder.anOrder().withReference( this.transactionId ).build() )
                .withSoftDescriptor( this.softDescriptor )
                .build();
    }

    public RedirectionPaymentRequestMock withRedirectionContext( String redirectionContext ){
        this.redirectionContext = redirectionContext;
        return this;
    }
}
