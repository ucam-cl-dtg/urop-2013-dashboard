package uk.ac.cam.dashboard.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.exceptions.AuthException;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.util.Strings;

public class ApplicationController {
	
	// Logger
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(ApplicationController.class);
	
	@Context
	HttpServletRequest sRequest;

	public static enum Permissions {
		USER,
		GLOBAL
	}
	
	protected Permissions getPermissions() {
		String userId = (String) sRequest.getAttribute("userId");
		if (userId == null) { return Permissions.GLOBAL; } 
		else { return Permissions.USER; }
	}
	
	protected User getUser() {
		String userId = (String) sRequest.getAttribute("userId");
		if(userId==null){ return null; }
		return User.registerUser(userId);
	}
	
	protected User getSpecifiedUser(String userId) {
		return User.registerUser(userId);
	}
	
	protected User validateUser() throws AuthException {
		
		User user = getUser();
		Permissions permissions = getPermissions();
		
		if (user == null) {
			if(permissions == Permissions.GLOBAL){
				String queryUser = sRequest.getParameter("userId");
				if (queryUser != null && getSpecifiedUser(queryUser) != null) {
					user = getSpecifiedUser(queryUser);
				} else {
					throw new AuthException(Strings.AUTHEXCEPTION_GLOBAL_USER);
				}
			} else if(permissions == Permissions.USER) {
				throw new AuthException(Strings.AUTHEXCEPTION_USER);
			} else {
				throw new AuthException(Strings.AUTHEXCEPTION_GENERAL);
			}
		}
		
		return user;
	}
	
	protected boolean validateGlobal() throws AuthException {
		
		Permissions permissions = getPermissions();
		
		if (permissions == Permissions.GLOBAL) {
			return true;
		} else {
			throw new AuthException(Strings.AUTHEXCEPTION_GLOBAL);
		}
		
	}
	
}
