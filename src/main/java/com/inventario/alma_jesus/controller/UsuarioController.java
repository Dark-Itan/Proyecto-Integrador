package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.service.UsuarioService;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

@SuppressWarnings("unchecked")

public class UsuarioController {
    private UsuarioService usuarioService = new UsuarioService();
    private ObjectMapper objectMapper = new ObjectMapper();

    public void listarUsuarios(Context ctx) {
        try {
            Map<String, Object> result = usuarioService.listarUsuarios();

            if ((Boolean) result.get("success")) {
                ctx.status(200).json(result);
            } else {
                ctx.status(500).json(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno al listar usuarios"
            ));
        }
    }

    public void obtenerUsuarioPorId(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            if (id == null || id.isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "ID de usuario es requerido"
                ));
                return;
            }

            Map<String, Object> result = usuarioService.obtenerUsuarioPorId(id);

            if ((Boolean) result.get("success")) {
                ctx.status(200).json(result);
            } else {
                ctx.status(404).json(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno al obtener usuario"
            ));
        }
    }

    public void crearUsuario(Context ctx) {
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

            // Validar que vengan los datos
            if (bodyMap == null || bodyMap.isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "Datos del usuario son requeridos"
                ));
                return;
            }

            Map<String, Object> result = usuarioService.crearUsuario(bodyMap);

            if ((Boolean) result.get("success")) {
                ctx.status(201).json(result); // 201 Created
            } else {
                ctx.status(400).json(result); // 400 Bad Request
            }

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error en el formato JSON: " + e.getMessage()
            ));
        }
    }

    public void cambiarPassword(Context ctx) {
        try {
            String id = ctx.pathParam("id");
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
            String nuevaPassword = bodyMap.get("nuevaPassword");

            // Validar datos
            if (id == null || id.isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "ID de usuario es requerido"
                ));
                return;
            }

            if (nuevaPassword == null || nuevaPassword.isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "nuevaPassword es requerido"
                ));
                return;
            }

            Map<String, Object> result = usuarioService.cambiarPassword(id, nuevaPassword);

            if ((Boolean) result.get("success")) {
                ctx.status(200).json(result);
            } else {
                ctx.status(400).json(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno al cambiar contrasena"
            ));
        }
    }

    //NUEVO MÉTODO AGREGADO: Eliminar usuario
    public void eliminarUsuario(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            if (id == null || id.isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "ID de usuario es requerido"
                ));
                return;
            }

            Map<String, Object> result = usuarioService.eliminarUsuario(id);

            if ((Boolean) result.get("success")) {
                ctx.status(200).json(result);
            } else {
                ctx.status(400).json(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno al eliminar usuario"
            ));
        }
    }
}