package com.bridgelabz.demo.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class UserToken {
	public final static String TOKEN_SECRET = "SKJKDHUIh342k";

	public static String createToken(Long userId) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
			String token = JWT.create().withClaim("userId", userId).withIssuedAt(new Date()).sign(algorithm);
			return token;
		} catch (UnsupportedEncodingException | JWTCreationException exception) {
			exception.printStackTrace();
		}
		return null;
	}

	public static Long getUserIdFromToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
			JWTVerifier verifier = JWT.require(algorithm).build();
			DecodedJWT jwt = verifier.verify(token);
			return jwt.getClaim("userId").asLong();
		} catch (UnsupportedEncodingException | JWTVerificationException exception) {
			exception.printStackTrace();
		}
		return null;
	}

}
