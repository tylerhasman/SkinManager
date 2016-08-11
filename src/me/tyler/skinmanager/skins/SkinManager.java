package me.tyler.skinmanager.skins;

import java.util.UUID;

import me.tyler.skinmanager.SkinManagerPlugin;

import org.bukkit.entity.Player;

/**
 * 
 * @see SkinManagerPlugin#getSkinManager()
 *
 */
public interface SkinManager {

	/**
	 * Get the skin a player has on
	 * @param player the player
	 * @return the players skin
	 */
	public Skin getSkin(Player player);
	
	/**
	 * Set a players skin
	 * @param player the player
	 * @param skin the skin
	 */
	public void setSkin(Player player, Skin skin);
	
	/**
	 * Create a skin from a uuid
	 * @param uuid the uuid
	 * @return the skin
	 */
	public Skin createSkin(UUID uuid);
	
	/**
	 * Get a skin from a uuid
	 * @param uuid the uuid
	 * @return the skin
	 */
	public Skin getLoadedSkin(UUID uuid);
	
	
}
