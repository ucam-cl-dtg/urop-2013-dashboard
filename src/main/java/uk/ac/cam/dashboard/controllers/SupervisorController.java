package uk.ac.cam.dashboard.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.models.User;

import com.google.common.collect.ImmutableMap;

@Path("/api/dashboard/supervisor")
@Produces(MediaType.APPLICATION_JSON)
public class SupervisorController extends ApplicationController {
	
	private User currentUser;
	
	// Logger
	private static Logger log = LoggerFactory.getLogger(SupervisorController.class);
	
	// Index 
	@GET @Path("/") 
	public ImmutableMap<String, ?> indexSupervisor() {

		currentUser = initialiseUser();
		
		log.debug("Returning JSON of user, user created deadlines and user created groups");
		return ImmutableMap.of("user", currentUser.toMap(), "target", "deadlines", "cdeadlines", currentUser.createdDeadlinesToMap(), "cgroups", currentUser.groupsToMap(), "errors", "undefined");
	}
}