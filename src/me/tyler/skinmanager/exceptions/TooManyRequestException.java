package me.tyler.skinmanager.exceptions;

public class TooManyRequestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2411245448678789817L;

	@Override
	public String getMessage() {
		return "Could not grab skin data because we are requesting information to fast!";
	}
	
}

