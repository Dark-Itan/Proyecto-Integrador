package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.UsuarioController;
import io.javalin.Javalin;

public class UsuarioRouter {
    private UsuarioController usuarioController = new UsuarioController();

    public void configureRoutes(Javalin app) {
        // Endpoint 4: /api/v1/usuarios - Listar usuarios
        app.get("/api/v1/usuarios", usuarioController::listarUsuarios);

        // Endpoint 5: /api/v1/usuarios/{id} - Obtener usuario especifico
        app.get("/api/v1/usuarios/{id}", usuarioController::obtenerUsuarioPorId);

        // Endpoint 6: /api/v1/usuarios - Crear usuario
        app.post("/api/v1/usuarios", usuarioController::crearUsuario);

        // Endpoint 7: /api/v1/usuarios/{id}/password - Cambiar contrasena
        app.put("/api/v1/usuarios/{id}/password", usuarioController::cambiarPassword);

        // ✅ NUEVO ENDPOINT 8: /api/v1/usuarios/{id} - Eliminar usuario
        app.delete("/api/v1/usuarios/{id}", usuarioController::eliminarUsuario);
    }
}