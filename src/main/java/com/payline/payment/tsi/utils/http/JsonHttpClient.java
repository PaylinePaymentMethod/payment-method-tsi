package com.payline.payment.tsi.utils.http;

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

    /**
     * Send a POST request, with a JSON content type.
     *
     * @param scheme URL scheme
     * @param host URL host
     * @param path URL path
     * @param jsonContent The JSON content, as a string
     * @return The response returned from the HTTP call
     * @throws IOException
     */
    public Response doPost( String scheme, String host, String path, String jsonContent ) throws IOException {
        RequestBody body = RequestBody.create( MediaType.parse( CONTENT_TYPE ), jsonContent );
        return super.doPost( scheme, host, path, body, CONTENT_TYPE );
    }
}
