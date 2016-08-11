package me.tyler.skinmanager.skins;

import java.util.Map;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;

public interface Skin {

	public boolean isPartEnabled(SkinPart part);
	
	public Map<String, WrappedSignedProperty> getProperties();
	
	public WrappedSignedProperty getProperty(String key);
	
	public WrappedGameProfile getProfile();
	
	public static enum SkinPart{
		
		CAPE(0),
		JACKET(1),
		LEFT_SLEEVE(2),
		RIGHT_SLEEVE(3),
		LEFT_LEG(4),
		RIGHT_LEG(5),
		HAT(6);
		
		private final int bitIndex;
		
		private SkinPart(int bitIndex) {
			this.bitIndex = bitIndex;
		}
		
		protected boolean isEnabled(int bits){
			int i = (int) Math.pow(2, bitIndex);
			
			return (bits & i) == i;
		}
		
	}
	
}
