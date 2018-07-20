package main.router;

import org.json.JSONArray;
import org.json.JSONObject;

public class Router {


	public static void main(String[] args) {
		try {
			ReverseProxyServer reverseProxy = new ReverseProxyServer();

			String data = System.getProperty("routes");
			JSONArray routes = new JSONArray(data);
			for (int i = 0; i < routes.length(); i++) {
				JSONObject route = routes.getJSONObject(i);
				reverseProxy.addRoute(route.getString("vhost"), route.getString("backend"));
			}

			reverseProxy.start();
			reverseProxy.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
