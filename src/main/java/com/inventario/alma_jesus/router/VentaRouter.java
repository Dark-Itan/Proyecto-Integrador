package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.VentaController;
import io.javalin.Javalin;

public class VentaRouter {
    private VentaController ventaController = new VentaController();

    public void configureRoutes(Javalin app) {

        // LISTAR VENTAS
        app.get("/api/v1/ventas", ventaController::listarVentas);

        // OBTENER VENTA ESPECÍFICA
        app.get("/api/v1/ventas/{id}", ventaController::obtenerVentaPorId);

        // REGISTRAR VENTA
        app.post("/api/v1/ventas", ventaController::registrarVenta);

        // ELIMINAR VENTA
        app.delete("/api/v1/ventas/{id}", ventaController::eliminarVenta);

        // ✅ NUEVA RUTA: BUSCAR PRODUCTO EN PEDIDOS
        app.get("/api/v1/ventas/buscar-producto", ventaController::buscarProductoEnPedidos);

        System.out.println("Rutas de ventas configuradas");
        System.out.println("GET    /api/v1/ventas                 - Listar ventas");
        System.out.println("GET    /api/v1/ventas/{id}            - Obtener venta por ID");
        System.out.println("POST   /api/v1/ventas                 - Registrar venta");
        System.out.println("DELETE /api/v1/ventas/{id}            - Eliminar venta");
        System.out.println("GET    /api/v1/ventas/buscar-producto - Buscar producto en pedidos"); // ✅ Nueva
    }
}