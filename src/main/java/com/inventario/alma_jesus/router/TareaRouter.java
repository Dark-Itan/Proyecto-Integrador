package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.TareaController;
import io.javalin.Javalin;

public class TareaRouter {
    public static void configureRoutes(Javalin app) {
        TareaController controller = new TareaController();

        // Endpoint 50: Listar tareas (admin)
        app.get("/api/v1/tareas", controller::listarTareas);

        // NUEVO: Listar tareas por trabajador
        app.get("/api/v1/tareas/trabajador/{trabajadorId}", controller::listarTareasPorTrabajador);

        // Endpoint 51: Obtener tarea
        app.get("/api/v1/tareas/{id}", controller::obtenerTarea);

        // Endpoint 52: Crear tarea
        app.post("/api/v1/tareas", controller::crearTarea);

        // Endpoint 53: Actualizar estado
        app.put("/api/v1/tareas/{id}/estado", controller::actualizarEstado);

        // Endpoint 54: Eliminar tarea
        app.delete("/api/v1/tareas/{id}", controller::eliminarTarea);

        // Método adicional: Actualizar tarea completa
        app.put("/api/v1/tareas/{id}", controller::actualizarTarea);
    }
}