package me.tyler.skinmanager.mojang;

import java.util.UUID;

public interface MojangAPI {

	/**
	 * Get a players trimmed unique id from their username.
	 * @param username the players username
	 * @return their uuid or null if the player doesn't exist
	 */
	public UUID getUUID(String username);
	
}
