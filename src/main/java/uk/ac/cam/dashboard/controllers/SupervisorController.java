package uk.ac.cam.dashboard.controllers;

import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.exceptions.AuthException;
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
		return indexSupervisorTab("", null, null, null);
	}
	
	@GET @Path("/{tab}") 
	public ImmutableMap<String, ?> indexSupervisorTab(@PathParam("tab") String tab, 
			@QueryParam("title") String title,
			@QueryParam("message") String message,
			@QueryParam("url") String url) {

		try {
			currentUser = validateUser();
		} catch(AuthException e){
			return ImmutableMap.of("error", e.getMessage());
		}
		
		
		title = (title == null ? "" : title);
		message = (message == null ? "" : message);
		url = (url == null ? "" : url);
		
		Map<String, String> presetDeadline = ImmutableMap.of("title", title, "message", message, "url", url);
		
		if(currentUser.getSupervisor()){
			log.debug("User authorised, returning group and deadline management data JSON");
			tab = (tab == null || tab == "" ? "undefined" : tab);
			
			ImmutableMap.Builder<String, Object> builder = new ImmutableMap.Builder<String, Object>();
			builder.put("user", currentUser.toMap());
			builder.put("target", tab);
			builder.put("cdeadlines", currentUser.createdDeadlinesToMap());
			builder.put("presetDeadline", presetDeadline);
			builder.put("errors", "undefined");
			builder.put("cgroups", currentUser.groupsToMap());
			
			return builder.build();
		}
		
		log.debug("User is not authorised to view supervision homepage, redirecting");
		return ImmutableMap.of("redirectTo", "error");
	}
	
	@POST @Path("/add") 
	public ImmutableMap<String, ?> indexSupervisorTab(@FormParam("users") String users) {

		try {
			currentUser = validateUser();
		} catch(AuthException e){
			return ImmutableMap.of("errors", e.getMessage());
		}
		
		if(!currentUser.getSupervisor()){
			return ImmutableMap.of("errors", "You are not authorised to add a new supervisor.");
		}
		
		if(users==null||users==""){
			return ImmutableMap.of("errors", "No users specified");
		}
		
		String[] supervisors = users.split(",");
		for(String s : supervisors){
			User supervisor = User.registerUser(s);
			if(supervisor!=null){
				supervisor.setSupervisor(true);
			}
		}
		
		return ImmutableMap.of("errors", "undefined");
		
	}
	
}