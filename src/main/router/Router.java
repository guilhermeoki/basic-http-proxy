package main.router;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class Router {

	private static ServerConnector connectorHttp;

	public static void main(String[] args) throws Exception {
		Map routes = new HashMap();

		routes.put("test1.localdomain", "http://localhost:5001");
		routes.put("test2.localdomain", "http://localhost:5002");

		Server server = new Server();
		connectorHttp = new ServerConnector(server);
		connectorHttp.setPort(80);

		server.setConnectors(new Connector[]{connectorHttp});
		ReverseProxyHandler handler = new ReverseProxyHandler(routes);
		server.setHandler(handler);

		server.start();
		server.join();
	}
}
