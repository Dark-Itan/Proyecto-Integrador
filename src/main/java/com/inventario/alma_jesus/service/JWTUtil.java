package com.inventario.alma_jesus.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {
    private static final String SECRET = "inventario_alma_jesus_secret_key_2024";
    private static final long EXPIRATION_TIME = 86400000; // 24 horas

    public static String generateToken(String username, String rol) {
        try {
            return JWT.create()
                    .withSubject(username)
                    .withClaim("rol", rol)
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .sign(Algorithm.HMAC256(SECRET));
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error al generar token", e);
        }
    }

    public static boolean verifyToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(SECRET))
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            System.out.println("Token invalido: " + e.getMessage());
            return false;
        }
    }

    public static Map<String, String> decodeToken(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET))
                    .build()
                    .verify(token);

            Map<String, String> claims = new HashMap<>();
            claims.put("username", jwt.getSubject());
            claims.put("rol", jwt.getClaim("rol").asString());

            return claims;
        } catch (Exception e) {
            return null;
        }
    }
}