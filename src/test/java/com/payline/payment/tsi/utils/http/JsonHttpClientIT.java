package com.payline.payment.tsi.utils.http;

import com.payline.payment.tsi.exception.ExternalCommunicationException;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * To test the HttpClient behavior
 */
public class JsonHttpClientIT {

    static JsonHttpClient client = JsonHttpClient.getInstance();
    static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
    private static final int MYTHREADS = 30;

    private static class SomeTask implements Runnable
    {
        @Override
        public void run()
        {
            try {
                final StringResponse msg = client.doPost("http", "localhost:8080", "/test", "{\"key\":\"value\"}");
                System.out.println(dateFormat.format(new Date()) + " " + msg.getContent());
            } catch (ExternalCommunicationException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test() {

        ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
        for (int i = 0; i < MYTHREADS; i++) executor.execute(new SomeTask());

        executor.shutdown();

        // Wait until all threads are finish
        while (!executor.isTerminated()) { }

        System.out.println("END");
    }
}
