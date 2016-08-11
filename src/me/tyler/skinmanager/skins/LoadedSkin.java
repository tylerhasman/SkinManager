package me.tyler.skinmanager.skins;

import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import me.tyler.skinmanager.exceptions.SkinNotFoundException;
import me.tyler.skinmanager.exceptions.TooManyRequestException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;

public class LoadedSkin implements Skin {
	
	private Map<String, WrappedSignedProperty> propMap;
	private String trimmedUuid;
	private UUID uuid;
	
	protected LoadedSkin(UUID uuid) {
		trimmedUuid = uuid.toString().replace("-", "");
		propMap = new HashMap<>();
		
		this.uuid = uuid;
	}
	
	protected LoadedSkin(UUID uuid, Map<String, WrappedSignedProperty> properties){
		this(uuid);
		
		propMap = properties;
	}
	
	public Map<String, WrappedSignedProperty> getProperties(){
		return propMap;
	}
	
	@Override
	public WrappedGameProfile getProfile() {
		
		WrappedGameProfile profile = new WrappedGameProfile(uuid, getRandomName());
		
		for(String str : propMap.keySet()){
			profile.getProperties().put(str, getProperty(str));
		}
		
		return profile;
	}
	
	public WrappedSignedProperty getProperty(String key){
		return propMap.get(key);
	}
	
	/**
	 * Load with all errors suppressed
	 */
	public boolean loadGracefully(){
		try {
			loadFromWeb();
			return true;
		} catch (IOException e) {
		} catch (SkinNotFoundException e) {
		} catch (TooManyRequestException e) {
		}
		return false;
	}
	
	/**
	 * Load with all possible exceptions thrown
	 * @throws IOException
	 * @throws SkinNotFoundException
	 * @throws TooManyRequestException
	 */
	public void load() throws IOException, SkinNotFoundException, TooManyRequestException{
		loadFromWeb();
	}
	
	private void loadFromWeb() throws IOException, SkinNotFoundException, TooManyRequestException{
		JSONObject obj = grabJsonObject();
		
		String name = (String) obj.get("name");
		String value = (String) obj.get("value");
		String signature = (String) obj.get("signature");
		
		propMap.put(name, new WrappedSignedProperty(name, value, signature));
	}

	private String getRandomName(){
		return UUID.randomUUID().toString().substring(0, 16);
	}
	
	private JSONObject grabJsonObject() throws IOException, SkinNotFoundException, TooManyRequestException {
		URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/"+trimmedUuid+"?unsigned=false");
		URLConnection con = url.openConnection();
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		
		HttpsURLConnection httpUrlCon = (HttpsURLConnection) con;
		
		if(httpUrlCon.getResponseCode() == HttpResponseStatus.TOO_MANY_REQUESTS.code()){
			throw new TooManyRequestException();
		}else if(httpUrlCon.getResponseCode() == HttpResponseStatus.NOT_FOUND.code()){
			throw new SkinNotFoundException(uuid.toString());
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpUrlCon.getInputStream()));
		
		JSONObject obj = new JSONObject(reader.readLine());
		
		reader.close();
		
		JSONArray array = obj.getJSONArray("properties");
		
		return (JSONObject) array.get(0);
	}

	@Override
	public boolean isPartEnabled(SkinPart part) {
		return false;
	}
	
	@Override
	public String toString() {
		return uuid.toString();
	}
	
}
