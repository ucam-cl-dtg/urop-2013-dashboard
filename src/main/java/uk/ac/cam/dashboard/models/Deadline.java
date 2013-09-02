package uk.ac.cam.dashboard.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.collect.ImmutableMap;

@Entity
@Table(name="DEADLINES")
public class Deadline implements Mappable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="deadlineIdSeq") 
	@SequenceGenerator(name="deadlineIdSeq",sequenceName="DEADLINE_SEQ", allocationSize=1)
	private int id;

	private String title;
	private String message;
	private String url;

	private Calendar datetime;

	@OneToMany(mappedBy = "deadline", cascade = CascadeType.ALL, orphanRemoval=true)
	private Set<DeadlineUser> users = new HashSet<DeadlineUser>();
	
	@ManyToOne
	@JoinColumn(name="USER_CRSID")
	private User owner;
	
	public Deadline() {}
	public Deadline(User owner, String title) {
		this.owner = owner;
		this.title = title;
	}
	
	public int getId() { return this.id; }
	public void setId(int id) { this.id = id; }
	
	public String getTitle() { return this.title; }
	public void setTitle(String title) { this.title = title; }

	public String getMessage() { return this.message; }
	public void setMessage(String message) { this.message= message; }
	
	public String getURL() { return this.url; }
	public void setURL(String url) { this.url= url; }
	
	public Calendar getDatetime() { return this.datetime; }
	public void setDatetime(Calendar datetime) { this.datetime= datetime; }
	public String getFormattedDate() { 
		SimpleDateFormat df = new SimpleDateFormat("EEEEE, dd MMMMM yyyy");
		SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
		return df.format(datetime.getTime()) + " at " + tf.format(datetime.getTime());
	}
	
	public User getOwner() { return this.owner; }
	public void setOwner(User owner) { this.owner= owner; }
	
	public Set<DeadlineUser> getUsers() { return this.users; }
	public void clearUsers() { users.clear(); }
	public void setUsers(Set<DeadlineUser> users) { this.users.addAll(users); }
	
	public boolean getImminence() {
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DAY_OF_YEAR,1);
		return (this.datetime.before(tomorrow));
	}
	
	public int getCompletion() {
		int total = 0;
		for(DeadlineUser u : users){
			if(u.getComplete()){ total++;}
		}	
		if(users.size()!=0){ return (total*100)/users.size(); }
		else { return 100; }
	}
	
	public void archiveAll(){
		for(DeadlineUser u : users){
			u.setArchived(true);
		}
	}
	
	// toMap
	public Map<String, ?> dateToMap() { 
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
		SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
		String dateString = dateFormat.format(datetime.getTime());
		String hourString = hourFormat.format(datetime.getTime());
		String minuteString = minuteFormat.format(datetime.getTime());	
		return ImmutableMap.of("date", dateString, "hour", hourString, "minute", minuteString);
	}
	
	public List<ImmutableMap<String, ?>> usersToMap() {
		List<ImmutableMap<String, ?>> deadlineUsers = new ArrayList<ImmutableMap<String, ?>>();
		for(DeadlineUser u : users){
			deadlineUsers.add(u.userToMap());
		}
		return deadlineUsers;
	}
	
	@Override
	public Map<String, ?> toMap() {
		
		ImmutableMap.Builder<String, Object> map = new ImmutableMap.Builder<String, Object>();
		
			map.put("id", id);
			map.put("name", title);
			map.put("message", message);
			map.put("url", url);
			map.put("owner", owner.toMap());
			map.put("datetime", getFormattedDate());
			map.put("date", dateToMap());
			map.put("users", usersToMap());
			map.put("pComplete", getCompletion());

			return map.build();		
	}
}