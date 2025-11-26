package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.MateriaPrimaController;
import io.javalin.Javalin;

public class MateriaPrimaRouter {
    public static void configureRoutes(Javalin app) {
        MateriaPrimaController controller = new MateriaPrimaController();

        // Endpoint 25: Listar materiales
        app.get("/api/v1/materiales", controller::listarMateriales);

        // Endpoint 26: Obtener material
        app.get("/api/v1/materiales/{id}", controller::obtenerMaterial);

        // Endpoint 27: Crear material
        app.post("/api/v1/materiales", controller::crearMaterial);

        // Endpoint 28: Editar material completo
        app.put("/api/v1/materiales/{id}", controller::editarMaterial);

        // Endpoint 29: Actualizar stock
        app.put("/api/v1/materiales/{id}/stock", controller::actualizarStock);

        // Endpoint 30: Ver historial
        app.get("/api/v1/materiales/{id}/movimientos", controller::obtenerHistorial);

        // Endpoint 31: Eliminar material
        app.delete("/api/v1/materiales/{id}", controller::eliminarMaterial);
    }
}