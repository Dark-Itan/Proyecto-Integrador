package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.Usuario;
import com.inventario.alma_jesus.repository.UsuarioRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthService {
    private UsuarioRepository usuarioRepository = new UsuarioRepository();

    public Map<String, Object> login(String username, String password) {
        System.out.println("Intentando login para usuario: " + username);

        Map<String, Object> response = new HashMap<>();

        // Buscar usuario por username
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            System.out.println("Usuario no encontrado: " + username);
            response.put("success", false);
            response.put("message", "Usuario no encontrado");
            return response;
        }

        Usuario usuario = usuarioOpt.get();
        System.out.println("Usuario encontrado en BD:");
        System.out.println("   - ID: " + usuario.getId());
        System.out.println("   - Username: " + usuario.getUsername());
        System.out.println("   - Rol: " + usuario.getRol());
        System.out.println("   - Password en BD: " + usuario.getPassword());
        System.out.println("   - Password recibida: " + password);

        boolean passwordCorrecta = PasswordUtil.verifyPassword(password, usuario.getPassword());
        System.out.println("Contrasena correcta: " + passwordCorrecta);

        if (!passwordCorrecta) {
            response.put("success", false);
            response.put("message", "Contraseña incorrecta");
            return response;
        }

        // Generar token JWT
        String token = JWTUtil.generateToken(usuario.getUsername(), usuario.getRol());
        System.out.println("Token generado: " + token.substring(0, 50) + "...");

        // Respuesta exitosa
        response.put("success", true);
        response.put("message", "Login exitoso");
        response.put("token", token);
        response.put("usuario", Map.of(
                "id", usuario.getId(),
                "username", usuario.getUsername(),
                "rol", usuario.getRol(),
                "email", usuario.getEmail()
        ));

        System.out.println("Login exitoso para: " + username);
        return response;
    }

    public Map<String, Object> verifyToken(String token) {
        Map<String, Object> response = new HashMap<>();

        if (token == null || token.isEmpty()) {
            response.put("success", false);
            response.put("message", "Token no proporcionado");
            return response;
        }

        Map<String, String> tokenData = JWTUtil.decodeToken(token);

        if (tokenData == null) {
            response.put("success", false);
            response.put("message", "Token inválido o expirado");
            return response;
        }

        // Token válido
        response.put("success", true);
        response.put("message", "Token válido");
        response.put("usuario", tokenData);

        System.out.println("Token verificado para: " + tokenData.get("username"));
        return response;
    }

    public Map<String, Object> logout(String token) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Verificar que el token sea válido antes de "invalidarlo"
            Map<String, String> tokenData = JWTUtil.decodeToken(token);

            if (tokenData == null) {
                response.put("success", false);
                response.put("message", "Token invalido");
                return response;
            }

            String username = tokenData.get("username");

            response.put("success", true);
            response.put("message", "Sesion cerrada exitosamente");
            response.put("usuario", username);

            System.out.println("Logout exitoso para: " + username);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al cerrar sesion");
        }

        return response;
    }
}