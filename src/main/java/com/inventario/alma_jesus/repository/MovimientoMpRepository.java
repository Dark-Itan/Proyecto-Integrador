package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.MovimientoMp;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovimientoMpRepository {
    private final Connection connection;

    public MovimientoMpRepository() {
        try {
            this.connection = DatabaseConnection.getConnection();
            System.out.println("[MOVIMIENTO-REPO] Conectado a la base de datos");
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }

    // Endpoint 30: Ver historial de movimientos
    public List<MovimientoMp> findByMateriaId(Long materiaId) {
        System.out.println(" [MOVIMIENTO-REPO] Obteniendo historial para material ID: " + materiaId);
        List<MovimientoMp> movimientos = new ArrayList<>();
        // CAMBIADO: "MovimientoMp" por "movimientomp"
        String sql = "SELECT * FROM movimientomp WHERE materia_id = ? ORDER BY fecha DESC, fecha_registro DESC";

        System.out.println("[MOVIMIENTO-REPO] SQL: " + sql);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, materiaId);

            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                movimientos.add(mapResultSetToMovimiento(rs));
                count++;
            }
            System.out.println("[MOVIMIENTO-REPO] Encontrados " + count + " movimientos");
        } catch (SQLException e) {
            System.out.println("[MOVIMIENTO-REPO] Error al obtener historial: " + e.getMessage());
            throw new RuntimeException("Error al obtener historial para material ID: " + materiaId, e);
        }
        return movimientos;
    }

    // Registrar movimiento (se usará internamente)
    public MovimientoMp save(MovimientoMp movimiento) {
        System.out.println("[MOVIMIENTO-REPO] Registrando movimiento - Material: " + movimiento.getMateriaId() +
                ", Tipo: " + movimiento.getTipo() + ", Cantidad: " + movimiento.getCantidad());
        // CAMBIADO: "MovimientoMp" por "movimientomp"
        String sql = "INSERT INTO movimientomp (materia_id, fecha, tipo, cantidad, usuario_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        System.out.println("[MOVIMIENTO-REPO] SQL: " + sql);

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, movimiento.getMateriaId());
            stmt.setString(2, movimiento.getFecha());
            stmt.setString(3, movimiento.getTipo());
            stmt.setInt(4, movimiento.getCantidad());
            stmt.setString(5, movimiento.getUsuarioId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("[MOVIMIENTO-REPO] Error: ninguna fila afectada");
                throw new SQLException("Error al registrar movimiento, ninguna fila afectada");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    movimiento.setId(generatedKeys.getLong(1));
                    System.out.println("[MOVIMIENTO-REPO] Movimiento registrado con ID: " + movimiento.getId());
                }
            }
            return movimiento;
        } catch (SQLException e) {
            System.out.println("❌ [MOVIMIENTO-REPO] Error al registrar movimiento: " + e.getMessage());
            throw new RuntimeException("Error al registrar movimiento: " + e.getMessage(), e);
        }
    }

    private MovimientoMp mapResultSetToMovimiento(ResultSet rs) throws SQLException {
        MovimientoMp movimiento = new MovimientoMp();
        movimiento.setId(rs.getLong("id"));
        movimiento.setMateriaId(rs.getLong("materia_id"));
        movimiento.setFecha(rs.getString("fecha"));
        movimiento.setTipo(rs.getString("tipo"));
        movimiento.setCantidad(rs.getInt("cantidad"));
        movimiento.setUsuarioId(rs.getString("usuario_id"));
        movimiento.setFechaRegistro(rs.getString("fecha_registro"));
        return movimiento;
    }
}