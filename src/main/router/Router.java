package main.router;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class Router {

	private static ServerConnector connectorHttp;
	private static ServerConnector connectorHttps;
	private static SslContextFactory sslContextFactory;
	private static Server server;

	public static void main(String[] args) throws Exception {
		Map routes = new HashMap();


		routes.put("test1.localdomain", "http://localhost:5001");
		routes.put("test2.localdomain", "http://localhost:5002");

		server = new Server();

		connectorHttp = new ServerConnector(server);
		connectorHttp.setPort(8080);

		sslContextFactory = getSSLContextFactory();
		connectorHttps = getHTTPSConnector(sslContextFactory);

		server.setConnectors(new Connector[]{connectorHttp,connectorHttps});
		ReverseProxyHandler handler = new ReverseProxyHandler(routes);
		server.setHandler(handler);

		server.start();
		server.join();
	}

	private static String getKeystorePath(String keystorePath) {
		File keystoreFile = new File(keystorePath);
        if (!keystoreFile.exists())
			try {
				throw new FileNotFoundException(keystoreFile.getAbsolutePath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		return keystoreFile.getAbsolutePath();
	}

	private static SslContextFactory getSSLContextFactory()
	{
		String keystorePath = getKeystorePath("src/main/resources/keystore");
		SslContextFactory sslContextFactory = new SslContextFactory();

		sslContextFactory.setKeyStorePath(keystorePath);
		sslContextFactory.setKeyStorePassword("changeit");
		sslContextFactory.setKeyManagerPassword("changeit");

		return sslContextFactory;
	}

	private static ServerConnector getHTTPSConnector(SslContextFactory sslContextFactory)
	{
		ServerConnector connectorHttps = new ServerConnector(server,sslContextFactory);
		connectorHttps.setPort(8443);
		return connectorHttps;
	}
}
