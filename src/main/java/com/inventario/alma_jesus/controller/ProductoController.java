package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.service.ProductoService;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

/**
 * Controlador para gestionar las operaciones relacionadas con productos del catálogo.
 * <p>
 * Esta clase maneja el CRUD completo de productos, incluyendo listado filtrado por tipo,
 * gestión de imágenes, publicación de precios y todas las operaciones administrativas
 * relacionadas con el catálogo de productos disponibles para venta.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see ProductoService
 */
public class ProductoController {
    /**
     * Servicio que contiene la lógica de negocio para la gestión de productos.
     */
    private ProductoService productoService = new ProductoService();
    /**
     * Mapper para convertir entre JSON y objetos Java.
     */
    private ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Endpoint: Lista todos los productos disponibles en el catálogo.
     * <p>
     * Retorna la lista completa de productos activos con sus detalles,
     * incluyendo precios, stock y disponibilidad.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     *
     * @example
     * Petición GET: /api/v1/productos
     *
     * Respuesta exitosa (200):
     * <pre>
     * {
     *     "success": true,
     *     "data": [
     *         {
     *             "id": 1,
     *             "nombre": "Mesa de centro",
     *             "descripcion": "Mesa en roble macizo",
     *             "precio": 2500.00,
     *             "stock": 5,
     *             "tipo": "mueble",
     *             "activo": true
     *         },
     *         ...
     *     ],
     *     "total": 15
     * }
     * </pre>
     */
    public void listarProductos(Context ctx) {
        try {
            Map<String, Object> result = productoService.listarProductos();

            if ((Boolean) result.get("success")) {
                ctx.status(200).json(result);
            } else {
                ctx.status(500).json(result);
            }

        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno al listar productos"
            ));
        }
    }

    /**
     * Endpoint: Lista productos filtrados por tipo específico.
     * <p>
     * Permite obtener productos de una categoría específica como
     * "mueble", "accesorio", "decoracion", etc.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     *
     * @param tipo Parámetro de consulta requerido que especifica el tipo de producto
     *
     * @example
     * Petición GET: /api/v1/productos?tipo=mueble
     *
     * Respuesta de error (400):
     * <pre>
     * {
     *     "success": false,
     *     "message": "El parámetro 'tipo' es requerido"
     * }
     * </pre>
     */
    public void listarProductosPorTipo(Context ctx) {
        try {
            String tipo = ctx.queryParam("tipo");

            if (tipo == null || tipo.isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "El parámetro 'tipo' es requerido"
                ));
                return;
            }

            Map<String, Object> result = productoService.listarProductosPorTipo(tipo);

            if ((Boolean) result.get("success")) {
                ctx.status(200).json(result);
            } else {
                ctx.status(500).json(result);
            }

        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al filtrar productos por tipo"
            ));
        }
    }

    /**
     * Endpoint: Obtiene un producto específico por su ID.
     * <p>
     * Retorna todos los detalles de un producto individual,
     * incluyendo descripción completa, especificaciones técnicas,
     * imágenes y estado actual.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     *
     * @param id Parámetro de ruta con el ID numérico del producto
     *
     * @throws NumberFormatException Si el ID no es un número válido
     */
    public void obtenerProductoPorId(Context ctx) {
        try {
            String idParam = ctx.pathParam("id");

            if (idParam == null || idParam.isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "ID de producto es requerido"
                ));
                return;
            }

            int id = Integer.parseInt(idParam);
            Map<String, Object> result = productoService.obtenerProductoPorId(id);

            if ((Boolean) result.get("success")) {
                ctx.status(200).json(result);
            } else {
                ctx.status(404).json(result);
            }

        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "ID de producto debe ser un número válido"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno al obtener producto"
            ));
        }
    }

    /**
     * Endpoint: Crea un nuevo producto en el catálogo.
     * <p>
     * Registra un nuevo producto con todos sus atributos y lo deja
     * en estado "borrador" hasta que sea publicado explícitamente.
     * Requiere autenticación de administrador.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     *
     * @example
     * Petición POST: /api/v1/productos
     * Body:
     * <pre>
     * {
     *     "nombre": "Silla ejecutiva",
     *     "descripcion": "Silla ergonómica para oficina",
     *     "precio": 1800.00,
     *     "costo": 1200.00,
     *     "stock": 10,
     *     "tipo": "mueble",
     *     "categoria": "sillas",
     *     "creadoPor": "admin001"
     * }
     * </pre>
     */
    public void crearProducto(Context ctx) {
        try {
            String body = ctx.body();

            if (body == null || body.trim().isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "El cuerpo de la petición no puede estar vacío"
                ));
                return;
            }

            Map<String, Object> bodyMap = objectMapper.readValue(body, Map.class);

            if (bodyMap == null || bodyMap.isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "Datos del producto son requeridos"
                ));
                return;
            }

            if (bodyMap.get("creadoPor") == null || bodyMap.get("creadoPor").toString().isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "El campo 'creadoPor' es requerido"
                ));
                return;
            }

            Map<String, Object> result = productoService.crearProducto(bodyMap);

            if ((Boolean) result.get("success")) {
                ctx.status(201).json(result);
            } else {
                ctx.status(400).json(result);
            }

        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error en el formato JSON: " + e.getMessage()
            ));
        }
    }

    /**
     * Endpoint: Actualiza la información de un producto existente.
     * <p>
     * Permite modificar cualquier campo de un producto excepto
     * el historial de ventas y movimientos. Requiere validación
     * de datos y permisos de edición.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     *
     * @param id Parámetro de ruta con el ID del producto a actualizar
     */
    public void actualizarProducto(Context ctx) {
        try {
            String idParam = ctx.pathParam("id");
            String body = ctx.body();

            if (idParam == null || idParam.isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "ID de producto es requerido"
                ));
                return;
            }

            if (body == null || body.trim().isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "El cuerpo de la petición no puede estar vacío"
                ));
                return;
            }

            int id = Integer.parseInt(idParam);
            Map<String, Object> bodyMap = objectMapper.readValue(body, Map.class);

            Map<String, Object> result = productoService.actualizarProducto(id, bodyMap);

            if ((Boolean) result.get("success")) {
                ctx.status(200).json(result);
            } else {
                ctx.status(400).json(result);
            }

        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "ID de producto debe ser un número válido"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno al actualizar producto"
            ));
        }
    }

    public void eliminarProducto(Context ctx) {
        try {
            String idParam = ctx.pathParam("id");

            if (idParam == null || idParam.isEmpty()) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "ID de producto es requerido"
                ));
                return;
            }

            int id = Integer.parseInt(idParam);
            Map<String, Object> result = productoService.eliminarProducto(id);

            if ((Boolean) result.get("success")) {
                ctx.status(200).json(result);
            } else {
                ctx.status(400).json(result);
            }

        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "ID de producto debe ser un número válido"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno al eliminar producto"
            ));
        }
    }

    /**
     * Endpoint: Elimina un producto del catálogo.
     * <p>
     * Realiza una eliminación lógica (soft delete) del producto,
     * marcándolo como inactivo en lugar de eliminarlo físicamente
     * para preservar el historial.
     * </p>
     *
     * @param ctx Contexto de Javalin con la petición HTTP
     */    public void publicarPrecios(Context ctx) {
        try {
            Map<String, Object> result = productoService.publicarPrecios();

            if ((Boolean) result.get("success")) {
                ctx.status(200).json(result);
            } else {
                ctx.status(500).json(result);
            }

        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno al publicar precios"
            ));
        }
    }

    public void subirImagen(Context ctx) {
        try {
            var uploadedFile = ctx.uploadedFile("imagen");

            if (uploadedFile == null) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "No se proporcionó ninguna imagen"
                ));
                return;
            }

            byte[] fileBytes = uploadedFile.content().readAllBytes();
            String fileName = uploadedFile.filename();

            Map<String, Object> result = productoService.subirImagen(fileBytes, fileName);

            if ((Boolean) result.get("success")) {
                ctx.status(200).json(result);
            } else {
                ctx.status(500).json(result);
            }

        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno al subir imagen: " + e.getMessage()
            ));
        }
    }
}