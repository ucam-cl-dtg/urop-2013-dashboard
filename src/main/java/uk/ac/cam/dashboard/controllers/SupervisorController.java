package uk.ac.cam.dashboard.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.models.User;

import com.google.common.collect.ImmutableMap;

@Path("/api/supervisor")
@Produces(MediaType.APPLICATION_JSON)
public class SupervisorController extends ApplicationController {
	
	private User currentUser;
	
	// Logger
	private static Logger log = LoggerFactory.getLogger(SupervisorController.class);
	
	// Index 
	@GET @Path("/")
	public ImmutableMap<String, ?> indexSupervisor() {
		return indexSupervisorTab("");
	}
	
	@GET @Path("/{tab}") 
	public ImmutableMap<String, ?> indexSupervisorTab(@PathParam("tab") String tab) {

		currentUser = getUser();
		
		if(currentUser.getSupervisor()){
			log.debug("User authorised, returning group and deadline management data JSON");
			tab = (tab == null || tab == "" ? "undefined" : tab);
			return ImmutableMap.of("user", currentUser.toMap(), "target", tab, "cdeadlines", currentUser.createdDeadlinesToMap(), "cgroups", currentUser.groupsToMap(), "errors", "undefined");
		}
		
		log.debug("User is not authorised to view supervision homepage, redirecting");
		return ImmutableMap.of("redirectTo", "error");
	}
	
}