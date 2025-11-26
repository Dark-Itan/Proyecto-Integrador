package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.Venta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VentaRepository {

    public List<Venta> findAll() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT v.id, v.cliente_id, v.producto_id, p.modelo as producto_modelo, " +
                "v.cantidad, v.precio_unitario, v.precio_total, v.fecha, v.tipo, " +
                "v.usuario_registro, v.fecha_registro " +
                "FROM venta v " +
                "INNER JOIN producto p ON v.producto_id = p.id " +
                "ORDER BY v.fecha_registro DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Venta venta = mapearVenta(rs);
                ventas.add(venta);
            }

        } catch (SQLException e) {
            System.out.println("Error en VentaRepository.findAll: " + e.getMessage());
        }

        return ventas;
    }

    public Optional<Venta> findById(int id) {
        String sql = "SELECT v.id, v.cliente_id, v.producto_id, p.modelo as producto_modelo, " +
                "v.cantidad, v.precio_unitario, v.precio_total, v.fecha, v.tipo, " +
                "v.usuario_registro, v.fecha_registro " +
                "FROM venta v " +
                "INNER JOIN producto p ON v.producto_id = p.id " +
                "WHERE v.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Venta venta = mapearVenta(rs);
                return Optional.of(venta);
            }

        } catch (SQLException e) {
            System.out.println("Error en VentaRepository.findById: " + e.getMessage());
        }

        return Optional.empty();
    }

    public boolean crearVenta(Venta venta) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // ✅ ELIMINAR LA VERIFICACIÓN DE DUPLICADOS
            // Ya no verificamos duplicados porque en una tienda física
            // es normal vender el mismo producto varias veces al día

            // Insertar la venta directamente
            String insertSql = "INSERT INTO venta (cliente_id, producto_id, cantidad, precio_unitario, " +
                    "precio_total, fecha, tipo, usuario_registro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, venta.getClienteId());
                insertStmt.setInt(2, venta.getProductoId());
                insertStmt.setInt(3, venta.getCantidad());
                insertStmt.setInt(4, venta.getPrecioUnitario());
                insertStmt.setInt(5, venta.getPrecioTotal());
                insertStmt.setString(6, venta.getFecha());
                insertStmt.setString(7, venta.getTipo());
                insertStmt.setString(8, venta.getUsuarioRegistro());

                int filasAfectadas = insertStmt.executeUpdate();

                if (filasAfectadas > 0) {
                    conn.commit(); // Confirmar transacción
                    System.out.println("✅ Venta registrada exitosamente: Producto ID " + venta.getProductoId() + " - Fecha: " + venta.getFecha());
                    return true;
                } else {
                    conn.rollback(); // Revertir si no se insertó
                    return false;
                }
            }

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error en rollback: " + rollbackEx.getMessage());
            }
            System.out.println("❌ Error en VentaRepository.crearVenta: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error cerrando conexión: " + e.getMessage());
            }
        }
    }

    public boolean eliminarVenta(int id) {
        String sql = "DELETE FROM venta WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error en VentaRepository.eliminarVenta: " + e.getMessage());
            return false;
        }
    }

    // Método para debugging - encontrar ventas similares (opcional)
    public List<Venta> findDuplicadas(Venta venta) {
        List<Venta> duplicadas = new ArrayList<>();
        String sql = "SELECT v.id, v.cliente_id, v.producto_id, p.modelo as producto_modelo, " +
                "v.cantidad, v.precio_unitario, v.precio_total, v.fecha, v.tipo, " +
                "v.usuario_registro, v.fecha_registro " +
                "FROM venta v " +
                "INNER JOIN producto p ON v.producto_id = p.id " +
                "WHERE v.cliente_id = ? AND v.producto_id = ? AND v.cantidad = ? AND " +
                "v.precio_unitario = ? AND v.fecha = ? AND v.tipo = ? " +
                "ORDER BY v.fecha_registro DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, venta.getClienteId());
            stmt.setInt(2, venta.getProductoId());
            stmt.setInt(3, venta.getCantidad());
            stmt.setInt(4, venta.getPrecioUnitario());
            stmt.setString(5, venta.getFecha());
            stmt.setString(6, venta.getTipo());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                duplicadas.add(mapearVenta(rs));
            }

            System.out.println("ℹ️ Ventas similares encontradas (solo informativo): " + duplicadas.size());

        } catch (SQLException e) {
            System.out.println("Error buscando duplicados: " + e.getMessage());
        }

        return duplicadas;
    }

    private Venta mapearVenta(ResultSet rs) throws SQLException {
        Venta venta = new Venta();
        venta.setId(rs.getInt("id"));
        venta.setClienteId(rs.getInt("cliente_id"));
        venta.setProductoId(rs.getInt("producto_id"));
        venta.setProductoModelo(rs.getString("producto_modelo"));
        venta.setCantidad(rs.getInt("cantidad"));
        venta.setPrecioUnitario(rs.getInt("precio_unitario"));
        venta.setPrecioTotal(rs.getInt("precio_total"));
        venta.setFecha(rs.getString("fecha"));
        venta.setTipo(rs.getString("tipo"));
        venta.setUsuarioRegistro(rs.getString("usuario_registro"));
        venta.setFechaRegistro(rs.getString("fecha_registro"));

        return venta;
    }
}