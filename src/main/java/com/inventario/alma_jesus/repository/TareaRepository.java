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
            System.out.println("[TAREA-REPO] Conectado a la base de datos");
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }

    // Endpoint 50: GET /api/v1/tareas - Listar tareas (para admin)
    public List<Tarea> findAll(String buscar, String estado) {
        System.out.println("[TAREA-REPO] Listando tareas - Buscar: '" + buscar + "', Estado: '" + estado + "'");
        List<Tarea> tareas = new ArrayList<>();
        String sql = "SELECT * FROM tareas WHERE activo = true";
        String finalSql = buildQuery(sql, buscar, estado);

        System.out.println("[TAREA-REPO] SQL: " + finalSql);

        try (PreparedStatement stmt = connection.prepareStatement(finalSql)) {
            int paramIndex = 1;
            if (buscar != null && !buscar.isEmpty()) {
                stmt.setString(paramIndex++, "%" + buscar + "%");
                stmt.setString(paramIndex++, "%" + buscar + "%");
                System.out.println("[TAREA-REPO] Parámetro buscar: %" + buscar + "%");
            }
            if (estado != null && !estado.isEmpty() && !estado.equals("TODAS")) {
                stmt.setString(paramIndex, estado);
                System.out.println("[TAREA-REPO] Parámetro estado: " + estado);
            }

            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                tareas.add(mapResultSetToTarea(rs));
                count++;
            }
            System.out.println("[TAREA-REPO] Encontradas " + count + " tareas");
        } catch (SQLException e) {
            System.out.println("[TAREA-REPO] Error al listar tareas: " + e.getMessage());
            throw new RuntimeException("Error al listar tareas", e);
        }
        return tareas;
    }

    // NUEVO: Listar tareas por trabajador
    public List<Tarea> findByTrabajadorId(Long trabajadorId, String estado) {
        System.out.println("[TAREA-REPO] Buscando tareas para trabajador: " + trabajadorId + ", estado: " + estado);
        List<Tarea> tareas = new ArrayList<>();

        String sql = "SELECT * FROM tareas WHERE activo = true AND trabajador_id = ?";
        if (estado != null && !estado.isEmpty() && !estado.equals("TODAS")) {
            sql += " AND estado = ?";
        }
        sql += " ORDER BY fecha_creacion DESC";

        System.out.println("[TAREA-REPO] SQL: " + sql);
        System.out.println("[TAREA-REPO] Parámetros - trabajadorId: " + trabajadorId + ", estado: " + estado);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, trabajadorId);
            if (estado != null && !estado.isEmpty() && !estado.equals("TODAS")) {
                stmt.setString(2, estado);
                System.out.println("[TAREA-REPO] Ejecutando con estado: " + estado);
            }

            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                Tarea tarea = mapResultSetToTarea(rs);
                tareas.add(tarea);
                count++;
                System.out.println("[TAREA-REPO] Tarea encontrada: " + tarea.getAsunto() + " - Estado: " + tarea.getEstado());
            }
            System.out.println("[TAREA-REPO] Encontradas " + count + " tareas para trabajador " + trabajadorId);
        } catch (SQLException e) {
            System.out.println("[TAREA-REPO] Error al buscar tareas por trabajador: " + e.getMessage());
            throw new RuntimeException("Error al buscar tareas por trabajador", e);
        }
        return tareas;
    }

    // Endpoint 51: GET /api/v1/tareas/{id} - Obtener tarea
    public Optional<Tarea> findById(Long id) {
        System.out.println("[TAREA-REPO] Buscando tarea por ID: " + id);
        String sql = "SELECT * FROM tareas WHERE id = ? AND activo = true";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            System.out.println("[TAREA-REPO] SQL: " + sql + " | ID: " + id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Tarea tarea = mapResultSetToTarea(rs);
                System.out.println("[TAREA-REPO] Tarea encontrada: " + tarea.getAsunto());
                return Optional.of(tarea);
            } else {
                System.out.println("[TAREA-REPO] Tarea no encontrada ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("[TAREA-REPO] Error al buscar tarea: " + e.getMessage());
            throw new RuntimeException("Error al buscar tarea ID: " + id, e);
        }
        return Optional.empty();
    }

    // Endpoint 52: POST /api/v1/tareas - Crear tarea (ACTUALIZADO con trabajador_id)
    public Tarea save(Tarea tarea) {
        System.out.println("[TAREA-REPO] Creando nueva tarea: " + tarea.getAsunto());
        String sql = "INSERT INTO tareas (asunto, detalles, fecha_asignacion, fecha_entrega, " +
                "cantidad_figuras, estado, creado_por, trabajador_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        System.out.println("[TAREA-REPO] SQL: " + sql);
        System.out.println("[TAREA-REPO] Datos: " + tarea.getAsunto() + ", " +
                tarea.getFechaAsignacion() + " a " + tarea.getFechaEntrega() +
                ", Trabajador ID: " + tarea.getTrabajadorId());

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tarea.getAsunto());
            stmt.setString(2, tarea.getDetalles());
            stmt.setDate(3, Date.valueOf(tarea.getFechaAsignacion()));
            stmt.setDate(4, Date.valueOf(tarea.getFechaEntrega()));
            stmt.setObject(5, tarea.getCantidadFiguras(), Types.INTEGER);
            stmt.setString(6, tarea.getEstado() != null ? tarea.getEstado() : "PENDIENTE");
            stmt.setString(7, tarea.getCreadoPor());
            stmt.setLong(8, tarea.getTrabajadorId()); // NUEVO PARÁMETRO

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("❌ [TAREA-REPO] Error: ninguna fila afectada");
                throw new SQLException("Error al crear tarea, ninguna fila afectada");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tarea.setId(generatedKeys.getLong(1));
                    System.out.println("[TAREA-REPO] Tarea creada con ID: " + tarea.getId());
                }
            }
            return tarea;
        } catch (SQLException e) {
            System.out.println("[TAREA-REPO] Error al crear tarea: " + e.getMessage());
            throw new RuntimeException("Error al crear tarea: " + e.getMessage(), e);
        }
    }

    // Endpoint 53: PUT /api/v1/tareas/{id}/estado - Actualizar estado
    public boolean updateEstado(Long id, String nuevoEstado) {
        System.out.println("[TAREA-REPO] Actualizando estado tarea ID: " + id + " -> " + nuevoEstado);
        String sql = "UPDATE tareas SET estado = ? WHERE id = ? AND activo = true";

        System.out.println("[TAREA-REPO] SQL: " + sql);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setLong(2, id);

            int affectedRows = stmt.executeUpdate();
            System.out.println("[TAREA-REPO] Estado actualizado - Filas afectadas: " + affectedRows);
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("[TAREA-REPO] Error al actualizar estado: " + e.getMessage());
            throw new RuntimeException("Error al actualizar estado tarea ID: " + id, e);
        }
    }

    // Endpoint 54: DELETE /api/v1/tareas/{id} - Eliminar tarea
    public boolean delete(Long id) {
        System.out.println("[TAREA-REPO] Eliminando tarea ID: " + id + " (lógico)");
        String sql = "UPDATE tareas SET activo = false WHERE id = ?";

        System.out.println("[TAREA-REPO] SQL: " + sql);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

            int affectedRows = stmt.executeUpdate();
            System.out.println("[TAREA-REPO] Tarea eliminada - Filas afectadas: " + affectedRows);
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("[TAREA-REPO] Error al eliminar tarea: " + e.getMessage());
            throw new RuntimeException("Error al eliminar tarea ID: " + id, e);
        }
    }

    // Actualizar tarea completa (ACTUALIZADO con trabajador_id)
    public boolean update(Tarea tarea) {
        System.out.println("[TAREA-REPO] Actualizando tarea ID: " + tarea.getId());
        String sql = "UPDATE tareas SET asunto = ?, detalles = ?, fecha_asignacion = ?, " +
                "fecha_entrega = ?, cantidad_figuras = ?, trabajador_id = ? WHERE id = ? AND activo = true";

        System.out.println("[TAREA-REPO] SQL: " + sql);
        System.out.println("[TAREA-REPO] Nuevos datos: " + tarea.getAsunto() + ", " +
                tarea.getFechaAsignacion() + " a " + tarea.getFechaEntrega() +
                ", Trabajador ID: " + tarea.getTrabajadorId());

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tarea.getAsunto());
            stmt.setString(2, tarea.getDetalles());
            stmt.setDate(3, Date.valueOf(tarea.getFechaAsignacion()));
            stmt.setDate(4, Date.valueOf(tarea.getFechaEntrega()));
            stmt.setObject(5, tarea.getCantidadFiguras(), Types.INTEGER);
            stmt.setLong(6, tarea.getTrabajadorId()); // NUEVO PARÁMETRO
            stmt.setLong(7, tarea.getId());

            int affectedRows = stmt.executeUpdate();
            System.out.println("[TAREA-REPO] Filas afectadas: " + affectedRows);
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("[TAREA-REPO] Error al actualizar tarea: " + e.getMessage());
            throw new RuntimeException("Error al actualizar tarea ID: " + tarea.getId(), e);
        }
    }

    // MÉTODO ACTUALIZADO: mapResultSetToTarea (con trabajador_id)
    private Tarea mapResultSetToTarea(ResultSet rs) throws SQLException {
        Tarea tarea = new Tarea();
        tarea.setId(rs.getLong("id"));
        tarea.setAsunto(rs.getString("asunto"));
        tarea.setDetalles(rs.getString("detalles"));

        // Manejo seguro de fechas
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

        // NUEVO: trabajador_id
        tarea.setTrabajadorId(rs.getLong("trabajador_id"));
        if (rs.wasNull()) {
            System.out.println("[TAREA-REPO] ADVERTENCIA: trabajador_id es NULL para tarea ID: " + tarea.getId());
        }

        tarea.setFechaCreacion(rs.getString("fecha_creacion"));

        System.out.println("[TAREA-REPO] Tarea mapeada - ID: " + tarea.getId() +
                ", Asunto: " + tarea.getAsunto() +
                ", Trabajador ID: " + tarea.getTrabajadorId());

        return tarea;
    }

    // Métodos auxiliares
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

    // Método adicional: Verificar si existe trabajador_id en la tabla
    public boolean existeColumnaTrabajadorId() {
        System.out.println("[TAREA-REPO] Verificando existencia de columna trabajador_id");
        String sql = "SELECT COUNT(*) as count FROM information_schema.columns " +
                "WHERE table_name = 'tareas' AND column_name = 'trabajador_id'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean existe = rs.getInt("count") > 0;
                System.out.println("[TAREA-REPO] Columna trabajador_id existe: " + existe);
                return existe;
            }
        } catch (SQLException e) {
            System.out.println("[TAREA-REPO] Error al verificar columna: " + e.getMessage());
        }
        return false;
    }

    // Método adicional: Obtener tareas sin trabajador asignado (para migración)
    public List<Tarea> findTareasSinTrabajador() {
        System.out.println("[TAREA-REPO] Buscando tareas sin trabajador asignado");
        List<Tarea> tareas = new ArrayList<>();
        String sql = "SELECT * FROM tareas WHERE (trabajador_id IS NULL OR trabajador_id = 0) AND activo = true";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                tareas.add(mapResultSetToTarea(rs));
                count++;
            }
            System.out.println("[TAREA-REPO] Encontradas " + count + " tareas sin trabajador asignado");
        } catch (SQLException e) {
            System.out.println("[TAREA-REPO] Error al buscar tareas sin trabajador: " + e.getMessage());
        }
        return tareas;
    }

    // Método adicional: Asignar trabajador a tareas sin asignar
    public boolean asignarTrabajadorATareas(Long tareaId, Long trabajadorId) {
        System.out.println("[TAREA-REPO] Asignando trabajador " + trabajadorId + " a tarea " + tareaId);
        String sql = "UPDATE tareas SET trabajador_id = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, trabajadorId);
            stmt.setLong(2, tareaId);

            int affectedRows = stmt.executeUpdate();
            System.out.println("[TAREA-REPO] Tarea actualizada - Filas afectadas: " + affectedRows);
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("[TAREA-REPO] Error al asignar trabajador: " + e.getMessage());
            return false;
        }
    }
}