package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar las operaciones de base de datos relacionadas con productos.
 * <p>
 * Esta clase implementa el patrón Repository para la entidad Producto,
 * proporcionando métodos CRUD completos para la gestión del catálogo de productos
 * disponibles para venta en el sistema.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see Producto
 * @see DatabaseConnection
 */
public class ProductoRepository {

    /**
     * Obtiene todos los productos activos del catálogo.
     * <p>
     * Retorna una lista completa de productos marcados como activos,
     * ordenados según la consulta SQL por defecto.
     * </p>
     *
     * @return Lista de todos los productos activos
     *
     * @throws SQLException Si ocurre un error en la conexión o consulta SQL
     *
     * @example
     * <pre>
     * List<Producto> todosProductos = repository.findAll();
     * // Retorna todos los productos disponibles para venta
     * </pre>
     */
    public List<Producto> findAll() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE activo = true";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Producto producto = mapearProducto(rs);
                productos.add(producto);
            }

        } catch (SQLException e) {
            System.out.println("Error en ProductoRepository.findAll: " + e.getMessage());
            e.printStackTrace();
        }
        return productos;
    }

    /**
     * Busca un producto específico por su ID.
     * <p>
     * Retorna un Optional que contiene el producto si se encuentra
     * y está activo, o un Optional vacío si no existe o está inactivo.
     * </p>
     *
     * @param id ID numérico del producto a buscar
     * @return Optional con el producto si se encuentra, vacío si no
     *
     * @throws SQLException Si ocurre un error en la conexión o consulta SQL
     */
    public Optional<Producto> findById(int id) {
        String sql = "SELECT * FROM producto WHERE id = ? AND activo = true";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Producto producto = mapearProducto(rs);
                return Optional.of(producto);
            }

        } catch (SQLException e) {
            System.out.println("Error en ProductoRepository.findById: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Obtiene productos filtrados por tipo específico.
     * <p>
     * Retorna una lista de productos activos que pertenecen a una categoría
     * o tipo específico, como "mueble", "accesorio", "decoracion", etc.
     * </p>
     *
     * @param tipo Tipo o categoría de producto a filtrar
     * @return Lista de productos del tipo especificado
     *
     * @throws SQLException Si ocurre un error en la conexión o consulta SQL
     *
     * @example
     * <pre>
     * List<Producto> muebles = repository.findByTipo("mueble");
     * // Retorna todos los muebles activos en el catálogo
     * </pre>
     */
    public List<Producto> findByTipo(String tipo) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE tipo = ? AND activo = true";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Producto producto = mapearProducto(rs);
                productos.add(producto);
            }

        } catch (SQLException e) {
            System.out.println("Error en ProductoRepository.findByTipo: " + e.getMessage());
            e.printStackTrace();
        }
        return productos;
    }

    /**
     * Crea un nuevo producto en el catálogo.
     * <p>
     * Inserta un nuevo registro de producto con todos los atributos proporcionados.
     * Incluye validación y logging detallado para depuración.
     * </p>
     *
     * @param producto Objeto Producto con los datos a guardar
     * @return true si el producto se creó exitosamente, false en caso contrario
     *
     * @throws SQLException Si ocurre un error en la conexión o inserción SQL
     *
     * @example
     * <pre>
     * Producto nuevoProducto = new Producto();
     * nuevoProducto.setModelo("Mesa de centro");
     * nuevoProducto.setPrecio(2500);
     * nuevoProducto.setStock(5);
     * // ... otros atributos
     *
     * boolean creado = repository.crearProducto(nuevoProducto);
     * // true si se insertó correctamente en la base de datos
     * </pre>
     */
    public boolean crearProducto(Producto producto) {
        String sql = "INSERT INTO producto (modelo, color, precio, stock, tamaño, imagen_url, activo, creado_por, tipo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // DEBUG: Ver qué datos se están enviando
            System.out.println("🔄 DEBUG - Intentando insertar producto:");
            System.out.println("  📝 Modelo: " + producto.getModelo());
            System.out.println("  🎨 Color: " + producto.getColor());
            System.out.println("  💰 Precio: " + producto.getPrecio());
            System.out.println("  📦 Stock: " + producto.getStock());
            System.out.println("  📏 Tamaño: " + producto.getTamaño());
            System.out.println("  🖼️ ImagenURL: " + producto.getImagenUrl());
            System.out.println("  👤 CreadoPor: " + producto.getCreadoPor());
            System.out.println("  🏷️ Tipo: " + producto.getTipo());

            stmt.setString(1, producto.getModelo());
            stmt.setString(2, producto.getColor());
            stmt.setInt(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());
            stmt.setString(5, producto.getTamaño());
            stmt.setString(6, producto.getImagenUrl());
            stmt.setBoolean(7, true);
            stmt.setString(8, producto.getCreadoPor());
            stmt.setString(9, producto.getTipo());

            int filasAfectadas = stmt.executeUpdate();

            // DEBUG: Ver si se insertó
            System.out.println("✅ DEBUG - Filas afectadas en BD: " + filasAfectadas);

            if (filasAfectadas > 0) {
                System.out.println("🎉 ¡Producto insertado EXITOSAMENTE en la BD!");
            } else {
                System.out.println("❌ ¡CERO filas insertadas en la BD!");
            }

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("💥 ERROR SQL en crearProducto: " + e.getMessage());
            System.out.println("🔍 SQL State: " + e.getSQLState());
            System.out.println("⚡ Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza completamente un producto existente.
     * <p>
     * Modifica todos los campos de un producto excepto los de auditoría
     * (creado_por, fecha_creacion) y el estado activo.
     * </p>
     *
     * @param producto Objeto Producto con los datos actualizados
     * @return true si el producto se actualizó exitosamente, false en caso contrario
     *
     * @throws SQLException Si ocurre un error en la conexión o actualización SQL
     */
    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE producto SET modelo = ?, color = ?, precio = ?, stock = ?, tamaño = ?, imagen_url = ?, tipo = ? WHERE id = ? AND activo = true";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getModelo());
            stmt.setString(2, producto.getColor());
            stmt.setInt(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());
            stmt.setString(5, producto.getTamaño());
            stmt.setString(6, producto.getImagenUrl());
            stmt.setString(7, producto.getTipo());
            stmt.setInt(8, producto.getId());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error en ProductoRepository.actualizarProducto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Realiza una eliminación lógica de un producto.
     * <p>
     * Marca el producto como inactivo en lugar de eliminarlo físicamente,
     * preservando el historial y referencias en el sistema.
     * </p>
     *
     * @param id ID del producto a eliminar
     * @return true si el producto se eliminó exitosamente, false en caso contrario
     *
     * @throws SQLException Si ocurre un error en la conexión o actualización SQL
     */
    public boolean eliminarProducto(int id) {
        String sql = "UPDATE producto SET activo = false WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error en ProductoRepository.eliminarProducto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Publica los precios de todos los productos activos.
     * <p>
     * En una implementación completa, este método actualizaría el estado
     * de los productos de "borrador" a "publicado" y podría generar
     * reportes o notificaciones.
     * </p>
     *
     * @return true indicando que la operación fue exitosa
     *
     * @todo Implementar lógica completa de publicación de precios
     */
    public boolean publicarPrecios() {
        return true;
    }

    /**
     * Convierte un ResultSet de base de datos a un objeto Producto.
     * <p>
     * Método auxiliar que mapea los campos de la tabla producto
     * a las propiedades del modelo correspondiente.
     * </p>
     *
     * @param rs ResultSet con los datos obtenidos de la base de datos
     * @return Objeto Producto poblado con los datos del ResultSet
     * @throws SQLException Si ocurre un error al leer los datos del ResultSet
     */
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getInt("id"));
        producto.setModelo(rs.getString("modelo"));
        producto.setColor(rs.getString("color"));
        producto.setPrecio(rs.getInt("precio"));
        producto.setStock(rs.getInt("stock"));
        producto.setTamaño(rs.getString("tamaño"));
        producto.setImagenUrl(rs.getString("imagen_url"));
        producto.setActivo(rs.getBoolean("activo"));
        producto.setCreadoPor(rs.getString("creado_por"));
        producto.setFechaCreacion(rs.getString("fecha_creacion"));
        producto.setTipo(rs.getString("tipo"));
        return producto;
    }
}