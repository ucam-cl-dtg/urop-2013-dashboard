package uk.ac.cam.dashboard.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.models.User;
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
		
		// This will extract the CRSID of the current user and return it:
		log.debug("Getting crsid from raven");	
		String crsid = (String) sRequest.getSession().getAttribute("RavenRemoteUser");
		
		// Create UserLookupManager for this user
		log.debug("Creating userLookupManager");	
		ulm = UserLookupManager.getUserLookupManager(crsid);
		
		// Register or return the user
		return User.registerUser(crsid);
	}
	
	// temporary for testing
	protected User initialiseSpecifiedUser(String crsid) {
		
		// Create UserLookupManager for this user
		log.debug("Creating userLookupManager");	
		ulm = UserLookupManager.getUserLookupManager(crsid);
		
		// Register or return the user
		return User.registerUser(crsid);
	}

	// Validation
	
	public Permissions validateRequest() {
		String user = sRequest.getParameter("user");
		String apiToken = sRequest.getParameter("apiToken");
		String ravenUser = (String) sRequest.getSession().getAttribute("RavenRemoteUser");
		
		if (user != null && apiToken != null) {
			// Check user permissions
			if ( ApiController.validateApiKeyForUser(apiToken, user) ) {
				return Permissions.USER_API;
			}
			// Check if global permissions
			if ( ApiController.validateGlobalApiKey(apiToken) ) {
				return Permissions.GLOBAL_API_WITH_USER;
			}
		}
		
		if (user == null && apiToken != null) {
			// Check if global permissions
			if ( ApiController.validateGlobalApiKey(apiToken) ) {
				return Permissions.GLOBAL_API;
			}
		}
		
		if (ravenUser != null) {
			return Permissions.RAVEN_SESSION;
		}
		
		return Permissions.NO_PERMISSIONS;
		
	}
	
	public enum Permissions {
		NO_PERMISSIONS,
		RAVEN_SESSION,
		USER_API,
		GLOBAL_API,
		GLOBAL_API_WITH_USER
	}
	
}
