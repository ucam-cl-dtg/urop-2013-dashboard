package uk.ac.cam.dashboard.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.util.SessionManager;
import uk.ac.cam.dashboard.util.UserLookupManager;

public class ApplicationController {
	// Logger
	private static Logger log = LoggerFactory.getLogger(ApplicationController.class);
	
	// UserLookupManager for this user
	protected UserLookupManager ulm;
	
	// Raven session
	@Context
	HttpServletRequest sRequest;
	
	protected User initialiseUser() {
		
		log.debug("Getting crsid from raven");
		
		// Only do this once
		String crsid = (String) sRequest.getParameter("user");
		String auth = (String) sRequest.getParameter("auth");
		boolean ravenUser = (String) sRequest.getSession().getAttribute("RavenRemoteUser") != null ? true: false;
		
		sRequest.getSession().setAttribute("UserPermissions", new SessionManager(crsid, auth, ravenUser));

		return ((SessionManager)sRequest.getSession().getAttribute("UserPermissions")).getUser();
	}
	
	// temporary for testing
	protected User initialiseSpecifiedUser(String crsid) {
		
		// Create UserLookupManager for this user
		log.debug("Creating userLookupManager");	
		ulm = UserLookupManager.getUserLookupManager(crsid);
		
		// Register or return the user
		return User.registerUser(crsid);
	}
	
}
