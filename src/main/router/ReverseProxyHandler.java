package main.router;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;

public class ReverseProxyHandler extends AbstractHandler {
	HttpClient httpClient;;
	
	public ReverseProxyHandler() throws Exception {
		// TODO Auto-generated constructor stub
		httpClient = new HttpClient();
		httpClient.start();
	}
	
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// TODO Auto-generated method stub		
		ContentResponse content;
		if (request.getMethod().equals("GET")) {
			try {
				content = httpClient.GET("http://localhost:5000"+request.getRequestURI());
				baseRequest.setHandled(true);
				response.getOutputStream().write(content.getContent());
				response.setStatus(content.getStatus());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
