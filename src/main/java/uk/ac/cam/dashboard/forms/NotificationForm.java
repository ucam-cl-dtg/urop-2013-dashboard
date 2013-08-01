package uk.ac.cam.dashboard.forms;

import javax.ws.rs.FormParam;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.models.Notification;
import uk.ac.cam.dashboard.models.NotificationUser;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.util.HibernateUtil;

public class NotificationForm {
	@FormParam("message") String message;
	@FormParam("section") String section;
	@FormParam("link") String link;
	@FormParam("users") String users;
	
	//Logger
	private static Logger log = LoggerFactory.getLogger(NotificationForm.class);
	
	public int handle() {		
		
//		parseForm();
		
		Session session = HibernateUtil.getTransactionSession();
		
		// Create deadline prototype
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
	
}
