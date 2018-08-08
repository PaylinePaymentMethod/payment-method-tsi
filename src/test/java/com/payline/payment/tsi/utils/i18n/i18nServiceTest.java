package com.payline.payment.tsi.utils.i18n;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

public class i18nServiceTest {

    private i18nService service;

    @Before
    public void setup(){
        this.service = i18nService.getInstance();
    }

    @Test
    public void testGetMessage_default(){
        // when: recovering the same message in FR, EN and a likely-not-implemented language (co: Corsican)
        String coMessage = service.getMessage( "contractConfiguration.keyId.error", new Locale( "co" ) );
        String enMessage = service.getMessage( "contractConfiguration.keyId.error", new Locale( "en" ) );
        String frMessage = service.getMessage( "contractConfiguration.keyId.error", new Locale( "fr" ) );

        // then: all 3 messages are not null, FR is different then EN, CO equals EN.
        Assert.assertNotNull( coMessage );
        Assert.assertNotNull( enMessage );
        Assert.assertNotNull( frMessage );
        Assert.assertNotEquals( frMessage, enMessage );
        Assert.assertEquals( enMessage, coMessage );
    }

    @Test
    public void testGetMessage_notNull(){
        // when: recovering a message with a key that does not exist
        String message = service.getMessage( "this.key.surely.does.not.exist", Locale.getDefault() );

        // then: resulting message is not null
        Assert.assertNotNull( message );
    }

}
