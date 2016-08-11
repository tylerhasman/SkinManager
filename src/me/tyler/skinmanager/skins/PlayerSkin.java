package me.tyler.skinmanager.skins;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.collect.Multimap;

public class PlayerSkin implements Skin {

	private Multimap<String, WrappedSignedProperty> propMap;
	private WrappedGameProfile profile;
	
	protected PlayerSkin(Player player) {
		
		profile = WrappedGameProfile.fromPlayer(player);
		
		propMap = profile.getProperties();
	}
	
	public WrappedGameProfile getProfile() {
		return profile;
	}
	
	public WrappedSignedProperty getProperty(String key){
		return propMap.get(key).iterator().next();
	}
	
	@Override
	public boolean isPartEnabled(SkinPart part) {
		return false;
	}

	@Override
	public Map<String, WrappedSignedProperty> getProperties() {
		
		Map<String, WrappedSignedProperty> map = new HashMap<>();
		
		for(String str : propMap.keys()){
			WrappedSignedProperty prop = getProperty(str);
			map.put(str, new WrappedSignedProperty(prop.getName(), prop.getValue(), prop.getSignature()));
		}
		
		return map;
	}

	public static Skin getSkin(Player player) {
		return new PlayerSkin(player);
	}

}
