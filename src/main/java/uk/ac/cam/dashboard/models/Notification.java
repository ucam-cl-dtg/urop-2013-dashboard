package uk.ac.cam.dashboard.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.common.collect.ImmutableMap;

@Entity
@Table(name="NOTIFICATIONS")
public class Notification implements Mappable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="notificationIdSeq") 
	@SequenceGenerator(name="notificationIdSeq",sequenceName="NOTIFICATION_SEQ", allocationSize=1)
	private int id;
	
	@OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval=true)
	private Set<NotificationUser> users = new HashSet<NotificationUser>();
	
	private String message;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar timestamp;
	
	private String section;
	private String link;
	
	@Column(name="foreign_id")
	private String foreignId;

	public static final String[] VALID_SECTIONS = {"dashboard", "signups", "questions", "handins"}; // Shared with GetNotificationForm
	
	public Notification() {
		this.timestamp = Calendar.getInstance();
	}
	
	public Notification(String message, String section, String link) {
		this.message = message;
		this.section = section;
		this.link = link;
		this.timestamp = Calendar.getInstance();
	}
	
	public Notification(String message, String section, String link, String foreignId) {
		this.message = message;
		this.section = section;
		this.link = link;
		this.setForeignId(foreignId);
		this.timestamp = Calendar.getInstance();
	}
	
	public int getId() {return id;}
	public void setId(int id) { this.id = id; }
	
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
	
	public Calendar getTimestamp() { return timestamp; }
	public void setTimestamp(Calendar timestamp) { this.timestamp = timestamp; }
	
	public String getSection() {return section;}
	public void setSection(String section) {this.section = section;}
	
	public String getLink() {return link;}
	public void setLink(String link) {this.link = link;}

	public String getForeignId() {return foreignId;}
	public void setForeignId(String foreignId) {this.foreignId = foreignId;}
	
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		for(NotificationUser u : this.users){
			users.add(u.getUser());
		}
		return users;
	}
	
	@Override
	public ImmutableMap<String, Object> toMap() {
		
		ImmutableMap.Builder<String, Object> map = new ImmutableMap.Builder<String, Object>();
		
		map = map.put("id", this.id);
		map = map.put("message", this.message);
		map = map.put("section", this.section);
		map = map.put("link", this.link);
		map = map.put("timestamp", this.timestamp.getTime().toString());
		
		return map.build();
	}
		
}
