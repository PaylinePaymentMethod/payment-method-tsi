package com.payline.payment.tsi.response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TsiGoResponseTest {

    private TsiGoResponse.Builder builder;

    @Before
    public void setup(){
        this.builder = new TsiGoResponse.Builder();
    }

    @Test
    public void testBuilder_minimal(){
        // given: a minimal response body (status and message only)
        int status = 15;
        String message = "WRONG MAC";
        String json = mockJson( status, message );

        // when: instantiating the TsiGoResponse
        TsiGoResponse response = builder.fromJson( json );

        // then: fields values are correct
        Assert.assertNotNull( response );
        Assert.assertEquals( status, response.getStatus() );
        Assert.assertEquals( message, response.getMessage() );
    }

    public void testBuilder_full(){
        // given: a full response body
        int status = 1;
        String message = "OK";
        String url = "https://subdomain.tsiapi.com";
        String tid = "1234567890";
        String keyId = "123";
        String json = mockJson( status, message, url, tid, keyId );

        // when: instantiating the TsiGoResponse
        TsiGoResponse response = builder.fromJson( json );

        // then: fields values are correct
        Assert.assertNotNull( response );
        Assert.assertEquals( status, response.getStatus() );
        Assert.assertEquals( message, response.getMessage() );
        Assert.assertEquals( url, response.getUrl() );
        Assert.assertEquals( tid, response.getTid() );
        Assert.assertEquals( keyId, response.getKeyId() );
    }

    private String mockJson( int status, String message ){
        return mockJson( status, message, null, null, null );
    }

    private String mockJson( int status, String message, String url, String tid, String keyId ){
        String json = "{"
                + "\"status\":" + status
                + ",\"message\":\"" + message + "\"";
        if( url != null ){
            json += ",\"url\":\"" + url + "\"";
        }
        if( tid != null ){
            json += ",\"tid\":\"" + tid + "\"";
        }
        if( keyId != null ){
            json += ",\"keyid\":\"" + keyId + "\"";
        }
        json += "}";
        return json;
    }

}
