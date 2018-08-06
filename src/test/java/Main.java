package com.payline.payment.tsi.request;

import com.payline.payment.tsi.response.TsiGoResponse;
import com.payline.payment.tsi.security.Hmac;
import com.payline.payment.tsi.security.HmacAlgorithm;
import com.payline.payment.tsi.utils.JsonHttpClient;
import okhttp3.Response;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main( String[] args ) throws IOException {
        //hmacThis( "abcdefghijklmnopkrstuvwxyz12345|806" );
        http();
    }

    private static void hmac(){
        TsiGoRequest request = new TsiGoRequest(
                806,
                "abcdefghijklmnopkrstuvwxyz12346J",
                "1.02",
                "EUR",
                806,
                "Ticket Premium",
                "http://boutique.com/returnOK.php",
                "http://boutique.com/returnNOK.php",
                "http://boutique.com/returnS2S.php",
                "N",
                "Y",
                null
        );
        String key = "45f3bcf660df19f8364c222e887300fa";

        String message = request.buildSealMessage();
        System.out.println( "message: " + message );
        System.out.println( "key: " + key );
        Hmac hmac = new Hmac( key, HmacAlgorithm.MD5 );
        System.out.println( "hmac: " + hmac.digest( message ) );
    }

    private static void hmacThis( String message ){
        String key = "45f3bcf660df19f8364c222e887300fa";
        Hmac hmac = new Hmac( key, HmacAlgorithm.MD5 );
        System.out.println( hmac.digest( message ) );
    }

    private static void http() throws IOException {
        JsonHttpClient httpClient = new JsonHttpClient( 5, 10, 15 );

        // Build request
        Map<String, Object> custom = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "000000000000000000yyyyMMddHHmmss" );
        TsiGoRequest request = new TsiGoRequest(
                806,
                LocalDateTime.now().format( formatter ),
                "1.02",
                "EUR",
                806,
                "Ticket Premium",
                "http://boutique.com/returnOK.php",
                "http://boutique.com/returnNOK.php",
                "http://boutique.com/returnS2S.php",
                "N",
                "Y",
                null
        );

        // Seal request
        Hmac hmac = new Hmac( "45f3bcf660df19f8364c222e887300fa", HmacAlgorithm.MD5 );
        request.setMac( hmac.digest( request.buildSealMessage() ) );

        // Build request body
        System.out.println( request.buildBody() );

        // Send request
        Response response = httpClient.doPost( "https", "sandbox-voucher.tsiapi.com", "context", request.buildBody() );
        System.out.println( response );

        // Parse the response
        TsiGoResponse tsiGoResponse = (new TsiGoResponse.Builder()).fromJson( response.body().string() );
        System.out.println( "TsiGoResponse[" +
                "status=" + tsiGoResponse.getStatus() +
                ", message=\"" + tsiGoResponse.getMessage() + "\"" +
                ", url=\"" + tsiGoResponse.getUrl() + "\"" +
                ", tid=" + tsiGoResponse.getTid() +
                ", keyId=" + tsiGoResponse.getKeyId() +
                "]" );

        String statusCheckMac = hmac.digest( tsiGoResponse.getTid() + "|" + tsiGoResponse.getKeyId() );
        System.out.println( "Status Check mac: " + statusCheckMac );
    }

    private static void json(){
        int status = 15;
        String message = "WRONG HMAC";
        String url = "http://www.google.fr";
        String tid = "1234567890";
        String keyId = "806";

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

        System.out.println( json );
    }

}
