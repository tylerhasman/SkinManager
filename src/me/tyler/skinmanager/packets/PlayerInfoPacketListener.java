package me.tyler.skinmanager.packets;

import java.util.ArrayList;
import java.util.List;

import me.tyler.skinmanager.SkinManagerPlugin;
import me.tyler.skinmanager.skins.PlayerSkin;
import me.tyler.skinmanager.skins.Skin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;

public class PlayerInfoPacketListener extends PacketAdapter {

	public PlayerInfoPacketListener() {
		super(SkinManagerPlugin.getInstance(), PacketType.Play.Server.PLAYER_INFO);
	}

	@Override
	public void onPacketSending(PacketEvent event) {
		PacketType type = event.getPacketType();
		PacketContainer packet = event.getPacket();
		
		if(type == PacketType.Play.Server.PLAYER_INFO){
			
			PlayerInfoAction action = packet.getPlayerInfoAction().read(0);

			if(action == PlayerInfoAction.ADD_PLAYER){
				
				List<PlayerInfoData> replaced = new ArrayList<>();
				
				List<PlayerInfoData> original = packet.getPlayerInfoDataLists().read(0);
				
				for(PlayerInfoData data : original){
					
					Player player = Bukkit.getPlayer(data.getProfile().getUUID());
					
					if(player != null){
						
						Skin skin = SkinManagerPlugin.getSkinManager().getSkin(player);
						if(skin == null){
							SkinManagerPlugin.getSkinManager().setSkin(player, PlayerSkin.getSkin(player));
							skin = SkinManagerPlugin.getSkinManager().getSkin(player);
						}
						data = new PlayerInfoData(skin.getProfile(), data.getPing(), data.getGameMode(), data.getDisplayName());	
					}

					replaced.add(data);
					
				}

				packet.getPlayerInfoDataLists().write(0, replaced);
			}
			
			
			event.setPacket(packet);
			
		}
	}
	
}
