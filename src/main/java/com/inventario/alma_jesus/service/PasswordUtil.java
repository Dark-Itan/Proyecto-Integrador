package com.inventario.alma_jesus.service;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Utilidad para el manejo seguro de contraseñas en el sistema.
 * Proporciona métodos para encriptar y verificar contraseñas usando el algoritmo BCrypt.
 *
 * @author Alma & Jesús
 * @version 1.0
 * @since 2024
 */
public class PasswordUtil {

    /**
     * Encripta una contraseña usando el algoritmo BCrypt con factor de costo 12.
     *
     * @param password Contraseña en texto plano a encriptar.
     * @return Contraseña encriptada en formato BCrypt.
     * @throws NullPointerException Si la contraseña proporcionada es null.
     *
     * @example
     * <pre>{@code
     * String passwordPlana = "miContraseña123";
     * String passwordEncriptada = PasswordUtil.encryptPassword(passwordPlana);
     * // Resultado: "$2a$12$... (hash BCrypt)"
     * }</pre>
     */
    public static String encryptPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    /**
     * Verifica si una contraseña en texto plano coincide con una contraseña encriptada.
     *
     * @param password Contraseña en texto plano a verificar.
     * @param encryptedPassword Contraseña encriptada almacenada (hash BCrypt).
     * @return true si la contraseña coincide, false en caso contrario.
     * @throws NullPointerException Si alguno de los parámetros es null.
     *
     * @example
     * <pre>{@code
     * String passwordIngresada = "miContraseña123";
     * String hashAlmacenado = "$2a$12$...";
     * boolean esValida = PasswordUtil.verifyPassword(passwordIngresada, hashAlmacenado);
     * // Devuelve true si la contraseña es correcta
     * }</pre>
     */
    public static boolean verifyPassword(String password, String encryptedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), encryptedPassword);
        return result.verified;
    }
}