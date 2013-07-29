package uk.ac.cam.dashboard.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.models.Notification;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.util.HibernateUtil;

import com.google.common.collect.ImmutableMap;
import com.googlecode.htmleasy.ViewWith;


@Path("/")
public class NotificationsController {

	private Criteria criteria;
	
	public NotificationsController() {}
	
	public NotificationsController(Criteria criteria) {
		this.criteria = criteria;
	}

	public static NotificationsController all() {
		return new NotificationsController (
			HibernateUtil.getTransactionSession()
			.createCriteria(Notification.class)
			.addOrder(Order.desc("timestamp"))
		);
	}
	
	public NotificationsController forUser(User user) {
		criteria.createAlias("users", "u")
			.add(Restrictions.eq("u.CRSID", user.getCrsid()));
		return this;
	}
	
	public NotificationsController begins(int start) {
		this.criteria.setFirstResult(start - 1);
		return this;
	}

	public NotificationsController ends(int start, int end) {
		this.criteria.setMaxResults(end - start);
		return this;
	}
	
	public List<Notification> list() {
		return this.criteria.list();
	}
	
	public static void pushNotificationToUsers(String message, Set<User> users) {

		Session s = HibernateUtil.getTransactionSession();
			
		Notification notification = new Notification(message);
		
		for (User u:users) {
			s.saveOrUpdate(u);
			notification.addUser(u);	
		}
		
		s.save(notification);
		
	}
	
	private static Logger log = LoggerFactory.getLogger(NotificationsController.class);
	
	@GET
	@Path("/notifications/{id}")
	@ViewWith("/soy/notifications.notifications")
	public Map<String, ?> notificationStream(@PathParam("id") String id,
							   				 @QueryParam("start") Integer start,
							   				 @QueryParam("end") Integer end) {
		
		User user = new User(id);
		String uName = "test";
		
		NotificationsController controller = NotificationsController.all();
		controller.forUser(user);

		if (start != null && end != null && end > start && start > 0) {
			controller.begins(start);
			controller.ends(start, end);
		} else if (start == null && end != null) {
			String errorString = "Start of range must be specified"; 
			log.error(errorString);
			return ImmutableMap.of("error", 1, "errorMessage", errorString);
		} else if (start != null && end == null) {
			String errorString = "End of range must be specified"; 
			log.error(errorString);
			return ImmutableMap.of("error", 1, "errorMessage", errorString);
		} else if (start != null && end != null  && end <= start) {
			String errorString = "End of range must be greater than the start"; 
			log.error(errorString);
			return ImmutableMap.of("error", 1, "errorMessage", errorString);
		} else if (start != null && end != null && start <= 0) {
			String errorString = "Start of range must be greater than 0"; 
			log.error(errorString);
			return ImmutableMap.of("error", 1, "errorMessage", errorString);
		}
		
		List<?> result = controller.list();
		List<Map<?,?>> notifications = new ArrayList<Map<?,?>>();
		for (Object o:result) {
			notifications.add(((Notification)o).toMap());
		}
		
		if (notifications.size() == 0) {
			String errorString = "No notifications found"; 
			log.error(errorString);
			return ImmutableMap.of("error", 1, "errorMessage", errorString);
		}
		
		return ImmutableMap.of("userID", id, "uName", uName, "notifications", notifications, "error", 0);
		
	}
	
	@GET
	@Path("/notifications/create/")
	@ViewWith("/soy/notifications.createNotification")
	public Map<String, ?> createNotification(@QueryParam("message") String message,
								   			 @QueryParam("users") String users,
								   			 @QueryParam("submitted") String submitted) {
		
		if (message != null && users != null) {
			String [] userStrings = users.split(",");
			Set<User> userSet = new HashSet<User>();
			for (String u:userStrings) {
				User user = new User(u);
				userSet.add(user);
			}
			NotificationsController.pushNotificationToUsers(message, userSet);
			return ImmutableMap.of("error", 0);
		} else if (message == null && submitted == "submitted") {
			String errorString = "A notification message is required"; 
			log.error(errorString);
			return ImmutableMap.of("error", 1, "errorMessage", errorString);
		} else if (users == null && submitted == "submitted") {
			String errorString = "A list of users is required"; 
			log.error(errorString);
			return ImmutableMap.of("error", 1, "errorMessage", errorString);			
		}
		
		return ImmutableMap.of("error", 0);
		
	}

}
