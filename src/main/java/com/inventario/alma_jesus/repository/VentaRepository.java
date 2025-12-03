package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.Venta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar las operaciones de base de datos relacionadas con ventas.
 * <p>
 * Esta clase implementa el patrón Repository para la entidad Venta,
 * proporcionando métodos CRUD completos con soporte para transacciones,
 * eliminación de ventas y consultas avanzadas con joins a productos.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see Venta
 * @see DatabaseConnection
 */
public class VentaRepository {

    /**
     * Obtiene todas las ventas registradas en el sistema.
     * <p>
     * Retorna una lista completa de ventas, incluyendo información del producto
     * asociado mediante un JOIN. Las ventas se ordenan por fecha de registro
     * descendente (más recientes primero).
     * </p>
     *
     * @return Lista de todas las ventas registradas
     *
     * @throws SQLException Si ocurre un error en la conexión o consulta SQL
     *
     * @example
     * <pre>
     * List<Venta> todasVentas = repository.findAll();
     * // Retorna el historial completo de ventas con información de productos
     * </pre>
     */
    public List<Venta> findAll() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT v.id, v.cliente_id, v.producto_id, p.modelo as producto_modelo, " +
                "v.cantidad, v.precio_unitario, v.precio_total, v.fecha, v.tipo, " +
                "v.usuario_registro, v.fecha_registro " +
                "FROM venta v " +
                "INNER JOIN producto p ON v.producto_id = p.id " +
                "ORDER BY v.fecha_registro DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Venta venta = mapearVenta(rs);
                ventas.add(venta);
            }

        } catch (SQLException e) {
            System.out.println("Error en VentaRepository.findAll: " + e.getMessage());
        }

        return ventas;
    }

    /**
     * Busca una venta específica por su ID.
     * <p>
     * Retorna un Optional que contiene la venta si se encuentra,
     * incluyendo información del producto asociado mediante JOIN.
     * </p>
     *
     * @param id ID numérico de la venta a buscar
     * @return Optional con la venta si se encuentra, vacío si no
     *
     * @throws SQLException Si ocurre un error en la conexión o consulta SQL
     */
    public Optional<Venta> findById(int id) {
        String sql = "SELECT v.id, v.cliente_id, v.producto_id, p.modelo as producto_modelo, " +
                "v.cantidad, v.precio_unitario, v.precio_total, v.fecha, v.tipo, " +
                "v.usuario_registro, v.fecha_registro " +
                "FROM venta v " +
                "INNER JOIN producto p ON v.producto_id = p.id " +
                "WHERE v.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Venta venta = mapearVenta(rs);
                return Optional.of(venta);
            }

        } catch (SQLException e) {
            System.out.println("Error en VentaRepository.findById: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Registra una nueva venta en el sistema.
     * <p>
     * Inserta un nuevo registro de venta utilizando transacciones para
     * garantizar la integridad de los datos. No verifica duplicados ya que
     * en un entorno de tienda física es normal tener múltiples ventas
     * del mismo producto en un día.
     * </p>
     *
     * @param venta Objeto Venta con los datos de la transacción
     * @return true si la venta se registró exitosamente, false en caso contrario
     *
     * @throws SQLException Si ocurre un error en la conexión o inserción SQL
     *
     * @example
     * <pre>
     * Venta nuevaVenta = new Venta();
     * nuevaVenta.setClienteId(123);
     * nuevaVenta.setProductoId(456);
     * nuevaVenta.setCantidad(2);
     * nuevaVenta.setPrecioUnitario(1500);
     * nuevaVenta.setPrecioTotal(3000);
     * // ... otros atributos
     *
     * boolean registrada = repository.crearVenta(nuevaVenta);
     * // true si la venta se insertó correctamente en la base de datos
     * </pre>
     */
    public boolean crearVenta(Venta venta) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // Insertar la venta directamente
            String insertSql = "INSERT INTO venta (cliente_id, producto_id, cantidad, precio_unitario, " +
                    "precio_total, fecha, tipo, usuario_registro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, venta.getClienteId());
                insertStmt.setInt(2, venta.getProductoId());
                insertStmt.setInt(3, venta.getCantidad());
                insertStmt.setInt(4, venta.getPrecioUnitario());
                insertStmt.setInt(5, venta.getPrecioTotal());
                insertStmt.setString(6, venta.getFecha());
                insertStmt.setString(7, venta.getTipo());
                insertStmt.setString(8, venta.getUsuarioRegistro());

                int filasAfectadas = insertStmt.executeUpdate();

                if (filasAfectadas > 0) {
                    conn.commit(); // Confirmar transacción
                    System.out.println("Venta registrada exitosamente: Producto ID " + venta.getProductoId() + " - Fecha: " + venta.getFecha());
                    return true;
                } else {
                    conn.rollback(); // Revertir si no se insertó
                    return false;
                }
            }

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error en rollback: " + rollbackEx.getMessage());
            }
            System.out.println("❌ Error en VentaRepository.crearVenta: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error cerrando conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Elimina permanentemente una venta del sistema.
     * <p>
     * Realiza una eliminación física de la venta (no lógica).
     * Esta operación no se puede deshacer y debe usarse con precaución.
     * </p>
     *
     * @param id ID de la venta a eliminar
     * @return true si la venta se eliminó exitosamente, false en caso contrario
     *
     * @throws SQLException Si ocurre un error en la conexión o eliminación SQL
     */
    public boolean eliminarVenta(int id) {
        String sql = "DELETE FROM venta WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error en VentaRepository.eliminarVenta: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca ventas similares a la proporcionada (método para depuración).
     * <p>
     * Encuentra ventas con los mismos atributos clave (cliente, producto,
     * cantidad, precio, fecha y tipo). Este método es opcional y se usa
     * principalmente para debugging o análisis de datos duplicados.
     * </p>
     *
     * @param venta Venta de referencia para buscar similares
     * @return Lista de ventas que coinciden con los criterios
     *
     * @throws SQLException Si ocurre un error en la conexión o consulta SQL
     *
     * @example
     * <pre>
     * Venta referencia = new Venta();
     * referencia.setClienteId(123);
     * referencia.setProductoId(456);
     * // ... otros atributos
     *
     * List<Venta> similares = repository.findDuplicadas(referencia);
     * // Retorna ventas existentes con los mismos datos clave
     * </pre>
     */
    public List<Venta> findDuplicadas(Venta venta) {
        List<Venta> duplicadas = new ArrayList<>();
        String sql = "SELECT v.id, v.cliente_id, v.producto_id, p.modelo as producto_modelo, " +
                "v.cantidad, v.precio_unitario, v.precio_total, v.fecha, v.tipo, " +
                "v.usuario_registro, v.fecha_registro " +
                "FROM venta v " +
                "INNER JOIN producto p ON v.producto_id = p.id " +
                "WHERE v.cliente_id = ? AND v.producto_id = ? AND v.cantidad = ? AND " +
                "v.precio_unitario = ? AND v.fecha = ? AND v.tipo = ? " +
                "ORDER BY v.fecha_registro DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, venta.getClienteId());
            stmt.setInt(2, venta.getProductoId());
            stmt.setInt(3, venta.getCantidad());
            stmt.setInt(4, venta.getPrecioUnitario());
            stmt.setString(5, venta.getFecha());
            stmt.setString(6, venta.getTipo());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                duplicadas.add(mapearVenta(rs));
            }

            System.out.println("ℹVentas similares encontradas (solo informativo): " + duplicadas.size());

        } catch (SQLException e) {
            System.out.println("Error buscando duplicados: " + e.getMessage());
        }

        return duplicadas;
    }

    /**
     * Convierte un ResultSet de base de datos a un objeto Venta.
     * <p>
     * Método auxiliar que mapea los campos de las tablas venta y producto
     * (mediante JOIN) a las propiedades del modelo correspondiente.
     * </p>
     *
     * @param rs ResultSet con los datos obtenidos de la base de datos
     * @return Objeto Venta poblado con los datos del ResultSet
     * @throws SQLException Si ocurre un error al leer los datos del ResultSet
     */
    private Venta mapearVenta(ResultSet rs) throws SQLException {
        Venta venta = new Venta();
        venta.setId(rs.getInt("id"));
        venta.setClienteId(rs.getInt("cliente_id"));
        venta.setProductoId(rs.getInt("producto_id"));
        venta.setProductoModelo(rs.getString("producto_modelo"));
        venta.setCantidad(rs.getInt("cantidad"));
        venta.setPrecioUnitario(rs.getInt("precio_unitario"));
        venta.setPrecioTotal(rs.getInt("precio_total"));
        venta.setFecha(rs.getString("fecha"));
        venta.setTipo(rs.getString("tipo"));
        venta.setUsuarioRegistro(rs.getString("usuario_registro"));
        venta.setFechaRegistro(rs.getString("fecha_registro"));

        return venta;
    }
}