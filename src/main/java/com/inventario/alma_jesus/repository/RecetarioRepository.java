package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.Recetario;
import com.inventario.alma_jesus.model.RecetaMaterial;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecetarioRepository {

    public List<Recetario> findAll() throws SQLException {
        List<Recetario> recetas = new ArrayList<>();
        String sql = "SELECT * FROM recetario ORDER BY fecha_creacion DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Recetario receta = mapRecetario(rs);
                recetas.add(receta);
            }
        }
        return recetas;
    }

    public Recetario findById(Long id) throws SQLException {
        String sql = "SELECT * FROM recetario WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Recetario receta = mapRecetario(rs);
                    return receta;
                }
            }
        }
        return null;
    }

    public Long create(Recetario receta) throws SQLException {
        String sql = "INSERT INTO recetario (producto_id, tiempo_fabricacion, instrucciones, notas, herramientas, creado_por) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, receta.getProductoId());
            stmt.setString(2, receta.getTiempoFabricacion());
            stmt.setString(3, receta.getInstrucciones());
            stmt.setString(4, receta.getNotas());
            stmt.setString(5, receta.getHerramientas());
            stmt.setString(6, receta.getCreadoPor());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("No se pudo crear la receta");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("No se pudo obtener el ID generado");
                }
            }
        }
    }

    public boolean update(Recetario receta) throws SQLException {
        String sql = "UPDATE recetario SET producto_id = ?, tiempo_fabricacion = ?, instrucciones = ?, notas = ?, herramientas = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, receta.getProductoId());
            stmt.setString(2, receta.getTiempoFabricacion());
            stmt.setString(3, receta.getInstrucciones());
            stmt.setString(4, receta.getNotas());
            stmt.setString(5, receta.getHerramientas());
            stmt.setLong(6, receta.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public boolean delete(Long id) throws SQLException {
        // Primero eliminar los materiales de la receta
        String sqlDeleteMateriales = "DELETE FROM recetamaterial WHERE receta_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlDeleteMateriales)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }

        // Luego eliminar la receta
        String sql = "DELETE FROM recetario WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    private Recetario mapRecetario(ResultSet rs) throws SQLException {
        Recetario receta = new Recetario();
        receta.setId(rs.getLong("id"));
        receta.setProductoId(rs.getLong("producto_id"));
        receta.setTiempoFabricacion(rs.getString("tiempo_fabricacion"));
        receta.setInstrucciones(rs.getString("instrucciones"));
        receta.setNotas(rs.getString("notas"));
        receta.setHerramientas(rs.getString("herramientas"));
        receta.setCreadoPor(rs.getString("creado_por"));

        Timestamp timestamp = rs.getTimestamp("fecha_creacion");
        if (timestamp != null) {
            receta.setFechaCreacion(timestamp.toLocalDateTime());
        }

        return receta;
    }
}