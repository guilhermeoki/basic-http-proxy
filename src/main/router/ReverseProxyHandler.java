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

	public ReverseProxyHandler(Map<String,String> routes, HttpClient httpClient) throws Exception {
		this.httpClient = httpClient;
		this.routes = routes;
	}

	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String vhost = request.getServerName();
		if (isMethodGet(request) && hasRoute(vhost)) {
			try {
				String backend = routes.get(vhost);

				ContentResponse content = sendGetRequest(backend,target);
				serveHttp(content,response);

				baseRequest.setHandled(true);
			} catch(TimeoutException te) {
				System.out.println("The client request timeout is expired.");
			}
			catch(ExecutionException ee) {
				System.out.println("The request is aborted. The cause is"+ee.getCause().getMessage());
			}
			catch(InterruptedException ie) {
				System.out.println("The http client thread was interrupted");
			}
		}
	}

	private boolean isMethodGet(HttpServletRequest request) {
		if (request.getMethod().equals("GET"))
			return true;
		return false;
	}

	private boolean hasRoute(String vhost) {
		return routes.containsKey(vhost);
	}

	private ContentResponse sendGetRequest(String backend, String target) throws InterruptedException, ExecutionException, TimeoutException {
		ContentResponse content = httpClient.GET(backend+target);
		return content;

	}

	public HttpServletResponse serveHttp(ContentResponse content, HttpServletResponse response) throws IOException {
		response.setContentType(content.getMediaType());
		response.getOutputStream().write(content.getContent());
		response.setStatus(content.getStatus());
		return response;
	}

	public void addRoute(String vhost, String backend) {
		// TODO Auto-generated method stub
		this.routes.put(vhost, backend);
	}

	public void deleteRoute(String vhost) {
		this.routes.remove(vhost);
	}
}