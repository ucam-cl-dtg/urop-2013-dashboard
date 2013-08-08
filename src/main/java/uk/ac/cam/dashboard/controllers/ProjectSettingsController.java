package uk.ac.cam.dashboard.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.ImmutableMap;

@Path("api/dashboard/settings")
@Produces(MediaType.APPLICATION_JSON)
public class ProjectSettingsController {

	@GET @Path("/")
	public Map<String, ?> sidebarLinkHierarchy() {
		
		// *TODO* Validate global permissions
		
		Map<String, Object> sidebar = new HashMap<String, Object>();
		
		// Dashboard
		Map<String, Object> dashboard = new HashMap<String, Object>();
		dashboard.put("Home", ImmutableMap.of("link", "dashboard", "icon", "icon-globe", "icon-type", "test"));
		dashboard.put("Notifications", ImmutableMap.of("link", "dashboard/notifications", "icon", "icon-newspaper", "icon-type", "test"));
		dashboard.put("Deadlines", ImmutableMap.of("link", "dashboard/deadlines", "icon", "icon-ringbell", "icon-type", "test"));
		dashboard.put("Groups", ImmutableMap.of("link", "dashboard/groups", "icon", "icon-users", "icon-type", "test"));
		dashboard.put("Supervisor Homepage", ImmutableMap.of("link", "dashboard/supervisor", "icon", "icon-users", "icon-type", "test"));
		sidebar.put("Dashboard", ImmutableMap.of("links", dashboard, "icon", "&#97;", "icon-type", "test"));
		
		// Signups
		Map<String, Object> signups = new HashMap<String, Object>();
		signups.put("Events", ImmutableMap.of("link", "signapp/events", "icon", "&#63;", "icon-type", "test"));
		signups.put("Create new event", ImmutableMap.of("link", "signapp/events/new", "icon", "&#63;", "icon-type", "test"));
		sidebar.put("Timetable/Signups", ImmutableMap.of("links", signups, "icon", "&#80;", "icon-type", "test"));
		
		// Questions
		Map<String, Object> questions = new HashMap<String, Object>();
		questions.put("Browse questions", ImmutableMap.of("link", "q/search", "icon", "icon-list", "icon-type", "test"));
		questions.put("Browse question sets", ImmutableMap.of("link", "sets", "icon", "icon-file_open", "icon-type", "test"));
		questions.put("Browse own content", ImmutableMap.of("link", "users/me", "icon", "icon-file_open", "icon-type", "test"));
		questions.put("Create question set", ImmutableMap.of("link", "sets/add", "icon", "icon-plus", "icon-type", "test"));
		questions.put("Fairytale land", ImmutableMap.of("link", "fairytale", "icon", "icon-ringbell", "icon-type", "test"));
		sidebar.put("Setting Work", ImmutableMap.of("links", questions, "icon", "&#97;", "icon-type", "test"));
		
		// Handins
		Map<String, Object> handins = new HashMap<String, Object>();
		handins.put("Create bin", ImmutableMap.of("link", "bins/create", "icon", "&#44;", "icon-type", "test"));
		handins.put("Upload answers", ImmutableMap.of("link", "bins", "icon", "&#44;", "icon-type", "test"));
		handins.put("Mark answers", ImmutableMap.of("link", "marking", "icon", "&#67;", "icon-type", "test"));
		sidebar.put("Marking Work", ImmutableMap.of("links", handins, "icon", "&#70;", "icon-type", "test"));
		
		return sidebar;
		
	} 
	
}
