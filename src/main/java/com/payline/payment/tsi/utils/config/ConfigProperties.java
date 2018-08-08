package com.payline.payment.tsi.utils.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class which reads and provides config properties.
 */
public class ConfigProperties {

    private static final String FILENAME = "config.properties";

    private static final Logger logger = LogManager.getLogger( ConfigProperties.class );

    private static Properties properties;

    /* This class has only static methods: no need to instantiate it */
    private ConfigProperties() {
    }

    /**
     * Get a config property by its name.
     * Warning, if the property is environment-dependent, use {@link ConfigProperties#get(String, ConfigEnvironment)} instead.
     *
     * @param key The name of the property to recover
     * @return The property value. Can be null if the property has not been found.
     */
    public static String get( String key ){
        if( properties == null ){
            readProperties();
        }
        return properties.getProperty( key );
        // TODO: if null, backup to a code-based default configuration ? (to avoid runtime exception)
    }

    /**
     * Get a environment-dependent config property by its name.
     *
     * @param key The name of the property to recover
     * @param environment The runtime environment
     * @return The property value. Can be null if the property has not been found.
     */
    public static String get( String key, ConfigEnvironment environment ){
        String prefix = "";
        if( environment != null ){
            prefix += environment.getPrefix() + ".";
        }
        return get( prefix + key );
    }

    /**
     * Reads the properties file and stores the result.
     */
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
