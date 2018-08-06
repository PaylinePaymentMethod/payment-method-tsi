package com.payline.payment.tsi.request.mock;

import com.payline.payment.tsi.TsiConstants;
import com.payline.pmapi.bean.payment.ContractProperty;

public class TsiRedirectionPaymentRequestMock extends RedirectionPaymentRequestMock {

    @Override
    public TsiRedirectionPaymentRequestMock reset(){
        super.reset();
        this.contractProperties.put( TsiConstants.CONTRACT_MERCHANT_ID, new ContractProperty( "123" ) );
        this.contractProperties.put( TsiConstants.CONTRACT_KEY_ID, new ContractProperty( "234" ) );
        this.redirectionContext = "1234567890";
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

}
