package me.tyler.skinmanager.mojang;

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

import me.tyler.skinmanager.exceptions.UserNotFoundException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class IMojangAPI implements MojangAPI{

	private Map<String, UUID> usernames;
	
	public IMojangAPI() {
		usernames = new HashMap<>();
	}
	
	@Override
	public UUID getUUID(String username) {
		
		if(usernames.containsKey(username)){
			return usernames.get(username);
		}
		
		String id = null;
		
		try {
			id = getIdFromMojang(username);
		} catch (IOException e) {
		} catch (ParseException e) {
		} catch (UserNotFoundException e) {
		}
		
		if(id != null){
			UUID converted = convertToUuid(id);
			
			usernames.put(username, converted);
			
			return converted;
		}
		
		return null;
	}
	
	private UUID convertToUuid(String str){
		
		String converted = str.substring(0, 8)
				+ "-" + str.substring(8, 12)
				+ "-" + str.substring(12, 16)
				+ "-" + str.substring(16, 20)
				+ "-" + str.substring(20, str.length());
		
		return UUID.fromString(converted);
	}
	
	private String getIdFromMojang(String username) throws IOException, ParseException, UserNotFoundException{
		
		URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+username);
		URLConnection con = url.openConnection();
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		
		HttpsURLConnection httpUrlCon = (HttpsURLConnection) con;	
		
		if(httpUrlCon.getResponseCode() == HttpResponseStatus.NOT_FOUND.code()){
			throw new UserNotFoundException(username);
		}
		
		JSONParser parser = new JSONParser();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpUrlCon.getInputStream()));
		
		JSONObject obj = (JSONObject) parser.parse(reader);
		
		return (String) obj.get("id");
	}
	
}
