package uk.ac.cam.dashboard.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.models.Notification;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.util.HibernateUtil;

import com.google.common.collect.ImmutableMap;
import com.googlecode.htmleasy.RedirectException;

import dashboard.queries.NotificationQuery;

@Path("dashboard/notifications")
public class NotificationsController extends ApplicationController {
	
		// Create the logger
		private static Logger log = LoggerFactory.getLogger(GroupsController.class);
		
		// Get current user from raven session
		private User currentUser;
		
		// Notification create
		public static void pushNotificationToUsers(String message, Set<User> users) {

			Session s = HibernateUtil.getTransactionSession();
				
			Notification notification = new Notification(message);
			
			for (User u:users) {
				notification.addUser(u);	
			}
			
			s.save(notification);
			
		}
		
		// Index
		@GET @Path("/")
		@Produces(MediaType.APPLICATION_JSON)
		public Map<String, ?> getNotifications() {
			
			currentUser = initialiseUser();
			
			// Next retrieve the notifications associated with the current user
			NotificationQuery nq = NotificationQuery.all();
			nq.forUser(currentUser);
			
			List<?> result = nq.list();
			List<Map<?,?>> notifications = new ArrayList<Map<?,?>>();
			for (Object o:result) {
				notifications.add(((Notification)o).toMap());
			}
						
			return ImmutableMap.of("userId", currentUser.getCrsid(), "notifictions", notifications); 
			
		}
		
		// Create
		@POST @Path("/")
		public void createNotification(@QueryParam("message") String message,
									   @QueryParam("users") String users) {
			
			String [] userStrings = users.split(",");
			Set<User> userSet = new HashSet<User>();
			for (String u:userStrings) {
				User user = new User(u);
				userSet.add(user);
			}
			
			NotificationsController.pushNotificationToUsers(message, userSet);
			
			throw new RedirectException(NotificationsController.class, "createNotificationCallback");
			
		}
		
		public Map<String, ?> createNotificationCallback() {
			return ImmutableMap.of("success", "true");
		}

}