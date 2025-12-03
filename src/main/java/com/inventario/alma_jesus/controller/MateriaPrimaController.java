package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.model.MateriaPrima;
import com.inventario.alma_jesus.model.MovimientoMp;
import com.inventario.alma_jesus.service.MateriaPrimaService;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para gestionar las operaciones relacionadas con materias primas.
 * <p>
 * Esta clase maneja el CRUD de materiales, control de stock, historial de movimientos
 * y seguimiento de inventario de materias primas utilizadas en producción.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see MateriaPrimaService
 * @see MateriaPrima
 * @see MovimientoMp
 */
@SuppressWarnings("unchecked")
public class MateriaPrimaController {
    /**
     * Servicio que contiene la lógica de negocio para la gestión de materias primas.
     */
    private final MateriaPrimaService materiaService = new MateriaPrimaService();

    /**
     * Endpoint 25: Lista todas las materias primas con opciones de filtrado.
     * <p>
     * Permite listar materiales con filtros opcionales por búsqueda textual
     * y categoría. Retorna información detallada incluyendo stock actual.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     *
     * @example
     * Petición GET: /api/v1/materiales?categoria=Madera&buscar=roble
     */
    // Endpoint 25: GET /api/v1/materiales - Listar materiales
    public void listarMateriales(Context ctx) {
        try {
            String buscar = ctx.queryParam("buscar");
            String categoria = ctx.queryParam("categoria");

            List<MateriaPrima> materiales = materiaService.listarMateriales(buscar, categoria);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", materiales);
            response.put("total", materiales.size());

            ctx.json(response);
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al listar materiales: " + e.getMessage()
            ));
        }
    }

    /**
     * Endpoint 26: Obtiene una materia prima específica por ID.
     * <p>
     * Retorna todos los detalles de un material incluyendo su historial
     * de movimientos reciente y datos de proveedor si están disponibles.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     *
     * @throws NumberFormatException Si el ID no es un número válido
     */
    // Endpoint 26: GET /api/v1/materiales/{id} - Obtener material
    public void obtenerMaterial(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            MateriaPrima material = materiaService.obtenerMaterial(id);

            ctx.json(Map.of(
                    "success", true,
                    "data", material
            ));
        } catch (Exception e) {
            ctx.status(404).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * Endpoint 27: Crea una nueva materia prima en el inventario.
     * <p>
     * Registra un nuevo material con sus propiedades básicas y stock inicial.
     * Valida que no exista un material con el mismo nombre.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     *
     * @example
     * Petición POST: /api/v1/materiales
     * Body:
     * <pre>
     * {
     *     "nombre": "Tornillos 3x20",
     *     "descripcion": "Tornillos para madera cabeza plana",
     *     "categoria": "Fijación",
     *     "unidad": "pzas",
     *     "stock": 1000,
     *     "stockMinimo": 100,
     *     "proveedor": "Proveedor XYZ"
     * }
     * </pre>
     */
    // Endpoint 27: POST /api/v1/materiales - Crear material
    public void crearMaterial(Context ctx) {
        try {
            MateriaPrima material = ctx.bodyAsClass(MateriaPrima.class);
            MateriaPrima nuevoMaterial = materiaService.crearMaterial(material);

            ctx.status(201).json(Map.of(
                    "success", true,
                    "message", "Material creado exitosamente",
                    "data", nuevoMaterial
            ));
        } catch (Exception e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "Error al crear material: " + e.getMessage()
            ));
        }
    }

    /**
     * Endpoint 28: Edita completamente una materia prima existente.
     * <p>
     * Actualiza todos los campos de un material excepto el historial de movimientos.
     * Útil para correcciones de datos o cambios de especificaciones.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     */
    // Endpoint 28: PUT /api/v1/materiales/{id} - Editar material completo
    public void editarMaterial(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            MateriaPrima material = ctx.bodyAsClass(MateriaPrima.class);
            material.setId(id);

            boolean actualizado = materiaService.editarMaterial(material);

            if (actualizado) {
                ctx.json(Map.of(
                        "success", true,
                        "message", "Material actualizado exitosamente"
                ));
            } else {
                throw new RuntimeException("No se pudo actualizar el material");
            }
        } catch (Exception e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * Endpoint 29: Actualiza el stock de una materia prima con registro de movimiento.
     * <p>
     * Modifica la cantidad de stock y registra el movimiento en el historial
     * con el usuario responsable y una nota explicativa.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     *
     * @example
     * Petición PUT: /api/v1/materiales/456/stock
     * Body:
     * <pre>
     * {
     *     "cantidad": 1500,
     *     "usuarioId": "admin001",
     *     "nota": "Compra mensual de inventario"
     * }
     * </pre>
     */
    // Endpoint 29: PUT /api/v1/materiales/{id}/stock - Actualizar stock
    public void actualizarStock(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            Integer nuevaCantidad = (Integer) body.get("cantidad");
            String usuarioId = (String) body.get("usuarioId");
            String nota = (String) body.get("nota");

            if (nuevaCantidad == null) {
                throw new RuntimeException("La cantidad es requerida");
            }
            if (usuarioId == null) {
                throw new RuntimeException("El usuario es requerido");
            }

            boolean actualizado = materiaService.actualizarStock(id, nuevaCantidad, usuarioId, nota);

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
     * Endpoint 30: Obtiene el historial de movimientos de una materia prima.
     * <p>
     * Retorna la lista cronológica de todos los ajustes de stock realizados
     * en un material, incluyendo fechas, usuarios y notas.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     */
    // Endpoint 30: GET /api/v1/materiales/{id}/movimientos - Ver historial
    public void obtenerHistorial(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            List<MovimientoMp> movimientos = materiaService.obtenerHistorial(id);

            ctx.json(Map.of(
                    "success", true,
                    "data", movimientos
            ));
        } catch (Exception e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * Endpoint 31: Elimina una materia prima del inventario.
     * <p>
     * Elimina permanentemente un material del sistema.
     * Solo se permite si el material tiene stock cero y no tiene movimientos recientes.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     */
    // Endpoint 31: DELETE /api/v1/materiales/{id} - Eliminar material
    public void eliminarMaterial(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));

            boolean eliminado = materiaService.eliminarMaterial(id);

            if (eliminado) {
                ctx.json(Map.of(
                        "success", true,
                        "message", "Material eliminado exitosamente"
                ));
            } else {
                throw new RuntimeException("No se pudo eliminar el material");
            }
        } catch (Exception e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}