package com.payline.payment.tsi.utils;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This utility class provides a basic HTTP client to send requests, using OkHttp library.
 * Refactored from {@link com.payline.payment.p24.utils.HttpClient}.
 */
public class HttpClient {

    private OkHttpClient client;

    public HttpClient(){
        // TODO: make these values editable through a config file ?
        this.client = new OkHttpClient.Builder()
                .connectTimeout( 5, TimeUnit.SECONDS )
                .writeTimeout( 10, TimeUnit.SECONDS )
                .readTimeout( 15, TimeUnit.SECONDS )
                .build();
    }


    // TODO: doc!
    public Response doPost( String scheme, String host, String path, Map<String, String> body, String contentType ) throws IOException{
        // create body from Map params
        RequestBody requestBody = this.createMultipartBody( body );

        // create url
        HttpUrl url = new HttpUrl.Builder()
                .scheme( scheme )
                .host( host )
                .addPathSegment( path )
                .build();

        // create request
        Request request = new Request.Builder()
                .url( url )
                .post( requestBody )
                .addHeader( "Content-Type", contentType )
                .build();

        // do the request
        return this.client.newCall( request ).execute();
    }

    /**
     * Create a {@link RequestBody} from a map of args
     *
     * @param formData a Map containing all fields to put in the requestBody
     * @return the RequestBody instance
     */
    public RequestBody createMultipartBody( Map<String, String> formData ){
        MultipartBody.Builder mbb = new MultipartBody.Builder();
        mbb.setType( MultipartBody.FORM );
        for( String key : formData.keySet() ){
            mbb.addFormDataPart( key, formData.get( key ) );
        }
        return mbb.build();
    }
}
