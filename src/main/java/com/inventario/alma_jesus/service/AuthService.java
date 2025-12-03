package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.Usuario;
import com.inventario.alma_jesus.repository.UsuarioRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Servicio para la gestión de autenticación de usuarios.
 * <p>
 * Esta clase maneja las operaciones de login, verificación de tokens JWT
 * y logout del sistema. Utiliza {@link PasswordUtil} para verificación
 * segura de contraseñas y {@link JWTUtil} para generación y validación
 * de tokens de autenticación.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see UsuarioRepository
 * @see PasswordUtil
 * @see JWTUtil
 */
public class AuthService {
    private UsuarioRepository usuarioRepository = new UsuarioRepository();

    /**
     * Autentica un usuario en el sistema.
     * <p>
     * Verifica las credenciales del usuario contra la base de datos,
     * genera un token JWT si la autenticación es exitosa y retorna
     * información del usuario junto con el token.
     * </p>
     *
     * @param username Nombre de usuario (email o identificador único)
     * @param password Contraseña en texto plano (será encriptada para verificación)
     * @return Mapa con los siguientes elementos:
     *         <ul>
     *           <li>success: boolean indicando si el login fue exitoso</li>
     *           <li>message: Mensaje descriptivo del resultado</li>
     *           <li>token: Token JWT generado (solo si success=true)</li>
     *           <li>usuario: Información básica del usuario (solo si success=true)</li>
     *         </ul>
     * @throws NullPointerException Si username o password son nulos
     */
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

    /**
     * Verifica la validez de un token JWT.
     * <p>
     * Valida que el token no haya expirado, que tenga un formato correcto
     * y que contenga los claims necesarios. Utiliza {@link JWTUtil} para
     * el procesamiento del token.
     * </p>
     *
     * @param token Token JWT a verificar
     * @return Mapa con los siguientes elementos:
     *         <ul>
     *           <li>success: boolean indicando si el token es válido</li>
     *           <li>message: Mensaje descriptivo del resultado</li>
     *           <li>usuario: Datos decodificados del token (solo si success=true)</li>
     *         </ul>
     * @see JWTUtil#decodeToken(String)
     */
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

    /**
     * Cierra la sesión de un usuario.
     * <p>
     * Valida el token antes de proceder con el logout. En una implementación
     * básica de JWT, esto simplemente indica que el token ya no debe usarse,
     * aunque técnicamente los tokens JWT son stateless. Para invalidación
     * completa se requeriría una lista negra de tokens.
     * </p>
     *
     * @param token Token JWT de la sesión a cerrar
     * @return Mapa con los siguientes elementos:
     *         <ul>
     *           <li>success: boolean indicando si el logout fue exitoso</li>
     *           <li>message: Mensaje descriptivo del resultado</li>
     *           <li>usuario: Nombre de usuario de la sesión cerrada (solo si success=true)</li>
     *         </ul>
     */
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