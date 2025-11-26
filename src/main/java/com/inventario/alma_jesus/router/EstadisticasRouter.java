package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.EstadisticasController;
import com.inventario.alma_jesus.service.EstadisticasService;
import com.inventario.alma_jesus.repository.EstadisticasRepository;
import io.javalin.Javalin;

public class EstadisticasRouter {
    public void configureRoutes(Javalin app) {
        EstadisticasRepository repository = new EstadisticasRepository();
        EstadisticasService service = new EstadisticasService(repository);
        EstadisticasController controller = new EstadisticasController(service);

        app.get("/api/v1/dashboard/stats", controller.obtenerEstadisticasDashboard);
        app.get("/api/v1/analytics/venta", controller.obtenerDatosGraficaVentas);
        app.get("/api/v1/analytics/reparaciones", controller.obtenerDatosGraficaReparaciones);
        app.get("/api/v1/analytics/materiales", controller.obtenerDatosGraficaMateriales);
    }
}