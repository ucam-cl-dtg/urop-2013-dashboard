package uk.ac.cam.dashboard.models;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
	
	@ManyToMany
	private Set<User> users = new HashSet<User>();
	
	private String message;
	private Calendar timestamp;
	private String section;
	private String link;
	private boolean read;
	
	public Notification() {}
	public Notification(String message, String section, String link) {
		this.message = message;
		this.section = section;
		this.link = link;
		this.timestamp = Calendar.getInstance();
		this.read = false;
	}

	public Map<String,?> toMap() {
		ImmutableMap.Builder<String, Object> map = new ImmutableMap.Builder<String, Object>();
		
		map = map.put("id", this.id);
		map = map.put("message", this.message);
		map = map.put("section", this.section);
		map = map.put("link", this.link);
		map = map.put("timestamp", this.timestamp.getTime().toString());
		map = map.put("read", this.read);
		map = map.put("users", this.usersToSet());
		
		ImmutableMap<String, ?> finalMap = map.build();
		return finalMap; 
	}
	
	public Set<User> getUsers() {return users;}
	public void setUsers(Set<User> users) {this.users = users;}
	public void addUser(User user) {this.users.add(user);}
	public Set<String> usersToSet() {
		HashSet<String> userCrsids = new HashSet<String>();
		for (User u:this.users) {
			userCrsids.add(u.getCrsid());
		}
		return userCrsids;
	}
	
	public boolean isRead() {return read;}
	public void setRead(boolean read) {this.read = read;}
	
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
	
	public static void markAsRead(int id) {
		Session s = HibernateUtil.getTransactionSession();
		
		Query getNotification = s.createQuery("from Notification where id = :id").setParameter("id", id);
	  	Notification notification = (Notification) getNotification.uniqueResult();
	  	notification.setRead(true);
	  	s.update(notification);	
	}
	
	// Notification create
	public static void pushNotificationToUsers(String message, String section, String link, Set<User> users) {
		Session s = HibernateUtil.getTransactionSession();
			
		Notification notification = new Notification(message, section, link);
		
		for (User u:users) {
			notification.addUser(u);	
		}
		
		s.save(notification);
	}
		
}
