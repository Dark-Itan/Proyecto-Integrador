package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.Recetario;
import com.inventario.alma_jesus.model.RecetaMaterial;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para la gestión de operaciones CRUD de la entidad Recetario en la base de datos.
 * Proporciona métodos para crear, leer, actualizar y eliminar recetas del sistema de inventario.
 *
 * @author Alma & Jesús
 * @version 1.0
 * @since 2024
 */
public class RecetarioRepository {

    /**
     * Recupera todas las recetas del sistema, ordenadas por fecha de creación descendente.
     *
     * @return Lista de objetos Recetario con todas las recetas existentes.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
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

    /**
     * Recupera todas las recetas activas del sistema para visualización de trabajadores.
     * <p>
     * Este método está diseñado específicamente para trabajadores que necesitan ver
     * únicamente las recetas disponibles para producción. Actualmente devuelve todas
     * las recetas existentes, pero puede ser modificado para filtrar por estado
     * cuando se implemente la gestión de estados de recetas.
     * </p>
     *
     * @return Lista de objetos Recetario con todas las recetas existentes.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public List<Recetario> findActivas() throws SQLException {
        List<Recetario> recetas = new ArrayList<>();

        // Si tu tabla tiene un campo 'estado' o 'activo', modifica la consulta así:
        // String sql = "SELECT * FROM recetario WHERE estado = 'ACTIVO' ORDER BY fecha_creacion DESC";

        // Por ahora, devuelve todas las recetas (para compatibilidad)
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

    /**
     * Busca una receta específica por su identificador único.
     *
     * @param id Identificador único de la receta a buscar.
     * @return Objeto Recetario si se encuentra, null en caso contrario.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
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

    /**
     * Crea una nueva receta en la base de datos.
     *
     * @param receta Objeto Recetario con los datos de la nueva receta.
     * @return El identificador único generado para la nueva receta.
     * @throws SQLException Si ocurre un error al insertar los datos o al obtener el ID generado.
     */
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

    /**
     * Actualiza los datos de una receta existente en la base de datos.
     *
     * @param receta Objeto Recetario con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error al actualizar los datos.
     */
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

    /**
     * Elimina una receta del sistema, incluyendo todos sus materiales asociados.
     * Realiza una eliminación en cascada: primero elimina los materiales de la receta
     * y luego la receta en sí.
     *
     * @param id Identificador único de la receta a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error durante la eliminación.
     */
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

    /**
     * Convierte un ResultSet de base de datos en un objeto Recetario.
     * Mapea cada columna del resultado a su correspondiente atributo en el objeto.
     *
     * @param rs ResultSet con los datos obtenidos de la base de datos.
     * @return Objeto Recetario poblado con los datos del ResultSet.
     * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet.
     */
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