package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.Tarea;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TareaRepository {
    private final Connection connection;

    public TareaRepository() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }

    public List<Tarea> findAll(String buscar, String estado) {
        List<Tarea> tareas = new ArrayList<>();
        String sql = "SELECT * FROM tareas WHERE activo = true";
        String finalSql = buildQuery(sql, buscar, estado);

        try (PreparedStatement stmt = connection.prepareStatement(finalSql)) {
            int paramIndex = 1;
            if (buscar != null && !buscar.isEmpty()) {
                stmt.setString(paramIndex++, "%" + buscar + "%");
                stmt.setString(paramIndex++, "%" + buscar + "%");
            }
            if (estado != null && !estado.isEmpty() && !estado.equals("TODAS")) {
                stmt.setString(paramIndex, estado);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tareas.add(mapResultSetToTarea(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar tareas", e);
        }
        return tareas;
    }

    public List<Tarea> findByTrabajadorId(String trabajadorId, String estado) {
        List<Tarea> tareas = new ArrayList<>();

        String sql = "SELECT * FROM tareas WHERE activo = true AND trabajador_id = ?";
        if (estado != null && !estado.isEmpty() && !estado.equals("TODAS")) {
            sql += " AND estado = ?";
        }
        sql += " ORDER BY fecha_creacion DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, trabajadorId);
            if (estado != null && !estado.isEmpty() && !estado.equals("TODAS")) {
                stmt.setString(2, estado);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tareas.add(mapResultSetToTarea(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar tareas por trabajador", e);
        }
        return tareas;
    }

    public Optional<Tarea> findById(Long id) {
        String sql = "SELECT * FROM tareas WHERE id = ? AND activo = true";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToTarea(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar tarea ID: " + id, e);
        }
        return Optional.empty();
    }

    public Tarea save(Tarea tarea) {
        String sql = "INSERT INTO tareas (asunto, detalles, fecha_asignacion, fecha_entrega, " +
                "cantidad_figuras, estado, creado_por, trabajador_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tarea.getAsunto());
            stmt.setString(2, tarea.getDetalles());
            stmt.setDate(3, Date.valueOf(tarea.getFechaAsignacion()));
            stmt.setDate(4, Date.valueOf(tarea.getFechaEntrega()));
            stmt.setObject(5, tarea.getCantidadFiguras(), Types.INTEGER);
            stmt.setString(6, tarea.getEstado() != null ? tarea.getEstado() : "PENDIENTE");
            stmt.setString(7, tarea.getCreadoPor());
            stmt.setString(8, tarea.getTrabajadorId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear tarea, ninguna fila afectada");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tarea.setId(generatedKeys.getLong(1));
                }
            }
            return tarea;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear tarea: " + e.getMessage(), e);
        }
    }

    public boolean updateEstado(Long id, String nuevoEstado) {
        String sql = "UPDATE tareas SET estado = ? WHERE id = ? AND activo = true";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setLong(2, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estado tarea ID: " + id, e);
        }
    }

    public boolean delete(Long id) {
        String sql = "UPDATE tareas SET activo = false WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar tarea ID: " + id, e);
        }
    }

    public boolean update(Tarea tarea) {
        String sql = "UPDATE tareas SET asunto = ?, detalles = ?, fecha_asignacion = ?, " +
                "fecha_entrega = ?, cantidad_figuras = ?, trabajador_id = ? WHERE id = ? AND activo = true";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tarea.getAsunto());
            stmt.setString(2, tarea.getDetalles());
            stmt.setDate(3, Date.valueOf(tarea.getFechaAsignacion()));
            stmt.setDate(4, Date.valueOf(tarea.getFechaEntrega()));
            stmt.setObject(5, tarea.getCantidadFiguras(), Types.INTEGER);
            stmt.setString(6, tarea.getTrabajadorId());
            stmt.setLong(7, tarea.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar tarea ID: " + tarea.getId(), e);
        }
    }

    private Tarea mapResultSetToTarea(ResultSet rs) throws SQLException {
        Tarea tarea = new Tarea();
        tarea.setId(rs.getLong("id"));
        tarea.setAsunto(rs.getString("asunto"));
        tarea.setDetalles(rs.getString("detalles"));

        Date fechaAsignacion = rs.getDate("fecha_asignacion");
        if (fechaAsignacion != null) {
            tarea.setFechaAsignacion(fechaAsignacion.toLocalDate());
        }

        Date fechaEntrega = rs.getDate("fecha_entrega");
        if (fechaEntrega != null) {
            tarea.setFechaEntrega(fechaEntrega.toLocalDate());
        }

        tarea.setCantidadFiguras(rs.getInt("cantidad_figuras"));
        if (rs.wasNull()) tarea.setCantidadFiguras(null);

        tarea.setEstado(rs.getString("estado"));
        tarea.setActivo(rs.getBoolean("activo"));
        tarea.setCreadoPor(rs.getString("creado_por"));
        tarea.setTrabajadorId(rs.getString("trabajador_id"));
        tarea.setFechaCreacion(rs.getString("fecha_creacion"));

        return tarea;
    }

    private String buildQuery(String baseSql, String buscar, String estado) {
        StringBuilder query = new StringBuilder(baseSql);

        if (buscar != null && !buscar.isEmpty()) {
            query.append(" AND (asunto LIKE ? OR detalles LIKE ?)");
        }
        if (estado != null && !estado.isEmpty() && !estado.equals("TODAS")) {
            query.append(" AND estado = ?");
        }
        query.append(" ORDER BY fecha_creacion DESC");

        return query.toString();
    }
}