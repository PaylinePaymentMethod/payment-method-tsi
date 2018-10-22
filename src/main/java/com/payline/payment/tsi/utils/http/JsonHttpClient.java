package com.payline.payment.tsi.utils.http;

import com.payline.payment.tsi.exception.ExternalCommunicationException;
import org.apache.http.entity.ContentType;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

public class JsonHttpClient extends HttpClient {

    /**
     * Instantiate a HTTP client with default values.
     */
    private JsonHttpClient() {
        super();
    }

    private static class SingletonHolder {
        private final static JsonHttpClient INSTANCE = new JsonHttpClient();
    }

    /**
     * @return the singleton instance
     */
    public static JsonHttpClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Send a POST request, with a JSON content type.
     *
     * @param scheme URL scheme
     * @param host URL host
     * @param path URL path
     * @param jsonContent The JSON content, as a string
     * @return The response returned from the HTTP call
     * @throws ExternalCommunicationException
     * @throws UnsupportedEncodingException
     * @throws URISyntaxException
     */
    public StringResponse doPost(String scheme, String host, String path, String jsonContent ) throws ExternalCommunicationException, UnsupportedEncodingException, URISyntaxException {
        return super.doPost( scheme, host, path, jsonContent, ContentType.APPLICATION_JSON.toString());
    }
}
