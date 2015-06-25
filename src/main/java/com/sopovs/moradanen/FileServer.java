package com.sopovs.moradanen;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlets.gzip.GzipHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

public class FileServer
{
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setBaseResource(Resource.newResource("."));

        GzipHandler gzip = new GzipHandler();
        gzip.setMimeTypes("text/plain");
        server.setHandler(gzip);
        gzip.setHandler(webAppContext);

        server.start();
        server.join();
    }
}