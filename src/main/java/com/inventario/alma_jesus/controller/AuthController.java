package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.service.AuthService;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

/**
 * Controlador para gestionar las operaciones de autenticación y autorización.
 * <p>
 * Esta clase maneja las peticiones HTTP relacionadas con login, logout y verificación
 * de tokens JWT para el sistema de inventario "Alma Jesús".
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see AuthService
 */
@SuppressWarnings("unchecked")
public class AuthController {

    /**
     * Servicio de autenticación que contiene la lógica de negocio.
     */
    private AuthService authService = new AuthService();
    /**
     * Mapper para convertir entre JSON y objetos Java.
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Procesa la petición de inicio de sesión de un usuario.
     * <p>
     * Este método valida las credenciales proporcionadas (username y password)
     * y genera un token JWT si son correctas. Maneja errores de formato JSON,
     * datos faltantes y credenciales inválidas.
     * </p>
     *
     * @param ctx Contexto de Javalin que contiene la petición HTTP
     *
     * @throws Exception Si ocurre un error durante el procesamiento del JSON
     *
     * @example
     * Petición POST a /api/v1/auth/login con body JSON:
     * <pre>
     * {
     *     "username": "admin001",
     *     "password": "AdminAlm@2024"
     * }
     * </pre>
     *
     * Respuesta exitosa (200):
     * <pre>
     * {
     *     "success": true,
     *     "token": "eyJhbGciOiJIUzI1NiIs...",
     *     "user": { ... }
     * }
     * </pre>
     *
     * Respuesta de error (400, 401, 500):
     * <pre>
     * {
     *     "success": false,
     *     "message": "Mensaje de error descriptivo"
     * }
     * </pre>
     */
    public void login(Context ctx) {
        try {
            String body = ctx.body();

            // Verificar si el body está vacío
            if (body == null || body.trim().isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "El cuerpo de la peticion no puede estar vacio"
                ));
                return;
            }

            // Convertir JSON a Map
            Map<String, String> bodyMap = objectMapper.readValue(body, Map.class);
            String username = bodyMap.get("username");
            String password = bodyMap.get("password");

            // Validar que vengan los datos
            if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "Username y password son requeridos"
                ));
                return;
            }

            // Intentar login
            Map<String, Object> result = authService.login(username, password);

            if ((Boolean) result.get("success")) {
                ctx.status(200).json(result);
            } else {
                ctx.status(401).json(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error en el formato JSON: " + e.getMessage()
            ));
        }
    }

    /**
     * Verifica la validez de un token JWT.
     * <p>
     * Este método extrae el token del header Authorization (formato "Bearer {token}")
     * y verifica si es válido y no ha expirado.
     * </p>
     *
     * @param ctx Contexto de Javalin que contiene la petición HTTP
     *
     * @example
     * Petición GET a /api/v1/auth/verify con header:
     * <pre>
     * Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
     * </pre>
     *
     * Respuesta exitosa (200):
     * <pre>
     * {
     *     "success": true,
     *     "valid": true,
     *     "user": { ... }
     * }
     * </pre>
     *
     * Respuesta de error (401, 500):
     * <pre>
     * {
     *     "success": false,
     *     "message": "Token de autorización requerido"
     * }
     * </pre>
     */

    //NUEVO MÉTODO AGREGADO: Verificar token
    public void verifyToken(Context ctx) {
        try {
            String authHeader = ctx.header("Authorization");
            String token = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            if (token == null) {
                ctx.status(401).json(Map.of(
                        "success", false,
                        "message", "Token de autorización requerido"
                ));
                return;
            }

            Map<String, Object> result = authService.verifyToken(token);

            if ((Boolean) result.get("success")) {
                ctx.status(200).json(result);
            } else {
                ctx.status(401).json(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al verificar token"
            ));
        }
    }

    /**
     * Cierra la sesión de un usuario invalidando su token JWT.
     * <p>
     * Este método marca el token como inválido en el sistema para que no pueda
     * ser utilizado nuevamente. Requiere un token válido en el header Authorization.
     * </p>
     *
     * @param ctx Contexto de Javalin que contiene la petición HTTP
     *
     * @example
     * Petición POST a /api/v1/auth/logout con header:
     * <pre>
     * Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
     * </pre>
     *
     * Respuesta exitosa (200):
     * <pre>
     * {
     *     "success": true,
     *     "message": "Sesión cerrada exitosamente"
     * }
     * </pre>
     *
     * Respuesta de error (401, 500):
     * <pre>
     * {
     *     "success": false,
     *     "message": "Token de autorizacion requerido"
     * }
     * </pre>
     */
    //NUEVO MÉTODO AGREGADO: Logout
    public void logout(Context ctx) {
        try {
            // Obtener token del header Authorization
            String authHeader = ctx.header("Authorization");
            String token = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            if (token == null) {
                ctx.status(401).json(Map.of(
                        "success", false,
                        "message", "Token de autorizacion requerido"
                ));
                return;
            }

            // Procesar logout
            Map<String, Object> result = authService.logout(token);

            if ((Boolean) result.get("success")) {
                ctx.status(200).json(result);
            } else {
                ctx.status(401).json(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al cerrar sesion"
            ));
        }
    }
}