package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.AuthController;
import io.javalin.Javalin;

/**
 * Router para la configuración de rutas de autenticación.
 * <p>
 * Esta clase define y configura todas las rutas relacionadas con la autenticación
 * en la API REST. Centraliza la gestión de endpoints para login, logout y
 * verificación de tokens.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see AuthController
 */
public class AuthRouter {
    private AuthController authController = new AuthController();

    /**
     * Configura todas las rutas de autenticación en la aplicación Javalin.
     * <p>
     * Define los siguientes endpoints:
     * <ul>
     *   <li>POST /api/v1/auth/login - Para iniciar sesión y obtener token JWT</li>
     *   <li>POST /api/v1/auth/logout - Para cerrar sesión y invalidar token (si aplica)</li>
     *   <li>GET /api/v1/auth/verify - Para verificar la validez de un token JWT</li>
     * </ul>
     * Todos los endpoints siguen la convención de versionado /api/v1/ y están
     * agrupados bajo el path /auth.
     * </p>
     *
     * @param app Instancia de {@link Javalin} donde se configurarán las rutas
     * @throws IllegalArgumentException Si la aplicación Javalin es nula
     */
    public void configureRoutes(Javalin app) {
        // Endpoint 1: /api/v1/auth/login - Iniciar sesión
        app.post("/api/v1/auth/login", authController::login);

        // Endpoint 2: /api/v1/auth/logout - Cerrar sesión
        app.post("/api/v1/auth/logout", authController::logout);

        // Endpoint 3: /api/v1/auth/verify - Verificar token
        app.get("/api/v1/auth/verify", authController::verifyToken);
    }
}