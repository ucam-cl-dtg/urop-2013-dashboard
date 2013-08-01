package uk.ac.cam.dashboard.util;

import uk.ac.cam.dashboard.models.User;

public class SessionManager {

	private User user;
	private UserLookupManager ulm;
	private String auth;
	
	public SessionManager(String crsid){
		this.user = User.registerUser(crsid);
		this.ulm = UserLookupManager.getUserLookupManager(crsid);
	}
	

	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }

	public UserLookupManager getUlm() { return ulm; }
	public void setUlm(UserLookupManager ulm) { this.ulm = ulm; }

	public String getAuth() { return auth; }
	public void setAuth(String auth) { this.auth = auth; }
	
}
