package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.model.Tarea;
import com.inventario.alma_jesus.service.TareaService;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class TareaController {
    private final TareaService tareaService = new TareaService();

    // Endpoint 50: GET /api/v1/tareas - Listar tareas (admin)
    public void listarTareas(Context ctx) {
        try {
            String buscar = ctx.queryParam("buscar");
            String estado = ctx.queryParam("estado");

            List<Tarea> tareas = tareaService.listarTareas(buscar, estado);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", tareas);
            response.put("total", tareas.size());

            ctx.json(response);
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al listar tareas: " + e.getMessage()
            ));
        }
    }

    // NUEVO: GET /api/v1/tareas/trabajador/{trabajadorId} - Listar tareas por trabajador
    public void listarTareasPorTrabajador(Context ctx) {
        try {
            Long trabajadorId = Long.parseLong(ctx.pathParam("trabajadorId"));
            String estado = ctx.queryParam("estado");

            List<Tarea> tareas = tareaService.listarTareasPorTrabajador(trabajadorId, estado);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", tareas);
            response.put("total", tareas.size());

            ctx.json(response);
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al listar tareas del trabajador: " + e.getMessage()
            ));
        }
    }

    // Endpoint 51: GET /api/v1/tareas/{id} - Obtener tarea
    public void obtenerTarea(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            Tarea tarea = tareaService.obtenerTarea(id);

            ctx.json(Map.of(
                    "success", true,
                    "data", tarea
            ));
        } catch (Exception e) {
            ctx.status(404).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Endpoint 52: POST /api/v1/tareas - Crear tarea
    public void crearTarea(Context ctx) {
        try {
            Tarea tarea = ctx.bodyAsClass(Tarea.class);
            Tarea nuevaTarea = tareaService.crearTarea(tarea);

            ctx.status(201).json(Map.of(
                    "success", true,
                    "message", "Tarea creada exitosamente",
                    "data", nuevaTarea
            ));
        } catch (Exception e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "Error al crear tarea: " + e.getMessage()
            ));
        }
    }

    // Endpoint 53: PUT /api/v1/tareas/{id}/estado - Actualizar estado
    public void actualizarEstado(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String nuevoEstado = (String) body.get("estado");

            if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
                throw new RuntimeException("El estado es requerido");
            }

            boolean actualizado = tareaService.actualizarEstado(id, nuevoEstado.toUpperCase());

            if (actualizado) {
                ctx.json(Map.of(
                        "success", true,
                        "message", "Estado actualizado exitosamente"
                ));
            } else {
                throw new RuntimeException("No se pudo actualizar el estado");
            }
        } catch (Exception e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Endpoint 54: DELETE /api/v1/tareas/{id} - Eliminar tarea
    public void eliminarTarea(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));

            boolean eliminado = tareaService.eliminarTarea(id);

            if (eliminado) {
                ctx.json(Map.of(
                        "success", true,
                        "message", "Tarea eliminada exitosamente"
                ));
            } else {
                throw new RuntimeException("No se pudo eliminar la tarea");
            }
        } catch (Exception e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Método adicional: PUT /api/v1/tareas/{id} - Actualizar tarea completa
    public void actualizarTarea(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            Tarea tarea = ctx.bodyAsClass(Tarea.class);
            tarea.setId(id);

            boolean actualizado = tareaService.actualizarTarea(tarea);

            if (actualizado) {
                ctx.json(Map.of(
                        "success", true,
                        "message", "Tarea actualizada exitosamente"
                ));
            } else {
                throw new RuntimeException("No se pudo actualizar la tarea");
            }
        } catch (Exception e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}