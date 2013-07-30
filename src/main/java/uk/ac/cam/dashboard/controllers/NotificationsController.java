package uk.ac.cam.dashboard.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.models.Notification;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.queries.NotificationQuery;
import uk.ac.cam.dashboard.util.HibernateUtil;

import com.google.common.collect.ImmutableMap;
import com.googlecode.htmleasy.RedirectException;

@Path("dashboard/notifications")
public class NotificationsController extends ApplicationController {
	
		// Create the logger
		private static Logger log = LoggerFactory.getLogger(GroupsController.class);
		
		// Get current user from raven session
		private User currentUser;
		
		// Notification create
		public static void pushNotificationToUsers(String message, String section, String link, Set<User> users) {

			Session s = HibernateUtil.getTransactionSession();
				
			Notification notification = new Notification(message, section, link);
			
			for (User u:users) {
				notification.addUser(u);	
			}
			
			s.save(notification);
			
		}
		
		// Index
		@GET @Path("/")
		@Produces(MediaType.APPLICATION_JSON)
		public Map<String, ?> getNotifications( @QueryParam("offset") Integer offset,
												@QueryParam("limit") Integer limit,
												@QueryParam("section") String section,
												@QueryParam("read") Boolean read) throws RedirectException {
			
			currentUser = initialiseUser();
			
			NotificationQuery nq = NotificationQuery.all();
			nq.forUser(currentUser);
			
			if (offset != null) { nq.offset(offset); }
			if (limit != null) { nq.limit(limit); }
			if (section != null) { nq.inSection(section); }
			if (read != null) {	nq.isRead(read); }
			
			List<?> result = nq.list();
			List<Map<?,?>> notifications = new ArrayList<Map<?,?>>();
			for (Object o:result) {
				notifications.add(((Notification)o).toMap());
			}
						
			return ImmutableMap.of("userId", currentUser.getCrsid(), "notifications", notifications); 
			
		}
		
		// Create
		@POST @Path("/")
		public void createNotification(@FormParam("message") String message,
									   @FormParam("section") String section,
									   @FormParam("link") String link,
									   @FormParam("users") String users) throws RedirectException {
			
			String [] userStrings = users.split(",");
			Set<User> userSet = new HashSet<User>();
			for (String u:userStrings) {
				User user = User.registerUser(u);
				userSet.add(user);
			}
			
			NotificationsController.pushNotificationToUsers(message, section, link, userSet);
			
			throw new RedirectException(NotificationsController.class, "successCallback");
			
		}
		
		// Delete
		@DELETE @Path("/{id}")
		public void deleteNotification(@PathParam("id") int id) {
		
			Notification.delete(id);
			
			throw new RedirectException(NotificationsController.class, "successCallback");
			
		}
		
		// Update
		@PUT @Path("/{id}")
		public void markNotificationAsRead(@PathParam("id") int id) {
			
			Notification.markAsRead(id);
			
			throw new RedirectException(NotificationsController.class, "successCallback");
			
		}
		
		@GET @Path("/success")
		@Produces(MediaType.APPLICATION_JSON)
		public Map<String, ?> successCallback() {
			return ImmutableMap.of("success", 1);
		}

		@GET @Path("/error")
		@Produces(MediaType.APPLICATION_JSON)
		public Map<String, ?> errorCallback() {
			return ImmutableMap.of("error", 1);
		}
		
}