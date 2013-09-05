package uk.ac.cam.dashboard.forms;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.cl.dtg.ldap.LDAPObjectNotFoundException;
import uk.ac.cam.cl.dtg.ldap.LDAPQueryManager;
import uk.ac.cam.cl.dtg.ldap.LDAPUser;
import uk.ac.cam.cl.dtg.teaching.api.HandinsApi;
import uk.ac.cam.dashboard.models.Deadline;
import uk.ac.cam.dashboard.models.DeadlineUser;
import uk.ac.cam.dashboard.models.Group;
import uk.ac.cam.dashboard.models.Notification;
import uk.ac.cam.dashboard.models.NotificationUser;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.queries.DeadlineQuery;
import uk.ac.cam.dashboard.queries.GroupQuery;
import uk.ac.cam.dashboard.util.HibernateUtil;
import uk.ac.cam.dashboard.util.Mail;
import uk.ac.cam.dashboard.util.Strings;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;

public class CreateDeadlineForm {
	@FormParam("title") String title;
	@FormParam("date") String date;
	@FormParam("hour") String hour;
	@FormParam("minute") String minute;
	@FormParam("message") String message;
	@FormParam("url") String url;
	@FormParam("users") String users;
	@FormParam("groups") String groups;
	@FormParam("send-email") String sendMail;
	
    @Context HttpServletRequest request;
	//Logger
	private static Logger log = LoggerFactory.getLogger(CreateDeadlineForm.class);
	
