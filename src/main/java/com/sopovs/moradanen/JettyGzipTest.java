package com.sopovs.moradanen;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyGzipTest {

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		WebAppContext context = new WebAppContext();

		File root = File.createTempFile("jetty-gzip", "test");
		root.delete();
		root.mkdirs();
		File helloText = new File(root, "hello.txt");
		try (FileWriter helloWriter = new FileWriter(helloText)) {
			helloWriter.append("hello").flush();
		}

		char[] chars = new char[100000];
		Arrays.fill(chars, 'F');
		String largeContent = new String(chars);
		File largeText = new File(root, "large.txt");
		try (FileWriter largeWriter = new FileWriter(largeText)) {
			largeWriter.append(largeContent).flush();
		}

		context.setBaseResource(Resource.newResource(root.getCanonicalFile()));

		GzipHandler gzipHandler = new GzipHandler();
		gzipHandler.setIncludedMimeTypes("text/plain");
		gzipHandler.setMinGzipSize(2048);
		gzipHandler.setHandler(context);
		server.setHandler(gzipHandler);

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8080);
		server.setConnectors(new Connector[] { connector });

		server.start();
		server.join();
	}
}
