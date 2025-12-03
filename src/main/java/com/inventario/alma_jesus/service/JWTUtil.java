package com.inventario.alma_jesus.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilidad para la gestión de tokens JWT (JSON Web Tokens).
 * <p>
 * Esta clase proporciona métodos estáticos para generar, verificar y decodificar
 * tokens JWT utilizados en la autenticación del sistema. Utiliza la biblioteca
 * auth0-java-jwt para la implementación segura de tokens.
 * </p>
 * <p>
 * Los tokens incluyen claims estándar como subject (username) y claims personalizados
 * como rol del usuario. Tienen una validez de 24 horas por defecto.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see <a href="https://jwt.io/">JSON Web Tokens</a>
 * @see <a href="https://github.com/auth0/java-jwt">auth0 java-jwt library</a>
 */
public class JWTUtil {
    /**
     * Clave secreta utilizada para firmar y verificar tokens.
     * <p>
     * <strong>ADVERTENCIA:</strong> En producción, esta clave debe almacenarse
     * de forma segura (variables de entorno, secret manager) y no estar
     * hardcodeada en el código fuente.
     * </p>
     */
    private static final String SECRET = "inventario_alma_jesus_secret_key_2024";

    /**
     * Tiempo de expiración de los tokens en milisegundos.
     * Valor por defecto: 24 horas (86400000 ms).
     */
    private static final long EXPIRATION_TIME = 86400000; // 24 horas

    /**
     * Genera un nuevo token JWT para un usuario.
     * <p>
     * Crea un token firmado con el algoritmo HMAC256 que contiene:
     * <ul>
     *   <li><strong>Subject:</strong> Nombre de usuario (identificador único)</li>
     *   <li><strong>Rol:</strong> Rol del usuario en el sistema</li>
     *   <li><strong>Expiración:</strong> Fecha de expiración (24 horas desde la generación)</li>
     * </ul>
     * </p>
     *
     * @param username Nombre de usuario que será el subject del token
     * @param rol Rol del usuario en el sistema (ej: ADMIN, TRABAJADOR, SUPERVISOR)
     * @return Token JWT firmado como String
     * @throws RuntimeException Si ocurre un error durante la generación del token
     * @throws IllegalArgumentException Si username o rol son nulos o vacíos
     */
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

    /**
     * Verifica la validez de un token JWT.
     * <p>
     * Valida que el token esté correctamente firmado, no haya expirado
     * y tenga un formato válido.
     * </p>
     *
     * @param token Token JWT a verificar
     * @return true si el token es válido, false en caso contrario
     */
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

    /**
     * Decodifica un token JWT y extrae sus claims.
     * <p>
     * Verifica la validez del token y extrae la información contenida en él.
     * Retorna un mapa con los claims del token si es válido.
     * </p>
     *
     * @param token Token JWT a decodificar
     * @return Mapa con los claims del token (username y rol), o null si el token es inválido
     * @see #verifyToken(String)
     */
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