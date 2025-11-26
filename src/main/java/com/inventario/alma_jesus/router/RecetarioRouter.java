package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.RecetarioController;
import com.inventario.alma_jesus.service.RecetarioService;
import com.inventario.alma_jesus.repository.RecetarioRepository;
import io.javalin.Javalin;

public class RecetarioRouter {
    public void configureRoutes(Javalin app) {
        RecetarioRepository repository = new RecetarioRepository();
        RecetarioService service = new RecetarioService(repository);
        RecetarioController controller = new RecetarioController(service);

        app.get("/api/v1/recetas", controller.listarRecetas);
        app.get("/api/v1/recetas/{id}", controller.obtenerReceta);
        app.post("/api/v1/recetas", controller.crearReceta);
        app.put("/api/v1/recetas/{id}", controller.actualizarReceta);
        app.delete("/api/v1/recetas/{id}", controller.eliminarReceta);
    }
}