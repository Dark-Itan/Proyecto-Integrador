package com.inventario.alma_jesus.service;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {

    public static String encryptPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public static boolean verifyPassword(String password, String encryptedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), encryptedPassword);
        return result.verified;
    }
}