package com.payline.payment.tsi.response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TsiStatusCheckResponseTest {

    private TsiStatusCheckResponse.Builder builder;

    @Before
    public void setup(){
        this.builder = new TsiStatusCheckResponse.Builder();
    }

    @Test
    public void testBuilder_full(){
        // given: a full response body
        String authId = "1234567";
        String tid = "123456789012345678901234567890AB";
        String status = "OK";
        String erCode = "0";
        String message = "MESSAGE";
        String amount = "12,34";
        String multi = "f";
        String dtime = "2018-08-02 10:37:22";
        String country = "FRA";
        String json = mockJson( authId, tid, status, erCode, message, amount, multi, dtime, country );

        // when: instantiating the TsiGoResponse
        TsiStatusCheckResponse response = builder.fromJson( json );

        // then: fields values are correct
        Assert.assertNotNull( response );
        Assert.assertEquals( authId, response.getAuthId() );
        Assert.assertEquals( tid, response.getTid() );
        Assert.assertEquals( status, response.getStatus() );
        Assert.assertEquals( erCode, response.getErCode() );
        Assert.assertEquals( message, response.getMessage() );
        Assert.assertEquals( amount, response.getAmount() );
        Assert.assertEquals( multi, response.getMulti() );
        Assert.assertEquals( dtime, response.getDtime() );
        Assert.assertEquals( country, response.getCountry() );
    }

    public static String mockJson( String authId, String tid, String status, String erCode, String message,
                                   String amount, String multi, String dtime, String country ){
        List<String> fields = new ArrayList<>();

        if( authId != null ){
            fields.add( "\"authid\":\"" + authId + "\"" );
        }
        if( tid != null ){
            fields.add( "\"tid\":\"" + tid + "\"" );
        }
        if( status != null ){
            fields.add( "\"status\":\"" + status + "\"" );
        }
        if( erCode != null ){
            fields.add( "\"ercode\":\"" + erCode + "\"" );
        }
        if( message != null ){
            fields.add( "\"message\":\"" + message + "\"" );
        }
        if( amount != null ){
            fields.add( "\"amount\":\"" + amount + "\"" );
        }
        if( multi != null ){
            fields.add( "\"multi\":\"" + multi + "\"" );
        }
        if( dtime != null ){
            fields.add( "\"dtime\":\"" + dtime + "\"" );
        }
        if( country != null ){
            fields.add( "\"country\":\"" + country + "\"" );
        }

        return "{" + String.join( ",", fields ) + "}";
    }

}
