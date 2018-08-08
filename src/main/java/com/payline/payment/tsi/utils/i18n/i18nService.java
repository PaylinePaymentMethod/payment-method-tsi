package com.payline.payment.tsi.utils.i18n;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class i18nService {

    private static final Logger logger = LogManager.getLogger( i18nService.class );

    private static final String RESOURCE_BUNDLE_BASE_NAME = "messages";

    /**
     * Private constructor
     */
    private i18nService(){
        Locale.setDefault( new Locale( "en" ) );
    }

    /**
     * Holder
     */
    private static class SingletonHolder {
        /**
         * Unique instance, not preinitializes
         */
        private final static i18nService instance = new i18nService();
    }

    /**
     * Unique access point for the singleton instance
     */
    public static i18nService getInstance() {
        return SingletonHolder.instance;
    }

    public String getMessage( final String key, final Locale locale ){
        ResourceBundle messages = ResourceBundle.getBundle( RESOURCE_BUNDLE_BASE_NAME, locale );
        try {
            return messages.getString( key );
        }
        catch( MissingResourceException e ){
            logger.error( "Trying to get a message with a key that does not exist: " + key + " (language: " + locale.getLanguage() + ")" );
            return "???" + locale + "." + key + "???";
        }
    }

    // If ever needed, implement getMessage( String, Locale, String... ) to insert values into the translation messages
}
