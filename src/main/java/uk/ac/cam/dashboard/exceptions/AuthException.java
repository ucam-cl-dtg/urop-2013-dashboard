package uk.ac.cam.dashboard.exceptions;

public class AuthException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AuthException(){ super(); }
	public AuthException(String msg){
		super(msg);
	}
}
