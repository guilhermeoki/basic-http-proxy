package test.router;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import main.router.ReverseProxyServer;

public class TestRouter {
	private ReverseProxyServer reverseProxy;
	private Server app;
	private ServerConnector connectorHttp;
	private HttpClient clientHttp;
	
	public static class TestHandler extends AbstractHandler {

		@Override
		public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			// TODO Auto-generated method stub
			response.setContentType("text/html;charset=utf-8");
	        response.setStatus(HttpServletResponse.SC_OK);
	        baseRequest.setHandled(true);
	        response.getWriter().println("<h1>Test</h1>");
		}
	}
	
	@Before
	public void setUp() throws Exception {
		clientHttp = new HttpClient();
		clientHttp.start();
		
		reverseProxy = new ReverseProxyServer();
		reverseProxy.start();
		
		app = new Server();
		TestHandler handler = new TestHandler();
		connectorHttp = new ServerConnector(app);
		connectorHttp.setPort(5000);
		app.setConnectors(new Connector[]{connectorHttp});
		app.setHandler(handler);
		app.start();
	}
	
	@Test
	public void testRouter_sendGetRequestWithoutRoute_shouldReturn404() throws Exception {
		ContentResponse content = clientHttp.GET("http://localhost:8080");
		int actual = content.getStatus();

		Assert.assertEquals(404, actual);
	}
	
	@Test
	public void testRouter_sendGetRequestWithRoute_shouldReturn200() throws Exception {
		reverseProxy.addRoute("localhost", "http://localhost:5000");
		ContentResponse content = clientHttp.GET("http://localhost:8080");
		int actual = content.getStatus();
		

		Assert.assertEquals(200, actual);
	}
	
	@Test
	public void testRouter_sendGetRequestWithRoute_shoudReturnSameContentType() throws Exception {
		reverseProxy.addRoute("localhost", "http://localhost:5000");
		ContentResponse content = clientHttp.GET("http://localhost:5000");
		String actual = content.getMediaType();
		
		Assert.assertEquals("text/html", actual);
	}
	
	@Test
	public void testRouter_sendGetRequestWithRoute_shoudReturnSameContent() throws Exception {
		reverseProxy.addRoute("localhost", "http://localhost:5000");
		ContentResponse content = clientHttp.GET("http://localhost:5000");
		String actual = content.getContentAsString();
		
		Assert.assertEquals("<h1>Test</h1>\n", actual);
	}
	
	@After
	public void clear() throws Exception {
		reverseProxy.stop();
		app.stop();
	}

}
