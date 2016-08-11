package me.tyler.skinmanager.skins;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

public class SimpleSkinManager implements SkinManager {

	private Map<UUID, LoadedSkin> loadedSkins;
	private Map<UUID, Skin> playerSkins;
	
	public SimpleSkinManager() {
		loadedSkins = new HashMap<>();
		playerSkins = new HashMap<>();
	}

	@Override
	public Skin getSkin(Player player) {
		return playerSkins.get(player.getUniqueId());
	}

	@Override
	public void setSkin(Player player, Skin skin) {
		playerSkins.put(player.getUniqueId(), skin);
		
		refreshPlayer(player);
	}

	@Override
	public Skin createSkin(UUID uuid) {

		if(loadedSkins.containsKey(uuid)){
			return loadedSkins.get(uuid);
		}
		
		LoadedSkin skin = new LoadedSkin(uuid);
		
		if(skin.loadGracefully()){
			loadedSkins.put(uuid, skin);
		}
		
		return skin;
	}

	@Override
	public Skin getLoadedSkin(UUID uuid) {
		return loadedSkins.get(uuid);
	}
	
	private void refreshPlayer(Player player){
		
		WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo();
		
		List<PlayerInfoData> data = Arrays.asList(new PlayerInfoData[] {
				new PlayerInfoData(getProfile(player), 0, NativeGameMode.fromBukkit(player.getGameMode()), WrappedChatComponent.fromText(player.getName()))
		});
		
		info.setData(data);
		
		for(Player target : Bukkit.getOnlinePlayers())
		{
			info.setAction(PlayerInfoAction.REMOVE_PLAYER);
			info.sendPacket(target);
			info.setAction(PlayerInfoAction.ADD_PLAYER);
			info.sendPacket(target);
		}
		
		World world = null;
		
		for(World w : Bukkit.getWorlds()){
			if(w.equals(player.getWorld())){
				continue;
			}
			world = w;
			break;
		}
		
		Location originalLocation = player.getLocation().clone();
		
		player.teleport(world.getSpawnLocation());
		player.teleport(originalLocation);
		
	}

	private WrappedGameProfile getProfile(Player player){
		return getSkin(player).getProfile();
	}
	


}
