package com.payline.payment.tsi.request.mock;

import com.payline.payment.tsi.TsiConstants;
import com.payline.pmapi.bean.payment.ContractProperty;

public class TsiPaymentRequestMock extends PaymentRequestMock {

    @Override
    public TsiPaymentRequestMock reset(){
        super.reset();
        this.contractProperties.put( TsiConstants.CONTRACT_MERCHANT_ID, new ContractProperty( "123" ) );
        this.contractProperties.put( TsiConstants.CONTRACT_KEY_VALUE, new ContractProperty( "secret" ) );
        this.contractProperties.put( TsiConstants.CONTRACT_KEY_ID, new ContractProperty( "234" ) );
        this.contractProperties.put( TsiConstants.CONTRACT_PRODUCT_DESCRIPTION, new ContractProperty( "Ticket Premium" ) );
        return this;
    }

    public TsiPaymentRequestMock withMerchantId( Integer merchantId ){
        if( merchantId != null ){
            this.contractProperties.put( TsiConstants.CONTRACT_MERCHANT_ID, new ContractProperty( Integer.toString( merchantId ) ) );
        } else {
            this.contractProperties.remove( TsiConstants.CONTRACT_MERCHANT_ID );
        }
        return this;
    }

    public TsiPaymentRequestMock withKeyId( Integer keyId ){
        if( keyId != null ){
            this.contractProperties.put( TsiConstants.CONTRACT_KEY_ID, new ContractProperty( Integer.toString( keyId ) ) );
        } else {
            this.contractProperties.remove( TsiConstants.CONTRACT_KEY_ID );
        }
        return this;
    }

    public TsiPaymentRequestMock withKeyValue( String keyValue ){
        if( keyValue != null ){
            this.contractProperties.put( TsiConstants.CONTRACT_KEY_VALUE, new ContractProperty( keyValue ) );
        } else {
            this.contractProperties.remove( TsiConstants.CONTRACT_KEY_VALUE );
        }
        return this;
    }

    public TsiPaymentRequestMock withProductDescription( String productDescription ){
        if( productDescription != null ){
            this.contractProperties.put( TsiConstants.CONTRACT_PRODUCT_DESCRIPTION, new ContractProperty( productDescription ) );
        } else {
            this.contractProperties.remove( TsiConstants.CONTRACT_PRODUCT_DESCRIPTION );
        }
        return this;
    }
}
