package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.Reparacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar las operaciones de base de datos relacionadas con reparaciones.
 * <p>
 * Esta clase implementa el patrón Repository para la entidad Reparacion,
 * proporcionando métodos CRUD completos con soporte para filtros avanzados,
 * gestión de estados y operaciones específicas del ciclo de vida de reparaciones.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see Reparacion
 * @see DatabaseConnection
 */
public class ReparacionRepository {

    /**
     * Constructor por defecto.
     * Crea una nueva instancia del repositorio de reparaciones.
     */
    public ReparacionRepository() {}

    /**
     * Obtiene todas las reparaciones activas con filtros opcionales.
     * <p>
     * Retorna una lista de reparaciones que pueden ser filtradas por estado,
     * nombre de cliente o modelo del artículo. Las reparaciones se ordenan
     * por fecha de registro descendente (más recientes primero).
     * </p>
     *
     * @param estado Estado de la reparación a filtrar (opcional)
     * @param cliente Nombre o parte del nombre del cliente (opcional)
     * @param modelo Modelo o parte del modelo del artículo (opcional)
     * @return Lista de reparaciones que cumplen con los criterios
     *
     * @throws RuntimeException Si ocurre un error en la consulta SQL
     *
     * @example
     * <pre>
     * // Obtener reparaciones pendientes
     * List<Reparacion> pendientes = repository.findAll("Pendiente", null, null);
     *
     * // Buscar reparaciones de un cliente específico
     * List<Reparacion> clienteRep = repository.findAll(null, "Juan", null);
     *
     * // Filtrar por modelo y estado
     * List<Reparacion> sillasPend = repository.findAll("Pendiente", null, "Silla");
     * </pre>
     */
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

    /**
     * Busca una reparación específica por su ID.
     * <p>
     * Retorna un Optional que contiene la reparación si se encuentra
     * y está activa, o un Optional vacío si no existe o está inactiva.
     * </p>
     *
     * @param id ID numérico de la reparación a buscar
     * @return Optional con la reparación si se encuentra, vacío si no
     *
     * @throws RuntimeException Si ocurre un error en la consulta SQL
     */
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

    /**
     * Guarda una nueva reparación en la base de datos.
     * <p>
     * Inserta un nuevo registro de reparación con todos los atributos
     * proporcionados. Asigna automáticamente el ID generado por la base de datos
     * y establece valores por defecto para campos opcionales.
     * </p>
     *
     * @param reparacion Objeto Reparacion con los datos a guardar
     * @return La misma reparación con el ID asignado
     *
     * @throws RuntimeException Si ocurre un error en la inserción SQL
     * @throws SQLException Si no se afecta ninguna fila en la inserción
     *
     * @example
     * <pre>
     * Reparacion nuevaRep = new Reparacion("Juan Pérez", "555-1234", "Silla ejecutiva",
     *                                      "Madera", "Patas flojas", 350, 100, "2024-12-15",
     *                                      "Necesita refuerzo en patas");
     * Reparacion guardada = repository.save(nuevaRep);
     * // guardada.getId() ahora contiene el ID asignado por la BD
     * </pre>
     */
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

    /**
     * Actualiza completamente una reparación existente.
     * <p>
     * Modifica todos los campos editables de una reparación excepto
     * los campos de auditoría (creado_por, fecha_registro) y el ID.
     * </p>
     *
     * @param reparacion Objeto Reparacion con los datos actualizados
     * @return true si la reparación se actualizó exitosamente, false en caso contrario
     *
     * @throws RuntimeException Si ocurre un error en la actualización SQL
     */
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

    /**
     * Actualiza el estado de una reparación específica.
     * <p>
     * Cambia únicamente el campo de estado de una reparación,
     * utilizado para avanzar en el flujo de trabajo (Pendiente → En Proceso → Completada → Entregada).
     * </p>
     *
     * @param id ID de la reparación a actualizar
     * @param nuevoEstado Nuevo estado a asignar a la reparación
     * @return true si el estado se actualizó exitosamente, false en caso contrario
     *
     * @throws RuntimeException Si ocurre un error en la actualización SQL
     */
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

    /**
     * Realiza una eliminación lógica de una reparación.
     * <p>
     * Marca la reparación como inactiva en lugar de eliminarla físicamente,
     * preservando el historial y referencias en el sistema.
     * </p>
     *
     * @param id ID de la reparación a eliminar
     * @return true si la reparación se eliminó exitosamente, false en caso contrario
     *
     * @throws RuntimeException Si ocurre un error en la actualización SQL
     */
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

    /**
     * Convierte un ResultSet de base de datos a un objeto Reparacion.
     * <p>
     * Método auxiliar que mapea los campos de la tabla reparacion
     * a las propiedades del modelo correspondiente.
     * </p>
     *
     * @param rs ResultSet con los datos obtenidos de la base de datos
     * @return Objeto Reparacion poblado con los datos del ResultSet
     * @throws SQLException Si ocurre un error al leer los datos del ResultSet
     */
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