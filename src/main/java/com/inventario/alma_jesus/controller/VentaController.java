package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.service.VentaService;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import com.inventario.alma_jesus.repository.DatabaseConnection;

public class VentaController {
    private VentaService ventaService = new VentaService();
    private ObjectMapper objectMapper = new ObjectMapper();

    public void listarVentas(Context ctx) {
        try {
            Map<String, Object> result = ventaService.listarVentas();

            if ((Boolean) result.get("success")) {
                ctx.status(200).json(result);
            } else {
                ctx.status(500).json(result);
            }

        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno al listar ventas"
            ));
        }
    }

    public void obtenerVentaPorId(Context ctx) {
        try {
            String idParam = ctx.pathParam("id");

            if (idParam == null || idParam.isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "ID de venta es requerido"
                ));
                return;
            }

            int id = Integer.parseInt(idParam);
            Map<String, Object> result = ventaService.obtenerVentaPorId(id);

            if ((Boolean) result.get("success")) {
                ctx.status(200).json(result);
            } else {
                ctx.status(404).json(result);
            }

        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "ID de venta debe ser un número válido"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno al obtener venta"
            ));
        }
    }

    public void registrarVenta(Context ctx) {
        try {
            String body = ctx.body();
            System.out.println(" Request recibido en /api/v1/ventas: " + body);

            if (body == null || body.trim().isEmpty()) {
                System.out.println(" Body vacío");
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "El cuerpo de la petición no puede estar vacío"
                ));
                return;
            }

            Map<String, Object> bodyMap = objectMapper.readValue(body, Map.class);
            System.out.println(" Datos parseados: " + bodyMap);

            if (bodyMap == null || bodyMap.isEmpty()) {
                System.out.println(" BodyMap vacío");
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "Datos de la venta son requeridos"
                ));
                return;
            }

            Map<String, Object> result = ventaService.registrarVenta(bodyMap);
            System.out.println(" Resultado del servicio: " + result);

            if ((Boolean) result.get("success")) {
                ctx.status(201).json(result);
            } else {
                ctx.status(400).json(result);
            }

        } catch (Exception e) {
            System.out.println("❌ Error en VentaController.registrarVenta: " + e.getMessage());
            e.printStackTrace();
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error en el formato JSON: " + e.getMessage()
            ));
        }
    }

    public void eliminarVenta(Context ctx) {
        try {
            String idParam = ctx.pathParam("id");

            if (idParam == null || idParam.isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "ID de venta es requerido"
                ));
                return;
            }

            int id = Integer.parseInt(idParam);

            // Llamar al servicio para eliminar la venta
            boolean eliminado = ventaService.eliminarVenta(id);

            if (eliminado) {
                ctx.status(200).json(Map.of(
                        "success", true,
                        "message", "Venta eliminada exitosamente"
                ));
            } else {
                ctx.status(404).json(Map.of(
                        "success", false,
                        "message", "Venta no encontrada"
                ));
            }

        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "ID de venta debe ser un número válido"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno al eliminar venta"
            ));
        }
    }

    // NUEVO MÉTODO: Buscar producto en pedidos por nombre
    public void buscarProductoEnPedidos(Context ctx) {
        try {
            String nombreProducto = ctx.queryParam("nombre");

            if (nombreProducto == null || nombreProducto.trim().isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "Nombre de producto es requerido"
                ));
                return;
            }

            // Buscar en pedido_productos por nombre de producto
            String sql = "SELECT DISTINCT producto_id, producto_nombre FROM pedido_productos WHERE producto_nombre LIKE ? LIMIT 1";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, "%" + nombreProducto + "%");
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    Map<String, Object> producto = new HashMap<>();
                    producto.put("id", rs.getInt("producto_id"));
                    producto.put("nombre", rs.getString("producto_nombre"));

                    ctx.status(200).json(Map.of(
                            "success", true,
                            "producto", producto,
                            "message", "Producto encontrado en pedidos"
                    ));
                } else {
                    ctx.status(404).json(Map.of(
                            "success", false,
                            "message", "Producto no encontrado en pedidos: " + nombreProducto
                    ));
                }
            }

        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al buscar producto: " + e.getMessage()
            ));
        }
    }
}