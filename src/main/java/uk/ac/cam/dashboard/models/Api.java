package uk.ac.cam.dashboard.models;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "API")
public class Api {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private int id;
	
	@ManyToOne
	private User user;
	
	private String key;
	
	public Api() {
		String generatedKey = this.generateNewApiKey();
		this.setKey(generatedKey);
	}
	
	private String generateNewApiKey() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(200, random).toString(32);
	}

	public String getKey() { return key; }
	public void setKey(String key) { this.key = key; }
	
}
