package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.MaterialUtilizado;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialUtilizadoRepository {
    private final HikariDataSource dataSource;

    public MaterialUtilizadoRepository(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Buscar materiales por tipo de documento y ID
    public List<MaterialUtilizado> findByTipoDocumentoAndDocumentoIdOrderByFechaRegistroDesc(
            MaterialUtilizado.TipoDocumento tipoDocumento, Long documentoId) {
        List<MaterialUtilizado> materiales = new ArrayList<>();
        String sql = "SELECT * FROM materialutilizado WHERE tipo_documento = ? AND documento_id = ? ORDER BY fecha_registro DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipoDocumento.name());
            stmt.setLong(2, documentoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    materiales.add(mapToMaterialUtilizado(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener materiales utilizados", e);
        }
        return materiales;
    }

    // Buscar materiales por reparación
    public List<MaterialUtilizado> findByTipoDocumentoAndDocumentoId(
            MaterialUtilizado.TipoDocumento tipoDocumento, Long documentoId) {
        return findByTipoDocumentoAndDocumentoIdOrderByFechaRegistroDesc(tipoDocumento, documentoId);
    }

    // Calcular costo total de materiales por reparación
    public Integer calcularCostoTotalMaterialesPorReparacion(Long reparacionId) {
        String sql = "SELECT COALESCE(SUM(cantidad * costo_unitario), 0) FROM materialutilizado " +
                "WHERE tipo_documento = 'reparacion' AND documento_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, reparacionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al calcular costo de materiales", e);
        }
        return 0;
    }

    // Verificar si hay materiales utilizados para una reparación
    public boolean existsByTipoDocumentoAndDocumentoId(MaterialUtilizado.TipoDocumento tipoDocumento, Long documentoId) {
        String sql = "SELECT COUNT(*) FROM materialutilizado WHERE tipo_documento = ? AND documento_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipoDocumento.name());
            stmt.setLong(2, documentoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar materiales utilizados", e);
        }
        return false;
    }

    // Guardar material utilizado
    public MaterialUtilizado save(MaterialUtilizado material) {
        String sql = "INSERT INTO materialutilizado (tipo_documento, documento_id, materia_id, " +
                "cantidad, costo_unitario, fecha, usuario_id, fecha_registro) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, material.getTipoDocumento().name());
            stmt.setLong(2, material.getDocumentoId());
            stmt.setLong(3, material.getMateriaId());
            stmt.setInt(4, material.getCantidad());
            stmt.setInt(5, material.getCostoUnitario());
            stmt.setDate(6, Date.valueOf(material.getFecha()));
            stmt.setString(7, material.getUsuarioId());
            stmt.setTimestamp(8, Timestamp.valueOf(material.getFechaRegistro()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear material utilizado, ninguna fila afectada");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    material.setId(generatedKeys.getLong(1));
                }
            }

            return material;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar material utilizado", e);
        }
    }

    // Eliminar material utilizado
    public void deleteById(Long materialId) {
        String sql = "DELETE FROM materialutilizado WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, materialId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar material utilizado", e);
        }
    }

    // Verificar si existe material por ID
    public boolean existsById(Long materialId) {
        String sql = "SELECT COUNT(*) FROM materialutilizado WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, materialId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar material utilizado", e);
        }
        return false;
    }

    // Mapear ResultSet a MaterialUtilizado
    private MaterialUtilizado mapToMaterialUtilizado(ResultSet rs) throws SQLException {
        MaterialUtilizado material = new MaterialUtilizado();
        material.setId(rs.getLong("id"));
        material.setTipoDocumento(MaterialUtilizado.TipoDocumento.valueOf(rs.getString("tipo_documento")));
        material.setDocumentoId(rs.getLong("documento_id"));
        material.setMateriaId(rs.getLong("materia_id"));
        material.setCantidad(rs.getInt("cantidad"));
        material.setCostoUnitario(rs.getInt("costo_unitario"));
        material.setFecha(rs.getDate("fecha").toLocalDate());
        material.setUsuarioId(rs.getString("usuario_id"));
        material.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());

        return material;
    }
}