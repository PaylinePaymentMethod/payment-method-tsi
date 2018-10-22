package com.payline.payment.tsi.utils.http;

import com.payline.payment.tsi.exception.ExternalCommunicationException;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This utility class provides a basic HTTP client to send requests, using OkHttp library.
 * Refactored from p24 HttpClient
 * It must be extended to match each payment method needs.
 */
public abstract class HttpClient {

    protected CloseableHttpClient client;

    private static final Logger LOGGER = LogManager.getLogger(HttpClient.class);

    /**
     *  Instantiate a HTTP client.
     */
    public HttpClient() {

        final RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(5000)
            .setConnectionRequestTimeout(10000)
            .setSocketTimeout(10000).build();

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
    public StringResponse doPost(String scheme, String host, String path, String body, String contentType )
            throws URISyntaxException, UnsupportedEncodingException, ExternalCommunicationException {

        final URI uri = new URIBuilder()
                .setScheme(scheme)
                .setHost(host)
                .setPath(path)
                .build();

        final HttpPost httpPostRequest = new HttpPost(uri);
        httpPostRequest.setEntity(new StringEntity(body));
        httpPostRequest.setHeader(HttpHeaders.CONTENT_TYPE, contentType);

        final long start = System.currentTimeMillis();
        int count = 0;
        StringResponse strResp = null;
        while (count < 3 && strResp == null) {
            try (CloseableHttpResponse httpResp = this.client.execute(httpPostRequest)) {

                LOGGER.info("Start partner call... [HOST: {}]", host);

                strResp = new StringResponse();
                strResp.setCode(httpResp.getStatusLine().getStatusCode());
                strResp.setMessage(httpResp.getStatusLine().getReasonPhrase());

                if (httpResp.getEntity() != null) {
                    final String responseAsString = EntityUtils.toString(httpResp.getEntity()); // , "UTF-8"
                    strResp.setContent(responseAsString);
                }
                final long end = System.currentTimeMillis();

                LOGGER.info("End partner call [T: {}ms] [CODE: {}]", end - start, strResp.getCode());

            } catch (final IOException e) {
                LOGGER.error("Error while partner call [T: {}ms]", System.currentTimeMillis() - start, e);
                strResp = null;
            } finally {
                count++;
            }
        }

        if (strResp == null) {
            throw new ExternalCommunicationException("Partner response empty");
        }

        return strResp;
    }
}
