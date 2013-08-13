package uk.ac.cam.dashboard.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;
import org.jboss.resteasy.annotations.Form;

import uk.ac.cam.dashboard.exceptions.AuthException;
import uk.ac.cam.dashboard.forms.DeadlineForm;
import uk.ac.cam.dashboard.models.Deadline;
import uk.ac.cam.dashboard.models.DeadlineUser;
import uk.ac.cam.dashboard.models.Group;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.queries.DeadlineQuery;
import uk.ac.cam.dashboard.util.HibernateUtil;
import uk.ac.cam.dashboard.util.Util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;

@Path("/api/deadlines")
@Produces(MediaType.APPLICATION_JSON)
public class DeadlinesController extends ApplicationController {
	
	private User currentUser;
	
	// Index 
	@GET @Path("/") 
	public ImmutableMap<String, ?> indexDeadlines() {

		try {
			currentUser = validateUser();
		} catch (AuthException e) {
			return ImmutableMap.of("error", e.getMessage());
		}
		
		return ImmutableMap.of("user", currentUser.toMap(), "deadlines", currentUser.setDeadlinesToMap());
	}
	
	// Manage
	@GET @Path("/{id}") 
	public Map<String, ?> getDeadline(@PathParam("id") int id) {
		
		try {
			currentUser = validateUser();
		} catch (AuthException e) {
			return ImmutableMap.of("error", e.getMessage());
		}
		
	  	Deadline deadline = DeadlineQuery.get(id);
	  	
		return ImmutableMap.of("deadline", deadline.toMap(), "deadlineEdit", deadline.toMap(), "users", deadline.usersToMap(), "errors", "undefined");		
	}
	
	// Create
	@POST @Path("/") 
	public Map<String, ?> createDeadline(@Form DeadlineForm deadlineForm) throws Exception {

		try {
			currentUser = validateUser();
		} catch (AuthException e) {
			return ImmutableMap.of("error", e.getMessage());
		}
		
		ArrayListMultimap<String, String> errors = deadlineForm.validate();
		ImmutableMap<String, List<String>> actualErrors = Util.multimapToImmutableMap(errors);
		
		if(errors.isEmpty()){
			int id = deadlineForm.handleCreate(currentUser);
			return ImmutableMap.of("redirectTo", "dashboard/deadlines/"+id);
		} else {
			return ImmutableMap.of("deadline", deadlineForm.toMap(-1), "errors", actualErrors);
		}
	}
	
	// Update
	@POST @Path("/{id}")
	public Map<String, ?> updateDeadline(@Form DeadlineForm deadlineForm, @PathParam("id") int id) {
		
		try {
			currentUser = validateUser();
		} catch (AuthException e) {
			return ImmutableMap.of("error", e.getMessage());
		}
		
		ArrayListMultimap<String, String> errors = deadlineForm.validate();
		ImmutableMap<String, List<String>> actualErrors = Util.multimapToImmutableMap(errors);
		
		if(errors.isEmpty()){
			deadlineForm.handleUpdate(currentUser, id);
			return ImmutableMap.of("redirectTo", "dashboard/deadlines/"+id);
		} else {
			return ImmutableMap.of("errors", actualErrors, "deadlineEdit", deadlineForm.toMap(id), "target", "edit");
		}
	}
	
	// Delete
	@DELETE @Path("/{id}")
	public Map<String, ?> deleteDeadline(@PathParam("id") int id) {

		Session session = HibernateUtil.getTransactionSession();
				
		Deadline d = DeadlineQuery.get(id);

	  	session.delete(d);
		
		return ImmutableMap.of("success", true, "id", id);
		
	}
	
	// Mark as complete/not complete
	@PUT @Path("/{id}/complete")
	public Map<String, ?> updateComplete(@PathParam("id") int id) {
		
		DeadlineUser d = DeadlineQuery.getDUser(id);
		
		if(d.getComplete()){ d.toggleComplete(false);
		} else { d.toggleComplete(true); }

		return d.toMap();
	}
	
	// Mark as archived
	@PUT @Path("/{id}/archive")
	public Map<String, ?> updateArchive(@PathParam("id") int id) {
		
		DeadlineUser d = DeadlineQuery.getDUser(id);
		
		if(d.getArchived()){ d.toggleArchived(false);
		} else { d.toggleArchived(true); }

		return d.toMap();
	}
	
	// Find groups AJAX
	@POST @Path("/queryGroup")
	public List<ImmutableMap<String, ?>> queryCRSID(String q) {
		currentUser = getUser();
		
		//Remove q= prefix
		String x = q.substring(2);
		System.out.println("test");
		System.out.println(x);
		
		//List of group matches
		ArrayList<ImmutableMap<String,?>> matches = new ArrayList<ImmutableMap<String, ?>>();
		
		//Get matching group names.. is this too slow? 
		for(Group g : currentUser.getGroups()){
			if(g.getTitle().contains(x)){
				matches.add(ImmutableMap.of("group_id", g.getId(), "group_name", g.getTitle()));
			}
		}
		
		return matches;
	}
}