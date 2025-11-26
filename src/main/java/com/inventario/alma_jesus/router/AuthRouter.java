package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.AuthController;
import io.javalin.Javalin;

public class AuthRouter {
    private AuthController authController = new AuthController();

    public void configureRoutes(Javalin app) {
        // Endpoint 1: /api/v1/auth/login - Iniciar sesion
        app.post("/api/v1/auth/login", authController::login);

        //  NUEVO ENDPOINT 2: /api/v1/auth/logout - Cerrar sesion
        app.post("/api/v1/auth/logout", authController::logout);

        // Endpoint 3: /api/v1/auth/verify - Verificar token
        app.get("/api/v1/auth/verify", authController::verifyToken);
    }
}