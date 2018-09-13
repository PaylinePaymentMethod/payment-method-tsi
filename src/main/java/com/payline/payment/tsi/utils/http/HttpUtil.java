package com.payline.payment.tsi.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * To get the response content as a string
 */
public class HttpUtil {

    public static String inputStreamToString(final InputStream inputStream) throws IOException {
        Writer writer5 = new StringWriter();
        Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        int readLength = 0;
        char[] buffer = new char[1024];
        while ((readLength = reader.read(buffer)) != -1) {
            writer5.write(buffer, 0, readLength);
        }
        return writer5.toString();
    }

}
