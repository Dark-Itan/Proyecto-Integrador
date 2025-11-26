package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.HistorialReparacion;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistorialReparacionRepository {
    private final Connection connection;

    public HistorialReparacionRepository() {
        try {
            this.connection = DatabaseConnection.getConnection();
            System.out.println("📋 [HISTORIAL-REPO] Conectado a la base de datos");
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }

    // Endpoint 6: Ver historial de reparación
    public List<HistorialReparacion> findByReparacionId(Long reparacionId) {
        System.out.println("📋 [HISTORIAL-REPO] Obteniendo historial para reparación ID: " + reparacionId);
        List<HistorialReparacion> historiales = new ArrayList<>();
        String sql = "SELECT * FROM HistorialReparacion WHERE reparacion_id = ? ORDER BY fecha DESC, fecha_registro DESC";

        System.out.println("📋 [HISTORIAL-REPO] SQL: " + sql);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, reparacionId);

            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                historiales.add(mapResultSetToHistorial(rs));
                count++;
            }
            System.out.println("📋 [HISTORIAL-REPO] Encontrados " + count + " registros de historial");
        } catch (SQLException e) {
            System.out.println("❌ [HISTORIAL-REPO] Error al obtener historial: " + e.getMessage());
            throw new RuntimeException("Error al obtener historial para reparación ID: " + reparacionId, e);
        }
        return historiales;
    }

    // Registrar historial (se usará internamente)
    public HistorialReparacion save(HistorialReparacion historial) {
        System.out.println("📋 [HISTORIAL-REPO] Registrando historial - Reparación: " + historial.getReparacionId() +
                ", Estado: " + historial.getEstado() + ", Usuario: " + historial.getUsuarioId());
        String sql = "INSERT INTO HistorialReparacion (reparacion_id, fecha, estado, notas, usuario_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        System.out.println("📋 [HISTORIAL-REPO] SQL: " + sql);

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, historial.getReparacionId());
            stmt.setString(2, historial.getFecha());
            stmt.setString(3, historial.getEstado());
            stmt.setString(4, historial.getNotas());
            stmt.setString(5, historial.getUsuarioId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("❌ [HISTORIAL-REPO] Error: ninguna fila afectada");
                throw new SQLException("Error al registrar historial, ninguna fila afectada");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    historial.setId(generatedKeys.getLong(1));
                    System.out.println("📋 [HISTORIAL-REPO] Historial registrado con ID: " + historial.getId());
                }
            }
            return historial;
        } catch (SQLException e) {
            System.out.println("❌ [HISTORIAL-REPO] Error al registrar historial: " + e.getMessage());
            throw new RuntimeException("Error al registrar historial: " + e.getMessage(), e);
        }
    }

    private HistorialReparacion mapResultSetToHistorial(ResultSet rs) throws SQLException {
        HistorialReparacion historial = new HistorialReparacion();
        historial.setId(rs.getLong("id"));
        historial.setReparacionId(rs.getLong("reparacion_id"));
        historial.setFecha(rs.getString("fecha"));
        historial.setEstado(rs.getString("estado"));
        historial.setNotas(rs.getString("notas"));
        historial.setUsuarioId(rs.getString("usuario_id"));
        historial.setFechaRegistro(rs.getString("fecha_registro"));
        return historial;
    }
}