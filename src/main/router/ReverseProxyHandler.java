package main.router;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;

public class ReverseProxyHandler extends AbstractHandler {
	private HttpClient httpClient;
	private Map<String,String> routes;

	public ReverseProxyHandler(Map<String,String> routes) throws Exception {
		httpClient = new HttpClient();
		httpClient.start();
		this.routes = routes;
	}

	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
		String vhost = request.getServerName();
		if (isMethodGet(request) && hasRoute(vhost)) {
			try {
				String backend = routes.get(vhost);

				ContentResponse content = sendGetRequest(backend,target);
				serveHttp(content,response);

				baseRequest.setHandled(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isMethodGet(HttpServletRequest request) {
		if (request.getMethod().equals("GET"))
			return true;
		return false;
	}

	public boolean hasRoute(String vhost) {
		return routes.containsKey(vhost);
	}

	public ContentResponse sendGetRequest(String backend, String target) throws InterruptedException, ExecutionException, TimeoutException {
		ContentResponse content = httpClient.GET(backend+target);
		return content;

	}

	public HttpServletResponse serveHttp(ContentResponse content, HttpServletResponse response) throws IOException {
		response.getOutputStream().write(content.getContent());
		response.setStatus(content.getStatus());
		return response;
	}
}