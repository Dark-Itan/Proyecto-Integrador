package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.service.ProductoService;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class ProductoController {
    private ProductoService productoService = new ProductoService();
    private ObjectMapper objectMapper = new ObjectMapper();

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

    public void publicarPrecios(Context ctx) {
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