	public int handleCreate(User currentUser) {		
		
		Session session = HibernateUtil.getTransactionSession();
		
		Deadline deadline = new Deadline(currentUser, title);
		
		deadline.setDatetime(parseDate());
		
		deadline.setMessage(message);
		deadline.setURL(url);
		
		session.save(deadline);
		
		Set<User> userSet = createUserSet();
		Set<DeadlineUser> deadlineUsers = saveDeadlineUsers(userSet, deadline);
			
		Set<Group> groupList = new HashSet<Group>();
		for(String s : parseGroups()){
			groupList.add(GroupQuery.get(Integer.parseInt(s)));
		}
		
		for(Group g : groupList){
			Set<User> groupUsers = g.getUsers();
			deadlineUsers.addAll(saveDeadlineUsers(groupUsers, deadline));
		}
		
        // Create a bin for the deadline
        createBin(currentUser, deadlineUsers, title, url);
		// send notification
		Notification notification = new Notification(); 
		notification.setMessage(currentUser.getName() + " ("+currentUser.getCrsid()+")" +Strings.NOTIFICATION_SETDEADLINE + deadline.getTitle());
		notification.setSection("dashboard");
		notification.setLink("deadlines/");
		session.save(notification);
		for(DeadlineUser du : deadlineUsers){
			NotificationUser nu = new NotificationUser();
			nu.setUser(du.getUser());
			nu.setNotification(notification);
			session.save(nu);
		}
		
		// send email
		if(sendMail.equals("true")){
				Mail.setDeadline(currentUser, deadline, deadlineUsers);
		}
		
		boolean MailSent = Mail.sendNotificationEmail(notification.getMessage(), userSet, "dashboard");
		return deadline.getId();			
	}
    private Long extractSetId(String url) {
        // Assume url is the form http://<host>/questions/sets/{id}
        URL setURL = null;
        try {
            setURL = new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
        // Force the link host to be as the same as the server
        // This is to prevent the global key from leaking
        if (!request.getServerName().equals(setURL.getHost()))
            return null;

        // This should be now something like /questions/sets/1
        String path = setURL.getPath();

        Pattern p = Pattern.compile("^/questions/sets/([0-9]+).*$");
        Matcher m = p.matcher(path);

        if (m.find()) {
            String strId = m.group(1);
            try {
                return Long.parseLong(strId);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private void createBin(User owner, Set<DeadlineUser> students, String title, String url) {
        String apiKey = request.getSession().getServletContext().getInitParameter("apiKey");
        String handinsUrl = request.getSession().getServletContext().getInitParameter("handinsUrl");
        
        if (apiKey == null || handinsUrl == null)
            return ;
        
        HandinsApi.HandinsApiWrapper api = new HandinsApi.HandinsApiWrapper(handinsUrl, apiKey);

        String[] users = new String[students.size()];

        int index = 0;
        for (DeadlineUser student: students) {
            users[index] = student.getUser().getCrsid();
            index ++;
        }

        Long setId = extractSetId(url);

        HandinsApi.Bin bin = api.createBin(title, owner.getCrsid());

        if (bin == null )
            return ;

        api.setUsers(bin, users);
        if (setId != null) {
            api.importQuestionSet(bin, setId, owner.getCrsid());
        }
    }
    
	public Deadline handleUpdate(User currentUser, int id) {		
		
		Session session = HibernateUtil.getTransactionSession();
		
		Deadline deadline = DeadlineQuery.get(id);
		
		deadline.setTitle(title);
		
		deadline.setDatetime(parseDate());

		deadline.setMessage(message);
		deadline.setURL(url);
		
		Set<User> userSet = createUserSet();
		Set<DeadlineUser> deadlineUsers = saveDeadlineUsers(userSet, deadline);
		
		Set<Group> groupList = new HashSet<Group>();
		for(String s : parseGroups()){
			groupList.add(GroupQuery.get(Integer.parseInt(s)));
		}
		
		for(Group g : groupList){
			Set<User> groupUsers = g.getUsers();
			deadlineUsers.addAll(saveDeadlineUsers(groupUsers, deadline));
		}
		
		deadline.clearUsers();
		deadline.setUsers(deadlineUsers);
		
		session.update(deadline);
		
		Notification notification = new Notification(); 
		notification.setMessage(currentUser.getName() + " ("+currentUser.getCrsid()+")" +Strings.NOTIFICATION_UPDATEDEADLINE + deadline.getTitle());
		notification.setSection("dashboard");
		notification.setLink("deadlines/");
		session.save(notification);
		for(DeadlineUser du : deadlineUsers){
			NotificationUser nu = new NotificationUser();
			nu.setUser(du.getUser());
			nu.setNotification(notification);
			session.save(nu);
		}
		
		Mail.sendNotificationEmail(notification.getMessage(), userSet, "dashboard");
		
		return deadline;	
	}
	
	public ArrayListMultimap<String, String> validate() {
		ArrayListMultimap<String, String> errors = ArrayListMultimap.create();

		// title
		if (title.equals("") || title == null){
			errors.put("title", Strings.DEADLINE_NO_TITLE);
		} else if(title.length()>255){
			errors.put("title", Strings.DEADLINE_TITLE_LENGTH);
		}
		
		// date
		if((date==null||date.equals(""))){ 
			errors.put("date", Strings.DEADLINE_NO_DATE); 
		}	

		Calendar cal = parseDate();
		Calendar today = Calendar.getInstance();
		
		if(cal.getTime().before(today.getTime())){
			errors.put("date", Strings.DEADLINE_DATE_PASSED);
		}	
		
		// message (optional)
		if((message.equals("")||message==null)){
			message = Strings.DEADLINE_NOMESSAGE;
		} 
		
		// url (optional)
		if((url.equals("")||url==null)){
			url = Strings.DEADLINE_NOURL;
		} 
		
		// users/groups
		if((users==null||users.equals(""))&&(groups==null||groups.equals(""))){ 
			errors.put("users", Strings.DEADLINE_NO_USERS); 
		}
		
		// send mail
		if(sendMail==null||sendMail.equals("")){ 
			sendMail="false";
		}	

		return errors;	
	}
	
	public ImmutableMap<String, ?> toMap(int id) {
		ImmutableMap.Builder<String, Object> map = new ImmutableMap.Builder<String, Object>();
		map.put("id", id);
		map.put("name", title);
		map.put("date", ImmutableMap.of("date", date, "hour", hour, "minute", minute));
		map.put("message", message);
		map.put("url", url);
		map.put("users", usersToMap(parseUsers()));
		map.put("groups", groups);
		return map.build();
	}
	
	public Calendar parseDate(){
		
		if(date==""||date==null){
			return Calendar.getInstance();
		}
		
		String datetime = date+ " " + hour + ":" + minute;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		
		
		try {
			cal.setTime(sdf.parse(datetime));
		} catch (ParseException e) {
			log.error("e.getMessage()" +  ": error parsing date");
			cal = Calendar.getInstance();
		}
		
		return cal;
	}
	
	public List<ImmutableMap<String, String>> usersToMap(String[] crsids) {
		List<ImmutableMap<String, String>> users = new ArrayList<ImmutableMap<String, String>>();

		for(String c : crsids){
			try {
				LDAPUser u = LDAPQueryManager.getUser(c);
				users.add(ImmutableMap.of("crsid", c, "name", u.getDisplayName()));
			} catch (LDAPObjectNotFoundException e) {
				users.add(ImmutableMap.of("crsid", c, "name", Strings.USER_NOUSERNAME));				
			}
		}
		
		return users;
	}
	
	public String[] parseUsers(){
		if(!(users==null||users.equals(""))){ return users.split(","); }
		else { return new String[0]; }
	}
	public String[] parseGroups(){
		if(!(groups==null||groups.equals(""))){ return groups.split(","); }
		else { return new String[0]; }
	}
	
	public Set<User> createUserSet(){

		Set<User> userSet = new HashSet<User>();
		for(String c : parseUsers()){
			User user = User.registerUser(c);
			if(user!=null){
				userSet.add(user);
			}
		}
		return userSet;
	}
	
	public Set<DeadlineUser> saveDeadlineUsers(Set<User> userSet, Deadline deadline){
		Session session = HibernateUtil.getTransactionSession();

		Set<DeadlineUser> deadlineUsers = new HashSet<DeadlineUser>();
		for(User u : userSet){
			if(u!=null) { 
				DeadlineUser d = new DeadlineUser(u, deadline);
				session.save(d);
				deadlineUsers.add(d);
			}
		}
		return deadlineUsers;
	}
	

	
}
