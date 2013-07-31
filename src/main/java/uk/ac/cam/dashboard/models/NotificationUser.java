package uk.ac.cam.dashboard.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="NOTIFICATIONS_USERS")
public class NotificationUser {
	
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
	public void addNotification(Notification notification) { this.notification = notification; }
		
}
