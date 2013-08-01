//
// This is now void. Instead, the permissions get set in the ApiFilter class
//

package uk.ac.cam.dashboard.util;

import uk.ac.cam.dashboard.controllers.ApiController;
import uk.ac.cam.dashboard.models.User;

public class SessionManager {

	private User user;
	private UserLookupManager ulm;
	private boolean ravenUser;
	
	private String auth;
	
	private Permissions permissions;
	
	public static enum Permissions {
		NO_PERMISSIONS,
		RAVEN_SESSION,
		USER_API,
		GLOBAL_API,
		GLOBAL_API_WITH_USER
	}
	
	public SessionManager(String crsid, String auth, boolean ravenUser) {
		this.user = User.registerUser(crsid);
		this.ulm = UserLookupManager.getUserLookupManager(crsid);
		this.ravenUser = ravenUser;
		
		this.auth = auth;
		
		this.setPermissions();
	}
	

	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }

	public UserLookupManager getUlm() { return ulm; }
	public void setUlm(UserLookupManager ulm) { this.ulm = ulm; }

	public String getAuth() { return auth; }
	public void setAuth(String auth) { this.auth = auth; }
	
	public Permissions getPermissions() { return permissions; }
	public void setPermissions(Permissions permissions) { this.permissions = permissions; }
	
	// Validation
	
	public void setPermissions() {
		
		if (this.user != null && this.auth != null) {
			// Check user permissions
			if ( ApiController.validateApiKeyForUser(this.auth, this.user.getCrsid()) ) {
				permissions = Permissions.USER_API;
				return;
			}
			// Check if global permissions
			if ( ApiController.validateGlobalApiKey(this.auth) ) {
				permissions = Permissions.GLOBAL_API_WITH_USER;
				return;
			}
		}
		
		if (this.user == null && this.auth != null) {
			// Check if global permissions
			if ( ApiController.validateGlobalApiKey(this.auth) ) {
				permissions = Permissions.GLOBAL_API;
				return;
			}
		}
		
		if (this.ravenUser == true) {
			permissions = Permissions.RAVEN_SESSION;
			return;
		}
		
		permissions = Permissions.NO_PERMISSIONS;
		
	}
	
}
