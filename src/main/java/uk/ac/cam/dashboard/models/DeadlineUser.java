package uk.ac.cam.dashboard.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import uk.ac.cam.dashboard.util.HibernateUtil;

import com.google.common.collect.ImmutableMap;

@Entity
@Table(name="DEADLINES_USERS")
public class DeadlineUser implements Mappable{
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private int id;
	
	@ManyToOne
	@JoinColumn(name="user_crsid")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="deadline_id")
	private Deadline deadline;
	
	private boolean complete;
	
	private boolean archived;
	
	public DeadlineUser() {}
	public DeadlineUser(User u, Deadline d) {
		this.user = u;
		this.deadline = d;
		this.complete = false;
		this.archived = false;
	}
	
	public int getId() {return id;}
	
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	
	public Deadline getDeadline() { return deadline; }
	public void setDeadline(Deadline deadline) { this.deadline = deadline; }
	
	public boolean getComplete() { return complete; }
	public void setComplete(boolean complete) { this.complete = complete; }
	
	public boolean getArchived() { return archived; }
	public void setArchived(boolean archived) { this.archived = archived; }
	
	public void toggleComplete(boolean complete){
		this.setComplete(complete);
		
		HibernateUtil.getTransactionSession().update(this);
	}
	
	public void toggleArchived(boolean archived){
		this.setArchived(archived);
		
		HibernateUtil.getTransactionSession().update(this);
	}
	
	@Override
	public ImmutableMap<String, ?> toMap() {
		
		ImmutableMap.Builder<String, Object> map = new ImmutableMap.Builder<String, Object>();
		
		map.put("id", this.id);
		map.put("complete", this.complete);
		map.put("archived", this.archived);
		map.put("user", this.user.getCrsid());
		map.put("deadline_id", this.deadline.getId());
		map.put("name", this.deadline.getTitle());
		map.put("message", this.deadline.getMessage());
		map.put("url", this.deadline.getURL());
		map.put("owner", this.deadline.getOwner().toMap());
		map.put("datetime", this.deadline.getDateMap());
		
		return map.build();
	}

	
}
