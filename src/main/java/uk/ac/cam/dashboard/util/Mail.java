package uk.ac.cam.dashboard.util;

import java.util.List;
import java.util.Properties;

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


	
}
