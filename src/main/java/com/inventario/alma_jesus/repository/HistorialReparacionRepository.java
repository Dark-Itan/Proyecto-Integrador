package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.HistorialReparacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones de base de datos relacionadas con el historial de reparaciones.
 * <p>
 * Esta clase proporciona métodos para registrar y consultar el historial de cambios de estado
 * de las reparaciones, manteniendo un registro completo de todas las transiciones y modificaciones.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see HistorialReparacion
 * @see DatabaseConnection
 */
public class HistorialReparacionRepository {
    /**
     * Conexión a la base de datos utilizada por este repositorio.
     */
    private final Connection connection;

    /**
     * Constructor que inicializa la conexión a la base de datos.
     * Establece la conexión utilizando el pool de conexiones configurado.
     *
     * @throws RuntimeException Si no se puede establecer la conexión a la base de datos
     */
    public HistorialReparacionRepository() {
        try {
            this.connection = DatabaseConnection.getConnection();
            System.out.println("📋 [HISTORIAL-REPO] Conectado a la base de datos");
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }

    /**
     * Obtiene el historial completo de una reparación específica.
     * <p>
     * Recupera todos los registros de historial asociados a una reparación,
     * ordenados por fecha descendente para mostrar los cambios más recientes primero.
     * </p>
     *
     * @param reparacionId ID de la reparación cuyo historial se desea consultar
     * @return Lista de registros de historial ordenados por fecha descendente
     *
     * @throws RuntimeException Si ocurre un error en la consulta SQL
     *
     * @example
     * <pre>
     * List<HistorialReparacion> historial = repository.findByReparacionId(123L);
     * // Retorna todos los cambios de estado de la reparación 123
     * </pre>
     */
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

    /**
     * Registra un nuevo evento en el historial de una reparación.
     * <p>
     * Inserta un registro que documenta un cambio de estado, actualización
     * o evento importante en el ciclo de vida de una reparación.
     * Asigna automáticamente el ID generado por la base de datos.
     * </p>
     *
     * @param historial Objeto HistorialReparacion con los datos del evento
     * @return El mismo objeto historial con el ID asignado
     *
     * @throws RuntimeException Si ocurre un error en la inserción SQL
     * @throws SQLException Si no se afecta ninguna fila en la inserción
     *
     * @example
     * <pre>
     * HistorialReparacion nuevoHistorial = new HistorialReparacion();
     * nuevoHistorial.setReparacionId(123L);
     * nuevoHistorial.setEstado("EN_PROCESO");
     * nuevoHistorial.setNotas("Inicio de reparación");
     * nuevoHistorial.setUsuarioId("admin001");
     *
     * HistorialReparacion guardado = repository.save(nuevoHistorial);
     * // guardado.getId() ahora contiene el ID asignado por la BD
     * </pre>
     */
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

    /**
     * Convierte un ResultSet de base de datos a un objeto HistorialReparacion.
     * <p>
     * Método auxiliar que mapea los campos de la tabla HistorialReparacion
     * a las propiedades del modelo correspondiente.
     * </p>
     *
     * @param rs ResultSet con los datos obtenidos de la base de datos
     * @return Objeto HistorialReparacion poblado con los datos del ResultSet
     * @throws SQLException Si ocurre un error al leer los datos del ResultSet
     */
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