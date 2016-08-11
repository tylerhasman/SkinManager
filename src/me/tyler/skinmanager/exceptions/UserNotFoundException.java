package me.tyler.skinmanager.exceptions;

public class UserNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3151772224195672990L;
	
	private String username;
	
	public UserNotFoundException(String username) {
		this.username = username;
	}
	
	@Override
	public String getMessage() {
		return username+" could not be found!";
	}
	
	

}
