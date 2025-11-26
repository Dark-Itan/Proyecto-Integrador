package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.service.EstadisticasService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.HashMap;
import java.util.Map;

public class EstadisticasController {
    private final EstadisticasService estadisticasService;

    public EstadisticasController(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    public Handler obtenerEstadisticasDashboard = new Handler() {
        @Override
        public void handle(Context ctx) throws Exception {
            try {
                var stats = estadisticasService.obtenerEstadisticasDashboard();
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", stats);
                response.put("message", "Estadísticas obtenidas exitosamente");
                ctx.json(response).status(200);
            } catch (Exception e) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", e.getMessage());
                ctx.json(error).status(400);
            }
        }
    };

    public Handler obtenerDatosGraficaVentas = new Handler() {
        @Override
        public void handle(Context ctx) throws Exception {
            try {
                String periodo = ctx.queryParam("periodo");
                var datosVentas = estadisticasService.obtenerDatosGraficaVentas(periodo);
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", datosVentas);
                response.put("periodo", periodo != null ? periodo : "mensual");
                response.put("total", datosVentas.size());
                response.put("message", "Datos de ventas obtenidos exitosamente");
                ctx.json(response).status(200);
            } catch (Exception e) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", e.getMessage());
                ctx.json(error).status(400);
            }
        }
    };

    public Handler obtenerDatosGraficaReparaciones = new Handler() {
        @Override
        public void handle(Context ctx) throws Exception {
            try {
                String periodo = ctx.queryParam("periodo");
                var datosReparaciones = estadisticasService.obtenerDatosGraficaReparaciones(periodo);
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", datosReparaciones);
                response.put("periodo", periodo != null ? periodo : "mensual");
                response.put("total", datosReparaciones.size());
                response.put("message", "Datos de reparaciones obtenidos exitosamente");
                ctx.json(response).status(200);
            } catch (Exception e) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", e.getMessage());
                ctx.json(error).status(400);
            }
        }
    };

    public Handler obtenerDatosGraficaMateriales = new Handler() {
        @Override
        public void handle(Context ctx) throws Exception {
            try {
                String periodo = ctx.queryParam("periodo");
                var datosMateriales = estadisticasService.obtenerDatosGraficaMateriales(periodo);
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", datosMateriales);
                response.put("periodo", periodo != null ? periodo : "mensual");
                response.put("total", datosMateriales.size());
                response.put("message", "Datos de materiales obtenidos exitosamente");
                ctx.json(response).status(200);
            } catch (Exception e) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", e.getMessage());
                ctx.json(error).status(400);
            }
        }
    };
}