package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.MateriaPrima;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar las operaciones de base de datos relacionadas con materias primas.
 * <p>
 * Esta clase implementa el patrón Repository para la entidad MateriaPrima,
 * proporcionando métodos CRUD completos con soporte para filtros, búsqueda
 * y operaciones específicas de inventario.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see MateriaPrima
 * @see DatabaseConnection
 */
public class MateriaPrimaRepository {
    /**
     * Conexión a la base de datos utilizada por este repositorio.
     */
    private final Connection connection;

    /**
     * Constructor que inicializa la conexión a la base de datos.
     * Utiliza el pool de conexiones configurado en DatabaseConnection.
     *
     * @throws RuntimeException Si no se puede establecer la conexión a la base de datos
     */
    public MateriaPrimaRepository() {
        try {
            this.connection = DatabaseConnection.getConnection();
            System.out.println("[MATERIAL-REPO] Conectado a la base de datos");
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }

    /**
     * Obtiene todas las materias primas activas con filtros opcionales.
     * <p>
     * Retorna una lista de materiales que pueden ser filtrados por
     * texto de búsqueda (nombre o descripción) y por categoría.
     * Solo incluye materiales marcados como activos.
     * </p>
     *
     * @param buscar Texto para buscar en nombre o descripción (opcional)
     * @param categoria Categoría específica para filtrar (opcional)
     * @return Lista de materias primas que cumplen con los criterios
     *
     * @throws RuntimeException Si ocurre un error en la consulta SQL
     *
     * @example
     * <pre>
     * // Obtener todos los materiales
     * List<MateriaPrima> todos = repository.findAll(null, null);
     *
     * // Buscar materiales de madera
     * List<MateriaPrima> maderas = repository.findAll(null, "Madera");
     *
     * // Buscar tornillos en todas las categorías
     * List<MateriaPrima> tornillos = repository.findAll("tornillo", null);
     * </pre>
     */
    public List<MateriaPrima> findAll(String buscar, String categoria) {
        System.out.println("[MATERIAL-REPO] Listando materiales - Buscar: '" + buscar + "', Categoría: '" + categoria + "'");
        List<MateriaPrima> materiales = new ArrayList<>();
        String sql = "SELECT * FROM materiaprima WHERE activo = true";
        String finalSql = buildQuery(sql, buscar, categoria);

        System.out.println("[MATERIAL-REPO] SQL: " + finalSql);

        try (PreparedStatement stmt = connection.prepareStatement(finalSql)) {
            int paramIndex = 1;
            if (buscar != null && !buscar.isEmpty()) {
                stmt.setString(paramIndex++, "%" + buscar + "%");
                stmt.setString(paramIndex++, "%" + buscar + "%");
                System.out.println("[MATERIAL-REPO] Parámetro buscar: %" + buscar + "%");
            }
            if (categoria != null && !categoria.isEmpty() && !categoria.equals("Todas las materias primas")) {
                stmt.setString(paramIndex, categoria);
                System.out.println("[MATERIAL-REPO] Parámetro categoría: " + categoria);
            }

            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                materiales.add(mapResultSetToMateriaPrima(rs));
                count++;
            }
            System.out.println("[MATERIAL-REPO] Encontrados " + count + " materiales");
        } catch (SQLException e) {
            System.out.println("[MATERIAL-REPO] Error al listar materiales: " + e.getMessage());
            throw new RuntimeException("Error al listar materiales", e);
        }
        return materiales;
    }

