package uk.ac.cam.dashboard.models;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.GenericGenerator;

import uk.ac.cam.dashboard.util.HibernateUtil;

import com.google.common.collect.ImmutableMap;

@Entity
@Table(name="NOTIFICATIONS")
public class Notification {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private int id;
	
	@OneToMany(mappedBy = "notification")
	private Set<NotificationUser> notificationsUsers = new HashSet<NotificationUser>();
	
	private String message;
	private Calendar timestamp;
	private String section;
	private String link;
	
	public Notification() {}
	public Notification(String message, String section, String link) {
		this.message = message;
		this.section = section;
		this.link = link;
		this.timestamp = Calendar.getInstance();
	}

	public Map<String,?> toMap(User user) {
		Session s = HibernateUtil.getTransactionSession();

		ImmutableMap.Builder<String, Object> map = new ImmutableMap.Builder<String, Object>();
		
		map = map.put("id", this.id);
		map = map.put("message", this.message);
		map = map.put("section", this.section);
		map = map.put("link", this.link);
		map = map.put("timestamp", this.timestamp.getTime().toString());
		
		Query getNotificationUser = s.createQuery("from NotificationUser where notification = :notification and user = :user").setParameter("notification", this).setParameter("user", user);
	  	NotificationUser notificationUser = (NotificationUser) getNotificationUser.uniqueResult();
		map = map.put("read", notificationUser.isRead());
	  	
		map = map.put("users", this.usersToSet());
		
		ImmutableMap<String, ?> finalMap = map.build();
		return finalMap; 
	}
	
	public Set<NotificationUser> getNotificationsUsers() {return this.notificationsUsers;}
	public void setNotificationsUsers(Set<NotificationUser> notificationsUsers) {this.notificationsUsers = notificationsUsers;}
	
	public Set<String> usersToSet() {
		HashSet<String> userCrsids = new HashSet<String>();
		for (NotificationUser n:this.notificationsUsers) {
			userCrsids.add(n.getUser().getCrsid());
		}
		return userCrsids;
	}

	// Should not be needed, but included for posterity
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
	
	public Calendar getTimestamp() { return timestamp; }
	public void setTimestamp(Calendar timestamp) { this.timestamp = timestamp; }
	
	public String getSection() {return section;}
	public void setSection(String section) {this.section = section;}
	
	public String getLink() {return link;}
	public void setLink(String link) {this.link = link;}
	
	// Static functions
	public static void delete(int id) {
		Session s = HibernateUtil.getTransactionSession();
		
		Query getNotification = s.createQuery("from Notification where id = :id").setParameter("id", id);
	  	Notification notification = (Notification) getNotification.uniqueResult();	
	  	s.delete(notification); 	
	}
	
	public static void markAsRead(int notification, String user) {
		Session s = HibernateUtil.getTransactionSession();
		
		Query getNotificationUser = s.createQuery("from NotificationUser where notification = :notification and user = :user").setParameter("notification", notification).setParameter("user", user);
	  	NotificationUser notificationUser = (NotificationUser) getNotificationUser.uniqueResult();
	  	notificationUser.setRead(true);
	  	s.update(notificationUser);	
	}
	
	// Notification create
	public static void pushNotificationToUsers(String message, String section, String link, Set<User> users) {
		Session s = HibernateUtil.getTransactionSession();
			
		Notification notification = new Notification(message, section, link);

		for (User u:users) {
			NotificationUser notificationUser = new NotificationUser();
			notificationUser.setNotification(notification);
			notificationUser.setUser(u);
			s.save(notificationUser);
		}
		
	}
		
}
