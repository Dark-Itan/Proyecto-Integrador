package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.service.AuthService;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

@SuppressWarnings("unchecked")
public class AuthController {
    private AuthService authService = new AuthService();
    private ObjectMapper objectMapper = new ObjectMapper();

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