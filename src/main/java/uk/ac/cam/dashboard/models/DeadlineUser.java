package uk.ac.cam.dashboard.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import uk.ac.cam.cl.dtg.teaching.hibernate.HibernateUtil;

import com.google.common.collect.ImmutableMap;

@Entity
@Table(name="DEADLINES_USERS")
public class DeadlineUser implements Mappable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="deadlineUserIdSeq") 
	@SequenceGenerator(name="deadlineUserIdSeq",sequenceName="DEADLINE_USERS_SEQ", allocationSize=1)
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
		HibernateUtil.getInstance().getSession().update(this);
	}
	
	public void toggleArchived(boolean archived){
		this.setArchived(archived);
		HibernateUtil.getInstance().getSession().update(this);
	}
	
	public ImmutableMap<String, ?> userToMap() {
		
		ImmutableMap.Builder<String, Object> map = new ImmutableMap.Builder<String, Object>();

		map.put("id", this.id);
		map.put("complete", this.complete);
		map.put("archived", this.archived);
		map.put("crsid", this.user.getCrsid());
		map.put("name", this.user.getName());
		
		return map.build();
	}
	
	@Override
	public ImmutableMap<String, ?> toMap() {
		
		ImmutableMap.Builder<String, Object> map = new ImmutableMap.Builder<String, Object>();
		
		map.put("id", this.id);
		map.put("user", this.user.toMap());
		map.put("deadline_id", this.deadline.getId());
		map.put("name", this.deadline.getTitle());
		map.put("message", this.deadline.getMessage());
		map.put("url", this.deadline.getURL());
		map.put("owner", this.deadline.getOwner().toMap());
		map.put("datetime", this.deadline.getFormattedDate());
		map.put("imminent", this.deadline.getImminence());
		map.put("complete", this.complete);
		map.put("archived", this.archived);
		
		return map.build();
	}
	
}
