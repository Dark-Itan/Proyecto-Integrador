package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.EstadisticaDashboard;
import com.inventario.alma_jesus.model.EstadisticaVenta;
import com.inventario.alma_jesus.model.EstadisticaReparacion;
import com.inventario.alma_jesus.model.EstadisticaMaterial;
import com.inventario.alma_jesus.repository.EstadisticasRepository;
import java.sql.SQLException;
import java.util.List;

public class EstadisticasService {
    private final EstadisticasRepository repository;

    public EstadisticasService(EstadisticasRepository repository) {
        this.repository = repository;
    }

    public EstadisticaDashboard obtenerEstadisticasDashboard() {
        try {
            return repository.obtenerEstadisticasGenerales();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener estadísticas: " + e.getMessage(), e);
        }
    }

    public List<EstadisticaVenta> obtenerDatosGraficaVentas(String periodo) {
        try {
            if (periodo == null) periodo = "mensual";
            return repository.obtenerVentasPorPeriodo(periodo);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener ventas: " + e.getMessage(), e);
        }
    }

    public List<EstadisticaReparacion> obtenerDatosGraficaReparaciones(String periodo) {
        try {
            if (periodo == null) periodo = "mensual";
            return repository.obtenerReparacionesPorPeriodo(periodo);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener reparaciones: " + e.getMessage(), e);
        }
    }

    public List<EstadisticaMaterial> obtenerDatosGraficaMateriales(String periodo) {
        try {
            if (periodo == null) periodo = "mensual";
            return repository.obtenerMaterialesPorPeriodo(periodo);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener materiales: " + e.getMessage(), e);
        }
    }
}