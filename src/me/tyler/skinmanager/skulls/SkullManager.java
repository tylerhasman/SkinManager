package me.tyler.skinmanager.skulls;

import me.tyler.skinmanager.skins.Skin;
import me.tyler.skinmanager.skins.SkinManager;

import org.bukkit.inventory.ItemStack;

public interface SkullManager {

	/**
	 * Get a player skull item that looks like a skin
	 * @param skin
	 * @return the player skull
	 * @see SkinManager
	 */
	public ItemStack getPlayerSkull(Skin skin);
	
	/**
	 * Set a skulls player
	 * @param item the skull
	 * @param skin the skin
	 * @return the itemstack with the skin
	 */
	public ItemStack setPlayerSkull(ItemStack item, Skin skin);
	
	public ItemStack setPlayerSkull(ItemStack item, String id, String base64);
	
}
