package uk.ac.cam.dashboard.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.ws.rs.FormParam;

import com.google.common.collect.ImmutableMap;

@Entity
@Table(name="GROUPS")
public class Group implements Mappable {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="groupIdSeq") 
	@SequenceGenerator(name="groupIdSeq",sequenceName="GROUP_SEQ", allocationSize=1)
	private int id;

	@FormParam("title") private String title;

	@ManyToMany
	@JoinTable(name="GROUPS_USERS",
						joinColumns = {@JoinColumn(name = "GROUP_ID")},
						inverseJoinColumns = {@JoinColumn(name = "USER_CRSID")})
	private Set<User> users = new HashSet<User>(0);
	
	@ManyToOne
	@JoinColumn(name="USER_CRSID")
	private User owner;
	
	public Group() { }
	
	public Group(User owner, String title) {
		this.title = title;
		this.owner = owner;
	}
	
	public int getId() { return this.id; }
	public void setId(int id) { this.id = id; }
	
	public String getTitle() { return this.title; }
	public void setTitle(String title) { this.title = title; }
	
	public Set<User> getUsers() { return this.users; }
	public void setUsers(Set<User> users) { this.users = users; }
	
	public User getOwner() { return this.owner; }
	public void setOwner(User owner) { this.owner = owner; }
	
	public List<ImmutableMap<String, ?>> usersToMap() {
		List<ImmutableMap<String, ?>> groupMembers = new ArrayList<ImmutableMap<String, ?>>();
		for(User u : users){
			groupMembers.add(u.toMap());
		}
		return groupMembers;
	}
	
	@Override
	public Map<String, ?> toMap() {
		
		ImmutableMap.Builder<String, Object> map = new ImmutableMap.Builder<String, Object>();
		
		map.put("id",id);
		map.put("name",title);
		map.put("owner",owner.toMap());
		
		return map.build();	
	} 
		
}
