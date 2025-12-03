package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.model.Recetario;
import com.inventario.alma_jesus.service.RecetarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para gestionar las operaciones relacionadas con el recetario de producción.
 * <p>
 * Esta clase maneja el CRUD completo de recetas, que contienen las instrucciones,
 * materiales y pasos para fabricar productos específicos en el taller.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see RecetarioService
 * @see Recetario
 */
public class RecetarioController {

    /**
     * Servicio que contiene la lógica de negocio para la gestión de recetas.
     */
    private final RecetarioService recetarioService;

    /**
     * Mapper para convertir entre JSON y objetos Java.
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructor del controlador de recetario.
     *
     * @param recetarioService Instancia del servicio de recetas
     */
    public RecetarioController(RecetarioService recetarioService) {
        this.recetarioService = recetarioService;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Handler para listar todas las recetas del sistema.
     * <p>
     * Retorna la lista completa de recetas activas, ordenadas por nombre
     * o fecha de creación.
     * </p>
     *
     * @example
     * Petición GET: /api/v1/recetas
     *
     * Respuesta exitosa (200):
     * <pre>
     * {
     *     "success": true,
     *     "data": [...],
     *     "total": 12
     * }
     * </pre>
     */
    public Handler listarRecetas = new Handler() {
        @Override
        public void handle(Context ctx) throws Exception {
            try {
                var recetas = recetarioService.listarRecetas();

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", recetas);
                response.put("total", recetas.size());

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
     * Handler para listar recetas disponibles para trabajadores.
     * <p>
     * Este endpoint está específicamente diseñado para usuarios con rol TRABAJADOR.
     * Devuelve todas las recetas que han sido creadas por administradores,
     * permitiendo a los trabajadores visualizar las instrucciones de fabricación.
     * No requiere autenticación avanzada, ya que está destinado a visualización simple.
     * </p>
     *
     * @example
     * Petición GET: /api/v1/recetas/trabajador
     *
     * Respuesta exitosa (200):
     * <pre>
     * {
     *     "success": true,
     *     "data": [...],
     *     "total": 8,
     *     "rol": "TRABAJADOR"
     * }
     * </pre>
     */
    public Handler listarRecetasTrabajador = new Handler() {
        @Override
        public void handle(Context ctx) throws Exception {
            try {
                var recetas = recetarioService.listarRecetasParaTrabajador();

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", recetas);
                response.put("total", recetas.size());
                response.put("rol", "TRABAJADOR");

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
     * Handler para obtener una receta específica por ID.
     * <p>
     * Retorna todos los detalles de una receta incluyendo materiales requeridos,
     * pasos de fabricación, tiempo estimado y notas importantes.
     * </p>
     *
     * @param id Parámetro de ruta con el ID numérico de la receta
     *
     * @throws NumberFormatException Si el ID no es un número válido
     */
    public Handler obtenerReceta = new Handler() {
        @Override
        public void handle(Context ctx) throws Exception {
            try {
                String idParam = ctx.pathParam("id");
                Long id = Long.parseLong(idParam);
                var receta = recetarioService.obtenerReceta(id);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", receta);

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
     * Handler para crear una nueva receta.
     * <p>
     * Registra una nueva receta con todos sus componentes y la deja
     * en estado activo para ser utilizada en producción.
     * </p>
     *
     * @example
     * Petición POST: /api/v1/recetas
     * Body:
     * <pre>
     * {
     *     "nombre": "Mesa de centro en roble",
     *     "descripcion": "Fabricación de mesa de centro estilo rústico",
     *     "materiales": [
     *         {"material": "Madera de roble", "cantidad": 2.5, "unidad": "m2"},
     *         {"material": "Tornillos 4x40", "cantidad": 16, "unidad": "pzas"}
     *     ],
     *     "pasos": [
     *         {"orden": 1, "descripcion": "Cortar madera a medidas", "tiempo": 30},
     *         {"orden": 2, "descripcion": "Lijar superficies", "tiempo": 45}
     *     ],
     *     "tiempoTotal": 240,
     *     "dificultad": "media"
     * }
     * </pre>
     */
    public Handler crearReceta = new Handler() {
        @Override
        public void handle(Context ctx) throws Exception {
            try {
                String body = ctx.body();
                Recetario receta = objectMapper.readValue(body, Recetario.class);
                var nuevaReceta = recetarioService.crearReceta(receta);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Receta creada exitosamente");
                response.put("data", nuevaReceta);

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
     * Handler para actualizar una receta existente.
     * <p>
     * Permite modificar cualquier campo de una receta, útil para
     * actualizar materiales, pasos o ajustar tiempos de producción.
     * </p>
     *
     * @param id Parámetro de ruta con el ID de la receta a actualizar
     */
    public Handler actualizarReceta = new Handler() {
        @Override
        public void handle(Context ctx) throws Exception {
            try {
                String idParam = ctx.pathParam("id");
                Long id = Long.parseLong(idParam);
                Recetario receta = objectMapper.readValue(ctx.body(), Recetario.class);
                var recetaActualizada = recetarioService.actualizarReceta(id, receta);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Receta actualizada exitosamente");
                response.put("data", recetaActualizada);

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
     * Handler para eliminar una receta.
     * <p>
     * Realiza una eliminación lógica de la receta, marcándola como inactiva
     * en lugar de eliminarla físicamente para preservar el historial.
     * </p>
     *
     * @param id Parámetro de ruta con el ID de la receta a eliminar
     */
    public Handler eliminarReceta = new Handler() {
        @Override
        public void handle(Context ctx) throws Exception {
            try {
                String idParam = ctx.pathParam("id");
                Long id = Long.parseLong(idParam);
                boolean eliminado = recetarioService.eliminarReceta(id);

                if (eliminado) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Receta eliminada exitosamente");
                    ctx.json(response).status(200);
                } else {
                    Map<String, Object> error = new HashMap<>();
                    error.put("success", false);
                    error.put("message", "No se pudo eliminar la receta");
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