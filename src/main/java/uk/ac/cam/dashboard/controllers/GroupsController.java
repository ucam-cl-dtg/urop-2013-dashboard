package uk.ac.cam.dashboard.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;
import org.jboss.resteasy.annotations.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.cl.dtg.ldap.LDAPObjectNotFoundException;
import uk.ac.cam.cl.dtg.ldap.LDAPPartialQuery;
import uk.ac.cam.dashboard.forms.GroupForm;
import uk.ac.cam.dashboard.models.Group;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.queries.GroupQuery;
import uk.ac.cam.dashboard.util.HibernateUtil;
import uk.ac.cam.dashboard.util.Util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;

@Path("api/dashboard/groups")
@Produces(MediaType.APPLICATION_JSON)
public class GroupsController extends ApplicationController {
	
		// Create the logger
		private static Logger log = LoggerFactory.getLogger(GroupsController.class);
		
		// Get current user from raven session
		private User currentUser;
		
		// Index
		@GET @Path("/") 
		public Map<String, ?> indexGroups() {

			currentUser = initialiseUser();
			
			return ImmutableMap.of("user", currentUser.toMap(), "groups", currentUser.subscriptionsToMap());
		}
		
		// Create
		@POST @Path("/") 
		public Map<String, ?> createGroup(@Form GroupForm groupForm) throws Exception {
			currentUser = initialiseUser();
			
			ArrayListMultimap<String, String> errors = groupForm.validate();
			ImmutableMap<String, List<String>> actualErrors = Util.multimapToImmutableMap(errors);
			
			if(errors.isEmpty()){
				groupForm.handle(currentUser);
				return ImmutableMap.of("redirectTo", "dashboard/groups/");
			} else {
				return ImmutableMap.of("group", groupForm.toMap(-1), "errors", actualErrors);
			}
		}
		
		// Import from LDAP
		@POST @Path("/import") 
		public Map<String, ?> importGroup(@Form GroupForm groupForm) throws Exception {
			currentUser = initialiseUser();
			
			ArrayListMultimap<String, String> errors = groupForm.validate();
			ImmutableMap<String, List<String>> actualErrors = Util.multimapToImmutableMap(errors);
			
			if(errors.isEmpty()){
				groupForm.handleImport(currentUser);
				return ImmutableMap.of("redirectTo", "dashboard/supervisor/");
			} else {
				return ImmutableMap.of("group", groupForm.toMap(-1), "errors", actualErrors);
			}
		}
		
		// Get
		@GET @Path("/{id}") 
		public Map<String, ?> getGroup(@PathParam("id") int id) {
			
			currentUser = initialiseUser();

			Group group = Group.getGroup(id);
			
			return ImmutableMap.of("group", group.toMap(), "errors", "undefined");
		}
		
		// Update
		@POST @Path("/{id}")
		public Map<String, ?> updateGroup(@Form GroupForm groupForm, @PathParam("id") int id) {	
			currentUser = initialiseUser();
			
			ArrayListMultimap<String, String> errors = groupForm.validate();
			ImmutableMap<String, List<String>> actualErrors = Util.multimapToImmutableMap(errors);
			
			if(errors.isEmpty()){
				groupForm.handleUpdate(currentUser, id);
				return ImmutableMap.of("redirectTo", "dashboard/groups/"+id);
			} else {
				return ImmutableMap.of("group", groupForm.toMap(id), "errors", actualErrors);
			}
		}
		
		// Destroy 
		@DELETE @Path("/{id}")
		public Map<String, ?> deleteGroup(@PathParam("id") int id) {
			
			Session session = HibernateUtil.getTransactionSession();
			
			Group g = GroupQuery.get(id);

		  	session.delete(g);
			
			return ImmutableMap.of("success", true, "id", id);
			
		}
		
		// Find users by crsid
		@POST @Path("/queryCRSID")
		public List<HashMap<String, String>> queryCRSId(@FormParam("q") String x) {
			
			// Perform LDAP search
			List<HashMap<String, String>> matches = null;
			try {
				matches = LDAPPartialQuery.partialUserByCrsid(x);
			} catch (LDAPObjectNotFoundException e){
				log.error("Error performing LDAPQuery: " + e.getMessage());
				return new ArrayList<HashMap<String, String>>();
			}
			
			return matches;
		}
		
		// Find users by surname
		@POST @Path("/querySurname")
		public List<HashMap<String, String>> querySurname(@FormParam("q") String x) {
			
			// Perform LDAP search
			List<HashMap<String, String>> matches = null;
			try {
				matches = LDAPPartialQuery.partialUserBySurname(x);
			} catch (LDAPObjectNotFoundException e){
				log.error("Error performing LDAPQuery: " + e.getMessage());
				return new ArrayList<HashMap<String, String>>();
			}
			
			return matches;
		}
		
		// Find groups from LDAP
		@POST @Path("/queryGroup")
		public List<HashMap<String, String>> queryGroup(@FormParam("q") String x) {
			
			// Perform LDAP search
			List<HashMap<String, String>> matches = null;
			try {
				matches = LDAPPartialQuery.partialGroupByName(x);
			} catch (LDAPObjectNotFoundException e){
				log.error("Error performing LDAPQuery: " + e.getMessage());
				return new ArrayList<HashMap<String, String>>();
			}			
			return matches;
		}	
}