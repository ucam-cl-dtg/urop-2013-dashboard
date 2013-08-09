package uk.ac.cam.dashboard.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.models.User;

import com.googlecode.htmleasy.RedirectException;

public class ApplicationController {
	
	// Logger
	private static Logger log = LoggerFactory.getLogger(ApplicationController.class);
	
	// Raven session
	@Context
	HttpServletRequest sRequest;

	protected Permissions getPermissions() {
		
		String userId = (String) sRequest.getAttribute("userId");
		
		if (userId == null) {
			return Permissions.GLOBAL;
		} else {
			return Permissions.USER;
		}
		
	}
	
	public static enum Permissions {
		USER,
		GLOBAL
	}
	
	protected User getUser() {
		
		String userId = (String) sRequest.getAttribute("userId");
		
		return User.registerUser(userId);
		
	}
	
}
