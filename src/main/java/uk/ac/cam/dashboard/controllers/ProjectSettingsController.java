package uk.ac.cam.dashboard.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("api/dashboard/settings")
@Produces(MediaType.APPLICATION_JSON)
public class ProjectSettingsController {

	@GET @Path("/")
	public Map<String, ?> sidebarLinkHierarchy() {
		
		// *TODO* Validate global permissions
		
		Map<String, Object> sidebar = new HashMap<String, Object>();
		
		// Dashboard
		Map<String, Object> dashboard = new HashMap<String, Object>();
		dashboard.put("Home", "dashboard");
		dashboard.put("Notifications", "dashboard/notifications");
		dashboard.put("Deadlines", "dashboard/deadlines");
		dashboard.put("Groups", "dashboard/groups");
		dashboard.put("Supervisor Homepage", "dashboard/supervisor");
		sidebar.put("Dashboard", dashboard);
		
		// Signups
		Map<String, Object> signups = new HashMap<String, Object>();
		signups.put("Events", "signapp/events");
		signups.put("Create new event", "signapp/events/new");
		sidebar.put("Timetable/Signups", signups);
		
		// Questions
		Map<String, Object> questions = new HashMap<String, Object>();
		questions.put("Browse questions", "q/search");
		questions.put("Browse question sets", "sets");
		questions.put("Browse own content", "users/me");
		questions.put("Create question set", "sets/add");
		questions.put("Fairytale land", "fairytale");
		sidebar.put("Setting Work", questions);
		
		// Handins
		Map<String, Object> handins = new HashMap<String, Object>();
		handins.put("Create bin", "bins/create");
		handins.put("Upload answers", "bins");
		handins.put("Mark answers", "marking");
		sidebar.put("Marking Work", handins);
		
		return sidebar;
		
	} 
	
}
