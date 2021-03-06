package com.payline.payment.tsi.request.mock;

import com.payline.payment.tsi.TsiConstants;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.RequestContext;

import java.util.HashMap;
import java.util.Map;

public class TsiRedirectionPaymentRequestMock extends RedirectionPaymentRequestMock {

    @Override
    public TsiRedirectionPaymentRequestMock reset(){
        super.reset();
        this.contractProperties.put( TsiConstants.CONTRACT_MERCHANT_ID, new ContractProperty( "123" ) );
        this.contractProperties.put( TsiConstants.CONTRACT_KEY_VALUE, new ContractProperty( "secret" ) );
        this.contractProperties.put( TsiConstants.CONTRACT_KEY_ID, new ContractProperty( "234" ) );
        final Map<String, String> requestData = new HashMap<>();
        requestData.put(TsiConstants.REQUEST_CONTEXT_KEY_TID, "1234567890");
        final RequestContext qs = RequestContext.RequestContextBuilder.aRequestContext()
                .withRequestData(requestData)
                .build();
        this.requestContext = qs;
        return this;
    }

    public TsiRedirectionPaymentRequestMock withMerchantId( Integer merchantId ){
        if( merchantId != null ){
            this.contractProperties.put( TsiConstants.CONTRACT_MERCHANT_ID, new ContractProperty( Integer.toString( merchantId ) ) );
        } else {
            this.contractProperties.remove( TsiConstants.CONTRACT_MERCHANT_ID );
        }
        return this;
    }

    public TsiRedirectionPaymentRequestMock withKeyId( Integer keyId ){
        if( keyId != null ){
            this.contractProperties.put( TsiConstants.CONTRACT_KEY_ID, new ContractProperty( Integer.toString( keyId ) ) );
        } else {
            this.contractProperties.remove( TsiConstants.CONTRACT_KEY_ID );
        }
        return this;
    }

    public TsiRedirectionPaymentRequestMock withKeyValue( String keyValue ){
        if( keyValue != null ){
            this.contractProperties.put( TsiConstants.CONTRACT_KEY_VALUE, new ContractProperty( keyValue ) );
        } else {
            this.contractProperties.remove( TsiConstants.CONTRACT_KEY_VALUE );
        }
        return this;
    }

}
