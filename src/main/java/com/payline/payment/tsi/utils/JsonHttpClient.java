package com.payline.payment.tsi.utils;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class JsonHttpClient extends HttpClient {

    private static final String CONTENT_TYPE = "application/json";

    /**
     * Instantiate a HTTP client.
     *
     * @param connectTimeout Default connect timeout (in seconds) for new connections. A value of 0 means no timeout.
     * @param writeTimeout   Default write timeout (in seconds) for new connections. A value of 0 means no timeout.
     * @param readTimeout    Default read timeout (in seconds) for new connections. A value of 0 means no timeout.
     */
    public JsonHttpClient( int connectTimeout, int writeTimeout, int readTimeout ) {
        super( connectTimeout, writeTimeout, readTimeout );
    }

    // TODO: Doc !
    public Response doPost( String scheme, String host, String path, String jsonContent ) throws IOException {
        RequestBody body = RequestBody.create( MediaType.parse( CONTENT_TYPE ), jsonContent );
        return super.doPost( scheme, host, path, body, CONTENT_TYPE );
    }
}
