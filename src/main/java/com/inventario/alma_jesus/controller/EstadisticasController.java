package com.inventario.alma_jesus.controller;

import com.inventario.alma_jesus.service.EstadisticasService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para gestionar las peticiones relacionadas con estadísticas y análisis del sistema.
 * <p>
 * Esta clase proporciona endpoints para obtener datos estadísticos del dashboard,
 * gráficas de ventas, reparaciones y consumo de materiales. Los datos pueden ser
 * filtrados por periodo (semanal, mensual, anual) para análisis temporales.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see EstadisticasService
 */
public class EstadisticasController {
    /**
     * Servicio que contiene la lógica de negocio para el procesamiento de estadísticas.
     */
    private final EstadisticasService estadisticasService;
    /**
     * Constructor del controlador de estadísticas.
     *
     * @param estadisticasService Instancia del servicio de estadísticas
     *
     * @throws IllegalArgumentException Si el servicio es nulo
     */
    public EstadisticasController(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }
    /**
     * Handler para obtener las estadísticas generales del dashboard.
     * <p>
     * Este endpoint retorna un resumen con las métricas clave del sistema
     * como total de ventas, reparaciones pendientes, productos en stock, etc.
     * </p>
     *
     * @see EstadisticasService#obtenerEstadisticasDashboard()
     *
     * @example
     * Petición GET: /api/v1/dashboard/stats
     *
     * Respuesta exitosa (200):
     * <pre>
     * {
     *     "success": true,
     *     "data": {
     *         "totalVentas": 150,
     *         "ventasMensuales": 25,
     *         "reparacionesPendientes": 8,
     *         "reparacionesCompletadas": 42,
     *         "productosStock": 156,
     *         "productosBajoStock": 12,
     *         "ingresosTotales": 12500.50,
     *         "ingresosMensuales": 2500.75
     *     },
     *     "message": "Estadísticas obtenidas exitosamente"
     * }
     * </pre>
     *
     * Respuesta de error (400):
     * <pre>
     * {
     *     "success": false,
     *     "message": "Error al obtener estadísticas"
     * }
     * </pre>
     */
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

    /**
     * Handler para obtener datos de gráfica de ventas por periodo.
     * <p>
     * Este endpoint retorna datos estructurados para generar gráficas de ventas,
     * agrupadas por el periodo especificado (semanal, mensual, anual).
     * </p>
     *
     * @param periodo Parámetro de consulta opcional que especifica el periodo
     *                de agrupación. Valores aceptados: "semanal", "mensual", "anual".
     *                Por defecto es "mensual".
     *
     * @see EstadisticasService#obtenerDatosGraficaVentas(String)
     *
     * @example
     * Petición GET: /api/v1/analytics/venta?periodo=mensual
     *
     * Respuesta exitosa (200):
     * <pre>
     * {
     *     "success": true,
     *     "data": [
     *         {"mes": "Enero", "ventas": 15, "ingresos": 1500.50},
     *         {"mes": "Febrero", "ventas": 18, "ingresos": 1800.75},
     *         ...
     *     ],
     *     "periodo": "mensual",
     *     "total": 12,
     *     "message": "Datos de ventas obtenidos exitosamente"
     * }
     * </pre>
     */
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

    /**
     * Handler para obtener datos de gráfica de reparaciones por periodo.
     * <p>
     * Este endpoint retorna datos estructurados para generar gráficas de
     * reparaciones, mostrando tendencias y estadísticas por periodo.
     * </p>
     *
     * @param periodo Parámetro de consulta opcional que especifica el periodo
     *                de agrupación. Valores aceptados: "semanal", "mensual", "anual".
     *                Por defecto es "mensual".
     *
     * @see EstadisticasService#obtenerDatosGraficaReparaciones(String)
     *
     * @example
     * Petición GET: /api/v1/analytics/reparaciones?periodo=semanal
     *
     * Respuesta exitosa (200):
     * <pre>
     * {
     *     "success": true,
     *     "data": [
     *         {"semana": "Semana 1", "completadas": 5, "pendientes": 2},
     *         {"semana": "Semana 2", "completadas": 7, "pendientes": 1},
     *         ...
     *     ],
     *     "periodo": "semanal",
     *     "total": 4,
     *     "message": "Datos de reparaciones obtenidos exitosamente"
     * }
     * </pre>
     */
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

    /**
     * Handler para obtener datos de gráfica de consumo de materiales por periodo.
     * <p>
     * Este endpoint retorna datos sobre el consumo de materias primas y materiales,
     * útil para análisis de inventario y planificación de compras.
     * </p>
     *
     * @param periodo Parámetro de consulta opcional que especifica el periodo
     *                de agrupación. Valores aceptados: "semanal", "mensual", "anual".
     *                Por defecto es "mensual".
     *
     * @see EstadisticasService#obtenerDatosGraficaMateriales(String)
     *
     * @example
     * Petición GET: /api/v1/analytics/materiales?periodo=anual
     *
     * Respuesta exitosa (200):
     * <pre>
     * {
     *     "success": true,
     *     "data": [
     *         {"material": "Tornillos", "consumo": 1500, "unidad": "pzas"},
     *         {"material": "Madera", "consumo": 250, "unidad": "m2"},
     *         ...
     *     ],
     *     "periodo": "anual",
     *     "total": 8,
     *     "message": "Datos de materiales obtenidos exitosamente"
     * }
     * </pre>
     */
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