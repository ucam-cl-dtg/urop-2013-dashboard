package uk.ac.cam.dashboard.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.models.User;

import com.google.common.collect.ImmutableMap;
import com.googlecode.htmleasy.RedirectException;

public class ApplicationController {
	
	// Logger
	private static Logger log = LoggerFactory.getLogger(ApplicationController.class);
	
	// Raven session
	@Context
	HttpServletRequest sRequest;

	public static enum Permissions {
		USER,
		GLOBAL
	}
	
	protected Permissions getPermissions() {
		String userId = (String) sRequest.getAttribute("userId");
		if (userId == null) {
			return Permissions.GLOBAL;
		} else {
			return Permissions.USER;
		}	
	}
	
	protected User getUser() {
		String userId = (String) sRequest.getAttribute("userId");
		return User.registerUser(userId);
	}
	
	protected User getSpecifiedUser(String userId) {
		return User.registerUser(userId);
	}
	
	protected User validateUserOrApiUser(String userId) throws Exception {
		
		User user;
		Permissions permissions = getPermissions();
		
		if (permissions == Permissions.GLOBAL) {
			if (userId != null) {
				user = getSpecifiedUser(userId);
				if (user != null) {
					return user;
				} else {
					throw new Exception("Could not find information for user");
				}
			} else {
				throw new Exception("Cannot retrieve user specific information for a global key - include a userId in the query string");
			}
		} else if (permissions == Permissions.USER) {
			user = getUser();
			if (user != null) {
				return user;
			} else {
				throw new Exception("Could not find information for user");
			}
		} else {
			throw new Exception("Could not validate permissions");
		}
		
	}
	
	protected boolean validateGlobal() throws Exception {
		
		Permissions permissions = getPermissions();
		
		if (permissions == Permissions.GLOBAL) {
			return true;
		} else {
			throw new Exception("Could not validate global permissions");
		}
		
	}
	
}
