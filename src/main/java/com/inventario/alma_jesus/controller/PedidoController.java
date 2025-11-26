package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.model.Pedido;
import com.inventario.alma_jesus.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.HashMap;
import java.util.Map;

public class PedidoController {
    private final PedidoService pedidoService;
    private final ObjectMapper objectMapper;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
        this.objectMapper = new ObjectMapper();
    }

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