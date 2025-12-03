package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.ReparacionController;
import io.javalin.Javalin;

/**
 * Router para configurar las rutas relacionadas con la gestión de reparaciones en el sistema.
 * Define los endpoints CRUD completos para operaciones de reparación, incluyendo gestión de estados,
 * historial, generación de recibos y administración del ciclo de vida de reparaciones.
 *
 * @author Alma & Jesús
 * @version 1.0
 * @since 2024
 */
public class ReparacionRouter {
    private final ReparacionController reparacionController;

    /**
     * Constructor que inicializa el router de reparaciones.
     * Crea una nueva instancia del controlador de reparaciones para manejar las solicitudes.
     */
    public ReparacionRouter() {
        this.reparacionController = new ReparacionController();
        System.out.println("🔧 [REPARACION-ROUTER] Router de reparaciones inicializado");
    }

    /**
     * Configura todas las rutas relacionadas con reparaciones en la aplicación Javalin.
     * Establece los 8 endpoints RESTful para operaciones CRUD completas sobre reparaciones.
     *
     * <p><b>Endpoints configurados:</b></p>
     * <ol>
     *   <li>GET /api/v1/reparaciones - Listar todas las reparaciones</li>
     *   <li>GET /api/v1/reparaciones/{id} - Obtener una reparación específica</li>
     *   <li>POST /api/v1/reparaciones - Crear una nueva reparación</li>
     *   <li>PUT /api/v1/reparaciones/{id} - Actualizar una reparación completa</li>
     *   <li>PUT /api/v1/reparaciones/{id}/estado - Cambiar estado de reparación</li>
     *   <li>GET /api/v1/reparaciones/{id}/historial - Obtener historial de reparación</li>
     *   <li>GET /api/v1/reparaciones/{id}/recibo - Generar recibo de reparación</li>
     *   <li>DELETE /api/v1/reparaciones/{id} - Eliminar una reparación</li>
     * </ol>
     *
     * @param app Instancia de la aplicación Javalin donde se registrarán las rutas.
     * @throws IllegalArgumentException Si la app proporcionada es null.
     *
     * @example
     * <pre>{@code
     * Javalin app = Javalin.create();
     * ReparacionRouter router = new ReparacionRouter();
     * router.configureRoutes(app);
     * app.start(8080);
     * }</pre>
     */
    public void configureRoutes(Javalin app) {
        if (app == null) {
            throw new IllegalArgumentException("La instancia de Javalin no puede ser null");
        }

        System.out.println("🛣️ [REPARACION-ROUTER] Configurando 8 endpoints de reparaciones");

        // 1. Listar todas las reparaciones (operación READ - colección)
        app.get("/api/v1/reparaciones", reparacionController::listarReparaciones);

        // 2. Obtener una reparación específica por ID (operación READ - recurso)
        app.get("/api/v1/reparaciones/{id}", reparacionController::obtenerReparacion);

        // 3. Crear una nueva reparación (operación CREATE)
        app.post("/api/v1/reparaciones", reparacionController::crearReparacion);

        // 4. Actualizar una reparación completa (operación UPDATE - reemplazo total)
        app.put("/api/v1/reparaciones/{id}", reparacionController::actualizarReparacion);

        // 5. Cambiar estado de una reparación (operación UPDATE - parcial para estado)
        app.put("/api/v1/reparaciones/{id}/estado", reparacionController::cambiarEstado);

        // 6. Obtener historial de una reparación (operación READ - subrecurso de historial)
        app.get("/api/v1/reparaciones/{id}/historial", reparacionController::obtenerHistorial);

        // 7. Generar recibo de una reparación (operación READ - generación de documento)
        app.get("/api/v1/reparaciones/{id}/recibo", reparacionController::generarRecibo);

        // 8. Eliminar una reparación (operación DELETE)
        app.delete("/api/v1/reparaciones/{id}", reparacionController::eliminarReparacion);

        System.out.println("✅ [REPARACION-ROUTER] 8 endpoints configurados exitosamente");
    }

    /**
     * Obtiene información sobre todas las rutas configuradas para reparaciones.
     *
     * @return Array de strings con las rutas configuradas en formato "MÉTODO RUTA - Descripción".
     */
    public String[] getRutasDetalladas() {
        return new String[] {
                "GET    /api/v1/reparaciones - Listar todas las reparaciones",
                "GET    /api/v1/reparaciones/{id} - Obtener una reparación específica",
                "POST   /api/v1/reparaciones - Crear una nueva reparación",
                "PUT    /api/v1/reparaciones/{id} - Actualizar una reparación completa",
                "PUT    /api/v1/reparaciones/{id}/estado - Cambiar estado de reparación",
                "GET    /api/v1/reparaciones/{id}/historial - Obtener historial de reparación",
                "GET    /api/v1/reparaciones/{id}/recibo - Generar recibo de reparación",
                "DELETE /api/v1/reparaciones/{id} - Eliminar una reparación"
        };
    }

    /**
     * Verifica si una ruta dada corresponde a un endpoint de reparaciones.
     *
     * @param ruta Ruta a verificar (ej: "/api/v1/reparaciones").
     * @return true si la ruta pertenece a este router, false en caso contrario.
     */
    public boolean esRutaReparacion(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            return false;
        }

        return ruta.startsWith("/api/v1/reparaciones");
    }

    /**
     * Obtiene el número total de endpoints configurados por este router.
     *
     * @return Cantidad de endpoints configurados.
     */
    public int getTotalEndpoints() {
        return 8;
    }

    /**
     * Obtiene el controlador asociado a este router.
     *
     * @return Instancia de ReparacionController utilizada por este router.
     */
    public ReparacionController getController() {
        return reparacionController;
    }
}