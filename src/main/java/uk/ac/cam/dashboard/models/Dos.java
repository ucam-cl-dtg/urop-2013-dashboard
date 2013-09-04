package uk.ac.cam.dashboard.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="DOS")
public class Dos {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="dosIdSeq") 
	@SequenceGenerator(name="dosIdSeq",sequenceName="DOS_SEQ", allocationSize=1)
	private int id;

	private String crsid;
	
	private String instID;
	
	public Dos() {}
	
	public String getCrsid() {return crsid;}
	
	public String getInstID() {return instID;}
	public void setInstID(String instID){
		this.instID = instID;
	}
}
