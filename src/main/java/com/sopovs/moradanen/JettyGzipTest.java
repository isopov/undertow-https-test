package com.sopovs.moradanen;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlets.gzip.GzipHandler;

public class JettyGzipTest {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        GzipHandler gzipHandler = new GzipHandler();
        gzipHandler.setMimeTypes("text/plain");
        gzipHandler.setMinGzipSize(2048);
        gzipHandler.setHandler(new TestHandler());
        server.setHandler(gzipHandler);

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[] { connector });

        server.start();
        server.join();
    }

    public static class TestHandler extends AbstractHandler {

        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {
            if ("/small".equals(target)) {
                System.out.println(target);
                response.setContentType("text/plain");
                response.setContentLength("hello".length());
                response.getWriter().println("hello");
            } else {
                response.setContentType("text/plain");
                response.setContentLength(10000);
                char[] chars = new char[100000];
                Arrays.fill(chars, 'F');
                String largeContent = new String(chars);
                response.getWriter().println(largeContent);
            }
        }

    }
}
