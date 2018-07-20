package main.router;

import org.json.JSONArray;
import org.json.JSONException;
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
		} catch (JSONException e) {
			System.out.println("Error in the syntax of json args. The message is "+e.getMessage());
		}
		catch(NullPointerException ne) {
			System.out.println("You need to pass the routes to start webserver");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to start server");
			e.printStackTrace();
		}

	}
}
