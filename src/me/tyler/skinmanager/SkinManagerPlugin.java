package me.tyler.skinmanager;

import java.io.File;

import me.tyler.skinmanager.mojang.IMojangAPI;
import me.tyler.skinmanager.mojang.MojangAPI;
import me.tyler.skinmanager.packets.PlayerInfoPacketListener;
import me.tyler.skinmanager.skins.SimpleSkinManager;
import me.tyler.skinmanager.skins.SkinManager;
import me.tyler.skinmanager.skulls.ISkullManager;
import me.tyler.skinmanager.skulls.SkullManager;

import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;

public class SkinManagerPlugin extends JavaPlugin {

	private static SkinManagerPlugin instance = null;
	
	private SkinManager skinManager;
	private MojangAPI mojangApi;
	private SkullManager skullManager;
	
	@Override
	public void onEnable() {
		instance = this;
		
		skinManager = new SimpleSkinManager();
		
		mojangApi = new IMojangAPI();
		skullManager = new ISkullManager();
		
		if(!new File(getDataFolder(), "config.yml").exists()){
			saveDefaultConfig();
		}
		
		if(getConfig().getBoolean("enable_player_skins")){
			ProtocolLibrary.getProtocolManager().addPacketListener(new PlayerInfoPacketListener());
		}
		
	}
	
	public static SkinManager getSkinManager(){
		return getInstance().skinManager;
	}
	
	public static SkullManager getSkullManager(){
		return getInstance().skullManager;
	}
	
	public static MojangAPI getMojangAPI(){
		return getInstance().mojangApi;
	}

	public static void setSkinManager(SkinManager skinManager){
		getInstance().skinManager = skinManager;
	}
	
	public static SkinManagerPlugin getInstance() {
		return instance;
	}
	
}
