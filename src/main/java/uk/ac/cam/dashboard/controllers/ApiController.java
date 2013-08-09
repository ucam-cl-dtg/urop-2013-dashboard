package uk.ac.cam.dashboard.controllers;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;

import uk.ac.cam.dashboard.models.Api;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.util.HibernateUtil;

import com.google.common.collect.ImmutableMap;

@Path("/api/keys")
@Produces(MediaType.APPLICATION_JSON)
public class ApiController extends ApplicationController {
	
	/* TODO Validation */
	
	// Creation
	
	@GET @Path("/")
	public ImmutableMap<String, ?> getUserApiKeys() {
		User currentUser = getUser();
		
		return ImmutableMap.of("user", currentUser.getCrsid(), "keys", currentUser.apisToMap());
	}
	
	@GET @Path("/new")
	public Map<String, String> getNewApiKey() {
		Session s = HibernateUtil.getTransactionSession();

		User currentUser = getUser();
		
		Api api = new Api();
		api.setUser(currentUser);
		
		s.save(api);
		
		return ImmutableMap.of("user", currentUser.getCrsid(), "key", api.getKey());
	}
	
	@GET @Path("/newGlobal")
	public Map<String, String> getNewGlobalApiKey() {
		Session s = HibernateUtil.getTransactionSession();

		Api api = new Api();
		api.setGlobalPermissions(true);
		
		s.save(api);
		
		return ImmutableMap.of("key", api.getKey());
	}
	
	// Does not require verification
	// Excluded from API filter
	
	@GET @Path("/type/{key}")
	public static Map<String, String> checkApiKeyType(@PathParam("key") String key) {
		
		Session s = HibernateUtil.getTransactionSession();
		
		if (key == null || key == "") {
			return ImmutableMap.of("error", "A key must be provided");
		}
		
		Api api = (Api) s.createQuery("from Api where key = :key").setParameter("key", key).uniqueResult();
		
		if (api == null) {
			return ImmutableMap.of("error", "Invalid key");
		} else if (api.isGlobalPermissions()) {
			return ImmutableMap.of("type", "global");
		} else {
			return ImmutableMap.of("type", "user", "userId", api.getUser().getCrsid());
		}
		
	}
	
}
