package uk.ac.cam.dashboard.controllers;

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

import uk.ac.cam.dashboard.exceptions.AuthException;
import uk.ac.cam.dashboard.forms.CreateNotificationForm;
import uk.ac.cam.dashboard.forms.GetNotificationForm;
import uk.ac.cam.dashboard.models.Notification;
import uk.ac.cam.dashboard.models.NotificationUser;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.queries.NotificationQuery;

import com.google.common.collect.ImmutableMap;

@Path("/api/notifications")
@Produces(MediaType.APPLICATION_JSON)
public class NotificationsController extends ApplicationController {
	
		// Create the logger
		private static Logger log = LoggerFactory.getLogger(NotificationsController.class);
		
		// Get current user from raven session
		private User currentUser;
		
		// Get notifications
		public Map<String, ?> getNotifications(GetNotificationForm notificationForm, boolean read) {
			
			try {
				currentUser = validateUser();
			} catch (AuthException e) {
				return ImmutableMap.of("error", e.getMessage());
			}
			
			ImmutableMap<String, List<String>> errors = notificationForm.validate();

			if (errors.isEmpty()) {
				return notificationForm.handle(currentUser, read);
			} else {
				return ImmutableMap.of("formErrors", errors, "data", notificationForm.toMap());
			}
			
		}
		
		// Unread notifications
		@GET @Path("/")
		public Map<String, ?> getUnreadNotifications(@Form GetNotificationForm notificationForm) {
			
			return getNotifications(notificationForm, false);
			
		}

		// Read notifications
		@GET @Path("/archive")
		public Map<String, ?> getReadNotifications(@Form GetNotificationForm notificationForm) {
			
			return getNotifications(notificationForm, true);
			
		}
		
		// Individual notification
		@GET @Path("/{id}")
		public Map<String, ?> getNotification(@PathParam("id") int id) {
			
			try {
				currentUser = validateUser();
			} catch (Exception e) {
				return ImmutableMap.of("error", e.getMessage());
			}
			
			Notification notification = NotificationQuery.get(id);
			
			if (notification != null) {
				return notification.toMap();
			} else {
				return ImmutableMap.of("error", "Could not find a notification with id " + id);
			}
			
		}
		
		// Create
		@POST @Path("/")
		public Map<String, ?> createNotification(@Form CreateNotificationForm notificationForm) {
			
			try {
				validateGlobal();
			} catch (Exception e) {
				return ImmutableMap.of("error", e.getMessage());
			}
			
			ImmutableMap<String, List<String>> errors = notificationForm.validate();

			if (errors.isEmpty()) {
				notificationForm.handle();
				return ImmutableMap.of("success", true);
			} else {
				return ImmutableMap.of("formErrors", errors, "data", notificationForm.toMap());
			}
			
		}
		
		// Update
		@PUT @Path("/{id}")
		public ImmutableMap<String, String> markNotificationAsRead(@PathParam("id") int id, @QueryParam("read") boolean read) {
	
			// Validate user
			try {
				currentUser = validateUser();
			} catch (Exception e) {
				return ImmutableMap.of("error", e.getMessage());
			}
			
			// Initialise possible errors
			ImmutableMap<String, String> error;
			if (read == true) {
				error = ImmutableMap.of("formErrors", "Could not mark notification as read");
			} else {
				error = ImmutableMap.of("formErrors", "Could not mark notification as unread");
			}
			
			// Attempt to mark notification as read
			//*TODO* validation
			try {
				if ( NotificationUser.markAsReadUnread(currentUser, id, read) != read ) {
					return error;
				}
			} catch (Exception e) {
				return error;
			}
			
			return ImmutableMap.of("redirectTo", "dashboard/notifications");
		}
		
}