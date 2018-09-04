package com.payline.payment.tsi.utils.http;

import okhttp3.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * This utility class provides a basic HTTP client to send requests, using OkHttp library.
 * Refactored from {@link com.payline.payment.p24.utils.HttpClient}.
 * It must be extended to match each payment method needs.
 */
public abstract class HttpClient {

    private OkHttpClient client;

    /**
     *  Instantiate a HTTP client.
     *
     * @param connectTimeout Default connect timeout (in seconds) for new connections. A value of 0 means no timeout.
     * @param writeTimeout Default write timeout (in seconds) for new connections. A value of 0 means no timeout.
     * @param readTimeout Default read timeout (in seconds) for new connections. A value of 0 means no timeout.
     */
    public HttpClient( int connectTimeout, int writeTimeout, int readTimeout ) throws GeneralSecurityException {
        this.client = new OkHttpClient.Builder()
                .connectTimeout( connectTimeout, TimeUnit.SECONDS )
                .writeTimeout( writeTimeout, TimeUnit.SECONDS )
                .readTimeout( readTimeout, TimeUnit.SECONDS )
                .sslSocketFactory(HttpsURLConnection.getDefaultSSLSocketFactory(), getX509TrustManager())
                .build();
    }

    /**
     * From example in {@link okhttp3.OkHttpClient.Builder#sslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager)}
     *
     * @return the default X509TrustManager
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     */
    public X509TrustManager getX509TrustManager() throws NoSuchAlgorithmException, KeyStoreException {
        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        final TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        final X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
        return trustManager;
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
     */
    public Response doPost( String scheme, String host, String path, RequestBody body, String contentType )
            throws IOException {
        // create url
        HttpUrl url = new HttpUrl.Builder()
                .scheme( scheme )
                .host( host )
                .addPathSegment( path )
                .build();

        // create request
        Request request = new Request.Builder()
                .url( url )
                .post( body )
                .addHeader( "Content-Type", contentType )
                .build();

        // do the request
        return this.client.newCall( request ).execute();
    }
}
