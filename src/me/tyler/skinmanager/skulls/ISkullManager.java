package me.tyler.skinmanager.skulls;

import java.util.Collections;

import me.dpohvar.powernbt.PowerNBT;
import me.dpohvar.powernbt.api.NBTCompound;
import me.dpohvar.powernbt.api.NBTList;
import me.tyler.skinmanager.reflection.ReflectionManager;
import me.tyler.skinmanager.skins.Skin;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.wrappers.WrappedGameProfile;

public class ISkullManager implements SkullManager {
	
	@Override
	public ItemStack getPlayerSkull(Skin skin) {
		
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		
		return setPlayerSkull(item, skin);
	}

	@Override
	public ItemStack setPlayerSkull(ItemStack item, Skin skin) {
		
		WrappedGameProfile profile = skin.getProfile();
		
		item = ReflectionManager.setTextureTag(item, "SkullOwner", profile);
		
		return item;
	}

	@Override
	public ItemStack setPlayerSkull(ItemStack item, String id, String base64) {
		NBTCompound comp = PowerNBT.getApi().read(item);
		
		if(comp == null){
			comp = new NBTCompound();
		}
		
		NBTCompound skullOwner = new NBTCompound();
		skullOwner.put("Id", id);
		NBTCompound properties = new NBTCompound();
		properties.put("textures", new NBTList(new Object[] { Collections.singletonMap("Value", base64) }));
		
		skullOwner.put("Properties", properties);
		
		comp.put("SkullOwner", skullOwner);
		
		PowerNBT.getApi().write(item, comp);
		
		return item;
	}

}
