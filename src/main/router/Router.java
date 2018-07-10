package main.router;


public class Router {


	public static void main(String[] args) {
		try {
			ReverseProxyServer reverseProxy = new ReverseProxyServer();
			reverseProxy.addRoute("test1.localdomain", "http://localhost:5001");
			reverseProxy.addRoute("test2.localdomain", "http://localhost:5002");
			reverseProxy.start();
			reverseProxy.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
