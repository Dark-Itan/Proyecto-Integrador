package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.MateriaPrimaController;
import io.javalin.Javalin;

/**
 * Router para configurar las rutas relacionadas con la gestión de materia prima en el sistema de inventario.
 * Define los endpoints CRUD completos para la administración de materiales, incluyendo operaciones
 * de stock, historial de movimientos y gestión de inventario.
 *
 * @author Alma & Jesús
 * @version 1.0
 * @since 2024
 */
public class MateriaPrimaRouter {

    /**
     * Configura todas las rutas relacionadas con materia prima en la aplicación Javalin.
     * Establece los endpoints RESTful para operaciones CRUD completas sobre materiales.
     *
     * <p><b>Endpoints configurados:</b></p>
     * <ol start="25">
     *   <li>GET /api/v1/materiales - Listar todos los materiales (Endpoint 25)</li>
     *   <li>GET /api/v1/materiales/{id} - Obtener un material específico (Endpoint 26)</li>
     *   <li>POST /api/v1/materiales - Crear un nuevo material (Endpoint 27)</li>
     *   <li>PUT /api/v1/materiales/{id} - Editar un material completo (Endpoint 28)</li>
     *   <li>PUT /api/v1/materiales/{id}/stock - Actualizar stock de material (Endpoint 29)</li>
     *   <li>GET /api/v1/materiales/{id}/movimientos - Ver historial de movimientos (Endpoint 30)</li>
     *   <li>DELETE /api/v1/materiales/{id} - Eliminar un material (Endpoint 31)</li>
     * </ol>
     *
     * @param app Instancia de la aplicación Javalin donde se registrarán las rutas.
     * @throws IllegalArgumentException Si la app proporcionada es null.
     *
     * @example
     * <pre>{@code
     * Javalin app = Javalin.create();
     * MateriaPrimaRouter.configureRoutes(app);
     * app.start(8080);
     * }</pre>
     */
    public static void configureRoutes(Javalin app) {
        if (app == null) {
            throw new IllegalArgumentException("La instancia de Javalin no puede ser null");
        }

        MateriaPrimaController controller = new MateriaPrimaController();

        // Endpoint 25: Listar todos los materiales (operación READ - colección)
        app.get("/api/v1/materiales", controller::listarMateriales);

        // Endpoint 26: Obtener un material específico por ID (operación READ - recurso)
        app.get("/api/v1/materiales/{id}", controller::obtenerMaterial);

        // Endpoint 27: Crear un nuevo material (operación CREATE)
        app.post("/api/v1/materiales", controller::crearMaterial);

        // Endpoint 28: Editar un material completo (operación UPDATE - reemplazo total)
        app.put("/api/v1/materiales/{id}", controller::editarMaterial);

        // Endpoint 29: Actualizar stock de material (operación UPDATE - parcial para stock)
        app.put("/api/v1/materiales/{id}/stock", controller::actualizarStock);

        // Endpoint 30: Ver historial de movimientos del material (operación READ - historial)
        app.get("/api/v1/materiales/{id}/movimientos", controller::obtenerHistorial);

        // Endpoint 31: Eliminar un material (operación DELETE)
        app.delete("/api/v1/materiales/{id}", controller::eliminarMaterial);
    }

    /**
     * Obtiene información sobre todas las rutas configuradas para materia prima.
     *
     * @return Array de strings con las rutas configuradas en formato "MÉTODO RUTA - Descripción".
     */
    public static String[] getRutasDetalladas() {
        return new String[] {
                "GET    /api/v1/materiales - Listar todos los materiales (Endpoint 25)",
                "GET    /api/v1/materiales/{id} - Obtener un material específico (Endpoint 26)",
                "POST   /api/v1/materiales - Crear un nuevo material (Endpoint 27)",
                "PUT    /api/v1/materiales/{id} - Editar material completo (Endpoint 28)",
                "PUT    /api/v1/materiales/{id}/stock - Actualizar stock (Endpoint 29)",
                "GET    /api/v1/materiales/{id}/movimientos - Ver historial de movimientos (Endpoint 30)",
                "DELETE /api/v1/materiales/{id} - Eliminar material (Endpoint 31)"
        };
    }

    /**
     * Verifica si una ruta dada corresponde a un endpoint de materia prima.
     *
     * @param ruta Ruta a verificar (ej: "/api/v1/materiales").
     * @return true si la ruta pertenece a este router, false en caso contrario.
     */
    public static boolean esRutaMateriaPrima(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            return false;
        }

        return ruta.startsWith("/api/v1/materiales");
    }
}