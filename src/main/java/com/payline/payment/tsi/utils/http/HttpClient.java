package com.payline.payment.tsi.utils.http;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

/**
 * This utility class provides a basic HTTP client to send requests, using OkHttp library.
 * Refactored from p24 HttpClient
 * It must be extended to match each payment method needs.
 */
public abstract class HttpClient {

    protected org.apache.http.client.HttpClient client;

    /**
     *  Instantiate a HTTP client.
     *
     * @param connectTimeout Determines the timeout in milliseconds until a connection is established
     * @param requestTimeout The timeout in milliseconds used when requesting a connection from the connection manager
     * @param socketTimeout Defines the socket timeout (SO_TIMEOUT) in milliseconds, which is the timeout for waiting for data or, put differently, a maximum period inactivity between two consecutive data packets)
     * @throws GeneralSecurityException
     */
    public HttpClient( int connectTimeout, int requestTimeout, int socketTimeout ) throws GeneralSecurityException {

        final RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(connectTimeout * 1000)
            .setConnectionRequestTimeout(requestTimeout * 1000)
            .setSocketTimeout(socketTimeout * 1000).build();

        final HttpClientBuilder builder = HttpClientBuilder.create();
        builder.useSystemProperties()
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCredentialsProvider(new BasicCredentialsProvider())
                .setSSLSocketFactory(new SSLConnectionSocketFactory(HttpsURLConnection.getDefaultSSLSocketFactory(), SSLConnectionSocketFactory.getDefaultHostnameVerifier()));
        this.client = builder.build();
    }

    /**
     * Send a POST request.
     *
     * @param scheme URL scheme
     * @param host URL host
     * @param path URL path
     * @param body Request body
     * @param contentType The content type of the request body
     * @return The response returned from the HTTP call
     * @throws IOException
     * @throws URISyntaxException
     */
    public HttpResponse doPost(String scheme, String host, String path, String body, String contentType )
            throws IOException, URISyntaxException {

        final URI uri = new URIBuilder()
                .setScheme(scheme)
                .setHost(host)
                .setPath(path)
                .build();

        final HttpPost httpPostRequest = new HttpPost(uri);
        httpPostRequest.setEntity(new StringEntity(body));
        httpPostRequest.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
        final HttpResponse httpResp = this.client.execute(httpPostRequest);
        return httpResp;
    }
}
