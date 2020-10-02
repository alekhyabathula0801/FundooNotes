package com.bridgelabz.demo.util;

public class Utility {
	
	public static String getBody(String link, Long userId) {
		return "http://localhost:4200/fundoo/"+link+"/"+UserToken.createToken(userId);
	}

}
