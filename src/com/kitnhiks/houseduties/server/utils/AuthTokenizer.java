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

	public static boolean verifyToken(String token, House house){
		String[] tokenDecrypted = decryptToken(token);
		return house.getName().equals(tokenDecrypted[0]) && house.getPassword().equals(tokenDecrypted[1]);
	}

	public static boolean isAdmin(HttpHeaders headers){
		List<String> authHeader = headers.getRequestHeader(AUTH_KEY_HEADER);
		if (authHeader== null || authHeader.size()!=1){
			return false;
		}else{
			return isAdminToken(headers.getRequestHeader(AUTH_KEY_HEADER).get(0));
		}
	}

	private static boolean isAdminToken(String token){
		return AUTH_KEY_ADMIN.equals(token);
	}

	public static boolean isValidAuth(HttpHeaders headers, House house){
		List<String> authHeader = headers.getRequestHeader(AUTH_KEY_HEADER);
		if (authHeader== null || authHeader.size()!=1){
			return false;
		}else{
			String token = headers.getRequestHeader(AUTH_KEY_HEADER).get(0);
			if (isAdminToken(token)){
				return true;
			}else{
				return verifyToken(token, house);
			}
		}
	}
}
