package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.TareaController;
import io.javalin.Javalin;

/**
 * Router para la configuración de rutas de gestión de tareas de producción.
 * <p>
 * Esta clase define y configura todas las rutas relacionadas con la gestión
 * de tareas asignadas a trabajadores en la API REST. Las tareas representan
 * actividades de producción específicas dentro del flujo de trabajo del taller.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see TareaController
 */
public class TareaRouter {

    /**
     * Configura todas las rutas de gestión de tareas en la aplicación Javalin.
     * <p>
     * Define los siguientes endpoints bajo el path base /api/v1/tareas:
     * <ul>
     *   <li>GET /api/v1/tareas - Listar todas las tareas (acceso administrador)</li>
     *   <li>GET /api/v1/tareas/trabajador/{trabajadorId} - Listar tareas asignadas a un trabajador específico</li>
     *   <li>GET /api/v1/tareas/{id} - Obtener tarea específica por ID</li>
     *   <li>POST /api/v1/tareas - Crear nueva tarea de producción</li>
     *   <li>PUT /api/v1/tareas/{id}/estado - Actualizar estado de una tarea (ej: PENDIENTE → EN_PROGRESO)</li>
     *   <li>DELETE /api/v1/tareas/{id} - Eliminar tarea (eliminación lógica)</li>
     *   <li>PUT /api/v1/tareas/{id} - Actualizar todos los datos de una tarea existente</li>
     * </ul>
     * El sistema diferencia entre vista administrativa (todas las tareas) y
     * vista por trabajador (tareas asignadas específicamente).
     * </p>
     *
     * @param app Instancia de {@link Javalin} donde se configurarán las rutas
     * @throws IllegalArgumentException Si la aplicación Javalin es nula
     */
    public static void configureRoutes(Javalin app) {
        TareaController controller = new TareaController();

        // Endpoint 50: Listar tareas (vista administrativa)
        // GET /api/v1/tareas
        // Permite filtros opcionales: ?buscar=texto&estado=ESTADO
        app.get("/api/v1/tareas", controller::listarTareas);

        // Endpoint: Listar tareas por trabajador (vista específica)
        // GET /api/v1/tareas/trabajador/{trabajadorId}
        // Filtro opcional: ?estado=ESTADO
        app.get("/api/v1/tareas/trabajador/{trabajadorId}", controller::listarTareasPorTrabajador);

        // Endpoint 51: Obtener tarea específica
        // GET /api/v1/tareas/{id}
        app.get("/api/v1/tareas/{id}", controller::obtenerTarea);

        // Endpoint 52: Crear tarea
        // POST /api/v1/tareas
        // Body debe incluir asunto, detalles, trabajadorId, fechas, etc.
        app.post("/api/v1/tareas", controller::crearTarea);

        // Endpoint 53: Actualizar estado de tarea
        // PUT /api/v1/tareas/{id}/estado
        // Body: { "estado": "NUEVO_ESTADO" }
        // Estados válidos: PENDIENTE, EN_PROGRESO, COMPLETADA, VERIFICADA, CANCELADA
        app.put("/api/v1/tareas/{id}/estado", controller::actualizarEstado);

        // Endpoint 54: Eliminar tarea
        // DELETE /api/v1/tareas/{id}
        // Eliminación lógica (activo = false)
        app.delete("/api/v1/tareas/{id}", controller::eliminarTarea);

        // Endpoint: Actualizar tarea completa
        // PUT /api/v1/tareas/{id}
        // Permite modificar todos los campos editables excepto estado
        app.put("/api/v1/tareas/{id}", controller::actualizarTarea);
    }
}