package com.payline.payment.tsi.utils.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

// TODO: Doc !
public class ConfigProperties {

    private static final String FILENAME = "config.properties";

    private static final Logger logger = LogManager.getLogger( ConfigProperties.class );

    private static Properties properties;

    public static String get( String key ){
        if( properties == null ){
            readProperties();
        }
        return properties.getProperty( key );
        // TODO: if null, backup to a code-based default configuration ? (to avoid runtime exception)
    }

    public static String get( String key, ConfigEnvironment environment ){
        String prefix = "";
        if( environment != null ){
            prefix += environment.getPrefix() + ".";
        }
        return get( prefix + key );
    }

    private static void readProperties(){
        properties = new Properties();

        try {
            InputStream inputStream = ConfigProperties.class.getClassLoader().getResourceAsStream( FILENAME );
            properties.load( inputStream );
        }
        catch( Exception e ){
            logger.error("An error occurred reading the configuration properties file");
            // TODO: backup to a code-based default configuration ? (to avoid runtime exception)
        }
    }

}
