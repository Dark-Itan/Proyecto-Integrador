package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.EstadisticaDashboard;
import com.inventario.alma_jesus.model.EstadisticaVenta;
import com.inventario.alma_jesus.model.EstadisticaReparacion;
import com.inventario.alma_jesus.model.EstadisticaMaterial;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EstadisticasRepository {

    public EstadisticaDashboard obtenerEstadisticasGenerales() throws SQLException {
        EstadisticaDashboard stats = new EstadisticaDashboard();

        String sql = "SELECT " +
                "(SELECT COALESCE(SUM(precio_total), 0) FROM venta) as total_ventas, " +
                "(SELECT COUNT(*) FROM reparacion WHERE estado != 'Completada' AND activo = true) as reparaciones_pendientes, " +
                "(SELECT COUNT(*) FROM reparacion WHERE activo = true) as total_reparaciones, " +
                "(SELECT COUNT(DISTINCT cliente_id) FROM venta) as clientes_activos, " +
                "(SELECT COALESCE(SUM(cantidad), 0) FROM movimientomp WHERE tipo = 'consumo') as materiales_consumidos";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                stats.setTotalVentas(BigDecimal.valueOf(rs.getInt("total_ventas")));
                stats.setPedidosPendientes(rs.getInt("reparaciones_pendientes"));
                stats.setTotalReparaciones(rs.getInt("total_reparaciones"));
                stats.setClientesActivos(rs.getInt("clientes_activos"));
                stats.setMaterialesUtilizados(BigDecimal.valueOf(rs.getInt("materiales_consumidos")));
            }
        }
        return stats;
    }

    public List<EstadisticaVenta> obtenerVentasPorPeriodo(String periodo) throws SQLException {
        List<EstadisticaVenta> ventas = new ArrayList<>();
        String sql = "";

        if ("semanal".equalsIgnoreCase(periodo)) {
            sql = "SELECT CONCAT('Semana ', WEEK(fecha)) as periodo, COUNT(*) as cantidad, COALESCE(SUM(precio_total), 0) as monto FROM venta WHERE fecha >= DATE_SUB(CURDATE(), INTERVAL 8 WEEK) GROUP BY WEEK(fecha) ORDER BY WEEK(fecha)";
        } else if ("mensual".equalsIgnoreCase(periodo)) {
            sql = "SELECT DATE_FORMAT(fecha, '%Y-%m') as periodo, COUNT(*) as cantidad, COALESCE(SUM(precio_total), 0) as monto FROM venta WHERE fecha >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH) GROUP BY DATE_FORMAT(fecha, '%Y-%m') ORDER BY DATE_FORMAT(fecha, '%Y-%m')";
        } else if ("anual".equalsIgnoreCase(periodo)) {
            sql = "SELECT YEAR(fecha) as periodo, COUNT(*) as cantidad, COALESCE(SUM(precio_total), 0) as monto FROM venta WHERE fecha >= DATE_SUB(CURDATE(), INTERVAL 3 YEAR) GROUP BY YEAR(fecha) ORDER BY YEAR(fecha)";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EstadisticaVenta venta = new EstadisticaVenta();
                venta.setPeriodo(rs.getString("periodo"));
                venta.setCantidad(BigDecimal.valueOf(rs.getInt("cantidad")));
                venta.setMonto(BigDecimal.valueOf(rs.getInt("monto")));
                ventas.add(venta);
            }
        }
        return ventas;
    }

    public List<EstadisticaReparacion> obtenerReparacionesPorPeriodo(String periodo) throws SQLException {
        List<EstadisticaReparacion> reparaciones = new ArrayList<>();
        String sql = "";

        if ("semanal".equalsIgnoreCase(periodo)) {
            sql = "SELECT CONCAT('Semana ', WEEK(fecha_ingreso)) as periodo, COUNT(*) as cantidad_reparaciones, 'General' as tipo_reparacion FROM reparacion WHERE fecha_ingreso >= DATE_SUB(CURDATE(), INTERVAL 8 WEEK) AND activo = true GROUP BY WEEK(fecha_ingreso) ORDER BY WEEK(fecha_ingreso)";
        } else if ("mensual".equalsIgnoreCase(periodo)) {
            sql = "SELECT DATE_FORMAT(fecha_ingreso, '%Y-%m') as periodo, COUNT(*) as cantidad_reparaciones, 'General' as tipo_reparacion FROM reparacion WHERE fecha_ingreso >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH) AND activo = true GROUP BY DATE_FORMAT(fecha_ingreso, '%Y-%m') ORDER BY DATE_FORMAT(fecha_ingreso, '%Y-%m')";
        } else if ("anual".equalsIgnoreCase(periodo)) {
            sql = "SELECT YEAR(fecha_ingreso) as periodo, COUNT(*) as cantidad_reparaciones, 'General' as tipo_reparacion FROM reparacion WHERE fecha_ingreso >= DATE_SUB(CURDATE(), INTERVAL 3 YEAR) AND activo = true GROUP BY YEAR(fecha_ingreso) ORDER BY YEAR(fecha_ingreso)";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EstadisticaReparacion reparacion = new EstadisticaReparacion();
                reparacion.setPeriodo(rs.getString("periodo"));
                reparacion.setCantidadReparaciones(rs.getInt("cantidad_reparaciones"));
                reparacion.setTipoReparacion(rs.getString("tipo_reparacion"));
                reparaciones.add(reparacion);
            }
        }
        return reparaciones;
    }

    public List<EstadisticaMaterial> obtenerMaterialesPorPeriodo(String periodo) throws SQLException {
        List<EstadisticaMaterial> materiales = new ArrayList<>();
        String sql = "";

        if ("semanal".equalsIgnoreCase(periodo)) {
            sql = "SELECT CONCAT('Semana ', WEEK(m.fecha)) as periodo, mp.nombre as material, SUM(m.cantidad) as cantidad_consumida FROM movimientomp m INNER JOIN materiaprima mp ON m.materia_id = mp.id WHERE m.tipo = 'consumo' AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 8 WEEK) GROUP BY WEEK(m.fecha), mp.nombre ORDER BY WEEK(m.fecha), cantidad_consumida DESC";
        } else if ("mensual".equalsIgnoreCase(periodo)) {
            sql = "SELECT DATE_FORMAT(m.fecha, '%Y-%m') as periodo, mp.nombre as material, SUM(m.cantidad) as cantidad_consumida FROM movimientomp m INNER JOIN materiaprima mp ON m.materia_id = mp.id WHERE m.tipo = 'consumo' AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH) GROUP BY DATE_FORMAT(m.fecha, '%Y-%m'), mp.nombre ORDER BY DATE_FORMAT(m.fecha, '%Y-%m'), cantidad_consumida DESC";
        } else if ("anual".equalsIgnoreCase(periodo)) {
            sql = "SELECT YEAR(m.fecha) as periodo, mp.nombre as material, SUM(m.cantidad) as cantidad_consumida FROM movimientomp m INNER JOIN materiaprima mp ON m.materia_id = mp.id WHERE m.tipo = 'consumo' AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 3 YEAR) GROUP BY YEAR(m.fecha), mp.nombre ORDER BY YEAR(m.fecha), cantidad_consumida DESC";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EstadisticaMaterial material = new EstadisticaMaterial();
                material.setPeriodo(rs.getString("periodo"));
                material.setMaterial(rs.getString("material"));
                material.setCantidadConsumida(BigDecimal.valueOf(rs.getInt("cantidad_consumida")));
                materiales.add(material);
            }
        }
        return materiales;
    }
}