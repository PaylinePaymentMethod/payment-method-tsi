package com.payline.payment.tsi.service;

import com.payline.payment.tsi.TsiConstants;
import com.payline.pmapi.bean.configuration.AbstractParameter;
import com.payline.pmapi.bean.configuration.ContractParametersCheckRequest;
import com.payline.pmapi.bean.payment.ContractConfiguration;
import com.payline.pmapi.bean.payment.PaylineEnvironment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ConfigurationServiceImplTest {

    private ConfigurationServiceImpl service;

    @Before
    public void setup(){
        service = new ConfigurationServiceImpl();
    }

    @Test
    public void testConfigurationService_getParameters(){
        // when: recovering contract parameters
        List<AbstractParameter> parameters = service.getParameters( Locale.FRANCE );

        // then: exactly 2 parameters are returned
        Assert.assertEquals( 2, parameters.size() );
    }

    @Test
    public void testConfigurationService_check_wrongMerchantId(){
        // given: a not-integer merchant id
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put( TsiConstants.CONTRACT_MERCHANT_ID, "abc" );
        parameters.put( TsiConstants.CONTRACT_KEY_ID, "123" );
        ContractParametersCheckRequest checkRequest = this.setupCheckRequest( parameters );

        // when: checking configuration fields values
        Map<String, String> errors = service.check( checkRequest );

        // then:
        Assert.assertEquals( 1, errors.size() );
    }

    @Test
    public void testConfigurationService_check_wrongKeyId(){
        // given:
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put( TsiConstants.CONTRACT_MERCHANT_ID, "1234" );
        parameters.put( TsiConstants.CONTRACT_KEY_ID, "ABC" );
        ContractParametersCheckRequest checkRequest = this.setupCheckRequest( parameters );

        // when:
        Map<String, String> errors = service.check( checkRequest );

        // then:
        Assert.assertEquals( 1, errors.size() );
    }

    @Test
    public void testConfigurationService_isInteger_digits(){
        Assert.assertTrue( service.isInteger( "1234567890" ) );
    }

    @Test
    public void testConfigurationService_isInteger_zero(){
        Assert.assertTrue( service.isInteger( "0" ) );
    }

    @Test
    public void testConfigurationService_isInteger_null(){
        Assert.assertFalse( service.isInteger( null ) );
    }

    @Test
    public void testConfigurationService_isInteger_empty(){
        Assert.assertFalse( service.isInteger( "" ) );
    }

    @Test
    public void testConfigurationService_isInteger_negative(){
        Assert.assertFalse( service.isInteger( "-123" ) );
    }


    private ContractParametersCheckRequest setupCheckRequest( Map<String, String> accountInfo ){
        return ContractParametersCheckRequest.CheckRequestBuilder.aCheckRequest()
                .withAccountInfo( accountInfo )
                .withContractConfiguration( new ContractConfiguration( null, null ) )
                .withPaylineEnvironment( new PaylineEnvironment( "", "", "", true ) )
                .withLocale( Locale.FRANCE )
                .build();
    }

}
