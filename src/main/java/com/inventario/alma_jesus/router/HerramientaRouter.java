package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.HerramientaController;
import io.javalin.Javalin;

/**
 * Router para la configuración de rutas de gestión de herramientas.
 * <p>
 * Esta clase define y configura todas las rutas relacionadas con la gestión
 * de herramientas del taller en la API REST. Incluye operaciones CRUD completas
 * y acciones específicas para el control de inventario de herramientas.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see HerramientaController
 */
public class HerramientaRouter {

    /**
     * Configura todas las rutas de gestión de herramientas en la aplicación Javalin.
     * <p>
     * Define los siguientes endpoints bajo el path base /api/v1/herramientas:
     * <ul>
     *   <li>GET /api/v1/herramientas - Listar todas las herramientas</li>
     *   <li>GET /api/v1/herramientas/{idONombre} - Obtener herramienta específica por ID o nombre</li>
     *   <li>POST /api/v1/herramientas - Crear nueva herramienta</li>
     *   <li>PUT /api/v1/herramientas/{idONombre}/stock - Actualizar cantidad en stock</li>
     *   <li>PUT /api/v1/herramientas/{idONombre}/tomar - Tomar/retirar herramienta del inventario</li>
     *   <li>PUT /api/v1/herramientas/{idONombre}/devolver - Devolver herramienta al inventario</li>
     *   <li>DELETE /api/v1/herramientas/{idONombre} - Eliminar herramienta (lógicamente)</li>
     * </ul>
     * El parámetro {idONombre} puede ser un ID numérico o el nombre de la herramienta.
     * </p>
     *
     * @param app Instancia de {@link Javalin} donde se configurarán las rutas
     * @throws IllegalArgumentException Si la aplicación Javalin es nula
     */
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