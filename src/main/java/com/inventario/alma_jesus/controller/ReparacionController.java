package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.model.Reparacion;
import com.inventario.alma_jesus.service.ReparacionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import java.util.*;

@SuppressWarnings("unchecked")
public class ReparacionController {
    private final ReparacionService reparacionService;
    private final ObjectMapper objectMapper;

    public ReparacionController() {
        this.reparacionService = new ReparacionService();
        this.objectMapper = new ObjectMapper();
    }

    public void listarReparaciones(Context ctx) {
        try {
            System.out.println("🎯 [REPARACION-CONTROLLER] Listando reparaciones");

            String estado = ctx.queryParam("estado");
            String cliente = ctx.queryParam("cliente");
            String modelo = ctx.queryParam("modelo");

            List<Reparacion> reparaciones = reparacionService.listarReparaciones(estado, cliente, modelo);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", reparaciones);
            response.put("total", reparaciones.size());

            ctx.json(response).status(200);
            System.out.println("✅ [REPARACION-CONTROLLER] Listado exitoso: " + reparaciones.size() + " reparaciones");

        } catch (Exception e) {
            System.err.println("❌ [REPARACION-CONTROLLER] Error al listar: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al listar reparaciones: " + e.getMessage());

            ctx.json(error).status(500);
        }
    }

    public void obtenerReparacion(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            System.out.println("🎯 [REPARACION-CONTROLLER] Obteniendo reparación ID: " + id);

            Optional<Reparacion> reparacionOpt = reparacionService.obtenerReparacion(id);

            if (reparacionOpt.isPresent()) {
                Reparacion reparacion = reparacionOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", reparacion);
                response.put("saldoPendiente", reparacionService.calcularSaldoPendiente(reparacion));

                ctx.json(response).status(200);
                System.out.println("✅ [REPARACION-CONTROLLER] Reparación encontrada ID: " + id);
            } else {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Reparación no encontrada");

                ctx.json(error).status(404);
                System.out.println("⚠️ [REPARACION-CONTROLLER] Reparación no encontrada ID: " + id);
            }

        } catch (NumberFormatException e) {
            System.err.println("❌ [REPARACION-CONTROLLER] ID inválido");

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "ID de reparación inválido");

