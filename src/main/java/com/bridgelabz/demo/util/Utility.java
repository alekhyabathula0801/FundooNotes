package com.bridgelabz.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Utility {
	
	@Autowired
	UserToken userToken;
	
	public Utility() {
		
	}

	public String getBody(String link, Long userId) {
		return "http://localhost:4200/fundoo/"+link+"/"+userToken.createToken(userId);
	}

}
