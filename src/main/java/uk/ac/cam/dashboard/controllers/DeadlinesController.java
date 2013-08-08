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

@Path("api/dashboard/deadlines")
@Produces(MediaType.APPLICATION_JSON)
public class DeadlinesController extends ApplicationController {
	
	private User currentUser;
	
	// Index 
	@GET @Path("/") 
	public ImmutableMap<String, ?> indexDeadlines() {

		currentUser = initialiseUser();
		
		return ImmutableMap.of("user", currentUser.toMap(), "deadlines", currentUser.deadlinesToMap());
	}
	
	// Get
	@GET @Path("/{id}") 
	public Map<String, ?> getDeadline(@PathParam("id") int id) {
		
		currentUser = initialiseUser();
		
	  	Deadline deadline = Deadline.getDeadline(id);
	  	
		return ImmutableMap.of("errors", "undefined","deadline", deadline.toMap(), "deadlineEdit", deadline.toMap());		
	}
	
	// Create
	@POST @Path("/") 
	public Map<String, ?> createDeadline(@Form DeadlineForm deadlineForm) throws Exception {
		currentUser = initialiseUser();
		
		ArrayListMultimap<String, String> errors = deadlineForm.validate();
		ImmutableMap<String, List<String>> actualErrors = Util.multimapToImmutableMap(errors);
		
		if(errors.isEmpty()){
			int id = deadlineForm.handleCreate(currentUser);
			return ImmutableMap.of("redirectTo", "dashboard/deadlines/"+id);
		} else {
			return ImmutableMap.of("data", deadlineForm.toMap(-1), "errors", actualErrors);
		}
	}
	
	// Update
	@POST @Path("/{id}")
	public Map<String, ?> updateDeadline(@Form DeadlineForm deadlineForm, @PathParam("id") int id) {
		
		currentUser = initialiseUser();
		
		ArrayListMultimap<String, String> errors = deadlineForm.validate();
		ImmutableMap<String, List<String>> actualErrors = Util.multimapToImmutableMap(errors);
		
		if(errors.isEmpty()){
			deadlineForm.handleUpdate(currentUser, id);
			return ImmutableMap.of("redirectTo", "dashboard/deadlines/"+id);
		} else {
			return ImmutableMap.of("errors", actualErrors, "deadline", deadlineForm.toMap(id), "target", "edit");
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
	
	// Change completed status
	@PUT @Path("/{id}/complete")
	public Map<String, ?> updateComplete(@PathParam("id") int id) {
		
		currentUser = initialiseUser();
		
		DeadlineUser d = DeadlineQuery.getDUser(id);
		
		if(d.getComplete()){ d.toggleComplete(false);
		} else { d.toggleComplete(true); }

		return d.toMap();
	}
	
	// Change archived status	
	@PUT @Path("/{id}/archive")
	public Map<String, ?> updateArchive(@PathParam("id") int id) {
		
		currentUser = initialiseUser();
		
		DeadlineUser d = DeadlineQuery.getDUser(id);
		
		if(d.getArchived()){ d.toggleArchived(false);
		} else { d.toggleArchived(true); }

		return d.toMap();
	}
	
	// Find groups AJAX
	@POST @Path("/queryGroup")
	public List<ImmutableMap<String, ?>> queryCRSID(String q) {
		currentUser = initialiseUser();
		
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