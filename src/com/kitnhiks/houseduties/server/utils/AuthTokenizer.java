package com.kitnhiks.houseduties.server.utils;

import com.kitnhiks.houseduties.server.model.House;

public class AuthTokenizer {

	public static String generateToken(String login, String password){
		// TODO : real token handler
		return login+"_"+password;
	}
	
	public static House decryptToken(String token){
		// TODO : real token handler
		House house = new House();
		String[] tokenItems = token.split("_");
		house.setName(tokenItems[0]);
		house.setPassword(tokenItems[1]);
		return house;
	}
	
	public static boolean verifyToken(String token, House house){
		return house.equals(decryptToken(token));
	}
}
