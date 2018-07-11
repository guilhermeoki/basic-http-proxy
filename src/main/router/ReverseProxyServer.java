package main.router;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import static org.eclipse.jetty.util.resource.Resource.newClassPathResource;

public class ReverseProxyServer {
	private static final String DEFAULT_KEYSTORE_PASSWORD = "changeit";
	private static final int HTTP_PORT = 8080;
	private static final int HTTPS_PORT = 8443;
	
	private Server server;
	private ServerConnector connectorHttp;
	private ServerConnector connectorHttps;
	private SslContextFactory sslContextFactory;
	
	private int httpPort = HTTP_PORT;
	private int httpsPort = HTTPS_PORT;
	
	private Map<String,String> routes;
	private ReverseProxyHandler handler;
	
	private String keystorePassword = DEFAULT_KEYSTORE_PASSWORD;
	private String KeyManagerPassword = DEFAULT_KEYSTORE_PASSWORD;
	
	public ReverseProxyServer() throws Exception {
		this.routes = new HashMap<String, String>();
		this.server = new Server();
		this.handler = new ReverseProxyHandler(routes);
		configServer();
	}
	
	public void start() throws Exception {
		this.server.start();
	}

	public void join() throws Exception {
		this.server.join();
	}

	public void stop() throws Exception {
		this.server.stop();
	}
	
	public void addRoute(String vhost, String backend) {
		this.handler.addRoute(vhost,backend);
	}
	
	public void deleteRoute(String vhost) {
		this.handler.deleteRoute(vhost);
	}
	
	private void configServer() {
		configHttpServer(this.httpPort);
		configHttpsServer(this.httpsPort);
		this.server.setConnectors(new Connector[]{connectorHttp,connectorHttps});
		this.server.setHandler(handler);
	}
	
	private void configHttpServer(int port) {
		this.connectorHttp = new ServerConnector(server);
		this.connectorHttp.setPort(port);
	}
	
	private void configHttpsServer(int port) {
		this.sslContextFactory = getSSLContextFactory();
		this.connectorHttps = getHTTPSConnector(sslContextFactory);
		this.connectorHttps.setPort(port);
	}

	private SslContextFactory getSSLContextFactory()
	{
		SslContextFactory sslContextFactory = new SslContextFactory();

		sslContextFactory.setKeyStoreResource(newClassPathResource("keystore"));
		sslContextFactory.setKeyStorePassword(keystorePassword);
		sslContextFactory.setKeyManagerPassword(KeyManagerPassword);

		return sslContextFactory;
	}

	private ServerConnector getHTTPSConnector(SslContextFactory sslContextFactory)
	{
		ServerConnector connectorHttps = new ServerConnector(server,sslContextFactory);
		return connectorHttps;
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}

	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	public String getKeyManagerPassword() {
		return KeyManagerPassword;
	}

	public void setKeyManagerPassword(String keyManagerPassword) {
		KeyManagerPassword = keyManagerPassword;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public int getHttpsPort() {
		return httpsPort;
	}

	public void setHttpsPort(int httpsPort) {
		this.httpsPort = httpsPort;
	}
}
