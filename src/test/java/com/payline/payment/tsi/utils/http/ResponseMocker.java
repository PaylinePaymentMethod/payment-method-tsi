package com.payline.payment.tsi.utils.http;


import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;

import java.io.UnsupportedEncodingException;

/**
 * Utility test class which enable to mock {@link okhttp3.Response} objects.
 */
public class ResponseMocker {

    public static HttpResponse mock(int httpCode, String httpMessage, String jsonBody ) throws UnsupportedEncodingException {
        final HttpResponse httpResponse = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1) , httpCode, httpMessage);
        httpResponse.setEntity(jsonBody == null ? null: new StringEntity(jsonBody));
        return httpResponse;
    }

}
