package com.payline.payment.tsi.service;

import com.payline.payment.tsi.TsiConstants;
import com.payline.pmapi.bean.configuration.AbstractParameter;
import com.payline.pmapi.bean.configuration.ContractParametersCheckRequest;
import com.payline.pmapi.bean.configuration.ReleaseInformation;
import com.payline.pmapi.bean.payment.ContractConfiguration;
import com.payline.pmapi.bean.payment.PaylineEnvironment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RunWith( MockitoJUnitRunner.class )
public class ConfigurationServiceImplTest {

    @InjectMocks
    private ConfigurationServiceImpl service;

    @Test
    public void testGetParameters(){
        // when: recovering contract parameters
        List<AbstractParameter> parameters = service.getParameters( Locale.FRANCE );

        // then: exactly 2 parameters are returned
        Assert.assertEquals( 4, parameters.size() );
    }

    @Test
    public void testCheck_wrongMerchantId(){
        // given: a non-integer merchant id
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put( TsiConstants.CONTRACT_MERCHANT_ID, "abc" );
        parameters.put( TsiConstants.CONTRACT_KEY_VALUE, "secret" );
        parameters.put( TsiConstants.CONTRACT_KEY_ID, "123" );
        parameters.put( TsiConstants.CONTRACT_PRODUCT_DESCRIPTION, "Ticket Premium" );
        ContractParametersCheckRequest checkRequest = setupCheckRequest( parameters );

        // when: checking configuration fields values
        Map<String, String> errors = service.check( checkRequest );

        // then: result contains 1 error
        Assert.assertEquals( 1, errors.size() );
    }

    @Test
    public void testCheck_wrongKeyId(){
        // given: a non-integer key id
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put( TsiConstants.CONTRACT_MERCHANT_ID, "1234" );
        parameters.put( TsiConstants.CONTRACT_KEY_VALUE, "secret" );
        parameters.put( TsiConstants.CONTRACT_KEY_ID, "ABC" );
        parameters.put( TsiConstants.CONTRACT_PRODUCT_DESCRIPTION, "Ticket Premium" );
        ContractParametersCheckRequest checkRequest = setupCheckRequest( parameters );

        // when: checking configuration fields values
        Map<String, String> errors = service.check( checkRequest );

        // then: result contains 1 error
        Assert.assertEquals( 1, errors.size() );
    }

    @Test
    public void testGetReleaseInformation_ok(){
        // when: getReleaseInformation method is called
        ReleaseInformation releaseInformation = service.getReleaseInformation();

        // then: result is not null
        Assert.assertNotNull( releaseInformation );
        Assert.assertNotEquals( "unknown", releaseInformation.getVersion() );
        Assert.assertNotEquals( 1900, releaseInformation.getDate().getYear() );
    }

    @Test
    public void testGetReleaseInformation_versionFormat(){
        // when: getReleaseInformation method is called
        ReleaseInformation releaseInformation = service.getReleaseInformation();

        // then: the version has a valid format
        Assert.assertNotNull( releaseInformation );
        Assert.assertTrue( releaseInformation.getVersion().matches( "^\\d\\.\\d(\\.\\d)?$" ) );
    }

    // TODO: Improve this test case ! Testing the result is not empty is not enough.
    @Test
    public void testGetName_notNull(){
        // when: getReleaseInformation method is called
        String name = service.getName( Locale.FRANCE );

        // then: result is not null and not empty
        Assert.assertNotNull( name );
        Assert.assertFalse( name.isEmpty() );
    }

    @Test
    public void testIsInteger_digits(){
        Assert.assertTrue( service.isInteger( "1234567890" ) );
    }

    @Test
    public void testIsInteger_zero(){
        Assert.assertTrue( service.isInteger( "0" ) );
    }

    @Test
    public void testIsInteger_null(){
        Assert.assertFalse( service.isInteger( null ) );
    }

    @Test
    public void testIsInteger_empty(){
        Assert.assertFalse( service.isInteger( "" ) );
    }

    @Test
    public void testIsInteger_negative(){
        Assert.assertFalse( service.isInteger( "-123" ) );
    }


    static ContractParametersCheckRequest setupCheckRequest( Map<String, String> accountInfo ){
        return ContractParametersCheckRequest.CheckRequestBuilder.aCheckRequest()
                .withAccountInfo( accountInfo )
                .withContractConfiguration( new ContractConfiguration( null, null ) )
                .withPaylineEnvironment( new PaylineEnvironment( "", "", "", true ) )
                .withLocale( Locale.FRANCE )
                .build();
    }

}
