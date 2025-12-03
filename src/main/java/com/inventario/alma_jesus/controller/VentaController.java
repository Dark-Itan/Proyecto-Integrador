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

/**
 * Controlador para gestionar las operaciones relacionadas con ventas.
 * <p>
 * Esta clase maneja el registro, consulta y administración de ventas,
 * incluyendo la búsqueda de productos en pedidos existentes.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see VentaService
 */
public class VentaController {
    private VentaService ventaService = new VentaService();
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Endpoint: Lista todas las ventas registradas en el sistema.
     * <p>
     * Retorna una lista completa de ventas con sus detalles incluyendo
     * productos, cantidades, precios y fechas de registro.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     * @example GET /api/v1/ventas
     */
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

    /**
     * Endpoint: Obtiene una venta específica por su ID.
     * <p>
     * Busca y retorna todos los detalles de una venta incluyendo
     * información del cliente, productos vendidos y totales.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     * @example GET /api/v1/ventas/123
     */
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

    /**
     * Endpoint: Registra una nueva venta en el sistema.
     * <p>
     * Procesa una venta completa incluyendo productos, cantidades,
     * descuentos y actualiza el inventario automáticamente.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     * @example
     * POST /api/v1/ventas
     * Body:
     * {
     *     "cliente_id": "CLI001",
     *     "vendedor_id": "VEN001",
     *     "productos": [
     *         {
     *             "producto_id": 1,
     *             "cantidad": 2,
     *             "precio_unitario": 150.50
     *         }
     *     ],
     *     "descuento": 0.0,
     *     "metodo_pago": "EFECTIVO"
     * }
     */
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

    /**
     * Endpoint: Elimina una venta del sistema.
     * <p>
     * Realiza una eliminación completa de la venta, incluyendo
     * la reversión de inventario si corresponde.
     * Solo disponible para ventas recientes sin facturación electrónica.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     * @example DELETE /api/v1/ventas/123
     */
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

    /**
     * Endpoint: Busca un producto en pedidos por nombre.
     * <p>
     * Realiza una búsqueda en los pedidos existentes para encontrar
     * productos por nombre o parte del nombre. Útil para autocompletar
     * en interfaces de usuario.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     * @example GET /api/v1/ventas/buscar-producto?nombre=mesa
     */
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