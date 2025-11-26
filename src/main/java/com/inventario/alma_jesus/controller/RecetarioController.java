package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.model.Recetario;
import com.inventario.alma_jesus.service.RecetarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.HashMap;
import java.util.Map;

public class RecetarioController {
    private final RecetarioService recetarioService;
    private final ObjectMapper objectMapper;

    public RecetarioController(RecetarioService recetarioService) {
        this.recetarioService = recetarioService;
        this.objectMapper = new ObjectMapper();
    }

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