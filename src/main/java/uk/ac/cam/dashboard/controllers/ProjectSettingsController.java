package uk.ac.cam.dashboard.controllers;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import uk.ac.cam.dashboard.models.Settings;
import uk.ac.cam.dashboard.models.User;

import com.google.common.collect.ImmutableMap;

@Path("/api/dashboard/settings")
@Produces(MediaType.APPLICATION_JSON)
public class ProjectSettingsController extends ApplicationController {

	private User currentUser;
	
	@GET @Path("/")
	public ImmutableMap<String, ?> sidebarLinkHierarchy() {
		
		// *TODO* Validate global permissions
		currentUser = initialiseSpecifiedUser("jd658");
		Settings settings = currentUser.getSettings();
		
		List<Object> sidebar = new LinkedList<Object>();
		
		// Dashboard
		List<Object> dashboard = new LinkedList<Object>();
		dashboard.add(ImmutableMap.of("name", "Home", "link", "dashboard", "icon", "icon-globe", "iconType", 1, "notificationCount", 2));
		dashboard.add(ImmutableMap.of("name", "Notifications", "link", "dashboard/notifications", "icon", "icon-newspaper", "iconType", 1, "notificationCount", 2));
		dashboard.add(ImmutableMap.of("name", "Deadlines","link", "dashboard/deadlines", "icon", "icon-ringbell", "iconType", 1, "notificationCount", 2));
		dashboard.add(ImmutableMap.of("name", "Groups", "link", "dashboard/groups", "icon", "icon-users", "iconType", 1, "notificationCount", 2));
		dashboard.add(ImmutableMap.of("name", "Supervisor Homepage", "link", "dashboard/supervisor", "icon", "icon-users", "iconType", 1, "notificationCount", 2));
		sidebar.add(ImmutableMap.of("name", "Dashboard", "links", dashboard, "icon", "a", "iconType", 2, "notificationCount", 2));
		
		// Signups
		if (settings.isSignupsOptIn()) {
			List<Object> signups = new LinkedList<Object>();
			signups.add(ImmutableMap.of("name", "Events", "link", "signapp/events", "icon", "?", "iconType", 2, "notificationCount", 2));
			signups.add(ImmutableMap.of("name", "Create new event", "link", "signapp/events/new", "icon", "?", "iconType", 2, "notificationCount", 2));
			sidebar.add(ImmutableMap.of("name", "Timetable/Signups", "links", signups, "icon", "P", "iconType", 2, "notificationCount", 2));
		}
		
		// Questions
		if (settings.isQuestionsOptIn()) {
			List<Object> questions = new LinkedList<Object>();
			questions.add(ImmutableMap.of("name", "Browse questions", "link", "q/search", "icon", "icon-list", "iconType", 1, "notificationCount", 2));
			questions.add(ImmutableMap.of("name", "Browse question sets", "link", "sets", "icon", "icon-file_open", "iconType", 1, "notificationCount", 2));
			questions.add(ImmutableMap.of("name", "Browse own content", "link", "users/me", "icon", "icon-file_open", "iconType", 1, "notificationCount", 2));
			questions.add(ImmutableMap.of("name", "Create question set", "link", "sets/add", "icon", "icon-plus", "iconType", 1, "notificationCount", 2));
			questions.add(ImmutableMap.of("name", "Fairytale land", "link", "fairytale", "icon", "icon-ringbell", "iconType", 1, "notificationCount", 2));
			sidebar.add(ImmutableMap.of("name", "Setting Work", "links", questions, "icon", "a", "iconType", 2, "notificationCount", 2));
		}
		
		// Handins
		if (settings.isHandinsOptIn()) {
			List<Object> handins = new LinkedList<Object>();
			handins.add(ImmutableMap.of("name", "Create bin", "link", "bins/create", "icon", ",", "iconType", 2, "notificationCount", 2));
			handins.add(ImmutableMap.of("name", "Upload answers", "link", "bins", "icon", ",", "iconType", 2, "notificationCount", 2));
			handins.add(ImmutableMap.of("name", "Mark answers", "link", "marking", "icon", "C", "iconType", 2, "notificationCount", 2));
			sidebar.add(ImmutableMap.of("name", "Marking Work", "links", handins, "icon", "F", "iconType", 2, "notificationCount", 2));
		}
		
		return ImmutableMap.of("sidebar", sidebar);
		
	} 
	
}
