package uk.ac.cam.dashboard.util;

import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import uk.ac.cam.cl.dtg.ldap.LDAPObjectNotFoundException;
import uk.ac.cam.cl.dtg.ldap.LDAPQueryManager;
import uk.ac.cam.cl.dtg.ldap.LDAPUser;
import uk.ac.cam.dashboard.models.Deadline;
import uk.ac.cam.dashboard.models.DeadlineUser;
import uk.ac.cam.dashboard.models.User;

public class Mail {

	private final static String SMTP_ADDRESS = "smtp.gmail.com";
	private final static String SMTP_PORT = "587";
	
	public static void sendMail(String[] recipients, String from, String contents, String subject){
		
		final String username = "uropdashboardapp@gmail.com";
		final String password = "XxxYyyZzz99";
		
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		 };
		
		Properties p = new Properties();
		p.put("mail.smtp.auth", "true");
		p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.host", SMTP_ADDRESS);
        p.put("mail.smtp.port", SMTP_PORT);

        
        Session s = Session.getDefaultInstance(p, auth);
        Message msg = new MimeMessage(s);

        InternetAddress[] sender = new InternetAddress[1];
        InternetAddress[] recievers = new InternetAddress[recipients.length];
        
        try {
        	
            sender[0] = new InternetAddress(from);
            for(int i=0; i<recipients.length; i++){
            	recievers[i] = new InternetAddress(recipients[i]);
            }
        } catch (AddressException e) {
            // TODO return an error
        	System.out.println("Unable to parse to/from");
            e.printStackTrace();
        }
        
        try {
            msg.setFrom(sender[0]);            
            msg.setReplyTo(sender);
            msg.setRecipients(RecipientType.TO, recievers);
            msg.setSubject(subject);
            msg.setText(contents);
 
            Transport.send(msg);
            
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
        	System.out.println("Unable to send");
            e.printStackTrace();
        }
    }

	public static void setDeadline(User currentUser, Deadline deadline, Set<DeadlineUser> deadlineUsers){
		
		String[] recipients = new String[deadlineUsers.size()];
		int i=0;
		for(DeadlineUser du : deadlineUsers){
			String email;
			try{
				LDAPUser u = LDAPQueryManager.getUser(du.getUser().getCrsid());
				email = u.getEmail();
				if(email==null){ email = du.getUser().getCrsid()+"@cam.ac.uk"; }
			} catch (LDAPObjectNotFoundException e){
				email = du.getUser().getCrsid()+"@cam.ac.uk"; 
			}
			recipients[i] = email;
			i++;
		}
		String subject = currentUser.getName() + " ("+currentUser.getCrsid()+")" + Strings.MAIL_SETDEADLINE_SUBJECT;
		String eol = System.getProperty("line.separator"); 
		String body = Strings.MAIL_SETDEADLINE_HEADER + eol +
						"Deadline: " + deadline.getTitle() + eol +
						"Due: " + deadline.getFormattedDate() + eol +
						"Message: " + deadline.getMessage() + eol +
						"http://localhost:8080/dashboard/deadlines/" + eol +
						Strings.MAIL_SETDEADLINE_FOOTER;
				
		String sender;
		try{
			LDAPUser u = LDAPQueryManager.getUser(currentUser.getCrsid());
			sender = u.getEmail();
			if(sender==null){ sender = currentUser.getCrsid()+"@cam.ac.uk"; }
		} catch(LDAPObjectNotFoundException e){
			sender = currentUser.getCrsid()+"@cam.ac.uk";
		}
		
		Mail.sendMail(recipients, sender, body, subject);	
	}
	
}
