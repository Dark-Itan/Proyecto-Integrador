package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.EstadisticasController;
import com.inventario.alma_jesus.service.EstadisticasService;
import com.inventario.alma_jesus.repository.EstadisticasRepository;
import io.javalin.Javalin;

/**
 * Router para configurar las rutas relacionadas con estadísticas y análisis del sistema.
 * Define los endpoints de la API para el módulo de estadísticas y dashboard.
 *
 * @author Alma & Jesús
 * @version 1.0
 * @since 2024
 */
public class EstadisticasRouter {

    /**
     * Configura todas las rutas relacionadas con estadísticas en la aplicación Javalin.
     * Inicializa las dependencias necesarias (Repository, Service, Controller)
     * y define los endpoints de la API.
     *
     * @param app Instancia de la aplicación Javalin donde se registrarán las rutas.
     * @throws NullPointerException Si la app proporcionada es null.
     */
    public void configureRoutes(Javalin app) {
        if (app == null) {
            throw new NullPointerException("La aplicación Javalin no puede ser null");
        }

        // Inicialización de dependencias siguiendo el patrón de inyección
        EstadisticasRepository repository = new EstadisticasRepository();
        EstadisticasService service = new EstadisticasService(repository);
        EstadisticasController controller = new EstadisticasController(service);

        // Rutas para estadísticas del dashboard
        app.get("/api/v1/dashboard/stats", controller.obtenerEstadisticasDashboard);

        // Rutas para análisis y gráficos
        app.get("/api/v1/analytics/venta", controller.obtenerDatosGraficaVentas);
        app.get("/api/v1/analytics/reparaciones", controller.obtenerDatosGraficaReparaciones);
        app.get("/api/v1/analytics/materiales", controller.obtenerDatosGraficaMateriales);
    }
}