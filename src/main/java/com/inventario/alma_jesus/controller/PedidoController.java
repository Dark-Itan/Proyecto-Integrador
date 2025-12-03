package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.model.Pedido;
import com.inventario.alma_jesus.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para gestionar las operaciones relacionadas con pedidos de clientes.
 * <p>
 * Esta clase maneja el ciclo de vida completo de los pedidos, desde la creación
 * hasta el seguimiento de etapas, filtrado por fecha y eliminación.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see PedidoService
 * @see Pedido
 */
public class PedidoController {
    /**
     * Servicio que contiene la lógica de negocio para la gestión de pedidos.
     */
    private final PedidoService pedidoService;
    /**
     * Mapper para convertir entre JSON y objetos Java.
     */
    private final ObjectMapper objectMapper;
    /**
     * Constructor del controlador de pedidos.
     *
     * @param pedidoService Instancia del servicio de pedidos
     */
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Handler para listar pedidos filtrados por fecha específica.
     * <p>
     * Retorna todos los pedidos cuya fecha coincide con la proporcionada
     * en el parámetro de consulta. Formato de fecha: YYYY-MM-DD.
     * </p>
     *
     * @param fecha Parámetro de consulta requerido con la fecha en formato YYYY-MM-DD
     *
     * @example
     * Petición GET: /api/v1/pedidos/fecha?fecha=2025-11-23
     *
     * Respuesta exitosa (200):
     * <pre>
     * {
     *     "success": true,
     *     "data": [...],
     *     "total": 3,
     *     "fecha": "2025-11-23",
     *     "message": "3 pedidos encontrados para la fecha 2025-11-23"
     * }
     * </pre>
     */
    // NUEVO HANDLER: Listar pedidos por fecha
    public Handler listarPedidosPorFecha = new Handler() {
        @Override
        public void handle(Context ctx) throws Exception {
            try {
                // Obtener fecha del query parameter
                String fecha = ctx.queryParam("fecha");

                // Validar que la fecha fue proporcionada
                if (fecha == null || fecha.trim().isEmpty()) {
                    Map<String, Object> error = new HashMap<>();
                    error.put("success", false);
                    error.put("message", "El parámetro 'fecha' es requerido. Ejemplo: ?fecha=2025-11-23");
                    ctx.json(error).status(400);
                    return;
                }

                // Llamar al servicio para obtener pedidos por fecha
                var pedidos = pedidoService.listarPedidosPorFecha(fecha);

                // Construir respuesta exitosa
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", pedidos);
                response.put("total", pedidos.size());
                response.put("fecha", fecha);
                response.put("message", pedidos.size() + " pedidos encontrados para la fecha " + fecha);

                ctx.json(response).status(200);

            } catch (Exception e) {
                // Manejar errores
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", e.getMessage());
                ctx.json(error).status(400);
            }
        }
    };