            ctx.json(error).status(400);
        } catch (Exception e) {
            System.err.println("❌ [REPARACION-CONTROLLER] Error al obtener: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al obtener reparación: " + e.getMessage());

            ctx.json(error).status(500);
        }
    }

    public void crearReparacion(Context ctx) {
        try {
            System.out.println("🎯 [REPARACION-CONTROLLER] Creando nueva reparación");

            Reparacion reparacion = objectMapper.readValue(ctx.body(), Reparacion.class);

            if (reparacion.getNombreCliente() == null || reparacion.getNombreCliente().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del cliente es requerido");
            }

            Reparacion reparacionCreada = reparacionService.crearReparacion(reparacion);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Reparación creada exitosamente");
            response.put("data", reparacionCreada);
            response.put("saldoPendiente", reparacionService.calcularSaldoPendiente(reparacionCreada));

            ctx.json(response).status(201);
            System.out.println("✅ [REPARACION-CONTROLLER] Reparación creada ID: " + reparacionCreada.getId());

        } catch (IllegalArgumentException e) {
            System.err.println("❌ [REPARACION-CONTROLLER] Error de validación: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());

            ctx.json(error).status(400);
        } catch (Exception e) {
            System.err.println("❌ [REPARACION-CONTROLLER] Error al crear: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al crear reparación: " + e.getMessage());

            ctx.json(error).status(500);
        }
    }

    // NUEVO MÉTODO: ACTUALIZAR REPARACIÓN COMPLETA
    public void actualizarReparacion(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            System.out.println("🎯 [REPARACION-CONTROLLER] Actualizando reparación ID: " + id);

            Reparacion reparacion = objectMapper.readValue(ctx.body(), Reparacion.class);
            reparacion.setId(id);

            boolean actualizado = reparacionService.actualizarReparacion(reparacion);

            if (actualizado) {
                Optional<Reparacion> reparacionActualizada = reparacionService.obtenerReparacion(id);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Reparación actualizada exitosamente");
                response.put("data", reparacionActualizada.orElse(null));

                ctx.json(response).status(200);
                System.out.println("✅ [REPARACION-CONTROLLER] Reparación actualizada ID: " + id);
            } else {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Reparación no encontrada o no se pudo actualizar");

                ctx.json(error).status(404);
                System.out.println("⚠️ [REPARACION-CONTROLLER] No se pudo actualizar ID: " + id);
            }

        } catch (IllegalArgumentException e) {
            System.err.println("❌ [REPARACION-CONTROLLER] Error de validación: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());

            ctx.json(error).status(400);
        } catch (Exception e) {
            System.err.println("❌ [REPARACION-CONTROLLER] Error al actualizar: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al actualizar reparación: " + e.getMessage());

            ctx.json(error).status(500);
        }
    }

    public void cambiarEstado(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            System.out.println("🎯 [REPARACION-CONTROLLER] Cambiando estado reparación ID: " + id);

            Map<String, String> requestBody = objectMapper.readValue(ctx.body(), Map.class);
            String nuevoEstado = requestBody.get("estado");

            if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
                throw new IllegalArgumentException("El estado es requerido");
            }

            boolean actualizado = reparacionService.cambiarEstado(id, nuevoEstado);

            if (actualizado) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Estado actualizado exitosamente");
                response.put("nuevoEstado", nuevoEstado);

                ctx.json(response).status(200);
                System.out.println("✅ [REPARACION-CONTROLLER] Estado actualizado ID: " + id + " -> " + nuevoEstado);
            } else {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Reparación no encontrada o no se pudo actualizar el estado");

                ctx.json(error).status(404);
                System.out.println("⚠️ [REPARACION-CONTROLLER] No se pudo cambiar estado ID: " + id);
            }

        } catch (IllegalArgumentException e) {
            System.err.println("❌ [REPARACION-CONTROLLER] Error de validación: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());

            ctx.json(error).status(400);
        } catch (Exception e) {
            System.err.println("❌ [REPARACION-CONTROLLER] Error al cambiar estado: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al cambiar estado: " + e.getMessage());

            ctx.json(error).status(500);
        }
    }

    public void obtenerHistorial(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            System.out.println("🎯 [REPARACION-CONTROLLER] Obteniendo historial ID: " + id);

            List<Map<String, Object>> historial = reparacionService.obtenerHistorial(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", historial);
            response.put("total", historial.size());

            ctx.json(response).status(200);
            System.out.println("✅ [REPARACION-CONTROLLER] Historial obtenido ID: " + id + " - " + historial.size() + " entradas");

        } catch (NumberFormatException e) {
            System.err.println("❌ [REPARACION-CONTROLLER] ID inválido para historial");

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "ID de reparación inválido");

            ctx.json(error).status(400);
        } catch (Exception e) {
            System.err.println("❌ [REPARACION-CONTROLLER] Error al obtener historial: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al obtener historial: " + e.getMessage());

            ctx.json(error).status(500);
        }
    }

    public void generarRecibo(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            System.out.println("🎯 [REPARACION-CONTROLLER] Generando recibo ID: " + id);

            Map<String, Object> recibo = reparacionService.generarRecibo(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Recibo generado exitosamente");
            response.put("data", recibo);

            ctx.json(response).status(200);
            System.out.println("✅ [REPARACION-CONTROLLER] Recibo generado ID: " + id);

        } catch (NumberFormatException e) {
            System.err.println("❌ [REPARACION-CONTROLLER] ID inválido para recibo");

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "ID de reparación inválido");

            ctx.json(error).status(400);
        } catch (Exception e) {
            System.err.println("❌ [REPARACION-CONTROLLER] Error al generar recibo: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al generar recibo: " + e.getMessage());

            ctx.json(error).status(500);
        }
    }

    public void eliminarReparacion(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            System.out.println("🎯 [REPARACION-CONTROLLER] Eliminando reparación ID: " + id);

            boolean eliminado = reparacionService.eliminarReparacion(id);

            if (eliminado) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Reparación eliminada exitosamente");

                ctx.json(response).status(200);
                System.out.println("✅ [REPARACION-CONTROLLER] Reparación eliminada ID: " + id);
            } else {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Reparación no encontrada o no se pudo eliminar");

                ctx.json(error).status(404);
                System.out.println("⚠️ [REPARACION-CONTROLLER] Reparación no encontrada para eliminar ID: " + id);
            }

        } catch (NumberFormatException e) {
            System.err.println("❌ [REPARACION-CONTROLLER] ID inválido");

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "ID de reparación inválido");

            ctx.json(error).status(400);
        } catch (Exception e) {
            System.err.println("❌ [REPARACION-CONTROLLER] Error al eliminar: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al eliminar reparación: " + e.getMessage());

            ctx.json(error).status(500);
        }
    }
}