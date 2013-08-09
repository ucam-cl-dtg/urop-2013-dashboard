package uk.ac.cam.dashboard.forms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.FormParam;

import org.hibernate.Session;

import uk.ac.cam.cl.dtg.ldap.LDAPGroup;
import uk.ac.cam.cl.dtg.ldap.LDAPObjectNotFoundException;
import uk.ac.cam.cl.dtg.ldap.LDAPQueryManager;
import uk.ac.cam.dashboard.models.Group;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.util.HibernateUtil;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.googlecode.htmleasy.RedirectException;

public class GroupForm {
	@FormParam("title") String title;
	@FormParam("users") String users;
	@FormParam("import_id") String import_id;
	
	public int handle(User currentUser) {		
		
		Session session = HibernateUtil.getTransactionSession();
		
		// Create group prototype
		Group group = new Group();
		group.setTitle(title);

		// Set owner of the user to current user
		group.setOwner(currentUser);

		
		// Create set of users for group
		Set<User> groupMembers = new HashSet<User>();
		if(!users.equals("")){
			User user;		
			String[] crsids = users.split(",");
			for(int i=0;i<crsids.length;i++){
				// Register user (adds user to database if they don't exist
				user = User.registerUser(crsids[i]);
				// Add to set of users
				groupMembers.add(user);
			}		
		}
		
		group.setUsers(groupMembers);
		
		session.save(group);
		
		// Add this group to the group members groups
		for(User u : groupMembers){
			Set<Group> subscriptions = u.getSubscriptions();
			subscriptions.add(group);
			session.update(u);
		}
		
		return group.getId();
				
	}

	public Group handleUpdate(User currentUser, int id) {		
		
		Session session = HibernateUtil.getTransactionSession();
		
		// Get the group to edit
		Group group = Group.getGroup(id);
	  	
		// Check the owner is current user
		if(!group.getOwner().equals(currentUser)){
			throw new RedirectException("/app/#signapp/groups/2");
		}
		
		// Set new values
		group.setTitle(title);
		
		// Create new set of users for group
		Set<User> groupMembers = new HashSet<User>();
		if(!users.equals("")){
			User user;		
			String[] crsids = users.split(",");
			for(int i=0;i<crsids.length;i++){
				// Register user (adds user to database if they don't exist
				user = User.registerUser(crsids[i]);
				// Add to set of users
				groupMembers.add(user);
			}		
		}
		
		group.setUsers(groupMembers);
		
		session.update(group);
		
		return group;
				
	}
	
	public Group handleImport(User currentUser) {	
		

		
		Session session = HibernateUtil.getTransactionSession();
		
		// Get group info from LDAP
		LDAPGroup g = null;
		try {
			g = LDAPQueryManager.getGroup(import_id);
		} catch (LDAPObjectNotFoundException e) {
			// handle error
		}
		String name = g.getName();
		List<String> members = g.getUsers();
		
		// If group has no members, throw new redirect exception
		if(members==null){
			//handle error
			return null;
		}		
		
		// If group is larger than 50 members, throw new redirect exception
		if(members.size()>50){
			//handle error
			throw new RedirectException("/app/#signapp/groups/error/5");
		}
		
		// Create group prototype
		Group group = new Group();
		group.setTitle(name);

		// Set owner of the user to current user
		group.setOwner(currentUser);
		
		// Create set of users for group
		User user;
		Set<User> groupMembers = new HashSet<User>();
		for(String m : members){
			// Register user (adds user to database if they don't exist
			user = User.registerUser(m);
			// Add to set of users
			groupMembers.add(user);
		}	
		
		group.setUsers(groupMembers);
		
		session.save(group);
		
		// Add this group to the group members groups
		for(User u : groupMembers){
			Set<Group> subscriptions = u.getSubscriptions();
			subscriptions.add(group);
			session.update(u);
		}
		
		return group;
				
	}

	public ArrayListMultimap<String, String> validate() {
		ArrayListMultimap<String, String> errors = ArrayListMultimap.create();

		// title
		if (title.equals("") || title == null){
			errors.put("title", "Please give your group a name");
		} else if(title.length()>255){
			errors.put("title", "Name cannot be longer than 255 characters");
		}
		
		// users
		if((users==null||users.equals(""))){ 
			errors.put("users", "You must add at least one user to this group"); 
		}	
		
		return errors;	
	}
	
	public ArrayListMultimap<String, String> validateImport() {
		ArrayListMultimap<String, String> errors = ArrayListMultimap.create();
		
		// users
		if((import_id==null||import_id.equals(""))){ 
			errors.put("import_id", "Please choose at least one group to import"); 
		}	
		
		return errors;	
	}
	
	public ImmutableMap<String, ?> toMap(int id) {
		ImmutableMap.Builder<String, Object> builder = new ImmutableMap.Builder<String, Object>();
		builder.put("id", id);
		if(!(title==null||title.equals(""))){
			builder.put("name", title);
		}
		if(!(users==null||users.equals(""))){
			List<ImmutableMap<String,String>> userMaps = new ArrayList<ImmutableMap<String,String>>();
			String[] crsids = users.split(",");
			for(String s: crsids){
				User user = User.registerUser(s);
				userMaps.add(ImmutableMap.of("crsid", user.getCrsid(), "name", user.getName()));
			}
			builder.put("users", userMaps);
		} else {
			builder.put("users", "");
		}
		
		return builder.build();
	}
	
}
