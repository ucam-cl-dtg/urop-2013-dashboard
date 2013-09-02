package uk.ac.cam.dashboard.models;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.collect.ImmutableMap;

@Entity
public class Settings {

    @Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private int id;
	
	private boolean signupsOptIn = true;
	private boolean questionsOptIn = true;
	private boolean handinsOptIn = true;
	
	private boolean dashboardSendsEmail = false;
	private boolean signupsSendsEmail = false;
	private boolean questionsSendsEmail = false;
	private boolean handinsSendsEmail = false;
	
	private boolean isSupervisor = false;
	private boolean isDOS = false;
	
	@OneToOne (mappedBy="settings")
	@JoinColumn(name="USER_CRSID")
	private User user;
	
	public Settings() {}
	
	// Setters and getters
	public boolean isSignupsOptIn() {return signupsOptIn;}
	public void setSignupsOptIn(boolean signupsOptIn) {this.signupsOptIn = signupsOptIn;}
	
	public boolean isQuestionsOptIn() {return questionsOptIn;}
	public void setQuestionsOptIn(boolean questionsOptIn) {this.questionsOptIn = questionsOptIn;}
	
	public boolean isHandinsOptIn() {return handinsOptIn;}
	public void setHandinsOptIn(boolean handinsOptIn) {this.handinsOptIn = handinsOptIn;}

	public boolean getDashboardSendsEmail(){return this.dashboardSendsEmail;}
	public void setDashboardSendsEmail(boolean sendsEmail){this.dashboardSendsEmail = sendsEmail;}
	public boolean getSignupsSendsEmail(){return this.signupsSendsEmail;}
	public void setSignupsSendsEmail(boolean sendsEmail){this.signupsSendsEmail = sendsEmail;}
	public boolean getQuestionsSendsEmail(){return this.questionsSendsEmail;}
	public void setQuestionsSendsEmail(boolean sendsEmail){this.questionsSendsEmail = sendsEmail;}
	public boolean getHandinsSendsEmail(){return this.handinsSendsEmail;}
	public void setHandinsSendsEmail(boolean sendsEmail){this.handinsSendsEmail = sendsEmail;}
	
	public boolean getSupervisor(){return this.isSupervisor;}
	public void setSupervisor(boolean supervisor){this.isSupervisor = supervisor;}
	public boolean getDOS(){return this.isDOS;}
	public void setDOS(boolean DOS){this.isDOS = DOS;}
	
	public boolean filterMail(String type){
		if(type.equals("dashboard")){
			return dashboardSendsEmail;
		} else if(type.equals("signapp")){
			return signupsSendsEmail;
		} else if(type.equals("questions")){
			return questionsSendsEmail;
		} else if(type.equals("handins")){
			return handinsSendsEmail;
		} else {
			return false;
		}
	}
	
	public Map<String, Object> toMap() {
		ImmutableMap<String, Object> map = new ImmutableMap.Builder<String, Object>()
				.put("signups", signupsOptIn)
				.put("questions", questionsOptIn)
				.put("handins", handinsOptIn)
				.put("emails", ImmutableMap.of("dashboard", dashboardSendsEmail, 
												"signups", signupsSendsEmail,
												"questions", questionsSendsEmail,
												"handins", handinsSendsEmail))
				.put("supervisor", isSupervisor)
				.put("dos", isDOS)
				.build();
		return map;
	}

}