    /**
     * Busca una materia prima específica por su ID.
     * <p>
     * Retorna un Optional que contiene la materia prima si se encuentra
     * y está activa, o un Optional vacío si no existe o está inactiva.
     * </p>
     *
     * @param id ID numérico de la materia prima a buscar
     * @return Optional con la materia prima si se encuentra, vacío si no
     *
     * @throws RuntimeException Si ocurre un error en la consulta SQL
     */
    public Optional<MateriaPrima> findById(Long id) {
        System.out.println("[MATERIAL-REPO] Buscando material por ID: " + id);
        String sql = "SELECT * FROM materiaprima WHERE id = ? AND activo = true";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            System.out.println("[MATERIAL-REPO] SQL: " + sql + " | ID: " + id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("[MATERIAL-REPO] Material encontrado: " + rs.getString("nombre"));
                return Optional.of(mapResultSetToMateriaPrima(rs));
            } else {
                System.out.println("[MATERIAL-REPO] Material no encontrado ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("[MATERIAL-REPO] Error al buscar material: " + e.getMessage());
            throw new RuntimeException("Error al buscar material ID: " + id, e);
        }
        return Optional.empty();
    }

    /**
     * Guarda una nueva materia prima en la base de datos.
     * <p>
     * Inserta un nuevo registro de materia prima con todos los atributos
     * proporcionados. Asigna automáticamente el ID generado por la base de datos.
     * </p>
     *
     * @param material Objeto MateriaPrima con los datos a guardar
     * @return La misma materia prima con el ID asignado
     *
     * @throws RuntimeException Si ocurre un error en la inserción SQL
     * @throws SQLException Si no se afecta ninguna fila en la inserción
     */
    public MateriaPrima save(MateriaPrima material) {
        System.out.println("[MATERIAL-REPO] Creando nuevo material: " + material.getNombre());
        String sql = "INSERT INTO materiaprima (nombre, descripcion, cantidad, unidad, " +
                "stock_minimo, costo, categoria, creado_por) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        System.out.println("[MATERIAL-REPO] SQL: " + sql);
        System.out.println("[MATERIAL-REPO] Datos: " + material.getNombre() + ", " + material.getCantidad() + " " + material.getUnidad() + ", $" + material.getCosto());

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, material.getNombre());
            stmt.setString(2, material.getDescripcion());
            stmt.setInt(3, material.getCantidad());
            stmt.setString(4, material.getUnidad());
            stmt.setInt(5, material.getStockMinimo());
            stmt.setDouble(6, material.getCosto());
            stmt.setString(7, material.getCategoria());
            stmt.setString(8, material.getCreadoPor());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("❌ [MATERIAL-REPO] Error: ninguna fila afectada");
                throw new SQLException("Error al crear material, ninguna fila afectada");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    material.setId(generatedKeys.getLong(1));
                    System.out.println("[MATERIAL-REPO] Material creado con ID: " + material.getId());
                }
            }
            return material;
        } catch (SQLException e) {
            System.out.println("[MATERIAL-REPO] Error al crear material: " + e.getMessage());
            throw new RuntimeException("Error al crear material: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza completamente una materia prima existente.
     * <p>
     * Modifica todos los campos de una materia prima excepto la cantidad
     * (que se actualiza con updateStock) y los campos de auditoría.
     * </p>
     *
     * @param material Objeto MateriaPrima con los datos actualizados
     * @return true si se actualizó correctamente, false si no se encontró el material
     *
     * @throws RuntimeException Si ocurre un error en la actualización SQL
     */
    public boolean update(MateriaPrima material) {
        System.out.println("[MATERIAL-REPO] Actualizando material ID: " + material.getId());
        String sql = "UPDATE materiaprima SET nombre = ?, descripcion = ?, unidad = ?, " +
                "stock_minimo = ?, costo = ?, categoria = ? WHERE id = ? AND activo = true";

        System.out.println("[MATERIAL-REPO] SQL: " + sql);
        System.out.println("[MATERIAL-REPO] Nuevos datos: " + material.getNombre() + ", " + material.getCategoria() + ", $" + material.getCosto());

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, material.getNombre());
            stmt.setString(2, material.getDescripcion());
            stmt.setString(3, material.getUnidad());
            stmt.setInt(4, material.getStockMinimo());
            stmt.setDouble(5, material.getCosto());
            stmt.setString(6, material.getCategoria());
            stmt.setLong(7, material.getId());

            int affectedRows = stmt.executeUpdate();
            System.out.println("[MATERIAL-REPO] Filas afectadas: " + affectedRows);
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("[MATERIAL-REPO] Error al actualizar material: " + e.getMessage());
            throw new RuntimeException("Error al actualizar material ID: " + material.getId(), e);
        }
    }

    /**
     * Actualiza la cantidad en stock de una materia prima.
     * <p>
     * Modifica únicamente el campo de cantidad (stock actual) de un material.
     * Utilizado para ajustes de inventario, entradas y salidas.
     * </p>
     *
     * @param id ID de la materia prima a actualizar
     * @param nuevaCantidad Nuevo valor para la cantidad en stock
     * @return true si se actualizó correctamente, false si no se encontró el material
     *
     * @throws RuntimeException Si ocurre un error en la actualización SQL
     */
    public boolean updateStock(Long id, Integer nuevaCantidad) {
        System.out.println("[MATERIAL-REPO] Actualizando stock material ID: " + id + " -> " + nuevaCantidad);
        String sql = "UPDATE materiaprima SET cantidad = ? WHERE id = ? AND activo = true";

        System.out.println("[MATERIAL-REPO] SQL: " + sql);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, nuevaCantidad);
            stmt.setLong(2, id);

            int affectedRows = stmt.executeUpdate();
            System.out.println("[MATERIAL-REPO] Stock actualizado - Filas afectadas: " + affectedRows);
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("[MATERIAL-REPO] Error al actualizar stock: " + e.getMessage());
            throw new RuntimeException("Error al actualizar stock material ID: " + id, e);
        }
    }

    /**
     * Realiza una eliminación lógica de una materia prima.
     * <p>
     * Marca la materia prima como inactiva en lugar de eliminarla físicamente,
     * preservando el historial y referencias en el sistema.
     * </p>
     *
     * @param id ID de la materia prima a eliminar
     * @return true si se eliminó correctamente, false si no se encontró el material
     *
     * @throws RuntimeException Si ocurre un error en la actualización SQL
     */
    public boolean delete(Long id) {
        System.out.println("[MATERIAL-REPO] Eliminando material ID: " + id + " (lógico)");
        String sql = "UPDATE materiaprima SET activo = false WHERE id = ?";

        System.out.println("[MATERIAL-REPO] SQL: " + sql);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

            int affectedRows = stmt.executeUpdate();
            System.out.println("[MATERIAL-REPO] Material eliminado - Filas afectadas: " + affectedRows);
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("[MATERIAL-REPO] Error al eliminar material: " + e.getMessage());
            throw new RuntimeException("Error al eliminar material ID: " + id, e);
        }
    }

    /**
     * Construye una consulta SQL dinámica basada en los filtros proporcionados.
     *
     * @param baseSql Consulta SQL base
     * @param buscar Filtro de texto para buscar en nombre o descripción
     * @param categoria Filtro de categoría específica
     * @return Consulta SQL completa con filtros aplicados
     */
    private String buildQuery(String baseSql, String buscar, String categoria) {
        StringBuilder query = new StringBuilder(baseSql);

        if (buscar != null && !buscar.isEmpty()) {
            query.append(" AND (nombre LIKE ? OR descripcion LIKE ?)");
        }
        if (categoria != null && !categoria.isEmpty() && !categoria.equals("Todas las materias primas")) {
            query.append(" AND categoria = ?");
        }
        query.append(" ORDER BY nombre");

        return query.toString();
    }

    /**
     * Convierte un ResultSet de base de datos a un objeto MateriaPrima.
     *
     * @param rs ResultSet con los datos obtenidos de la base de datos
     * @return Objeto MateriaPrima poblado con los datos del ResultSet
     * @throws SQLException Si ocurre un error al leer los datos del ResultSet
     */
    private MateriaPrima mapResultSetToMateriaPrima(ResultSet rs) throws SQLException {
        MateriaPrima material = new MateriaPrima();
        material.setId(rs.getLong("id"));
        material.setNombre(rs.getString("nombre"));
        material.setDescripcion(rs.getString("descripcion"));
        material.setCantidad(rs.getInt("cantidad"));
        material.setUnidad(rs.getString("unidad"));
        material.setStockMinimo(rs.getInt("stock_minimo"));
        material.setCosto(rs.getDouble("costo"));
        material.setCategoria(rs.getString("categoria"));
        material.setActivo(rs.getBoolean("activo"));
        material.setCreadoPor(rs.getString("creado_por"));
        return material;
    }
}