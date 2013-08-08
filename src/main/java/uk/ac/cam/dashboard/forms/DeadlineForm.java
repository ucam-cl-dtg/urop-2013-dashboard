package uk.ac.cam.dashboard.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.FormParam;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.models.Deadline;
import uk.ac.cam.dashboard.models.DeadlineUser;
import uk.ac.cam.dashboard.models.Group;
import uk.ac.cam.dashboard.models.Notification;
import uk.ac.cam.dashboard.models.NotificationUser;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.queries.DeadlineQuery;
import uk.ac.cam.dashboard.util.HibernateUtil;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;

public class DeadlineForm {
	@FormParam("title") String title;
	@FormParam("date") String date;
	@FormParam("hour") String hour;
	@FormParam("minute") String minute;
	@FormParam("message") String message;
	@FormParam("url") String url;
	@FormParam("users") String users;
	@FormParam("groups") String groups;
	
	//Logger
	private static Logger log = LoggerFactory.getLogger(DeadlineForm.class);
	
	public int handleCreate(User currentUser) {		
		
		Session session = HibernateUtil.getTransactionSession();
		
		// Create deadline 
		Deadline deadline = new Deadline();
		
		// Set mandatory fields
		deadline.setTitle(title);
		deadline.setOwner(currentUser);
		
		// Format and set date
		String datetime = date+ " " + hour + ":" + minute;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		try {
			cal.setTime(sdf.parse(datetime));
		} catch (Exception e) {
			log.error("e.getMessage()" +  ": error parsing date");
			cal = Calendar.getInstance();
		}
		deadline.setDatetime(cal);
		
		// Set optional fields
		if(!(message.equals("")||message==null)){
			deadline.setMessage(message);
		} else {
			deadline.setMessage("No message");
		}
		if(!(message.equals("")||message==null)){
			deadline.setURL(url);
		} else {
			deadline.setURL("#");
		}
		
		session.save(deadline);
		
		// Add users from users field
		List<DeadlineUser> dUsers = new ArrayList<DeadlineUser>();
		if(!users.equals("")){
			User user;
			DeadlineUser dUser;
			String[] crsids = users.split(",");
			for(String u : crsids){
				user = User.registerUser(u);
				dUser = new DeadlineUser(user, deadline);
				dUsers.add(dUser);
				session.save(dUser);
			}		
		}
		
		// Add users from groups field
		if(!groups.equals("")){
			Set<User> groupUsers;
			DeadlineUser dUser;
			String[] groupIds = groups.split(",");
			for(String g : groupIds){
				// Get group users
				groupUsers = Group.getGroup(Integer.parseInt(g)).getUsers();
			  	for(User u : groupUsers){
					dUser = new DeadlineUser(u, deadline);
					dUsers.add(dUser);
					session.save(dUser);
			  	}
			}	
		}
		// Create notification
		Notification n = new Notification();
		n.setMessage(currentUser.getCrsid() + " set you a deadline: " + deadline.getTitle());
		n.setSection("deadlines");
		n.setLink("/#dashboard/deadlines");
		n.setTimestamp(Calendar.getInstance());
		session.save(n);
		// Associate notification with users
		for(DeadlineUser d : dUsers){
			System.out.println("User " + d.getUser().getCrsid());
			session.save(new NotificationUser(d.getUser(), n));
		}
		
		
		return deadline.getId();			
	}
	
	public int handleUpdate(User currentUser, int id) {		
		
		Session session = HibernateUtil.getTransactionSession();
		
		// Get the deadline to edit
		Deadline deadline = DeadlineQuery.get(id);
		
		// Set new mandatory values
		deadline.setTitle(title);
		
		// Format and set date
		String datetime = date+ " " + hour + ":" + minute;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		try {
			cal.setTime(sdf.parse(datetime));
		} catch (Exception e) {
			log.error("e.getMessage()" +  ": error parsing date");
			cal = Calendar.getInstance();
		}
		deadline.setDatetime(cal);

		// Set new optional fields
		if(!(message.equals("")||message==null)){
			deadline.setMessage(message);
		} else {
			deadline.setMessage("No message");
		}
		if(!(message.equals("")||message==null)){
			deadline.setURL(url);
		} else {
			deadline.setURL("#");
		}
		
		// Add users from users field
		Set<DeadlineUser> deadlineUsers = new HashSet<DeadlineUser>();
		if(!users.equals("")){
			User user;
			String[] crsids = users.split(",");
			for(String u : crsids){
				user = User.registerUser(u);
				deadlineUsers.add(new DeadlineUser(user, deadline));
			}		
		}	
		// Add users from groups field
		if(!groups.equals("")){
			Set<User> groupUsers;
			String[] groupIds = groups.split(",");
			
			for(String g : groupIds){
				// Get group users
				groupUsers = Group.getGroup(Integer.parseInt(g)).getUsers();
			  	for(User u : groupUsers){
					deadlineUsers.add(new DeadlineUser(u, deadline));
			  	}
			}	
		}
		
		// Clear old users
		deadline.clearUsers();
		// Set new users
		deadline.setUsers(deadlineUsers);
		
		session.update(deadline);
		
		return deadline.getId();	
	}
	
	public ArrayListMultimap<String, String> validate() {
		ArrayListMultimap<String, String> errors = ArrayListMultimap.create();

		
		// title
		if (title.equals("") || title == null){
			errors.put("title", "Please give your deadline a name");
		} else if(title.length()>255){
			errors.put("title", "Name cannot be longer than 255 characters");
		}
		
		// date
		if((date==null||date.equals(""))){ 
			errors.put("date", "Please choose a due date for the deadline"); 
		}	
		//check its not in the past
		String datetime = date;
		datetime += " " + hour + ":" + minute;
		
		Calendar cal = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		try {
			cal.setTime(sdf.parse(datetime));
		} catch (ParseException e){
			log.error("Error parsing datetime string");
			cal = Calendar.getInstance();
		}
		
		if(cal.getTime().before(today.getTime())){
			errors.put("datepast", "Deadline due date cannot be in the past");
		}	
		
		// users/groups
		if((users==null||users.equals(""))&&(groups==null||groups.equals(""))){ 
			errors.put("users", "You must assign at least one user to this deadline"); 
		}	
		
		return errors;	
	}
	
	public ImmutableMap<String, ?> toMap(int id) {
		ImmutableMap.Builder<String, Object> builder = new ImmutableMap.Builder<String, Object>();
		builder.put("id", id);
		builder.put("name", title);
		builder.put("date", ImmutableMap.of("date", date, "hour", hour, "minute", minute));
		builder.put("message", message);
		builder.put("url", url);
		
		if(!users.equals("")){
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
		
		builder.put("groups", groups);
		return builder.build();
	}
	
}
