package com.bridgelabz.demo.util;

public class Utility {

	public static String getBody(String link, Long userId) {
		return "http://localhost:8080/user/" + link + "/" + UserToken.createToken(userId);
	}

}
