package me.tyler.skinmanager.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.wrappers.WrappedGameProfile;

public class ReflectionManager {

	public static ItemStack setTextureTag(ItemStack item, String key, WrappedGameProfile profile){
		
		try {
			Object nms = getNMSCopy(item);
			
			Object tag = getTagCompound(nms);
			
			Object handle = profile.getHandle();
			
			Method serialize = Class.forName(getNMSPackage()+".GameProfileSerializer").getMethod("serialize", tag.getClass(), handle.getClass());
			
			Method set = tag.getClass().getDeclaredMethod("set", String.class, tag.getClass().getSuperclass());
			
			set.invoke(tag, key, serialize.invoke(null, tag, handle));
			
			Class<?> nmsItemClass = nms.getClass();
			Method method = nmsItemClass.getDeclaredMethod("setTag", tag.getClass());
			
			method.invoke(nms, tag);
			
			Method toCraftStack = Class.forName(getCraftPackage()+".inventory.CraftItemStack").getMethod("asBukkitCopy", nms.getClass());
			
			return (ItemStack) toCraftStack.invoke(null, nms);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return item;
	}
	
	private static Object getTagCompound(Object nms) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		
		
		Method getTag = nms.getClass().getDeclaredMethod("getTag");
		
		Object compound = getTag.invoke(nms);
		
		if(compound == null){
			Class<?> nmsCompoundClass = Class.forName(getNMSPackage()+".NBTTagCompound");
			
			return nmsCompoundClass.newInstance();
		}else{
			return compound;
		}
		
		
	
	}
	
	private static Object getNMSCopy(ItemStack item) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class<?> craftItemStackClass = Class.forName(getCraftPackage()+".inventory.CraftItemStack");
		
		Method method = craftItemStackClass.getDeclaredMethod("asNMSCopy", ItemStack.class);
		
		return method.invoke(null, item);
	}
	
	private static String getNMSPackage() {
		String serverPackage = Bukkit.getServer().getClass().getPackage()
				.getName();

		return "net.minecraft.server."
				+ serverPackage.substring(serverPackage.lastIndexOf('.') + 1);
	}
	
	private static String getCraftPackage() {
		String serverPackage = Bukkit.getServer().getClass().getPackage()
				.getName();

		return "org.bukkit.craftbukkit."
				+ serverPackage.substring(serverPackage.lastIndexOf('.') + 1);
	}
}
