package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.VentaController;
import io.javalin.Javalin;

/**
 * Router para configurar las rutas relacionadas con la gestión de ventas en el sistema.
 * Define los endpoints CRUD para operaciones de ventas, incluyendo registro, consulta,
 * eliminación y búsqueda específica de productos en pedidos.
 *
 * @author Alma & Jesús
 * @version 1.0
 * @since 2024
 */
public class VentaRouter {
    private VentaController ventaController = new VentaController();

    /**
     * Configura todas las rutas relacionadas con ventas en la aplicación Javalin.
     * Establece los 5 endpoints RESTful para operaciones sobre ventas.
     *
     * <p><b>Endpoints configurados:</b></p>
     * <ul>
     *   <li>GET /api/v1/ventas - Listar todas las ventas</li>
     *   <li>GET /api/v1/ventas/{id} - Obtener venta específica por ID</li>
     *   <li>POST /api/v1/ventas - Registrar nueva venta</li>
     *   <li>DELETE /api/v1/ventas/{id} - Eliminar venta</li>
     *   <li>GET /api/v1/ventas/buscar-producto - Buscar producto en pedidos (nueva funcionalidad)</li>
     * </ul>
     *
     * @param app Instancia de la aplicación Javalin donde se registrarán las rutas.
     */
    public void configureRoutes(Javalin app) {

        // LISTAR VENTAS
        app.get("/api/v1/ventas", ventaController::listarVentas);

        // OBTENER VENTA ESPECÍFICA
        app.get("/api/v1/ventas/{id}", ventaController::obtenerVentaPorId);

        // REGISTRAR VENTA
        app.post("/api/v1/ventas", ventaController::registrarVenta);

        // ELIMINAR VENTA
        app.delete("/api/v1/ventas/{id}", ventaController::eliminarVenta);

        // NUEVA RUTA: BUSCAR PRODUCTO EN PEDIDOS
        app.get("/api/v1/ventas/buscar-producto", ventaController::buscarProductoEnPedidos);

        System.out.println("Rutas de ventas configuradas");
        System.out.println("GET    /api/v1/ventas                 - Listar ventas");
        System.out.println("GET    /api/v1/ventas/{id}            - Obtener venta por ID");
        System.out.println("POST   /api/v1/ventas                 - Registrar venta");
        System.out.println("DELETE /api/v1/ventas/{id}            - Eliminar venta");
        System.out.println("GET    /api/v1/ventas/buscar-producto - Buscar producto en pedidos"); //Nueva
    }
}