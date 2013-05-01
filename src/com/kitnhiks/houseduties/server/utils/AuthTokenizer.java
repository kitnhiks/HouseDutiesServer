package com.kitnhiks.houseduties.server.utils;

import static com.kitnhiks.houseduties.server.resources.RESTConst.AUTH_KEY_ADMIN;
import static com.kitnhiks.houseduties.server.resources.RESTConst.AUTH_KEY_HEADER;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import com.kitnhiks.houseduties.server.model.House;

public class AuthTokenizer {

	public static String generateToken(String login, String password){
		// TODO : real token handler
		return login+"#"+password;
	}

	public static String[] decryptToken(String token){
		// TODO : real token handler
		return token.split("#");
	}

	private static boolean verifyToken(String token, House house){
		if (house == null){
			return false;
		}
		String[] tokenDecrypted = decryptToken(token);
		return tokenDecrypted[0].equals(house.getName()) && tokenDecrypted[1].equals(house.getPassword());
	}

	public static boolean isAdminToken(String token){
		return AUTH_KEY_ADMIN.equals(token);
	}

	public static House getHouseFromToken(String token){
		String[] houseDecrypted = decryptToken(token);
		if (houseDecrypted.length != 2){
			return null;
		}
		House house = new House();
		house.setName(houseDecrypted[0]);
		house.setPassword(houseDecrypted[1]);
		return house;
	}

	public static boolean isValidToken(String token, House house){
		if (token == null){
			return false;
		}
		if (isAdminToken(token)){
			return true;
		}else{
			return verifyToken(token, house);
		}
	}

	public static String getToken(HttpHeaders headers){
		List<String> authHeader = headers.getRequestHeader(AUTH_KEY_HEADER);
		if (authHeader == null || authHeader.size()!=1){
			return null;
		}else{
			return headers.getRequestHeader(AUTH_KEY_HEADER).get(0);
		}
	}

	public static String renewToken(String token){
		if (isAdminToken(token)){
			return "";
		}else{
			String[] tokenDecrypted = decryptToken(token);
			return generateToken(tokenDecrypted[0], tokenDecrypted[1]);
		}
	}
}
