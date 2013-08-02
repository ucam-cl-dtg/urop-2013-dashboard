package uk.ac.cam.dashboard.forms;

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

import com.googlecode.htmleasy.RedirectException;

public class DeadlineForm {
	@FormParam("title") String title;
	@FormParam("date") String date;
	@FormParam("hour") String hour;
	@FormParam("minute") String minute;
	@FormParam("message") String message;
	@FormParam("url") String url;
	@FormParam("users[]") String users;
	@FormParam("groups[]") String groups;
	
	//Logger
	private static Logger log = LoggerFactory.getLogger(DeadlineForm.class);
	
	public int handleCreate(User currentUser) {		
		
		parseForm();
		
		Session session = HibernateUtil.getTransactionSession();
		
		// Create deadline prototype
		Deadline deadline = new Deadline();
		deadline.setTitle(title);
		deadline.setOwner(currentUser);
		deadline.setMessage(message);
		deadline.setURL(url);
		
		// Format and set date
		String datetime = date;
		datetime += " " + hour + ":" + minute;
		
		Calendar cal = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		try {
			cal.setTime(sdf.parse(datetime));
		} catch (Exception e) {
			log.error("e.getMessage()" +  ": error parsing date");
		}
		
		//check its not in the past
		if(cal.getTime().before(today.getTime())){
			throw new RedirectException("/app/#signapp/deadlines/error/4");
		}
		
		deadline.setDatetime(cal);
		
		session.save(deadline);
		
		List<DeadlineUser> dUsers = new ArrayList<DeadlineUser>();
		
		// Create deadlineUser objects from users field
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
			String[] groupIds = groups.split(",");
			
			DeadlineUser dUser;
			
			for(String g : groupIds){
				// Get group users
				groupUsers = Group.getGroup(Integer.parseInt(g)).getUsers();
			  	for(User user : groupUsers){
					dUser = new DeadlineUser(user, deadline);
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
	
	public Deadline handleUpdate(User currentUser, int id) {		
		
		parseForm();
		
		Session session = HibernateUtil.getTransactionSession();
		
		// Get the deadline to edit
		Deadline deadline = DeadlineQuery.get(id);
	  	
		// Check the owner is current user
		if(!deadline.getOwner().equals(currentUser)){
			throw new RedirectException("/app/#dashboard/deadlines");
		}
		
		// Set new values
		deadline.setTitle(title);
		deadline.setMessage(message);	
		deadline.setURL(url);	
		
		// Format and set date
		String datetime = date;
		if(date!=null||date!=""){
			datetime += " " + hour + ":" + minute;
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			try {
				cal.setTime(sdf.parse(datetime));
			} catch (Exception e) {
				log.error("e.getMessage()" +  ": error parsing date");
			}
			deadline.setDatetime(cal);
		} else {
			deadline.setDatetime(Calendar.getInstance());
		}

		
		// Current deadline users
		Set<DeadlineUser> deadlineUsers = new HashSet<DeadlineUser>();
		
		// Create deadlineUser objects from users field
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
			  	for(User user : groupUsers){
					deadlineUsers.add(new DeadlineUser(user, deadline));
			  	}
			}	
		}
		
		deadline.clearUsers();
		deadline.setUsers(deadlineUsers);
		
		for(DeadlineUser d: deadline.getUsers()){
			System.out.println(d.getUser().getCrsid());
		}
		
		session.update(deadline);
		
		return deadline;	
	}
	
	public void parseForm() {
				
		// Check for empty fields
		if(title==null||title.equals("")){ this.title = "Untitled Deadline"; }
		if(hour==null||hour.equals("")){ this.hour = "0"; }
		if(minute==null||minute.equals("")){ this.minute = "0"; }
		if(message==null||message.equals("")){ this.message = "No message"; }		
		if(url==null||url.equals("")){ this.url = "none"; }	
		if(users==null||users.equals("")){ this.users = ""; }		
		if(groups==null||groups.equals("")){ this.groups = ""; }	
				
	}
	
}
