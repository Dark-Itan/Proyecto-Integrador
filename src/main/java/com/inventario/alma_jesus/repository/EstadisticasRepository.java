package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.EstadisticaDashboard;
import com.inventario.alma_jesus.model.EstadisticaVenta;
import com.inventario.alma_jesus.model.EstadisticaReparacion;
import com.inventario.alma_jesus.model.EstadisticaMaterial;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para la obtención de estadísticas del sistema.
 * <p>
 * Esta clase proporciona métodos para consultar y generar estadísticas
 * de ventas, reparaciones, materiales y métricas generales del dashboard.
 * Utiliza consultas SQL optimizadas para diferentes períodos de tiempo.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class EstadisticasRepository {

    /**
     * Obtiene las estadísticas generales para el dashboard principal.
     * <p>
     * Consulta múltiples métricas en una sola consulta SQL para eficiencia:
     * - Total de ventas
     * - Reparaciones pendientes y totales
     * - Clientes activos
     * - Materiales consumidos
     * </p>
     *
     * @return Objeto {@link EstadisticaDashboard} con todas las métricas
     * @throws SQLException Si ocurre un error en la consulta a la base de datos
     */
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

    /**
     * Obtiene estadísticas de ventas agrupadas por período.
     * <p>
     * Soporta diferentes niveles de agrupación temporal:
     * - "diario": Últimos 7 días agrupados por día de la semana
     * - "semanal": Últimas 8 semanas
     * - "mensual": Últimos 12 meses (valor por defecto)
     * - "anual": Últimos 3 años
     * </p>
     *
     * @param periodo Tipo de período para agrupar los datos ("diario", "semanal", "mensual", "anual")
     * @return Lista de {@link EstadisticaVenta} con cantidad y monto por período
     * @throws SQLException Si ocurre un error en la consulta a la base de datos
     */
    public List<EstadisticaVenta> obtenerVentasPorPeriodo(String periodo) throws SQLException {
        List<EstadisticaVenta> ventas = new ArrayList<>();
        String sql = "";

        if ("diario".equalsIgnoreCase(periodo)) {
            // CONSULTA CORREGIDA: compatible con only_full_group_by
            sql = "SELECT " +
                    "CASE DAYOFWEEK(fecha) " +
                    "  WHEN 1 THEN 'Domingo' " +
                    "  WHEN 2 THEN 'Lunes' " +
                    "  WHEN 3 THEN 'Martes' " +
                    "  WHEN 4 THEN 'Miércoles' " +
                    "  WHEN 5 THEN 'Jueves' " +
                    "  WHEN 6 THEN 'Viernes' " +
                    "  WHEN 7 THEN 'Sábado' " +
                    "END as periodo, " +
                    "COUNT(*) as cantidad, " +
                    "COALESCE(SUM(precio_total), 0) as monto " +
                    "FROM venta " +
                    "WHERE fecha >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
                    "GROUP BY DAYOFWEEK(fecha) " + // SOLO DAYOFWEEK en GROUP BY
                    "ORDER BY DAYOFWEEK(fecha)";   // Ordenar por DAYOFWEEK directamente
        } else if ("semanal".equalsIgnoreCase(periodo)) {
            sql = "SELECT " +
                    "CONCAT('Semana ', WEEK(fecha, 1)) as periodo, " +
                    "COUNT(*) as cantidad, " +
                    "COALESCE(SUM(precio_total), 0) as monto " +
                    "FROM venta " +
                    "WHERE fecha >= DATE_SUB(CURDATE(), INTERVAL 8 WEEK) " +
                    "GROUP BY WEEK(fecha, 1) " + // SOLO WEEK en GROUP BY
                    "ORDER BY WEEK(fecha, 1)";   // Ordenar por WEEK directamente
        } else if ("mensual".equalsIgnoreCase(periodo)) {
            sql = "SELECT " +
                    "DATE_FORMAT(fecha, '%Y-%m') as periodo, " +
                    "COUNT(*) as cantidad, " +
                    "COALESCE(SUM(precio_total), 0) as monto " +
                    "FROM venta " +
                    "WHERE fecha >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH) " +
                    "GROUP BY DATE_FORMAT(fecha, '%Y-%m') " + // SOLO DATE_FORMAT en GROUP BY
                    "ORDER BY DATE_FORMAT(fecha, '%Y-%m')";   // Ordenar por DATE_FORMAT directamente
        } else if ("anual".equalsIgnoreCase(periodo)) {
            sql = "SELECT " +
                    "YEAR(fecha) as periodo, " +
                    "COUNT(*) as cantidad, " +
                    "COALESCE(SUM(precio_total), 0) as monto " +
                    "FROM venta " +
                    "WHERE fecha >= DATE_SUB(CURDATE(), INTERVAL 3 YEAR) " +
                    "GROUP BY YEAR(fecha) " + // SOLO YEAR en GROUP BY
                    "ORDER BY YEAR(fecha)";   // Ordenar por YEAR directamente
        } else {
            // Por defecto: últimos 12 meses
            sql = "SELECT " +
                    "DATE_FORMAT(fecha, '%Y-%m') as periodo, " +
                    "COUNT(*) as cantidad, " +
                    "COALESCE(SUM(precio_total), 0) as monto " +
                    "FROM venta " +
                    "WHERE fecha >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH) " +
                    "GROUP BY DATE_FORMAT(fecha, '%Y-%m') " +
                    "ORDER BY DATE_FORMAT(fecha, '%Y-%m')";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EstadisticaVenta venta = new EstadisticaVenta();
                venta.setPeriodo(rs.getString("periodo"));
                venta.setCantidad(BigDecimal.valueOf(rs.getLong("cantidad")));
                venta.setMonto(BigDecimal.valueOf(rs.getDouble("monto")));
                ventas.add(venta);
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerVentasPorPeriodo: " + e.getMessage());
            throw e;
        }

        return ventas;
    }

    /**
     * Obtiene estadísticas de reparaciones agrupadas por período.
     * <p>
     * Consulta el volumen de reparaciones realizadas, considerando
     * solo las reparaciones activas en el sistema.
     * </p>
     *
     * @param periodo Tipo de período para agrupar los datos ("diario", "semanal", "mensual", "anual")
     * @return Lista de {@link EstadisticaReparacion} con cantidad por período
     * @throws SQLException Si ocurre un error en la consulta a la base de datos
     */
    public List<EstadisticaReparacion> obtenerReparacionesPorPeriodo(String periodo) throws SQLException {
        List<EstadisticaReparacion> reparaciones = new ArrayList<>();
        String sql = "";

        if ("diario".equalsIgnoreCase(periodo)) {
            sql = "SELECT " +
                    "CASE DAYOFWEEK(fecha_ingreso) " +
                    "  WHEN 1 THEN 'Domingo' " +
                    "  WHEN 2 THEN 'Lunes' " +
                    "  WHEN 3 THEN 'Martes' " +
                    "  WHEN 4 THEN 'Miércoles' " +
                    "  WHEN 5 THEN 'Jueves' " +
                    "  WHEN 6 THEN 'Viernes' " +
                    "  WHEN 7 THEN 'Sábado' " +
                    "END as periodo, " +
                    "COUNT(*) as cantidad_reparaciones " + // Removido dia_orden que no se usa
                    "FROM reparacion " +
                    "WHERE fecha_ingreso >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) AND activo = true " +
                    "GROUP BY DAYOFWEEK(fecha_ingreso) " + // SOLO DAYOFWEEK en GROUP BY
                    "ORDER BY DAYOFWEEK(fecha_ingreso)";   // Ordenar por DAYOFWEEK directamente
        } else if ("semanal".equalsIgnoreCase(periodo)) {
            sql = "SELECT " +
                    "CONCAT('Semana ', WEEK(fecha_ingreso)) as periodo, " +
                    "COUNT(*) as cantidad_reparaciones " +
                    "FROM reparacion " +
                    "WHERE fecha_ingreso >= DATE_SUB(CURDATE(), INTERVAL 8 WEEK) AND activo = true " +
                    "GROUP BY WEEK(fecha_ingreso) " + // SOLO WEEK en GROUP BY
                    "ORDER BY WEEK(fecha_ingreso)";   // Ordenar por WEEK directamente
        } else if ("mensual".equalsIgnoreCase(periodo)) {
            sql = "SELECT " +
                    "DATE_FORMAT(fecha_ingreso, '%Y-%m') as periodo, " +
                    "COUNT(*) as cantidad_reparaciones " +
                    "FROM reparacion " +
                    "WHERE fecha_ingreso >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH) AND activo = true " +
                    "GROUP BY DATE_FORMAT(fecha_ingreso, '%Y-%m') " + // SOLO DATE_FORMAT en GROUP BY
                    "ORDER BY DATE_FORMAT(fecha_ingreso, '%Y-%m')";   // Ordenar por DATE_FORMAT directamente
        } else if ("anual".equalsIgnoreCase(periodo)) {
            sql = "SELECT " +
                    "YEAR(fecha_ingreso) as periodo, " +
                    "COUNT(*) as cantidad_reparaciones " +
                    "FROM reparacion " +
                    "WHERE fecha_ingreso >= DATE_SUB(CURDATE(), INTERVAL 3 YEAR) AND activo = true " +
                    "GROUP BY YEAR(fecha_ingreso) " + // SOLO YEAR en GROUP BY
                    "ORDER BY YEAR(fecha_ingreso)";   // Ordenar por YEAR directamente
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EstadisticaReparacion reparacion = new EstadisticaReparacion();
                reparacion.setPeriodo(rs.getString("periodo"));
                reparacion.setCantidadReparaciones(rs.getInt("cantidad_reparaciones"));
                reparacion.setTipoReparacion("General"); // Valor fijo
                reparaciones.add(reparacion);
            }
        }
        return reparaciones;
    }

    /**
     * Obtiene estadísticas de consumo de materiales agrupadas por período.
     * <p>
     * Consulta los materiales consumidos en movimientos de tipo 'consumo',
     * agrupados por material y período. Útil para análisis de costos y
     * previsión de inventario.
     * </p>
     *
     * @param periodo Tipo de período para agrupar los datos ("diario", "semanal", "mensual", "anual")
     * @return Lista de {@link EstadisticaMaterial} con material y cantidad consumida
     * @throws SQLException Si ocurre un error en la consulta a la base de datos
     */
    public List<EstadisticaMaterial> obtenerMaterialesPorPeriodo(String periodo) throws SQLException {
        List<EstadisticaMaterial> materiales = new ArrayList<>();
        String sql = "";

        if ("diario".equalsIgnoreCase(periodo)) {
            sql = "SELECT " +
                    "CASE DAYOFWEEK(m.fecha) " +
                    "  WHEN 1 THEN 'Domingo' " +
                    "  WHEN 2 THEN 'Lunes' " +
                    "  WHEN 3 THEN 'Martes' " +
                    "  WHEN 4 THEN 'Miércoles' " +
                    "  WHEN 5 THEN 'Jueves' " +
                    "  WHEN 6 THEN 'Viernes' " +
                    "  WHEN 7 THEN 'Sábado' " +
                    "END as periodo, " +
                    "mp.nombre as material, " +
                    "SUM(m.cantidad) as cantidad_consumida " +
                    "FROM movimientomp m " +
                    "INNER JOIN materiaprima mp ON m.materia_id = mp.id " +
                    "WHERE m.tipo = 'consumo' AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
                    "GROUP BY DAYOFWEEK(m.fecha), mp.nombre " + // SOLO columnas reales en GROUP BY
                    "ORDER BY DAYOFWEEK(m.fecha), SUM(m.cantidad) DESC";
        } else if ("semanal".equalsIgnoreCase(periodo)) {
            sql = "SELECT " +
                    "CONCAT('Semana ', WEEK(m.fecha)) as periodo, " +
                    "mp.nombre as material, " +
                    "SUM(m.cantidad) as cantidad_consumida " +
                    "FROM movimientomp m " +
                    "INNER JOIN materiaprima mp ON m.materia_id = mp.id " +
                    "WHERE m.tipo = 'consumo' AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 8 WEEK) " +
                    "GROUP BY WEEK(m.fecha), mp.nombre " + // SOLO WEEK y nombre en GROUP BY
                    "ORDER BY WEEK(m.fecha), SUM(m.cantidad) DESC";
        } else if ("mensual".equalsIgnoreCase(periodo)) {
            sql = "SELECT " +
                    "DATE_FORMAT(m.fecha, '%Y-%m') as periodo, " +
                    "mp.nombre as material, " +
                    "SUM(m.cantidad) as cantidad_consumida " +
                    "FROM movimientomp m " +
                    "INNER JOIN materiaprima mp ON m.materia_id = mp.id " +
                    "WHERE m.tipo = 'consumo' AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH) " +
                    "GROUP BY DATE_FORMAT(m.fecha, '%Y-%m'), mp.nombre " + // SOLO DATE_FORMAT y nombre
                    "ORDER BY DATE_FORMAT(m.fecha, '%Y-%m'), SUM(m.cantidad) DESC";
        } else if ("anual".equalsIgnoreCase(periodo)) {
            sql = "SELECT " +
                    "YEAR(m.fecha) as periodo, " +
                    "mp.nombre as material, " +
                    "SUM(m.cantidad) as cantidad_consumida " +
                    "FROM movimientomp m " +
                    "INNER JOIN materiaprima mp ON m.materia_id = mp.id " +
                    "WHERE m.tipo = 'consumo' AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 3 YEAR) " +
                    "GROUP BY YEAR(m.fecha), mp.nombre " + // SOLO YEAR y nombre en GROUP BY
                    "ORDER BY YEAR(m.fecha), SUM(m.cantidad) DESC";
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