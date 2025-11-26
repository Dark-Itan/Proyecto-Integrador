package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.HerramientaController;
import io.javalin.Javalin;

public class HerramientaRouter {
    public static void configureRoutes(Javalin app) {
        HerramientaController controller = new HerramientaController();

        // Endpoint 18: Listar herramientas
        app.get("/api/v1/herramientas", controller::listarHerramientas);

        // Endpoint 19: Obtener herramienta por ID o nombre
        app.get("/api/v1/herramientas/{idONombre}", controller::obtenerHerramienta);

        // Endpoint 20: Crear herramienta
        app.post("/api/v1/herramientas", controller::crearHerramienta);

        // Endpoint 21: Actualizar stock
        app.put("/api/v1/herramientas/{idONombre}/stock", controller::actualizarStock);

        // Endpoint 22: Tomar herramienta
        app.put("/api/v1/herramientas/{idONombre}/tomar", controller::tomarHerramienta);

        // Endpoint 23: Devolver herramienta
        app.put("/api/v1/herramientas/{idONombre}/devolver", controller::devolverHerramienta);

        // Endpoint 24: Eliminar herramienta
        app.delete("/api/v1/herramientas/{idONombre}", controller::eliminarHerramienta);
    }
}