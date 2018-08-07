package com.payline.payment.tsi.service;

import com.payline.payment.tsi.TsiConstants;
import com.payline.pmapi.bean.configuration.*;
import com.payline.pmapi.service.ConfigurationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ConfigurationServiceImpl implements ConfigurationService {

    // TODO: check PM-API doc for this format (and add it if necessary)
    private static final String RELEASE_DATE_FORMAT = "dd/MM/yyyy";

    @Override
    public List<AbstractParameter> getParameters( Locale locale ){
        List<AbstractParameter> parameters = new ArrayList<>();

        // Merchant ID
        final InputParameter merchantId = new InputParameter();
        merchantId.setKey( TsiConstants.CONTRACT_MERCHANT_ID );
        merchantId.setLabel( "merchant id" ); // TODO: internationalize
        merchantId.setDescription( "Merchant identifier" ); // TODO: internationalize
        merchantId.setRequired( true );

        parameters.add( merchantId );

        // Key value
        final InputParameter keyValue = new InputParameter();
        keyValue.setKey( TsiConstants.CONTRACT_KEY_VALUE );
        keyValue.setLabel( "secret key" ); // TODO: internationalize
        keyValue.setDescription( "The secret key provided by TSI" ); // TODO: internationalize
        keyValue.setRequired( true );

        parameters.add( keyValue );

        // Key ID
        final InputParameter keyId = new InputParameter();
        keyId.setKey( TsiConstants.CONTRACT_KEY_ID );
        keyId.setLabel( "key id" ); // TODO: internationalize
        keyId.setDescription( "Identifier of the key" ); // TODO: internationalize
        keyId.setRequired( true );

        parameters.add( keyId );

        // Product description
        // TODO !

        return parameters;
    }

    @Override
    public Map<String, String> check( ContractParametersCheckRequest contractParametersCheckRequest ){
        //Locale locale = contractParametersCheckRequest.getLocale();
        Map<String, String> errors = new HashMap<>();
        final Map<String, String> accountInfo = contractParametersCheckRequest.getAccountInfo();

        // Merchant id
        final String merchantId = accountInfo.get( TsiConstants.CONTRACT_MERCHANT_ID );
        if( !isInteger( merchantId ) ){
            errors.put( TsiConstants.CONTRACT_MERCHANT_ID, "merchant identifier must be an integer" ); // TODO: internationalize
        }

        // Key id
        final String keyId = accountInfo.get( TsiConstants.CONTRACT_KEY_ID );
        if( !isInteger( keyId ) ){
            errors.put( TsiConstants.CONTRACT_KEY_ID, "key identifier must be an integer" ); // TODO: internationalize
        }

        // TODO: is the secret key always 32-characters-long ?

        // TODO: is there a TSI endpoint to test the connection ?

        return errors;
    }

    @Override
    public ReleaseInformation getReleaseInformation(){
        // TODO: recover release version from build.gradle ?
        // TODO: insert release date during publish gradle task ?
        LocalDate date = LocalDate.parse( "26/09/2018", DateTimeFormatter.ofPattern( RELEASE_DATE_FORMAT ) );
        return ReleaseInformation.ReleaseBuilder.aRelease()
                .withDate( date )
                .withVersion( "1.0.0-SNAPSHOT" )
                .build();
    }

    @Override
    public String getName( Locale locale ){
        // TODO: use locale to determine name
        return "TSI";
    }

    /**
     * Checks if the given string is a positive integer.
     * @param s the string to ckeck
     * @return true if the string is not null, not empty and only composed of digits, false otherwise.
     */
    protected boolean isInteger( String s ){
        if( s == null || s.isEmpty() ){
            return false;
        }
        for( int i = 0; i < s.length(); i++ ){
            if( !Character.isDigit( s.charAt( i ) ) ){
                return false;
            }
        }
        return true;
    }
}
