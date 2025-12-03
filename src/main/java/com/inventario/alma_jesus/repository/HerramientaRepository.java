package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.Herramienta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar las operaciones de base de datos relacionadas con herramientas.
 * <p>
 * Esta clase proporciona métodos CRUD para la entidad Herramienta, incluyendo
 * operaciones de búsqueda, creación, actualización y eliminación lógica.
 * Implementa el patrón Repository para separar la lógica de acceso a datos.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see Herramienta
 * @see DatabaseConnection
 */
public class HerramientaRepository {
    /**
     * Conexión a la base de datos utilizada por este repositorio.
     */
    private final Connection connection;

    /**
     * Constructor que inicializa la conexión a la base de datos.
     *
     * @throws RuntimeException Si no se puede establecer la conexión a la BD
     */
    public HerramientaRepository() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }

    /**
     * Obtiene todas las herramientas activas con filtros opcionales.
     * <p>
     * Retorna una lista de herramientas que pueden ser filtradas por
     * texto de búsqueda (nombre o descripción) y por estado (estatus).
     * Solo incluye herramientas marcadas como activas.
     * </p>
     *
     * @param buscar Texto para buscar en nombre o descripción (opcional)
     * @param estatus Estado de la herramienta a filtrar (opcional)
     * @return Lista de herramientas que cumplen con los criterios
     *
     * @throws RuntimeException Si ocurre un error en la consulta SQL
     */
    public List<Herramienta> findAll(String buscar, String estatus) {
        System.out.println("🔧 [HERRAMIENTA] Listando herramientas - Buscar: " + buscar + ", Estatus: " + estatus);
        List<Herramienta> herramientas = new ArrayList<>();
        String sql = "SELECT * FROM herramienta WHERE activo = true";

        try (PreparedStatement stmt = connection.prepareStatement(buildQuery(sql, buscar, estatus))) {
            int paramIndex = 1;
            if (buscar != null && !buscar.isEmpty()) {
                stmt.setString(paramIndex++, "%" + buscar + "%");
                stmt.setString(paramIndex++, "%" + buscar + "%");
            }
            if (estatus != null && !estatus.isEmpty()) {
                stmt.setString(paramIndex, estatus);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                herramientas.add(mapResultSetToHerramienta(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar herramientas", e);
        }
        return herramientas;
    }

    /**
     * Busca una herramienta por su ID numérico o nombre exacto.
     * <p>
     * Este método intenta primero interpretar el parámetro como un número (ID),
     * y si falla, lo trata como un nombre. Solo retorna herramientas activas.
     * </p>
     *
     * @param idOrNombre ID numérico o nombre exacto de la herramienta
     * @return Optional que contiene la herramienta si se encuentra, vacío si no
     *
     * @throws RuntimeException Si ocurre un error en la consulta SQL
     */
    public Optional<Herramienta> findByIdOrNombre(String idOrNombre) {
        System.out.println("🔧 [HERRAMIENTA] Buscando herramienta: " + idOrNombre);
        String sql = "SELECT * FROM herramienta WHERE (id = ? OR nombre = ?) AND activo = true";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try {
                Long id = Long.parseLong(idOrNombre);
                stmt.setLong(1, id);
                stmt.setString(2, idOrNombre);
            } catch (NumberFormatException e) {
                stmt.setLong(1, -1);
                stmt.setString(2, idOrNombre);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToHerramienta(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar herramienta: " + idOrNombre, e);
        }
        return Optional.empty();
    }

    /**
     * Guarda una nueva herramienta en la base de datos.
     * <p>
     * Inserta una herramienta con estado inicial "Disponible" y establece
     * tanto la cantidad total como disponible al valor proporcionado.
     * Asigna automáticamente el ID generado por la base de datos.
     * </p>
     *
     * @param herramienta Objeto Herramienta con los datos a guardar
     * @return La misma herramienta con el ID asignado
     *
     * @throws RuntimeException Si ocurre un error en la inserción SQL
     * @throws SQLException Si no se afecta ninguna fila
     */
    public Herramienta save(Herramienta herramienta) {
        System.out.println("🔧 [HERRAMIENTA] Creando herramienta: " + herramienta.getNombre());
        String sql = "INSERT INTO herramienta (nombre, descripcion, cantidad_total, cantidad_disponible, " +
                "estatus, creado_por) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, herramienta.getNombre());
            stmt.setString(2, herramienta.getDescripcion());
            stmt.setInt(3, herramienta.getCantidadTotal());
            stmt.setInt(4, herramienta.getCantidadTotal());
            stmt.setString(5, "Disponible");
            stmt.setString(6, herramienta.getCreadoPor());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear herramienta, ninguna fila afectada");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    herramienta.setId(generatedKeys.getLong(1));
                }
            }
            return herramienta;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear herramienta: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza el stock total y disponible de una herramienta.
     * <p>
     * Establece tanto la cantidad total como disponible al nuevo valor
     * y actualiza la fecha de modificación.
     * </p>
     *
     * @param id ID de la herramienta a actualizar
     * @param nuevaCantidad Nuevo valor para el stock
     * @return true si se actualizó correctamente, false si no se encontró la herramienta
     *
     * @throws RuntimeException Si ocurre un error en la actualización SQL
     */
    public boolean updateStock(Long id, Integer nuevaCantidad) {
        String sql = "UPDATE herramienta SET cantidad_total = ?, cantidad_disponible = ?, " +
                "fecha_actualizacion = CURRENT_TIMESTAMP WHERE id = ? AND activo = true";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, nuevaCantidad);
            stmt.setInt(2, nuevaCantidad);
            stmt.setLong(3, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar stock de herramienta ID: " + id, e);
        }
    }

    /**
     * Asigna una herramienta a un usuario.
     * <p>
     * Reduce el stock disponible en 1, establece el estado a "En Uso",
     * registra el usuario asignado y quién realizó la asignación.
     * Solo funciona si hay stock disponible.
     * </p>
     *
     * @param id ID de la herramienta a asignar
     * @param usuarioAsignado ID del usuario que recibe la herramienta
     * @param asignadoPor ID del usuario que realiza la asignación
     * @return true si se asignó correctamente, false si no hay stock disponible
     *
     * @throws RuntimeException Si ocurre un error en la actualización SQL
     */
    public boolean asignarHerramienta(Long id, String usuarioAsignado, String asignadoPor) {
        String sql = "UPDATE herramienta SET usuario_asignado = ?, asignado_por = ?, " +
                "estatus = 'En Uso', fecha_asignacion = CURRENT_TIMESTAMP, " +
                "fecha_actualizacion = CURRENT_TIMESTAMP, cantidad_disponible = cantidad_disponible - 1 " +
                "WHERE id = ? AND cantidad_disponible > 0 AND activo = true";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuarioAsignado);
            stmt.setString(2, asignadoPor);
            stmt.setLong(3, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al asignar herramienta ID: " + id, e);
        }
    }

    /**
     * Devuelve una herramienta previamente asignada.
     * <p>
     * Incrementa el stock disponible en 1, establece el estado a "Disponible",
     * y limpia los campos de asignación.
     * </p>
     *
     * @param id ID de la herramienta a devolver
     * @return true si se devolvió correctamente, false si no se encontró la herramienta
     *
     * @throws RuntimeException Si ocurre un error en la actualización SQL
     */
    public boolean devolverHerramienta(Long id) {
        String sql = "UPDATE herramienta SET usuario_asignado = NULL, asignado_por = NULL, " +
                "estatus = 'Disponible', fecha_asignacion = NULL, " +
                "fecha_actualizacion = CURRENT_TIMESTAMP, cantidad_disponible = cantidad_disponible + 1 " +
                "WHERE id = ? AND activo = true";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al devolver herramienta ID: " + id, e);
        }
    }

    /**
     * Realiza una eliminación lógica de una herramienta.
     * <p>
     * Marca la herramienta como inactiva en lugar de eliminarla físicamente,
     * preservando el historial y referencias.
     * </p>
     *
     * @param id ID de la herramienta a eliminar
     * @return true si se eliminó correctamente, false si no se encontró la herramienta
     *
     * @throws RuntimeException Si ocurre un error en la actualización SQL
     */
    public boolean delete(Long id) {
        String sql = "UPDATE herramienta SET activo = false, fecha_actualizacion = CURRENT_TIMESTAMP WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar herramienta ID: " + id, e);
        }
    }

    /**
     * Construye una consulta SQL dinámica basada en los filtros proporcionados.
     *
     * @param baseSql Consulta SQL base
     * @param buscar Filtro de texto (opcional)
     * @param estatus Filtro de estado (opcional)
     * @return Consulta SQL completa con filtros aplicados
     */
    private String buildQuery(String baseSql, String buscar, String estatus) {
        StringBuilder query = new StringBuilder(baseSql);

        if (buscar != null && !buscar.isEmpty()) {
            query.append(" AND (nombre LIKE ? OR descripcion LIKE ?)");
        }
        if (estatus != null && !estatus.isEmpty()) {
            query.append(" AND estatus = ?");
        }
        query.append(" ORDER BY nombre");

        return query.toString();
    }

    /**
     * Mapea un ResultSet a un objeto Herramienta.
     *
     * @param rs ResultSet con los datos de la herramienta
     * @return Objeto Herramienta poblado
     * @throws SQLException Si ocurre un error al leer los datos del ResultSet
     */
    private Herramienta mapResultSetToHerramienta(ResultSet rs) throws SQLException {
        Herramienta herramienta = new Herramienta(
                rs.getLong("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getInt("cantidad_total"),
                rs.getInt("cantidad_disponible"),
                rs.getString("estatus"),
                rs.getString("usuario_asignado"),
                rs.getString("asignado_por"),
                rs.getBoolean("activo"),
                rs.getString("creado_por")
        );

        return herramienta;
    }
}