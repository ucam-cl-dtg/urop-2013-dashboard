package uk.ac.cam.dashboard.tests;

import org.hibernate.Session;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.spi.UnhandledException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.ac.cam.dashboard.controllers.HomePageController;
import uk.ac.cam.dashboard.controllers.NotificationsController;

import com.googlecode.htmleasy.RedirectException;

public class ApiTest {
	final String testUser1 = "jd658";
	final String testUser2 = "psadf213"; // Invalid CRSID
	
	final String testNotificationMessage = "This is a test notification!";
	final String testNotificationLink = "http://cambridge.com";
	final String testNotificationSection = "handins";

	Session session;
	Dispatcher dispatcher;
	MockHttpRequest request;
	MockHttpResponse response;
	
	@Before
	public void initialize() throws Exception {
		response = new MockHttpResponse();
        dispatcher = MockDispatcherFactory.createDispatcher();
        dispatcher.getRegistry().addPerRequestResource(NotificationsController.class);
        dispatcher.getRegistry().addPerRequestResource(HomePageController.class);
	}
	
	@Test
	public void createNewNotificationThroughApi() throws Throwable {
		request = MockHttpRequest.post("/dashboard/notifications");
		request.addFormHeader("users", testUser1 + "," + testUser2);
		request.addFormHeader("message", this.testNotificationMessage);
		request.addFormHeader("section", this.testNotificationSection);
		request.addFormHeader("link", this.testNotificationLink);
		
		try {
			dispatcher.invoke(request, response);
		} catch(UnhandledException e) {
			RedirectException redirect = (RedirectException) e.getCause();
			Assert.assertEquals(redirect.getPath().toString(), "dashboard/notifications/success");
		}
		
	}
	
	/*
	@Test
	public void deleteNotificaitonThroughApi() throws Exception {
		request = MockHttpRequest.delete("/dashboard/notifications/3");
		
		dispatcher.invoke(request, response);

		try {
			dispatcher.invoke(request, response);
		} catch(UnhandledException e) {
			RedirectException redirect = (RedirectException) e.getCause();
			Assert.assertEquals(redirect.getPath().toString(), "dashboard/notifications/success");
		}

	}
	*/
	
}
