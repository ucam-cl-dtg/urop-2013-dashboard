package uk.ac.cam.dashboard.controllers;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//Import the following for logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.helpers.LDAPQueryHelper;
//Import models
import uk.ac.cam.dashboard.models.User;

import com.google.common.collect.ImmutableMap;
import com.googlecode.htmleasy.RedirectException;

@Path("api/")
public class HomePageController extends ApplicationController{
	
	// Logger
	private static Logger log = LoggerFactory.getLogger(HomePageController.class);
	
	private User currentUser;
	
	@GET @Path("/dashboard")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, ?> homePage() {
		
		currentUser = initialiseUser();
		//ImmutableMap<String, ?> userMap = ulm.getAll();
		
		return ImmutableMap.of("user", LDAPQueryHelper.getAll(currentUser.getCrsid()), "deadlines", currentUser.deadlinesToMap());
	}
	
	@GET @Path("/")
	public void localhostRedirect() {
		throw new RedirectException("/app/#dashboard/");
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