    /**
     * Handler para listar todos los pedidos del sistema.
     * <p>
     * Retorna la lista completa de pedidos sin filtros, ordenados
     * por fecha de creación descendente.
     * </p>
     *
     * @example
     * Petición GET: /api/v1/pedidos
     */
    public Handler listarPedidos = new Handler() {
        @Override
        public void handle(Context ctx) throws Exception {
            try {
                var pedidos = pedidoService.listarPedidos();

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", pedidos);
                response.put("total", pedidos.size());

                ctx.json(response).status(200);
            } catch (Exception e) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", e.getMessage());
                ctx.json(error).status(500);
            }
        }
    };

    /**
     * Handler para obtener un pedido específico por ID.
     * <p>
     * Retorna todos los detalles de un pedido incluyendo items,
     * historial de etapas y datos del cliente.
     * </p>
     *
     * @param id Parámetro de ruta con el ID numérico del pedido
     *
     * @throws NumberFormatException Si el ID no es un número válido
     */
    public Handler obtenerPedido = new Handler() {
        @Override
        public void handle(Context ctx) throws Exception {
            try {
                String idParam = ctx.pathParam("id");
                Long id = Long.parseLong(idParam);
                var pedido = pedidoService.obtenerPedido(id);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", pedido);

                ctx.json(response).status(200);
            } catch (NumberFormatException e) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "ID inválido: debe ser un número");
                ctx.json(error).status(400);
            } catch (Exception e) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", e.getMessage());
                ctx.json(error).status(404);
            }
        }
    };

    /**
     * Handler para crear un nuevo pedido.
     * <p>
     * Registra un pedido completo incluyendo items, datos del cliente
     * y configuración inicial de la primera etapa.
     * </p>
     *
     * @example
     * Petición POST: /api/v1/pedidos
     * Body:
     * <pre>
     * {
     *     "cliente": "Juan Pérez",
     *     "telefono": "555-1234",
     *     "correo": "juan@email.com",
     *     "descripcion": "Mesa de centro en roble",
     *     "etapa": "recepcion",
     *     "items": [
     *         {"producto": "Mesa de centro", "cantidad": 1, "precio": 2500}
     *     ]
     * }
     * </pre>
     */
    public Handler crearPedido = new Handler() {
        @Override
        public void handle(Context ctx) throws Exception {
            try {
                String body = ctx.body();
                Pedido pedido = objectMapper.readValue(body, Pedido.class);
                var nuevoPedido = pedidoService.crearPedido(pedido);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Pedido creado exitosamente");
                response.put("data", nuevoPedido);

                ctx.json(response).status(201);
            } catch (Exception e) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", e.getMessage());
                ctx.json(error).status(400);
            }
        }
    };

    /**
     * Handler para actualizar la etapa de un pedido.
     * <p>
     * Avanza o cambia la etapa de un pedido en el flujo de trabajo,
     * registrando la fecha del cambio y notas opcionales.
     * </p>
     *
     * @param id Parámetro de ruta con el ID del pedido
     * @param etapa Nueva etapa a asignar (requerida)
     * @param notas Notas opcionales sobre el cambio de etapa
     *
     * @example
     * Petición PUT: /api/v1/pedidos/789/etapa
     * Body:
     * <pre>
     * {
     *     "etapa": "produccion",
     *     "notas": "Asignado al equipo de carpintería"
     * }
     * </pre>
     */
    public Handler actualizarEtapa = new Handler() {
        @Override
        public void handle(Context ctx) throws Exception {
            try {
                String idParam = ctx.pathParam("id");
                Long id = Long.parseLong(idParam);

                Map<String, String> requestBody = objectMapper.readValue(ctx.body(), Map.class);
                String nuevaEtapa = requestBody.get("etapa");
                String notas = requestBody.get("notas");

                if (nuevaEtapa == null || nuevaEtapa.trim().isEmpty()) {
                    throw new RuntimeException("La nueva etapa es requerida");
                }

                var pedidoActualizado = pedidoService.actualizarEtapa(id, nuevaEtapa, notas);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Etapa del pedido actualizada exitosamente");
                response.put("data", pedidoActualizado);

                ctx.json(response).status(200);
            } catch (NumberFormatException e) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "ID inválido: debe ser un número");
                ctx.json(error).status(400);
            } catch (Exception e) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", e.getMessage());
                ctx.json(error).status(400);
            }
        }
    };

    /**
     * Handler para eliminar un pedido.
     * <p>
     * Elimina permanentemente un pedido del sistema.
     * Solo se permite para pedidos en etapa inicial o cancelados.
     * </p>
     *
     * @param id Parámetro de ruta con el ID del pedido a eliminar
     */
    public Handler eliminarPedido = new Handler() {
        @Override
        public void handle(Context ctx) throws Exception {
            try {
                String idParam = ctx.pathParam("id");
                Long id = Long.parseLong(idParam);
                boolean eliminado = pedidoService.eliminarPedido(id);

                if (eliminado) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Pedido eliminado exitosamente");
                    ctx.json(response).status(200);
                } else {
                    Map<String, Object> error = new HashMap<>();
                    error.put("success", false);
                    error.put("message", "No se pudo eliminar el pedido");
                    ctx.json(error).status(400);
                }
            } catch (NumberFormatException e) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "ID inválido: debe ser un número");
                ctx.json(error).status(400);
            } catch (Exception e) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", e.getMessage());
                ctx.json(error).status(400);
            }
        }
    };
}