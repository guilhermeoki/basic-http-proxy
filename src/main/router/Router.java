package main.router;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class Router {

	private static ServerConnector connectorHttp;

	public static void main(String[] args) throws Exception {
		Server server = new Server();
		connectorHttp = new ServerConnector(server);
		connectorHttp.setPort(80);

		server.setConnectors(new Connector[]{connectorHttp});
		ReverseProxyHandler handler = new ReverseProxyHandler();
		server.setHandler(handler);

		server.start();
		server.join();
	}
}
