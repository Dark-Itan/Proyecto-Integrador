package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.model.Tarea;
import com.inventario.alma_jesus.service.TareaService;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para gestionar las operaciones relacionadas con tareas de producción.
 * <p>
 * Esta clase maneja la asignación, seguimiento y gestión de tareas para los trabajadores
 * del taller, permitiendo organizar el flujo de trabajo y priorizar actividades.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see TareaService
 * @see Tarea
 */
public class TareaController {

    /**
     * Servicio que contiene la lógica de negocio para la gestión de tareas.
     */
    private final TareaService tareaService = new TareaService();

    /**
     * Endpoint: Lista todas las tareas con opciones de filtrado.
     * <p>
     * Permite listar tareas con filtros por texto de búsqueda y estado.
     * Retorna tareas ordenadas por prioridad y fecha de creación.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     * @example GET /api/v1/tareas?estado=PENDIENTE&buscar=ensamblaje
     */
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

    /**
     * Endpoint: Lista tareas asignadas a un trabajador específico.
     * <p>
     * Retorna todas las tareas asignadas a un trabajador, útil para
     * dashboards individuales y gestión personal del trabajo.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     * @example GET /api/v1/tareas/trabajador/TRAB001?estado=EN_PROGRESO
     */
    public void listarTareasPorTrabajador(Context ctx) {
        try {
            String trabajadorId = ctx.pathParam("trabajadorId");
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

    /**
     * Endpoint: Obtiene una tarea específica por ID.
     * <p>
     * Retorna todos los detalles de una tarea incluyendo asignación,
     * descripción, fechas límite y progreso actual.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     * @example GET /api/v1/tareas/123
     */
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

    /**
     * Endpoint: Crea una nueva tarea.
     * <p>
     * Registra una nueva tarea con asignación a trabajador,
     * descripción detallada y fechas estimadas.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     * @example
     * POST /api/v1/tareas
     * Body:
     * {
     *     "titulo": "Ensamblar mesa de centro",
     *     "descripcion": "Ensamblar piezas cortadas de mesa de roble",
     *     "trabajadorAsignado": "TRAB001",
     *     "prioridad": "ALTA",
     *     "fechaLimite": "2024-12-15",
     *     "estimadoHoras": 4,
     *     "estado": "PENDIENTE"
     * }
     */
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

    /**
     * Endpoint: Actualiza el estado de una tarea.
     * <p>
     * Permite cambiar el estado de una tarea según el flujo de trabajo:
     * PENDIENTE → EN_PROGRESO → COMPLETADA → VERIFICADA.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     * @example
     * PATCH /api/v1/tareas/123/estado
     * Body: { "estado": "EN_PROGRESO" }
     */
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

    /**
     * Endpoint: Elimina una tarea del sistema.
     * <p>
     * Realiza una eliminación lógica de la tarea, solo permitida
     * para tareas en estado inicial o canceladas.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     * @example DELETE /api/v1/tareas/123
     */
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

    /**
     * Endpoint: Actualiza completamente una tarea existente.
     * <p>
     * Permite modificar cualquier campo de una tarea excepto
     * el historial de cambios de estado.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     * @example
     * PUT /api/v1/tareas/123
     * Body: { ...tarea completa actualizada... }
     */
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