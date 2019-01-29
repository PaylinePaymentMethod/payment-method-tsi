package com.payline.payment.tsi.request.mock;

import com.payline.pmapi.bean.common.Amount;
import com.payline.pmapi.bean.common.Buyer;
import com.payline.pmapi.bean.configuration.PartnerConfiguration;
import com.payline.pmapi.bean.payment.Browser;
import com.payline.pmapi.bean.payment.ContractConfiguration;
import com.payline.pmapi.bean.payment.Order;
import com.payline.pmapi.bean.payment.Environment;
import com.payline.pmapi.bean.payment.PaymentFormContext;
import com.payline.pmapi.bean.payment.RequestContext;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;

import java.util.HashMap;
import java.util.Locale;

/**
 * Generates RedirectionPaymentRequest mock objects.
 */
public class RedirectionPaymentRequestMock extends PaymentRequestMock {

    protected RequestContext requestContext;

    @Override
    public RedirectionPaymentRequest mock(){
        return RedirectionPaymentRequest.builder()
                .withRequestContext(this.requestContext)
                .withAmount( new Amount( this.amount, this.currency ) )
                .withBrowser( new Browser( "", Locale.FRANCE ) )
                .withContractConfiguration( new ContractConfiguration( "", this.contractProperties ) )
                .withEnvironment( new Environment( this.notificationUrl, this.successUrl, this.cancelUrl, true ) )
                .withTransactionId( this.transactionId )
                .withOrder( Order.OrderBuilder.anOrder().withReference( this.transactionId ).build() )
                .withSoftDescriptor( this.softDescriptor )
                .withBuyer(Buyer.BuyerBuilder.aBuyer().build())
                .withPartnerConfiguration(new PartnerConfiguration(new HashMap<>(),new HashMap<>()))
                .build();
    }

    public RedirectionPaymentRequestMock withRequestContext( String redirectionContext ){
        this.requestContext = requestContext;
        return this;
    }
}
