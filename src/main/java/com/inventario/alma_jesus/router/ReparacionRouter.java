package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.ReparacionController;
import io.javalin.Javalin;

public class ReparacionRouter {
    private final ReparacionController reparacionController;

    public ReparacionRouter() {
        this.reparacionController = new ReparacionController();
    }

    public void configureRoutes(Javalin app) {
        System.out.println("🛣️ [REPARACION-ROUTER] Configurando 8 endpoints de reparaciones");

        // TODOS LOS ENDPOINTS QUE PIDES:
        app.get("/api/v1/reparaciones", reparacionController::listarReparaciones);
        app.get("/api/v1/reparaciones/{id}", reparacionController::obtenerReparacion);
        app.post("/api/v1/reparaciones", reparacionController::crearReparacion);
        app.put("/api/v1/reparaciones/{id}", reparacionController::actualizarReparacion); // NUEVO ENDPOINT
        app.put("/api/v1/reparaciones/{id}/estado", reparacionController::cambiarEstado);
        app.get("/api/v1/reparaciones/{id}/historial", reparacionController::obtenerHistorial);
        app.get("/api/v1/reparaciones/{id}/recibo", reparacionController::generarRecibo);
        app.delete("/api/v1/reparaciones/{id}", reparacionController::eliminarReparacion);

        System.out.println("[REPARACION-ROUTER] 8 endpoints configurados exitosamente");
    }
}