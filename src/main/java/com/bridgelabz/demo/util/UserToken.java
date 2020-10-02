package com.bridgelabz.demo.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

public class UserToken {
	public static final String TOKEN_SECRET = "SKJKDHUIh342k";

	public static String createToken(Long userId) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
			String token = JWT.create()
							  .withClaim("userId", userId)
							  .withIssuedAt(new Date())
							  .sign(algorithm);
			return token;
		} catch (UnsupportedEncodingException | JWTCreationException exception) {
			exception.printStackTrace();
		}
		return null;
	}

}
