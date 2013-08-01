package uk.ac.cam.dashboard.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//Import the following for logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.models.Notification;
//Import models
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.queries.NotificationQuery;

import com.google.common.collect.ImmutableMap;
import com.googlecode.htmleasy.RedirectException;
import com.googlecode.htmleasy.ViewWith;

@Path("/")
public class HomePageController extends ApplicationController{
	
	// Logger
	private static Logger log = LoggerFactory.getLogger(HomePageController.class);
	
	private User currentUser;
	
//	@GET @Path("/dashboard")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Map<String, ?> homePage() {
//		currentUser = initialiseUser();
//		//ImmutableMap<String, ?> userMap = ulm.getAll();
//		
//		NotificationQuery nq = NotificationQuery.all();
//		nq.forUser(currentUser).limit(10);
//		
//		List<?> result = nq.list();
//		List<Map<?,?>> notifications = new ArrayList<Map<?,?>>();
//		for (Object o:result) {
//			notifications.add(((Notification) o).toMap());
//		}
//		
//		return ImmutableMap.of("user", currentUser.getCrsid(), "deadlines", currentUser.getUserDeadlinesMap(), "notifications", notifications);
//	}
//	
	/*
	 * Archive ----------------------------------------------
	 * ------------------------------------------------------ *
	 * ------------------------------------------------------ *
	 */
	
	@GET @Path("/")
	public void localhostRedirect() {
		throw new RedirectException("/app/#dashboard/");
	}
	
	// DOS Index
	@GET @Path("signapp/DoS") @ViewWith("/soy/home_page.dos")
	public Map dosHomePage() {
		
		// Initialise user
		initialiseUser();
		
		// Does user have staff level access?
		if(!isStaff()){
			throw new RedirectException("/");
		}
		
		return ImmutableMap.of();
	}
	
	// Admin Index
	@GET @Path("signapp/admin") @ViewWith("/soy/home_page.admin")
	public Map adminHomePage() {
		return ImmutableMap.of();
	}
	
	// Authenticate staff
	public boolean isStaff() {
		try {
			return (ulm.getStatus().equals("staff"));
		} catch(NullPointerException e) {
			log.error("User initialisation failed");
			return false;
		}
	}
}