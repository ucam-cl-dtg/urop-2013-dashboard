package uk.ac.cam.dashboard.forms;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.FormParam;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.models.Notification;
import uk.ac.cam.dashboard.models.NotificationUser;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.queries.NotificationQuery;
import uk.ac.cam.dashboard.util.HibernateUtil;
import uk.ac.cam.dashboard.util.Strings;
import uk.ac.cam.dashboard.util.Util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;

public class CreateNotificationForm {
	@FormParam("message") String message;
	@FormParam("section") String section;
	@FormParam("link") String link;
	@FormParam("users") String users;
	@FormParam("foreignId") String foreignId;
	
	// Logger
	private static Logger log = LoggerFactory.getLogger(CreateNotificationForm.class);
	
	public int handle() {
		
		Session session = HibernateUtil.getTransactionSession();
		
		Notification notification = new Notification(message, section, "/" + section + "/" + link, foreignId);
		
		session.save(notification);
		
		// Create notificationUser objects
		if(!users.equals("")){
			User user;
			
			NotificationUser nUser;
			
			String[] crsids = users.split(",");
			for(String u : crsids){
				user = User.registerUser(u);
				nUser = new NotificationUser(user, notification);
				session.save(nUser);
			}		
		}
		
		return notification.getId();
				
	}
	
	public ImmutableMap<String, List<String>> validate() {
		ArrayListMultimap<String, String> errors = ArrayListMultimap.create();
		
		// Message
		if (message == null || message.equals("")) {
			errors.put("message", Strings.NOTIFICATION_NO_MESSAGE);
		} else if (message.length() >= 255) {
			errors.put("message", Strings.NOTIFICATION_MESSAGE_LENGTH);
		}

		// Section
		String[] validSections = {"dashboard", "signups", "questions", "handins"}; // Shared with GetNotificationForm
		if (section == null || section.equals("")) {
			errors.put("section", Strings.NOTIFICATION_NO_SECTION);
		} else if (!Arrays.asList(validSections).contains(section)) {
			errors.put("section", Strings.NOTIFICATION_INVALID_SECTION);
		}
		
		// Link
		if (link == null || link.equals("")) {
			errors.put("link", Strings.NOTIFICATION_NO_LINK);
		}
		
		// Users
		if (users == null || users.equals("")) {
			errors.put("users", Strings.NOTIFICATION_NO_USERS);
		} else if (users.split(",").length > 50) {
			errors.put("users", Strings.NOTIFICATION_USERS_MAX);
		}
		
		// Foreign id
		if (foreignId != null) {
			// Query the database to ensure that the foreignId is unique
			List<NotificationUser> results = NotificationQuery.all().foreignId(foreignId).list();
			if (results.size() > 0) {
				errors.put("foreignId", Strings.NOTIFICATION_FOREIGN_ID_NOT_UNIQUE);
			}
		} else {
			foreignId = "none";
		}

		return Util.multimapToImmutableMap(errors);
	}
	
	public ImmutableMap<String, ?> toMap() {
		ImmutableMap.Builder<String, Object> builder = new ImmutableMap.Builder<String, Object>();
		
		String localMessage = (message == null ? "" : message);
		builder.put("message", localMessage);
		
		String localSection = (section == null ? "" : section);
		builder.put("section", localSection);
		
		String localLink = (link == null ? "" : link);
		builder.put("link", localLink);
		
		String localUsers = (users == null ? "" : users);
		builder.put("users", localUsers);
		
		String localForeignId = (foreignId == null ? "" : foreignId);
		builder.put("foreignId", localForeignId);
		
		return builder.build();
	}
	
}
