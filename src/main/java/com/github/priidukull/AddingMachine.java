package com.github.priidukull;

import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddingMachine extends AbstractHandler implements Runnable {

    private volatile Long tempSum = 0L;
    private volatile Long sumToReturn;

    @Override
    public void run() {
        QueuedThreadPool threadPool = new QueuedThreadPool(24);
        Server server = new Server(threadPool);
        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory());
        http.setPort(1337);
        server.addConnector(http);

        server.setHandler(new AddingMachine());

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void handle(String target,
                                    Request baseRequest,
                                    HttpServletRequest request,
                                    HttpServletResponse response)
            throws IOException {
        String input = request.getReader().readLine();
        if (input.equals("end"))
            resetAndNotify();
        else
            addInputAndWait(Long.valueOf(input));
        response.getWriter().println(sumToReturn);
        baseRequest.setHandled(true);
    }

    private void addInputAndWait(Long input) {
        tempSum = tempSum + input;
        synchronized (tempSum) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    private void resetAndNotify() {
        sumToReturn = tempSum;
        tempSum = 0L;
        notifyAll();
    }
}
