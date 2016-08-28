package me.tyler.skinmanager.skins;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.FieldAccessException;
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
		
		PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
		
		List<PlayerInfoData> data = Arrays.asList(new PlayerInfoData[] {
				new PlayerInfoData(getProfile(player), 0, NativeGameMode.fromBukkit(player.getGameMode()), WrappedChatComponent.fromText(player.getName()))
		});
		
		packet.getPlayerInfoDataLists().write(0, data);
		
		for(Player target : Bukkit.getOnlinePlayers())
		{
			try {
				packet.getPlayerInfoAction().write(0, PlayerInfoAction.REMOVE_PLAYER);
				ProtocolLibrary.getProtocolManager().sendServerPacket(target, packet);
				packet.getPlayerInfoAction().write(0, PlayerInfoAction.ADD_PLAYER);
				ProtocolLibrary.getProtocolManager().sendServerPacket(target, packet);
			} catch (FieldAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
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
