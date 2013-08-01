package uk.ac.cam.dashboard.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.forms.NotificationForm;
import uk.ac.cam.dashboard.models.Notification;
import uk.ac.cam.dashboard.models.NotificationUser;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.queries.NotificationQuery;

import com.googlecode.htmleasy.RedirectException;

@Path("dashboard/notifications")
public class NotificationsController extends ApplicationController {
	
		// Create the logger
		private static Logger log = LoggerFactory.getLogger(NotificationsController.class);
		
		// Get current user from raven session
		private User currentUser;
		
		// Index
		@GET @Path("/")
		@Produces(MediaType.APPLICATION_JSON)
		public Map<String, ?> getNotifications(@QueryParam("offset") Integer offset,
												@QueryParam("limit") Integer limit,
												@QueryParam("section") String section,
												@QueryParam("read") Boolean read) 
												throws RedirectException {
			
			Map<String, Object> userNotifications = new HashMap<String, Object>();
			
			currentUser = initialiseUser();
			
			NotificationQuery nq = NotificationQuery.all();
			nq.byUser(currentUser);
			
			if (offset != null) {
				nq.offset(offset);
				userNotifications.put("offset", offset);
			} else {
				userNotifications.put("offset", 0);
			}
			
			if (limit != null) {
				nq.limit(limit);
				userNotifications.put("limit", limit);
			} else {
				nq.limit(10);
				userNotifications.put("limit", 10);
			}
				
			if (section != null && !section.isEmpty()) {
				nq.inSection(section);
				userNotifications.put("section", section);
			}
			
			if (read != null) {
				nq.isRead(read);
				userNotifications.put("read", read);
			}
			
			List<?> result = nq.list();
			List<Map<?,?>> notifications = new ArrayList<Map<?,?>>();
			for (Object o:result) {
				notifications.add(((Notification) o).toMap());
			}
			userNotifications.put("notifications", notifications);

			return userNotifications;
			
		}
		
		// Create
		@POST @Path("/")
		public void createNotification(@Form NotificationForm notificationForm) throws RedirectException {
			
			notificationForm.handle();
			
			throw new RedirectException("/app/#dashboard/notifications");
		}
		
		// Update
		@PUT @Path("/{id}")
		public void markNotificationAsRead(@PathParam("id") int id) {
	
			currentUser = initialiseUser();
			
			NotificationUser.markAsRead(currentUser, id);
			
			throw new RedirectException("/app/#dashboard/notifications");
			
		}
		
//		// Callbacks
//		@GET @Path("/success")
//		@Produces(MediaType.APPLICATION_JSON)
//		public Map<String, ?> successCallback() {
//			return ImmutableMap.of("success", 1);
//		}
//
//		@GET @Path("/error")
//		@Produces(MediaType.APPLICATION_JSON)
//		public Map<String, ?> errorCallback() {
//			return ImmutableMap.of("error", 1);
//		}
		
}