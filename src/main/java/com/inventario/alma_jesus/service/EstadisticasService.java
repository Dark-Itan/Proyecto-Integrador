package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.EstadisticaDashboard;
import com.inventario.alma_jesus.model.EstadisticaVenta;
import com.inventario.alma_jesus.model.EstadisticaReparacion;
import com.inventario.alma_jesus.model.EstadisticaMaterial;
import com.inventario.alma_jesus.repository.EstadisticasRepository;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la obtención y gestión de estadísticas del sistema.
 * <p>
 * Esta clase actúa como intermediario entre los controladores y el repositorio
 * de estadísticas, proporcionando una capa de abstracción para la obtención
 * de datos estadísticos del dashboard y gráficas. Maneja la lógica de
 * configuración por defecto y el manejo de excepciones de la capa de datos.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see EstadisticasRepository
 * @see EstadisticaDashboard
 * @see EstadisticaVenta
 * @see EstadisticaReparacion
 * @see EstadisticaMaterial
 */
public class EstadisticasService {

    /**
     * Repositorio para la obtención de datos estadísticos desde la base de datos.
     */
    private final EstadisticasRepository repository;

    /**
     * Constructor del servicio.
     * <p>
     * Inicializa el servicio con un repositorio de estadísticas inyectado.
     * Sigue el principio de inversión de dependencias para facilitar testing.
     * </p>
     *
     * @param repository Repositorio de estadísticas a utilizar
     */
    public EstadisticasService(EstadisticasRepository repository) {
        this.repository = repository;
    }

    /**
     * Obtiene las estadísticas generales para el dashboard principal.
     * <p>
     * Retorna un objeto {@link EstadisticaDashboard} que contiene métricas
     * clave del sistema como total de ventas, reparaciones pendientes,
     * clientes activos y materiales consumidos.
     * </p>
     *
     * @return {@link EstadisticaDashboard} con las métricas generales del sistema
     * @throws RuntimeException Si ocurre un error al acceder a la base de datos
     * @see EstadisticasRepository#obtenerEstadisticasGenerales()
     */
    public EstadisticaDashboard obtenerEstadisticasDashboard() {
        try {
            return repository.obtenerEstadisticasGenerales();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener estadísticas: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene datos de ventas para gráficas por período.
     * <p>
     * Retorna datos de ventas agrupados por el período especificado.
     * Si no se proporciona un período, se usa "mensual" por defecto.
     * </p>
     *
     * @param periodo Período de agrupación ("diario", "semanal", "mensual", "anual")
     * @return Lista de {@link EstadisticaVenta} con cantidad y monto por período
     * @throws RuntimeException Si ocurre un error al acceder a la base de datos
     * @see EstadisticasRepository#obtenerVentasPorPeriodo(String)
     */
    public List<EstadisticaVenta> obtenerDatosGraficaVentas(String periodo) {
        try {
            if (periodo == null) periodo = "mensual";
            return repository.obtenerVentasPorPeriodo(periodo);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener ventas: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene datos de reparaciones para gráficas por período.
     * <p>
     * Retorna el volumen de reparaciones agrupadas por el período especificado.
     * Si no se proporciona un período, se usa "mensual" por defecto.
     * </p>
     *
     * @param periodo Período de agrupación ("diario", "semanal", "mensual", "anual")
     * @return Lista de {@link EstadisticaReparacion} con cantidad de reparaciones por período
     * @throws RuntimeException Si ocurre un error al acceder a la base de datos
     * @see EstadisticasRepository#obtenerReparacionesPorPeriodo(String)
     */
    public List<EstadisticaReparacion> obtenerDatosGraficaReparaciones(String periodo) {
        try {
            if (periodo == null) periodo = "mensual";
            return repository.obtenerReparacionesPorPeriodo(periodo);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener reparaciones: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene datos de consumo de materiales para gráficas por período.
     * <p>
     * Retorna el consumo de materiales agrupado por el período especificado.
     * Si no se proporciona un período, se usa "mensual" por defecto.
     * Los datos están organizados por tipo de material.
     * </p>
     *
     * @param periodo Período de agrupación ("diario", "semanal", "mensual", "anual")
     * @return Lista de {@link EstadisticaMaterial} con material y cantidad consumida
     * @throws RuntimeException Si ocurre un error al acceder a la base de datos
     * @see EstadisticasRepository#obtenerMaterialesPorPeriodo(String)
     */
    public List<EstadisticaMaterial> obtenerDatosGraficaMateriales(String periodo) {
        try {
            if (periodo == null) periodo = "mensual";
            return repository.obtenerMaterialesPorPeriodo(periodo);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener materiales: " + e.getMessage(), e);
        }
    }
}