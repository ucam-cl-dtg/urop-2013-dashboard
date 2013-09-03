package uk.ac.cam.dashboard.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DOS")
public class Dos {

	@Id
	private String crsid;
	
	private String instID;
	
	public Dos() {}
	
	public String getCrsid() {return crsid;}
	
	public String getInstID() {return instID;}
	public void setInstID(String instID){
		this.instID = instID;
	}
}
