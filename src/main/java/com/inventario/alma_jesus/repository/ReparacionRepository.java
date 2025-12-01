package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.Reparacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReparacionRepository {

    public ReparacionRepository() {}

    public List<Reparacion> findAll(String estado, String cliente, String modelo) {
        List<Reparacion> reparaciones = new ArrayList<>();

        String sql = "SELECT * FROM reparacion WHERE activo = true";
        List<Object> parametros = new ArrayList<>();

        if (estado != null && !estado.isEmpty()) {
            sql += " AND estado = ?";
            parametros.add(estado);
        }
        if (cliente != null && !cliente.isEmpty()) {
            sql += " AND nombre_cliente LIKE ?";
            parametros.add("%" + cliente + "%");
        }
        if (modelo != null && !modelo.isEmpty()) {
            sql += " AND modelo LIKE ?";
            parametros.add("%" + modelo + "%");
        }

        sql += " ORDER BY fecha_registro DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reparaciones.add(mapResultSetToReparacion(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar reparaciones", e);
        }

        return reparaciones;
    }

    public Optional<Reparacion> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        String sql = "SELECT * FROM reparacion WHERE id = ? AND activo = true";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToReparacion(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar reparacion ID: " + id, e);
        }

        return Optional.empty();
    }

    public Reparacion save(Reparacion reparacion) {
        if (reparacion == null) {
            throw new RuntimeException("Reparacion no puede ser null");
        }

        String sql = "INSERT INTO reparacion (cliente_id, nombre_cliente, contacto, modelo, " +
                "material_original, condicion, costo_total, anticipo, fecha_ingreso, fecha_entrega, " +
                "estado, notas, imagen_url, recibo_url, creado_por) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, reparacion.getClienteId() != null ? reparacion.getClienteId() : 1);
            stmt.setString(2, reparacion.getNombreCliente());
            stmt.setString(3, reparacion.getContacto());
            stmt.setString(4, reparacion.getModelo());
            stmt.setString(5, reparacion.getMaterialOriginal());
            stmt.setString(6, reparacion.getCondicion());
            stmt.setInt(7, reparacion.getCostoTotal() != null ? reparacion.getCostoTotal() : 0);
            stmt.setInt(8, reparacion.getAnticipo() != null ? reparacion.getAnticipo() : 0);
            stmt.setString(9, reparacion.getFechaIngreso() != null ? reparacion.getFechaIngreso() : java.time.LocalDate.now().toString());
            stmt.setString(10, reparacion.getFechaEntrega());
            stmt.setString(11, reparacion.getEstado() != null ? reparacion.getEstado() : "Pendiente");
            stmt.setString(12, reparacion.getNotas());
            stmt.setString(13, reparacion.getImagenUrl());
            stmt.setString(14, reparacion.getReciboUrl());
            stmt.setString(15, reparacion.getCreadoPor() != null ? reparacion.getCreadoPor() : "Sistema");

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear reparacion, ninguna fila afectada");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reparacion.setId(generatedKeys.getLong(1));
                    return reparacion;
                } else {
                    throw new SQLException("No se pudo obtener el ID generado");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear reparacion: " + e.getMessage(), e);
        }
    }

    public boolean update(Reparacion reparacion) {
        if (reparacion == null || reparacion.getId() == null) {
            return false;
        }

        String sql = "UPDATE reparacion SET nombre_cliente = ?, contacto = ?, modelo = ?, " +
                "material_original = ?, condicion = ?, costo_total = ?, anticipo = ?, " +
                "fecha_entrega = ?, estado = ?, notas = ?, imagen_url = ?, recibo_url = ? " +
                "WHERE id = ? AND activo = true";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, reparacion.getNombreCliente());
            stmt.setString(2, reparacion.getContacto());
            stmt.setString(3, reparacion.getModelo());
            stmt.setString(4, reparacion.getMaterialOriginal());
            stmt.setString(5, reparacion.getCondicion());
            stmt.setInt(6, reparacion.getCostoTotal());
            stmt.setInt(7, reparacion.getAnticipo());
            stmt.setString(8, reparacion.getFechaEntrega());
            stmt.setString(9, reparacion.getEstado());
            stmt.setString(10, reparacion.getNotas());
            stmt.setString(11, reparacion.getImagenUrl());
            stmt.setString(12, reparacion.getReciboUrl());
            stmt.setLong(13, reparacion.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar reparacion ID: " + reparacion.getId(), e);
        }
    }

    public boolean updateEstado(Long id, String nuevoEstado) {
        if (id == null || nuevoEstado == null) {
            return false;
        }

        String sql = "UPDATE reparacion SET estado = ? WHERE id = ? AND activo = true";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado);
            stmt.setLong(2, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al cambiar estado reparacion ID: " + id, e);
        }
    }

    public boolean delete(Long id) {
        if (id == null) {
            return false;
        }

        String sql = "UPDATE reparacion SET activo = false WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar reparacion ID: " + id, e);
        }
    }

    private Reparacion mapResultSetToReparacion(ResultSet rs) throws SQLException {
        Reparacion reparacion = new Reparacion();
        reparacion.setId(rs.getLong("id"));
        reparacion.setClienteId(rs.getLong("cliente_id"));
        reparacion.setNombreCliente(rs.getString("nombre_cliente"));
        reparacion.setContacto(rs.getString("contacto"));
        reparacion.setModelo(rs.getString("modelo"));
        reparacion.setMaterialOriginal(rs.getString("material_original"));
        reparacion.setCondicion(rs.getString("condicion"));
        reparacion.setCostoTotal(rs.getInt("costo_total"));
        reparacion.setAnticipo(rs.getInt("anticipo"));
        reparacion.setFechaIngreso(rs.getString("fecha_ingreso"));
        reparacion.setFechaEntrega(rs.getString("fecha_entrega"));
        reparacion.setEstado(rs.getString("estado"));
        reparacion.setNotas(rs.getString("notas"));
        reparacion.setImagenUrl(rs.getString("imagen_url"));
        reparacion.setReciboUrl(rs.getString("recibo_url"));
        reparacion.setCreadoPor(rs.getString("creado_por"));
        reparacion.setActivo(rs.getBoolean("activo"));
        reparacion.setFechaRegistro(rs.getString("fecha_registro"));
        return reparacion;
    }
}