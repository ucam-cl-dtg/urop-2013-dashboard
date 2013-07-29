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

import org.hibernate.annotations.GenericGenerator;

import uk.ac.cam.dashboard.models.User;

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
	
	public Notification() {}
	public Notification(String message) {
		this.message = message;
		this.timestamp = Calendar.getInstance();
	}

	public Map<String,?> toMap() {
		return ImmutableMap.of("id", id, "message", message, "timestamp", timestamp.getTime().toString(), "users", users);
	}
	
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	public void addUser(User user) {
		this.users.add(user);
	}
	
	// Should not be needed, but included for posterity
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
	
	public Calendar getTimestamp() { return timestamp; }
	public void setTimestamp(Calendar timestamp) { this.timestamp = timestamp; }
	
	
}
