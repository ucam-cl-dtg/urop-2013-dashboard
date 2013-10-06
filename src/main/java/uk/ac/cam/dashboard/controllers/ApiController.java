package uk.ac.cam.dashboard.controllers;

import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import uk.ac.cam.cl.dtg.teaching.hibernate.HibernateUtil;
import uk.ac.cam.dashboard.exceptions.AuthException;
import uk.ac.cam.dashboard.models.Api;
import uk.ac.cam.dashboard.models.User;
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
	public Map<String, ?> getNewApiKey() {
		Session s = HibernateUtil.getInstance().getSession();

		try {
			currentUser = validateUser();
		} catch (AuthException e) {
			return ImmutableMap.of("success", false, "errors", e.getMessage());
		}
		
		Criteria criteria = s.createCriteria(Api.class);
		criteria.add(Restrictions.eq("user", currentUser));
		@SuppressWarnings("unchecked")
		List<Api> keys = criteria.list();
		if(keys.size()>=5){
			return ImmutableMap.of("success", false, "errors", Strings.APIKEY_MAX);
		}	
		
		Api api = new Api();
		api.setUser(currentUser);
		
		s.save(api);
		
		return ImmutableMap.of("success", true, "user", currentUser.getCrsid(), "key", api.getKey());
	}
	
	@GET @Path("/newGlobal")
	public Map<String, String> getNewGlobalApiKey() {
		
		try {
			validateGlobal();
		} catch (Exception e) {
			return ImmutableMap.of("error", e.getMessage());
		}
		
		Session s = HibernateUtil.getInstance().getSession();

		Api api = new Api();
		api.setGlobalPermissions(true);
		
		s.save(api);
		
		return ImmutableMap.of("key", api.getKey());
	}
	
	// Deletion
	@DELETE @Path("/delete")
	public Map<String, ?> deleteApiKey(String key) {
		Session s = HibernateUtil.getInstance().getSession();

		try {
			currentUser = validateUser();
		} catch (AuthException e) {
			return ImmutableMap.of("errors", e.getMessage());
		}
		
		Criteria criteria = s.createCriteria(Api.class);
		criteria.add(Restrictions.eq("key", key));
		criteria.add(Restrictions.eq("user", currentUser));
		Api api = (Api)criteria.uniqueResult();
		
		if(api!=null){
			s.delete(api);
			return ImmutableMap.of("success", true);
		} else {
			return ImmutableMap.of("success", false);
		}
	}
	
	// Reset
	@POST @Path("/reset")
	public Map<String, ?> deleteApiKey() {
		Session s = HibernateUtil.getInstance().getSession();

		try {
			currentUser = validateUser();
		} catch (AuthException e) {
			return ImmutableMap.of("errors", e.getMessage());
		}
		
		for(Api a : currentUser.getApis()){
			s.delete(a);
		}
		
		currentUser.clearApis();
		
		Api api = new Api();
		api.setUser(currentUser);
		s.save(api);
		
		currentUser.addApi(api);
		
		s.save(currentUser);
		
		return ImmutableMap.of("success", true, "key", api.getKey(), "userID", currentUser.getCrsid());
	}
	
	// Validation
	
	// Does not require verification
	// Excluded from API filter
	
	@GET @Path("/type/{key}")
	public Map<String, String> checkApiKeyType(@PathParam("key") String key) {
		
		Session s = HibernateUtil.getInstance().getSession();
		
		if (key == null || key == "") {
			return ImmutableMap.of("error", Strings.AUTHEXCEPTION_NO_KEY);
		}

		if (key.equals(sRequest.getSession().getServletContext().getInitParameter("apiKey"))) {
			return ImmutableMap.of("type", "global");
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
