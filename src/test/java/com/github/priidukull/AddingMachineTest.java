package com.github.priidukull;

import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.Long.parseLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AddingMachineTest {

    @BeforeSuite
    static void startAddingMachine() {
        String[] args = {};
        Main.main(args);
    }

    @AfterMethod
    void resetAddingMachine() throws IOException {
        executeRequestWithEntity("end");
    }

    @Test
    void callEndImmediately() throws Exception {
        var actual = executeRequestWithEntity("end");

        assertEquals(HttpStatus.SC_OK, statusCode(actual));
        assertEquals(0L, responseBodyAsLong(actual));
    }

    @Test
    void doNotReturnValueIfEndHasNotBeenCalled() throws Exception {
        Assertions.assertThrows(SocketTimeoutException.class, () -> {
            HttpPost request = new HttpPost("http://localhost:1337");
            request.setEntity(new StringEntity("1"));
            var requestConfig = RequestConfig.custom().setSocketTimeout(200).build();
            request.setConfig(requestConfig);
            HttpClientBuilder.create().build().execute(request);
        });
    }

    @Test(threadPoolSize = 2)
    void returnValueAfterEndHasBeenCalled() throws Exception {
        Future<?> future = executeRequestInAThread(String.valueOf(1L), 1L);

        Thread.sleep(1000);
        var actual = executeRequestWithEntity("end");

        assertEquals(HttpStatus.SC_OK, statusCode(actual));
        assertEquals(1L, responseBodyAsLong(actual));
        future.get();
    }

    @Test(threadPoolSize = 20)
    void handle20SimultaneousRequests() throws Exception {
        List<Future> futures = new java.util.ArrayList<>(Collections.emptyList());

        for (int i = 0; i < 19; ++i) {
            Future<?> future = executeRequestInAThread(String.valueOf(1L), 19L);
            futures.add(future);
        }

        Thread.sleep(1000);
        var actual = executeRequestWithEntity("end");

        assertEquals(HttpStatus.SC_OK, statusCode(actual));
        assertEquals(19L, responseBodyAsLong(actual));
        for (Future future : futures)
            future.get();
    }

    @Test(threadPoolSize = 2)
    void handleBigInput() throws Exception {
        Long TEN_BILLION = 10000000000L;
        Future<?> future = executeRequestInAThread(String.valueOf(TEN_BILLION), TEN_BILLION);

        Thread.sleep(1000);
        var actual = executeRequestWithEntity("end");

        assertEquals(HttpStatus.SC_OK, statusCode(actual));
        assertEquals(TEN_BILLION, responseBodyAsLong(actual));
        future.get();
    }

    @Test(threadPoolSize = 2)
    void resetCounterWithEnd() throws Exception {
        Future<?> future = executeRequestInAThread(String.valueOf(1L), 1L);

        Thread.sleep(1000);
        var firstCall = executeRequestWithEntity("end");
        var actual = executeRequestWithEntity("end");

        assertEquals(HttpStatus.SC_OK, statusCode(firstCall));
        assertEquals(1L, responseBodyAsLong(firstCall));
        assertEquals(HttpStatus.SC_OK, statusCode(actual));
        assertEquals(0L, responseBodyAsLong(actual));
        future.get();
    }

    private Future<?> executeRequestInAThread(String input, long expectedBody) {
        var executorService = Executors.newSingleThreadExecutor();
        return executorService.submit(() -> {
            try {
                var actual = executeRequestWithEntity(input);
                assertEquals(HttpStatus.SC_OK, statusCode(actual));
                assertEquals(expectedBody, responseBodyAsLong(actual));
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        });
    }

    private static CloseableHttpResponse executeRequestWithEntity(String entity) throws IOException {
        var request = new HttpPost("http://localhost:1337");
        request.setEntity(new StringEntity(entity));
        return HttpClientBuilder.create().build().execute(request);
    }

    private int statusCode(CloseableHttpResponse actual) {
        return actual.getStatusLine().getStatusCode();
    }

    private long responseBodyAsLong(CloseableHttpResponse actual) throws IOException {
        return parseLong(EntityUtils.toString(actual.getEntity()).trim());
    }
}
