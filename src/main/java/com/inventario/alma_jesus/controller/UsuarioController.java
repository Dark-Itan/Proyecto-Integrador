package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.service.UsuarioService;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

/**
 * Controlador para gestionar las operaciones relacionadas con usuarios.
 * <p>
 * Esta clase maneja el CRUD completo de usuarios, incluyendo autenticación,
 * gestión de contraseñas y administración de perfiles de usuario.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see UsuarioService
 */
@SuppressWarnings("unchecked")
public class UsuarioController {
    private UsuarioService usuarioService = new UsuarioService();
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Endpoint: Lista todos los usuarios registrados en el sistema.
     * <p>
     * Retorna una lista completa de usuarios con sus datos básicos.
     * No incluye información sensible como contraseñas.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     * @example GET /api/v1/usuarios
     */
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

    /**
     * Endpoint: Obtiene un usuario específico por su ID.
     * <p>
     * Busca y retorna la información completa de un usuario
     * identificado por su ID único.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     * @example GET /api/v1/usuarios/USU001
     */
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

    /**
     * Endpoint: Crea un nuevo usuario en el sistema.
     * <p>
     * Registra un nuevo usuario con todos sus datos básicos.
     * La contraseña se encripta automáticamente antes de almacenarse.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     * @example
     * POST /api/v1/usuarios
     * Body:
     * {
     *     "id": "USU001",
     *     "nombre": "Juan Pérez",
     *     "email": "juan@ejemplo.com",
     *     "password": "ContraseñaSegura123",
     *     "rol": "TRABAJADOR",
     *     "activo": true
     * }
     */
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

    /**
     * Endpoint: Cambia la contraseña de un usuario existente.
     * <p>
     * Permite a los usuarios actualizar su contraseña.
     * La nueva contraseña se encripta antes de almacenarse.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     * @example
     * PATCH /api/v1/usuarios/USU001/password
     * Body: { "nuevaPassword": "NuevaContraseña456" }
     */
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

    /**
     * Endpoint: Elimina un usuario del sistema.
     * <p>
     * Realiza una eliminación lógica del usuario, marcándolo como inactivo
     * o eliminando permanentemente según la configuración del sistema.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     * @example DELETE /api/v1/usuarios/USU001
     */
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