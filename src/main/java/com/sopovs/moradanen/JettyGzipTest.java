package com.sopovs.moradanen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlets.gzip.GzipHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyGzipTest extends AbstractHandler {

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		WebAppContext context = new WebAppContext();

		File root = File.createTempFile("jetty-gzip", "test");
		root.delete();
		root.mkdirs();
		File text = new File(root, "text.txt");

		char[] chars = new char[100000];
		Arrays.fill(chars, 'F');
		String testContent = new String(chars);

		new FileWriter(text).append("hello").flush();

		context.setBaseResource(Resource.newResource(root.getCanonicalFile()));

		GzipHandler gzipHandler = new GzipHandler();
		gzipHandler.setMimeTypes("text/plain");
		gzipHandler.setMinGzipSize(2048);
		gzipHandler.setHandler(context);
		server.setHandler(gzipHandler);

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8080);
		server.setConnectors(new Connector[] { connector });

		server.start();
		server.join();
	}

	@Override
	public void handle(String target, org.eclipse.jetty.server.Request baseRequest,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException {
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		response.getWriter().println("<h1>Hello World</h1>");
	}

}
