package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.model.Reparacion;
import com.inventario.alma_jesus.service.ReparacionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import java.util.*;

public class ReparacionController {
    private final ReparacionService reparacionService;
    private final ObjectMapper objectMapper;

    public ReparacionController() {
        this.reparacionService = new ReparacionService();
        this.objectMapper = new ObjectMapper();
    }

    public void listarReparaciones(Context ctx) {
        try {
            System.out.println("Listando reparaciones");

            String estado = ctx.queryParam("estado");
            String cliente = ctx.queryParam("cliente");
            String modelo = ctx.queryParam("modelo");

            List<Reparacion> reparaciones = reparacionService.listarReparaciones(estado, cliente, modelo);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", reparaciones);
            response.put("total", reparaciones.size());

            ctx.json(response).status(200);
            System.out.println("Listado exitoso: " + reparaciones.size() + " reparaciones");

        } catch (Exception e) {
            System.err.println("Error al listar: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al listar reparaciones: " + e.getMessage());

            ctx.json(error).status(500);
        }
    }

    public void obtenerReparacion(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            System.out.println("Obteniendo reparacion ID: " + id);

            Optional<Reparacion> reparacionOpt = reparacionService.obtenerReparacion(id);

            if (reparacionOpt.isPresent()) {
                Reparacion reparacion = reparacionOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", reparacion);
                response.put("saldoPendiente", reparacionService.calcularSaldoPendiente(reparacion));

                ctx.json(response).status(200);
                System.out.println("Reparacion encontrada ID: " + id);
            } else {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Reparacion no encontrada");

                ctx.json(error).status(404);
                System.out.println("Reparacion no encontrada ID: " + id);
            }

        } catch (NumberFormatException e) {
            System.err.println("ID invalido");

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "ID de reparacion invalido");

            ctx.json(error).status(400);
        } catch (Exception e) {
            System.err.println("Error al obtener: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al obtener reparacion: " + e.getMessage());

            ctx.json(error).status(500);
        }
    }

    public void crearReparacion(Context ctx) {
        try {
            System.out.println("Creando nueva reparacion");

            Reparacion reparacion = objectMapper.readValue(ctx.body(), Reparacion.class);

            if (reparacion.getNombreCliente() == null || reparacion.getNombreCliente().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del cliente es requerido");
            }

            Reparacion reparacionCreada = reparacionService.crearReparacion(reparacion);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Reparacion creada exitosamente");
            response.put("data", reparacionCreada);
            response.put("saldoPendiente", reparacionService.calcularSaldoPendiente(reparacionCreada));

            ctx.json(response).status(201);
            System.out.println("Reparacion creada ID: " + reparacionCreada.getId());

        } catch (IllegalArgumentException e) {
            System.err.println("Error de validacion: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());

            ctx.json(error).status(400);
        } catch (Exception e) {
            System.err.println("Error al crear: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al crear reparacion: " + e.getMessage());

            ctx.json(error).status(500);
        }
    }

    public void actualizarReparacion(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            System.out.println("Actualizando reparacion ID: " + id);

            Reparacion reparacion = objectMapper.readValue(ctx.body(), Reparacion.class);
            reparacion.setId(id);

            boolean actualizado = reparacionService.actualizarReparacion(reparacion);

            if (actualizado) {
                Optional<Reparacion> reparacionActualizada = reparacionService.obtenerReparacion(id);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Reparacion actualizada exitosamente");
                response.put("data", reparacionActualizada.orElse(null));

                ctx.json(response).status(200);
                System.out.println("Reparacion actualizada ID: " + id);
            } else {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Reparacion no encontrada o no se pudo actualizar");

                ctx.json(error).status(404);
                System.out.println("No se pudo actualizar ID: " + id);
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Error de validacion: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());

            ctx.json(error).status(400);
        } catch (Exception e) {
            System.err.println("Error al actualizar: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al actualizar reparacion: " + e.getMessage());

            ctx.json(error).status(500);
        }
    }

    public void cambiarEstado(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            System.out.println("Cambiando estado reparacion ID: " + id);

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
                System.out.println("Estado actualizado ID: " + id + " -> " + nuevoEstado);
            } else {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Reparacion no encontrada o no se pudo actualizar el estado");

                ctx.json(error).status(404);
                System.out.println("No se pudo cambiar estado ID: " + id);
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Error de validacion: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());

            ctx.json(error).status(400);
        } catch (Exception e) {
            System.err.println("Error al cambiar estado: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al cambiar estado: " + e.getMessage());

            ctx.json(error).status(500);
        }
    }

    public void obtenerHistorial(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            System.out.println("Obteniendo historial ID: " + id);

            List<Map<String, Object>> historial = reparacionService.obtenerHistorial(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", historial);
            response.put("total", historial.size());

            ctx.json(response).status(200);
            System.out.println("Historial obtenido ID: " + id + " - " + historial.size() + " entradas");

        } catch (NumberFormatException e) {
            System.err.println("ID invalido para historial");

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "ID de reparacion invalido");

            ctx.json(error).status(400);
        } catch (Exception e) {
            System.err.println("Error al obtener historial: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al obtener historial: " + e.getMessage());

            ctx.json(error).status(500);
        }
    }

    public void generarRecibo(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            System.out.println("Generando recibo ID: " + id);

            Map<String, Object> recibo = reparacionService.generarRecibo(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Recibo generado exitosamente");
            response.put("data", recibo);

            ctx.json(response).status(200);
            System.out.println("Recibo generado ID: " + id);

        } catch (NumberFormatException e) {
            System.err.println("ID invalido para recibo");

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "ID de reparacion invalido");

            ctx.json(error).status(400);
        } catch (Exception e) {
            System.err.println("Error al generar recibo: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al generar recibo: " + e.getMessage());

            ctx.json(error).status(500);
        }
    }

    public void eliminarReparacion(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            System.out.println("Eliminando reparacion ID: " + id);

            boolean eliminado = reparacionService.eliminarReparacion(id);

            if (eliminado) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Reparacion eliminada exitosamente");

                ctx.json(response).status(200);
                System.out.println("Reparacion eliminada ID: " + id);
            } else {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Reparacion no encontrada o no se pudo eliminar");

                ctx.json(error).status(404);
                System.out.println("Reparacion no encontrada para eliminar ID: " + id);
            }

        } catch (NumberFormatException e) {
            System.err.println("ID invalido");

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "ID de reparacion invalido");

            ctx.json(error).status(400);
        } catch (Exception e) {
            System.err.println("Error al eliminar: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al eliminar reparacion: " + e.getMessage());

            ctx.json(error).status(500);
        }
    }
}