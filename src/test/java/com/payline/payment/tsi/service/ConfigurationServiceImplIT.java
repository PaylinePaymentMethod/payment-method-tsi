package com.payline.payment.tsi.service;

import com.payline.payment.tsi.TsiConstants;
import com.payline.pmapi.bean.configuration.ContractParametersCheckRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

// TODO: Mock httpClient response to integrate these tests to ConfigurationServiceImplTest
/**
 * THIS TEST NEEDS AN INTERNET CONNECTION TO PASS.
 */
@RunWith( MockitoJUnitRunner.class )
public class ConfigurationServiceImplIT {

    @InjectMocks
    private ConfigurationServiceImpl service;

    private Properties testConfig;

    @Before
    public void setup() throws IOException {
        testConfig = new Properties();
        testConfig.load( ConfigurationServiceImplTest.class.getClassLoader().getResourceAsStream( "testConfig.properties" ) );
    }

    @Test
    public void testCheck_ok(){
        // given: valid contract properties
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put( TsiConstants.CONTRACT_MERCHANT_ID, testConfig.getProperty( "contractConfiguration.merchantId" ) );
        parameters.put( TsiConstants.CONTRACT_KEY_VALUE, testConfig.getProperty( "contractConfiguration.keyValue" ) );
        parameters.put( TsiConstants.CONTRACT_KEY_ID, testConfig.getProperty( "contractConfiguration.keyId" ) );
        parameters.put( TsiConstants.CONTRACT_PRODUCT_DESCRIPTION, testConfig.getProperty( "contractConfiguration.productDescription" ) );
        ContractParametersCheckRequest checkRequest = ConfigurationServiceImplTest.setupCheckRequest( parameters );

        // when: checking configuration fields values
        Map<String, String> errors = service.check( checkRequest );

        // then: result contains no error
        Assert.assertEquals( 0, errors.size() );
    }

    @Test
    public void testCheck_wrongAccountData() {
        // given: contract properties with the right format but not valid
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put( TsiConstants.CONTRACT_MERCHANT_ID, testConfig.getProperty( "contractConfiguration.merchantId" ) );
        //parameters.put( TsiConstants.CONTRACT_MERCHANT_ID, "123" );
        //parameters.put( TsiConstants.CONTRACT_KEY_VALUE, testConfig.getProperty( "contractConfiguration.keyValue" ) );
        parameters.put( TsiConstants.CONTRACT_KEY_VALUE, "wrongsecretkey" );
        parameters.put( TsiConstants.CONTRACT_KEY_ID, testConfig.getProperty( "contractConfiguration.keyId" ) );
        parameters.put( TsiConstants.CONTRACT_PRODUCT_DESCRIPTION, testConfig.getProperty( "contractConfiguration.productDescription" ) );
        ContractParametersCheckRequest checkRequest = ConfigurationServiceImplTest.setupCheckRequest( parameters );

        // when: checking configuration fields values
        Map<String, String> errors = service.check( checkRequest );

        // then: result contains 1 error
        Assert.assertEquals( 1, errors.size() );
    }

}
