package com.sopovs.moradanen;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class JettyHttpsTest extends AbstractHandler {

	public static void main(String[] args) throws Exception {
		Server server = new Server(10443);
		server.setHandler(new JettyHttpsTest());

		SslContextFactory factory = new SslContextFactory();
		factory.setKeyStorePath("src/main/resources/test.jks");
		factory.setKeyStorePassword("secret");
		factory.setKeyManagerPassword("password");
		factory.setProtocol("TLS");

		ServerConnector sslConnector = new ServerConnector(server, factory);
		sslConnector.setPort(10443);
		server.setConnectors(new Connector[] { sslConnector });

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
