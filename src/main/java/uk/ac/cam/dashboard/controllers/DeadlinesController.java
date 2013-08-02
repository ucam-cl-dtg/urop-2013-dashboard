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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.forms.DeadlineForm;
import uk.ac.cam.dashboard.models.Deadline;
import uk.ac.cam.dashboard.models.DeadlineUser;
import uk.ac.cam.dashboard.models.Group;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.queries.DeadlineQuery;
import uk.ac.cam.dashboard.util.HibernateUtil;

import com.google.common.collect.ImmutableMap;
import com.googlecode.htmleasy.RedirectException;

@Path("/dashboard/deadlines")
public class DeadlinesController extends ApplicationController {
	
	private User currentUser;
	
	// Logger
	private static Logger log = LoggerFactory.getLogger(DeadlinesController.class);
	
	// Index 
	@GET @Path("/") 
	@Produces(MediaType.APPLICATION_JSON)
	public ImmutableMap<String, ?> indexDeadlines() {

		currentUser = initialiseUser();
		
		return ImmutableMap.of("user", currentUser.toMap(), "deadlines", currentUser.deadlinesToMap());
	}
	
	// Create
	@POST @Path("/") 
	public void createDeadline(@Form DeadlineForm deadlineForm) throws Exception {
		currentUser = initialiseUser();
		
		int id = deadlineForm.handleCreate(currentUser);
		
		throw new RedirectException("/app/#dashboard/supervisor");
	}
	
	// Edit
	@GET @Path("/{id}/edit") 
	@Produces(MediaType.APPLICATION_JSON)
	public Map editDeadline(@PathParam("id") int id) {
		
		currentUser = initialiseUser();
		
	  	Deadline deadline = Deadline.getDeadline(id);
	  	
		return ImmutableMap.of("deadline", deadline.toMap());		
	}
	
	// Update
	@POST @Path("/{id}/edit")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, ?> updateDeadline(@Form DeadlineForm deadlineForm, @PathParam("id") int id) {
		
		currentUser = initialiseUser();
		
		Deadline deadline = deadlineForm.handleUpdate(currentUser, id);

		return deadline.toMap();
	}
	
	// Delete
	@DELETE @Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, ?> deleteDeadline(@PathParam("id") int id) {

		Session session = HibernateUtil.getTransactionSession();
				
		Deadline d = DeadlineQuery.get(id);

	  	session.delete(d);
		
		return ImmutableMap.of("success", true, "id", id);
	}
	
	// Change completed status
	@PUT @Path("/{id}/complete")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, ?> updateComplete(@PathParam("id") int id) {
		
		currentUser = initialiseUser();
		
		DeadlineUser d = DeadlineQuery.getDUser(id);
		
		if(d.getComplete()){ d.toggleComplete(false);
		} else { d.toggleComplete(true); }

		return d.toMap();
	}
	
	// Change archived status	
	@PUT @Path("/{id}/archive")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, ?> updateArchive(@PathParam("id") int id) {
		
		currentUser = initialiseUser();
		
		DeadlineUser d = DeadlineQuery.getDUser(id);
		
		if(d.getArchived()){ d.toggleArchived(false);
		} else { d.toggleArchived(true); }

		return d.toMap();
	}
	
	// Find groups AJAX
	@POST @Path("/queryGroup")
	@Produces(MediaType.APPLICATION_JSON)
	public List queryCRSID(String q) {
		currentUser = initialiseUser();
		String crsid = currentUser.getCrsid();
		
		//Remove q= prefix
		String x = q.substring(2);
		
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