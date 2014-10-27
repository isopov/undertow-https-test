package com.sopovs.moradanen;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class UndertowHttpsTest {
	public static void main(String[] args) throws Exception {
		Undertow.builder().addHttpsListener(10443, "0.0.0.0", getKeyManagers(), null)
				.setHandler(new HttpHandler() {
					@Override
					public void handleRequest(final HttpServerExchange exchange)
							throws Exception {
						exchange.getResponseHeaders().put(Headers.CONTENT_TYPE,
								"text/plain");
						exchange.getResponseSender().send("Hello World");
					}
				}).build().start();
	}

	private static KeyManager[] getKeyManagers() {
		try {
			KeyStore keyStore = KeyStore.getInstance("JKS");
			keyStore.load(new FileInputStream("src/main/resources/test.jks"),
					"secret".toCharArray());

			KeyManagerFactory keyManagerFactory = KeyManagerFactory
					.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keyStore, "password".toCharArray());
			return keyManagerFactory.getKeyManagers();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static TrustManager[] getTrustManagers() {
		try {
			KeyStore trustedKeyStore = KeyStore.getInstance("JKS");
			trustedKeyStore.load(new FileInputStream("src/main/resources/test.jks"),
					"secret".toCharArray());

			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(trustedKeyStore);
			return trustManagerFactory.getTrustManagers();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
