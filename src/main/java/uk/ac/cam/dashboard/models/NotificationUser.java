package uk.ac.cam.dashboard.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.annotations.GenericGenerator;

import uk.ac.cam.dashboard.queries.NotificationQuery;
import uk.ac.cam.dashboard.util.HibernateUtil;

import com.google.common.collect.ImmutableMap;

@Entity
@Table(name="NOTIFICATIONS_USERS")
public class NotificationUser implements Mappable{
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private int id;
	
	@ManyToOne
	@JoinColumn(name="user_crsid")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="notification_id")
	private Notification notification;
	
	private boolean read;
	
	public NotificationUser() {}
	public NotificationUser(User u, Notification n) {
		this.user = u;
		this.notification = n;
		this.read = false;
	}
	
	public int getId() {return id;}
	
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	
	public Notification getNotification() { return notification; }
	public void setNotification(Notification notification) { this.notification = notification; }
	
	public boolean getRead() { return read; }
	public void setRead(boolean read) { this.read = read; }
	
	public static boolean markAsReadUnread(User user, int notificationId, boolean read) {
		
		NotificationUser nu = NotificationQuery.getNU(notificationId);
		
		Session session = HibernateUtil.getTransactionSession();
		nu.setRead(read);
		session.update(nu);
		
		return nu.getRead();
	}
	
	@Override
	public ImmutableMap<String, ?> toMap() {
		
		ImmutableMap.Builder<String, Object> map = new ImmutableMap.Builder<String, Object>();
		
		map.put("id", this.id);
		map.put("read", this.read);
		map.put("user", this.user.getCrsid());
		map.put("notification_id", this.notification.getId());
		map.put("message", this.notification.getMessage());
		map.put("section", this.notification.getSection());
		map.put("link", this.notification.getLink());
		map.put("timestamp", this.notification.getTimestamp().getTime().toString());
		
		return map.build();
	}

}
