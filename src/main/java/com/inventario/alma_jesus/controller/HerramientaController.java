package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.model.Herramienta;
import com.inventario.alma_jesus.service.HerramientaService;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para gestionar las operaciones relacionadas con herramientas del inventario.
 * <p>
 * Esta clase maneja todas las peticiones HTTP para el módulo de herramientas,
 * incluyendo CRUD básico, gestión de stock, asignación y devolución de herramientas.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see HerramientaService
 * @see Herramienta
 */
public class HerramientaController {
    /**
     * Servicio que contiene la lógica de negocio para la gestión de herramientas.
     */
    private final HerramientaService herramientaService = new HerramientaService();

    /**
     * Endpoint 18: Lista todas las herramientas con opciones de filtrado.
     * <p>
     * Permite listar herramientas con filtros opcionales por búsqueda textual
     * y estado. Si no se proporcionan filtros, retorna todas las herramientas.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     *
     * @example
     * Petición GET: /api/v1/herramientas?buscar=martillo&estatus=disponible
     *
     * Respuesta exitosa (200):
     * <pre>
     * {
     *     "success": true,
     *     "data": [...],
     *     "total": 5
     * }
     * </pre>
     */
    // Endpoint 18: GET /api/v1/herramientas - Listar herramientas
    public void listarHerramientas(Context ctx) {
        System.out.println("[CONTROLLER] Listar herramientas llamado");
        try {
            String buscar = ctx.queryParam("buscar");
            String estatus = ctx.queryParam("estatus");

            var herramientas = herramientaService.listarHerramientas(buscar, estatus);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", herramientas);
            response.put("total", herramientas.size());

            ctx.json(response);
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al listar herramientas: " + e.getMessage()
            ));
        }
    }

    /**
     * Endpoint 19: Obtiene una herramienta específica por ID o nombre.
     * <p>
     * Busca una herramienta utilizando su ID numérico o su nombre exacto.
     * Es útil para operaciones que requieren una herramienta específica.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     *
     * @example
     * Petición GET: /api/v1/herramientas/123  (por ID)
     * Petición GET: /api/v1/herramientas/Martillo%20de%20bola  (por nombre)
     */
    // Endpoint 19: GET /api/v1/herramientas/{id o nombre} - Obtener herramienta
    public void obtenerHerramienta(Context ctx) {
        try {
            String idONombre = ctx.pathParam("idONombre");
            Herramienta herramienta = herramientaService.obtenerPorIdONombre(idONombre);

            ctx.json(Map.of(
                    "success", true,
                    "data", herramienta
            ));
        } catch (Exception e) {
            ctx.status(404).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * Endpoint 20: Crea una nueva herramienta en el inventario.
     * <p>
     * Recibe los datos de una herramienta en formato JSON y la registra
     * en el sistema con stock inicial disponible.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     *
     * @example
     * Petición POST: /api/v1/herramientas
     * Body:
     * <pre>
     * {
     *     "nombre": "Taladro eléctrico",
     *     "descripcion": "Taladro de 18V con percutor",
     *     "categoria": "Eléctricas",
     *     "stock": 3,
     *     "stockDisponible": 3
     * }
     * </pre>
     */
    // Endpoint 20: POST /api/v1/herramientas - Crear herramienta
    public void crearHerramienta(Context ctx) {
        try {
            Herramienta herramienta = ctx.bodyAsClass(Herramienta.class);
            Herramienta nuevaHerramienta = herramientaService.crearHerramienta(herramienta);

            ctx.status(201).json(Map.of(
                    "success", true,
                    "message", "Herramienta creada exitosamente",
                    "data", nuevaHerramienta
            ));
        } catch (Exception e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "Error al crear herramienta: " + e.getMessage()
            ));
        }
    }

    /**
     * Endpoint 21: Actualiza el stock de una herramienta.
     * <p>
     * Permite modificar la cantidad total de stock de una herramienta,
     * útil para ajustes de inventario o correcciones.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     *
     * @example
     * Petición PUT: /api/v1/herramientas/123/stock
     * Body:
     * <pre>
     * {
     *     "cantidad": 10
     * }
     * </pre>
     */
    // Endpoint 21: PUT /api/v1/herramientas/{id o nombre}/stock - Actualizar stock
    public void actualizarStock(Context ctx) {
        try {
            String idONombre = ctx.pathParam("idONombre");
            Map<String, Integer> body = ctx.bodyAsClass(Map.class);
            Integer nuevaCantidad = body.get("cantidad");

            if (nuevaCantidad == null) {
                throw new RuntimeException("La cantidad es requerida");
            }

            boolean actualizado = herramientaService.actualizarStock(idONombre, nuevaCantidad);

            if (actualizado) {
                ctx.json(Map.of(
                        "success", true,
                        "message", "Stock actualizado exitosamente"
                ));
            } else {
                throw new RuntimeException("No se pudo actualizar el stock");
            }
        } catch (Exception e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * Endpoint 22: Asigna una herramienta a un usuario.
     * <p>
     * Registra el préstamo de una herramienta a un usuario específico,
     * reduciendo el stock disponible y registrando la asignación.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     *
     * @example
     * Petición PUT: /api/v1/herramientas/123/tomar
     * Body:
     * <pre>
     * {
     *     "usuarioAsignado": "juan.perez",
     *     "asignadoPor": "admin001"
     * }
     * </pre>
     */
    // Endpoint 22: PUT /api/v1/herramientas/{id o nombre}/tomar - Tomar herramienta
    public void tomarHerramienta(Context ctx) {
        try {
            String idONombre = ctx.pathParam("idONombre");
            Map<String, String> body = ctx.bodyAsClass(Map.class);
            String usuarioAsignado = body.get("usuarioAsignado");
            String asignadoPor = body.get("asignadoPor");

            boolean tomada = herramientaService.tomarHerramienta(idONombre, usuarioAsignado, asignadoPor);

            if (tomada) {
                ctx.json(Map.of(
                        "success", true,
                        "message", "Herramienta asignada exitosamente"
                ));
            } else {
                throw new RuntimeException("No hay stock disponible para asignar");
            }
        } catch (Exception e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * Endpoint 23: Devuelve una herramienta previamente asignada.
     * <p>
     * Marca una herramienta como devuelta, incrementando el stock disponible
     * y registrando la fecha de devolución.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     */
    // Endpoint 23: PUT /api/v1/herramientas/{id o nombre}/devolver - Devolver herramienta
    public void devolverHerramienta(Context ctx) {
        try {
            String idONombre = ctx.pathParam("idONombre");

            boolean devuelta = herramientaService.devolverHerramienta(idONombre);

            if (devuelta) {
                ctx.json(Map.of(
                        "success", true,
                        "message", "Herramienta devuelta exitosamente"
                ));
            } else {
                throw new RuntimeException("No se pudo devolver la herramienta");
            }
        } catch (Exception e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * Endpoint 24: Elimina una herramienta del inventario.
     * <p>
     * Elimina permanentemente una herramienta del sistema.
     * Solo se permite si la herramienta no tiene stock en uso.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     */
    // Endpoint 24: DELETE /api/v1/herramientas/{id o nombre} - Eliminar herramienta
    public void eliminarHerramienta(Context ctx) {
        try {
            String idONombre = ctx.pathParam("idONombre");

            boolean eliminada = herramientaService.eliminarHerramienta(idONombre);

            if (eliminada) {
                ctx.json(Map.of(
                        "success", true,
                        "message", "Herramienta eliminada exitosamente"
                ));
            } else {
                throw new RuntimeException("No se pudo eliminar la herramienta");
            }
        } catch (Exception e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}