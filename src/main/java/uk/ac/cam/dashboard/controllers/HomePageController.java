package uk.ac.cam.dashboard.controllers;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


//Import the following for logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Import models

import uk.ac.cam.dashboard.models.User;

import com.google.common.collect.ImmutableMap;
import com.googlecode.htmleasy.RedirectException;
import com.googlecode.htmleasy.ViewWith;

@Path("/dashboard")
public class HomePageController extends ApplicationController{
	
	// Create the logger
	private static Logger log = LoggerFactory.getLogger(HomePageController.class);
	
	private User user;
	
	@GET @Path("/")
	public void localhostRedirect() {
		throw new RedirectException("/app/#notifications/");
	}
	
	// Index
	@GET @Path("/notifications") 
	@Produces(MediaType.APPLICATION_JSON)
	public Map indexHomePage() {
		
		user = initialiseUser();
		
		// Get user details
		log.debug("Index GET: Getting user details");		
		
		// return ImmutableMap.of("deadlines", user.getUserDeadlinesMap());
		return ImmutableMap.of("test", "test");
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