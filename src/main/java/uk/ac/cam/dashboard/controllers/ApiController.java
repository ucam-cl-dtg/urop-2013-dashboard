package uk.ac.cam.dashboard.controllers;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;

import uk.ac.cam.dashboard.exceptions.AuthException;
import uk.ac.cam.dashboard.models.Api;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.util.HibernateUtil;
import uk.ac.cam.dashboard.util.Strings;

import com.google.common.collect.ImmutableMap;

@Path("/api/keys")
@Produces(MediaType.APPLICATION_JSON)
public class ApiController extends ApplicationController {
	
	private User currentUser;
	
	// Get
	
	@GET @Path("/")
	public ImmutableMap<String, ?> getUserApiKeys() {

		try {
			currentUser = validateUser();
		} catch (AuthException e) {
			return ImmutableMap.of("error", e.getMessage());
		}
		
		return ImmutableMap.of("user", currentUser.getCrsid(), "keys", currentUser.apisToMap());
	}
	
	// Creation
	
	@GET @Path("/new")
	public Map<String, String> getNewApiKey() {
		Session s = HibernateUtil.getTransactionSession();

		try {
			currentUser = validateUser();
		} catch (AuthException e) {
			return ImmutableMap.of("error", e.getMessage());
		}
		
		Api api = new Api();
		api.setUser(currentUser);
		
		s.save(api);
		
		return ImmutableMap.of("user", currentUser.getCrsid(), "key", api.getKey());
	}
	
	@GET @Path("/newGlobal")
	public Map<String, String> getNewGlobalApiKey() {
		
		try {
			validateGlobal();
		} catch (Exception e) {
			return ImmutableMap.of("error", e.getMessage());
		}
		
		Session s = HibernateUtil.getTransactionSession();

		Api api = new Api();
		api.setGlobalPermissions(true);
		
		s.save(api);
		
		return ImmutableMap.of("key", api.getKey());
	}
	
	// Validation
	
	// Does not require verification
	// Excluded from API filter
	
	@GET @Path("/type/{key}")
	public static Map<String, String> checkApiKeyType(@PathParam("key") String key) {
		
		Session s = HibernateUtil.getTransactionSession();
		
		if (key == null || key == "") {
			return ImmutableMap.of("error", Strings.AUTHEXCEPTION_NO_KEY);
		}
		
		Api api = (Api) s.createQuery("from Api where key = :key").setParameter("key", key).uniqueResult();
		
		if (api == null) {
			return ImmutableMap.of("error", Strings.AUTHEXCEPTION_GENERAL);
		} else if (api.isGlobalPermissions()) {
			return ImmutableMap.of("type", "global");
		} else {
			return ImmutableMap.of("type", "user", "userId", api.getUser().getCrsid());
		}
		
	}
	
}
