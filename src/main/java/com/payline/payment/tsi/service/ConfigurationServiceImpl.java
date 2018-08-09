package com.payline.payment.tsi.service;

import com.payline.payment.tsi.TsiConstants;
import com.payline.payment.tsi.utils.config.ConfigProperties;
import com.payline.payment.tsi.utils.i18n.I18nService;
import com.payline.pmapi.bean.configuration.*;
import com.payline.pmapi.service.ConfigurationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ConfigurationServiceImpl implements ConfigurationService {

    private static final Logger logger = LogManager.getLogger( ConfigurationServiceImpl.class );

    /** The release date format */
    private static final String RELEASE_DATE_FORMAT = "dd/MM/yyyy";

    private I18nService i18n = I18nService.getInstance();

    @Override
    public List<AbstractParameter> getParameters( Locale locale ){
        List<AbstractParameter> parameters = new ArrayList<>();

        // Merchant ID
        final InputParameter merchantId = new InputParameter();
        merchantId.setKey( TsiConstants.CONTRACT_MERCHANT_ID );
        merchantId.setLabel( i18n.getMessage( "contractConfiguration.merchantId.label", locale ) );
        merchantId.setDescription( i18n.getMessage( "contractConfiguration.merchantId.description", locale ) );
        merchantId.setRequired( true );

        parameters.add( merchantId );

        // Key value
        final InputParameter keyValue = new InputParameter();
        keyValue.setKey( TsiConstants.CONTRACT_KEY_VALUE );
        keyValue.setLabel( i18n.getMessage( "contractConfiguration.keyValue.label", locale ) );
        keyValue.setDescription( i18n.getMessage( "contractConfiguration.keyValue.description", locale ) );
        keyValue.setRequired( true );

        parameters.add( keyValue );

        // Key ID
        final InputParameter keyId = new InputParameter();
        keyId.setKey( TsiConstants.CONTRACT_KEY_ID );
        keyId.setLabel( i18n.getMessage( "contractConfiguration.keyId.label", locale ) );
        keyId.setDescription( i18n.getMessage( "contractConfiguration.keyId.description", locale ) );
        keyId.setRequired( true );

        parameters.add( keyId );

        // Product description
        final ListBoxParameter productDescription = new ListBoxParameter();
        Map<String, String> elements = new HashMap<>();
        elements.put( "Ticket Premium", "Ticket Premium" );
        productDescription.setList( elements );
        productDescription.setKey( TsiConstants.CONTRACT_PRODUCT_DESCRIPTION );
        productDescription.setLabel( i18n.getMessage( "contractConfiguration.productDescription.label", locale ) );
        productDescription.setDescription( i18n.getMessage( "contractConfiguration.productDescription.description", locale ) );
        productDescription.setRequired( true );

        parameters.add( productDescription );

        return parameters;
    }

    @Override
    public Map<String, String> check( ContractParametersCheckRequest contractParametersCheckRequest ){
        Locale locale = contractParametersCheckRequest.getLocale();
        Map<String, String> errors = new HashMap<>();
        final Map<String, String> accountInfo = contractParametersCheckRequest.getAccountInfo();

        // Merchant id
        final String merchantId = accountInfo.get( TsiConstants.CONTRACT_MERCHANT_ID );
        if( !isInteger( merchantId ) ){
            errors.put( TsiConstants.CONTRACT_MERCHANT_ID, i18n.getMessage( "contractConfiguration.merchantId.error", locale ) );
        }

        // Key id
        final String keyId = accountInfo.get( TsiConstants.CONTRACT_KEY_ID );
        if( !isInteger( keyId ) ){
            errors.put( TsiConstants.CONTRACT_KEY_ID, i18n.getMessage( "contractConfiguration.keyId.error", locale ) );
        }

        // TODO: is the secret key always 32-characters-long ?

        // TODO: is there a TSI endpoint to test the connection ?

        return errors;
    }

    @Override
    public ReleaseInformation getReleaseInformation(){
        Properties props = new Properties();
        try {
            props.load( ConfigurationServiceImpl.class.getClassLoader().getResourceAsStream( "release.properties" ) );
        } catch( IOException e ){
            logger.error("An error occurred reading the file: release.properties" );
            props.setProperty( "release.version", "unknown" );
            props.setProperty( "release.date", "01/01/1900" );
        }

        LocalDate date = LocalDate.parse( props.getProperty( "release.date" ), DateTimeFormatter.ofPattern( RELEASE_DATE_FORMAT ) );
        return ReleaseInformation.ReleaseBuilder.aRelease()
                .withDate( date )
                .withVersion( props.getProperty( "release.version" ) )
                .build();
    }

    @Override
    public String getName( Locale locale ){
        return i18n.getMessage( "paymentMethod.name", locale );
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
