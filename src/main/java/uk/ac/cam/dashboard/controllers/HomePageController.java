package uk.ac.cam.dashboard.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


//Import the following for logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.cl.dtg.ldap.LDAPObjectNotFoundException;
import uk.ac.cam.cl.dtg.ldap.LDAPQueryManager;
import uk.ac.cam.cl.dtg.ldap.LDAPUser;
import uk.ac.cam.dashboard.forms.GetNotificationForm;
//Import models
import uk.ac.cam.dashboard.models.User;

import com.google.common.collect.ImmutableMap;
import com.googlecode.htmleasy.RedirectException;

@Path("/api/")
@Produces(MediaType.APPLICATION_JSON)
public class HomePageController extends ApplicationController{
	
	// Logger
	private static Logger log = LoggerFactory.getLogger(HomePageController.class);
	
	private User currentUser;
	
	@GET @Path("/")
	public Map<String, ?> homePage() {
		
		currentUser = getUser();
		
		LDAPUser user = null;
		try {
			user = LDAPQueryManager.getUser(currentUser.getCrsid());
		} catch (LDAPObjectNotFoundException e){
			// Create map of default options or something
		}
		
		HashMap<String, String> userData = user.getAll();
		
		// Get notifications
		GetNotificationForm notificationForm = new GetNotificationForm();
		notificationForm.validate();
		
		return ImmutableMap.of("user", userData, "deadlines", currentUser.deadlinesToMap(), "userNotifications", notificationForm.handle(currentUser, false));
	}
	
//	@GET @Path("/")
//	public void localhostRedirect() {
//		throw new RedirectException("/dashboard/");
//	}
	
	// TODO: Authenticate staff
	public boolean isStaff() {
		return false;
	}
}