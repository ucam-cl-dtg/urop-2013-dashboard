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
import uk.ac.cam.dashboard.util.HibernateUtil;
import uk.ac.cam.dashboard.util.Util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;

public class CreateNotificationForm {
	@FormParam("message") String message;
	@FormParam("section") String section;
	@FormParam("link") String link;
	@FormParam("users") String users;
	
	// Logger
	private static Logger log = LoggerFactory.getLogger(CreateNotificationForm.class);
	
	public int handle() {
		
		Session session = HibernateUtil.getTransactionSession();
		
		Notification notification = new Notification();
		notification.setMessage(message);
		notification.setSection(section);
		notification.setLink(link);
		
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
			errors.put("message", "Message field cannot be empty.");
		} else if (message.length() >= 255) {
			errors.put("message", "Message length cannot be more than 255 characters.");
		}

		// Section
		String[] validSections = {"dashboard", "signups", "questions", "handins"}; // Shared with GetNotificationForm
		if (section == null || section.equals("")) {
			errors.put("section", "Section field cannot be empty.");
		} else if (!Arrays.asList(validSections).contains(section)) {
			errors.put("section", "Invalid section field.");
		}
		
		// Link
		if (link == null || link.equals("")) {
			errors.put("link", "Link field cannot be empty.");
		}
		
		// Users
		if (users == null || users.equals("")) {
			errors.put("users", "A list of comma separated users must be set.");
		} else if (users.split(",").length > 50) {
			errors.put("users", "A maximum of 50 users can be set for any notification.");
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
		
		return builder.build();
	}
	
}
