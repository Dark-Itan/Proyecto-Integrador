package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.Tarea;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de tareas de producción.
 * <p>
 * Esta clase maneja todas las operaciones CRUD para tareas asignadas a trabajadores.
 * Incluye funcionalidades para búsqueda filtrada por estado, trabajador y texto,
 * así como gestión del ciclo de vida de las tareas en el sistema de producción.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class TareaRepository {
    private final Connection connection;

    /**
     * Constructor del repositorio.
     * <p>
     * Establece la conexión a la base de datos utilizando {@link DatabaseConnection}.
     * Lanza una excepción si no puede establecer la conexión.
     * </p>
     *
     * @throws RuntimeException Si ocurre un error al conectar con la base de datos
     */
    public TareaRepository() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }

    /**
     * Obtiene todas las tareas con opciones de filtrado.
     * <p>
     * Retorna tareas activas filtradas por texto de búsqueda y/o estado,
     * ordenadas por fecha de creación descendente. El filtro de búsqueda
     * busca tanto en el asunto como en los detalles de la tarea.
     * </p>
     *
     * @param buscar Texto opcional para buscar en asunto o detalles
     * @param estado Estado opcional para filtrar tareas ("TODAS" para incluir todos los estados)
     * @return Lista de {@link Tarea} que cumplen con los criterios de búsqueda
     * @throws RuntimeException Si ocurre un error en la consulta SQL
     */
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

    /**
     * Busca tareas asignadas a un trabajador específico.
     * <p>
     * Retorna todas las tareas activas asignadas a un trabajador,
     * con opción de filtrar por estado. Ordenadas por fecha de creación
     * descendente (las más recientes primero).
     * </p>
     *
     * @param trabajadorId ID del trabajador para el cual buscar tareas
     * @param estado Estado opcional para filtrar tareas ("TODAS" para incluir todos los estados)
     * @return Lista de {@link Tarea} asignadas al trabajador especificado
     * @throws RuntimeException Si ocurre un error en la consulta SQL
     */
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

    /**
     * Busca una tarea por su ID.
     * <p>
     * Retorna una tarea específica si existe y está activa.
     * </p>
     *
     * @param id ID de la tarea a buscar
     * @return {@link Optional} con la {@link Tarea} encontrada, o vacío si no existe
     * @throws RuntimeException Si ocurre un error en la consulta SQL
     */
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

    /**
     * Crea una nueva tarea en el sistema.
     * <p>
     * Inserta una tarea en la base de datos y establece el ID generado
     * en el objeto tarea. Si no se especifica estado, se establece como
     * "PENDIENTE" por defecto.
     * </p>
     *
     * @param tarea Objeto {@link Tarea} a crear
     * @return El mismo objeto con el ID generado establecido
     * @throws RuntimeException Si ocurre un error en la inserción SQL
     * @throws IllegalArgumentException Si algún campo requerido es nulo
     */
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

    /**
     * Actualiza el estado de una tarea existente.
     * <p>
     * Cambia el estado de una tarea activa. Estados válidos incluyen:
     * PENDIENTE, EN_PROGRESO, COMPLETADA, VERIFICADA, CANCELADA.
     * </p>
     *
     * @param id ID de la tarea a actualizar
     * @param nuevoEstado Nuevo estado a asignar
     * @return true si la actualización fue exitosa, false si no se encontró la tarea
     * @throws RuntimeException Si ocurre un error en la actualización SQL
     */
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

    /**
     * Elimina una tarea del sistema (eliminación lógica).
     * <p>
     * Realiza una eliminación lógica estableciendo el campo 'activo' a false.
     * La tarea permanece en la base de datos para fines de historial y reportes,
     * pero no aparece en búsquedas ni operaciones normales.
     * </p>
     *
     * @param id ID de la tarea a eliminar
     * @return true si la eliminación fue exitosa, false si no se encontró la tarea
     * @throws RuntimeException Si ocurre un error en la actualización SQL
     */
    public boolean delete(Long id) {
        String sql = "UPDATE tareas SET activo = false WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar tarea ID: " + id, e);
        }
    }

    /**
     * Actualiza los datos de una tarea existente.
     * <p>
     * Modifica los campos editables de una tarea activa, excluyendo
     * el estado que debe actualizarse mediante {@link #updateEstado}.
     * </p>
     *
     * @param tarea Objeto {@link Tarea} con los datos actualizados
     * @return true si la actualización fue exitosa, false si no se encontró la tarea
     * @throws RuntimeException Si ocurre un error en la actualización SQL
     * @see #updateEstado
     */
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

    /**
     * Mapea un ResultSet a un objeto Tarea.
     * <p>
     * Método privado auxiliar para convertir filas de base de datos
     * a objetos del dominio. Maneja conversiones nulas apropiadamente.
     * </p>
     *
     * @param rs ResultSet con los datos de la tarea
     * @return Objeto {@link Tarea} mapeado
     * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet
     */
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

    /**
     * Construye una consulta SQL dinámica basada en parámetros de filtrado.
     * <p>
     * Método privado auxiliar para construir consultas con filtros opcionales.
     * </p>
     *
     * @param baseSql Consulta SQL base
     * @param buscar Parámetro de búsqueda opcional
     * @param estado Parámetro de estado opcional
     * @return Consulta SQL completa con filtros aplicados
     */
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