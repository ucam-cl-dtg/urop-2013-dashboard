package uk.ac.cam.dashboard.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.models.Notification;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.queries.NotificationQuery;

import com.google.common.collect.ImmutableMap;
import com.googlecode.htmleasy.RedirectException;

@Path("dashboard/notifications")
public class NotificationsController extends ApplicationController {
	
		// Create the logger
		private static Logger log = LoggerFactory.getLogger(GroupsController.class);
		
		// Get current user from raven session
		private User currentUser;
		
		// Index
		@GET @Path("/")
		@Produces(MediaType.APPLICATION_JSON)
		public Map<String, ?> getNotifications( @QueryParam("offset") Integer offset,
												@QueryParam("limit") Integer limit,
												@QueryParam("section") String section,
												@QueryParam("read") Boolean read) throws RedirectException {
			
			if ( validateRequest() == Permissions.NO_PERMISSIONS ) {
				throw new RedirectException(NotificationsController.class, "errorCallback");
			} else if ( validateRequest() == Permissions.GLOBAL_API ) {
				// A user must be provided
				throw new RedirectException(NotificationsController.class, "errorCallback");
			} else if ( validateRequest() == Permissions.GLOBAL_API_WITH_USER ) {
				currentUser = initialiseSpecifiedUser(sRequest.getParameter("user"));
			} else if ( validateRequest() == Permissions.USER_API ) {
				currentUser = initialiseSpecifiedUser(sRequest.getParameter("user"));
			} else if ( validateRequest() == Permissions.RAVEN_SESSION ) {
				currentUser = initialiseUser();
			}
			
			ImmutableMap.Builder<String, Object> map = new ImmutableMap.Builder<String, Object>();
			map = map.put("userId", currentUser.getCrsid());
			
			NotificationQuery nq = NotificationQuery.all();
			nq.forUser(currentUser);
			
			if (offset != null) {
				nq.offset(offset);
				map = map.put("offset", offset);
			} else {
				map = map.put("offset", 0);
			}
			
			if (limit != null) {
				nq.limit(limit);
				map = map.put("limit", limit);
			} else {
				nq.limit(10);
				map = map.put("limit", 10);
			}
				
			if (section != null && !section.isEmpty()) {
				nq.inSection(section);
				map = map.put("section", section);
			}
			
			if (read != null) {
				nq.isRead(read);
				map = map.put("read", read);
			}
			
			List<?> result = nq.list();
			List<Map<?,?>> notifications = new ArrayList<Map<?,?>>();
			for (Object o:result) {
				notifications.add(((Notification) o).toMap());
			}
			map = map.put("notifications", notifications);
			
			ImmutableMap<String, ?> finalMap = map.build();
			return finalMap;
			
		}
		
		// Create
		@POST @Path("/")
		public void createNotification( @FormParam("message") String message,
									    @FormParam("section") String section,
									    @FormParam("link") String link,
									    @FormParam("users") String users ) throws RedirectException {
			String [] userStrings = users.split(",");
			Set<User> userSet = new HashSet<User>();
			for (String u:userStrings) {
				User user = User.registerUser(u);
				userSet.add(user);
			}
			
			Notification.pushNotificationToUsers(message, section, link, userSet);
			
			throw new RedirectException(NotificationsController.class, "successCallback");
		}
		
		// Update
		@PUT @Path("/{id}")
		public void markNotificationAsRead(@PathParam("id") int id) {
			currentUser = initialiseUser();
			Set<Notification> notifications = currentUser.getNotifications();
			
			for (Notification n : notifications) {
				if ( n.getId() == id) {
					Notification.markAsRead(id);
					throw new RedirectException(NotificationsController.class, "successCallback");
				}
			}
			
			throw new RedirectException(NotificationsController.class, "errorCallback");
			
		}
		
		// Callbacks
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