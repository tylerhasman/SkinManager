package me.tyler.skinmanager.exceptions;

public class SkinNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1550540959940027029L;

	private String uuid;
	
	public SkinNotFoundException(String uuid) {
		this.uuid = uuid;
	}
	
	@Override
	public String getMessage() {
		return "Skin could not be found for "+uuid;
	}
	
	
}
