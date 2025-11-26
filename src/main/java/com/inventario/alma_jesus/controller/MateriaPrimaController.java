package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.model.MateriaPrima;
import com.inventario.alma_jesus.model.MovimientoMp;
import com.inventario.alma_jesus.service.MateriaPrimaService;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class MateriaPrimaController {
    private final MateriaPrimaService materiaService = new MateriaPrimaService();

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