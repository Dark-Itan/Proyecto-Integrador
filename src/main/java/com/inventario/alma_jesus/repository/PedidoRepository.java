package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.Pedido;
import com.inventario.alma_jesus.model.PedidoProducto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepository {

    public List<Pedido> findAll() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos ORDER BY fecha_creacion DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pedido pedido = mapPedido(rs);
                pedido.setProductos(findProductosByPedidoId(pedido.getId()));
                pedidos.add(pedido);
            }
        }
        return pedidos;
    }

    // NUEVO MÉTODO: Buscar pedidos por fecha
    public List<Pedido> findByFecha(String fecha) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE DATE(fecha_creacion) = ? ORDER BY fecha_creacion DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fecha);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = mapPedido(rs);
                    pedido.setProductos(findProductosByPedidoId(pedido.getId()));
                    pedidos.add(pedido);
                }
            }
        }
        return pedidos;
    }

    public Pedido findById(Long id) throws SQLException {
        String sql = "SELECT * FROM pedidos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Pedido pedido = mapPedido(rs);
                    pedido.setProductos(findProductosByPedidoId(pedido.getId()));
                    return pedido;
                }
            }
        }
        return null;
    }

    public Long create(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (cliente_nombre, cliente_contacto, fecha_entrega, notas, etapa, total, anticipo, total_cantidad, resumen_producto, creado_por) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, pedido.getClienteNombre());
            stmt.setString(2, pedido.getClienteContacto());
            stmt.setString(3, pedido.getFechaEntrega());
            stmt.setString(4, pedido.getNotas());
            stmt.setString(5, pedido.getEtapa());
            stmt.setBigDecimal(6, pedido.getTotal());
            stmt.setBigDecimal(7, pedido.getAnticipo());
            stmt.setInt(8, pedido.getTotalCantidad());
            stmt.setString(9, pedido.getResumenProducto());
            stmt.setString(10, pedido.getCreadoPor());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("No se pudo crear el pedido");
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

    public boolean updateEtapa(Long id, String nuevaEtapa, String notas) throws SQLException {
        String sql = "UPDATE pedidos SET etapa = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevaEtapa);
            stmt.setLong(2, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                // Registrar en historial de etapas
                registrarCambioEtapa(conn, id, nuevaEtapa, notas);
                return true;
            }
            return false;
        }
    }

    public boolean delete(Long id) throws SQLException {
        // Primero eliminar los productos del pedido
        String sqlDeleteProductos = "DELETE FROM pedido_productos WHERE pedido_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlDeleteProductos)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }

        // Luego eliminar el pedido
        String sql = "DELETE FROM pedidos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public void createProductos(Long pedidoId, List<PedidoProducto> productos) throws SQLException {
        if (productos == null || productos.isEmpty()) {
            return;
        }

        String sql = "INSERT INTO pedido_productos (pedido_id, producto_id, producto_nombre, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (PedidoProducto producto : productos) {
                stmt.setLong(1, pedidoId);
                stmt.setLong(2, producto.getProductoId());
                stmt.setString(3, producto.getProductoNombre());
                stmt.setInt(4, producto.getCantidad());
                stmt.setBigDecimal(5, producto.getPrecioUnitario());
                stmt.setBigDecimal(6, producto.getSubtotal());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private List<PedidoProducto> findProductosByPedidoId(Long pedidoId) throws SQLException {
        List<PedidoProducto> productos = new ArrayList<>();
        String sql = "SELECT * FROM pedido_productos WHERE pedido_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, pedidoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapPedidoProducto(rs));
                }
            }
        }
        return productos;
    }

    private void registrarCambioEtapa(Connection conn, Long pedidoId, String etapa, String notas) throws SQLException {
        String sql = "INSERT INTO pedido_etapas (pedido_id, etapa, notas, usuario) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, pedidoId);
            stmt.setString(2, etapa);
            stmt.setString(3, notas);
            stmt.setString(4, "sistema");
            stmt.executeUpdate();
        }
    }

    private Pedido mapPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId(rs.getLong("id"));
        pedido.setClienteNombre(rs.getString("cliente_nombre"));
        pedido.setClienteContacto(rs.getString("cliente_contacto"));
        pedido.setFechaEntrega(rs.getString("fecha_entrega"));
        pedido.setNotas(rs.getString("notas"));
        pedido.setEtapa(rs.getString("etapa"));
        pedido.setTotal(rs.getBigDecimal("total"));
        pedido.setAnticipo(rs.getBigDecimal("anticipo"));
        pedido.setTotalCantidad(rs.getInt("total_cantidad"));
        pedido.setResumenProducto(rs.getString("resumen_producto"));
        pedido.setCreadoPor(rs.getString("creado_por"));

        Timestamp timestamp = rs.getTimestamp("fecha_creacion");
        if (timestamp != null) {
            pedido.setFechaCreacion(timestamp.toLocalDateTime());
        }

        timestamp = rs.getTimestamp("fecha_actualizacion");
        if (timestamp != null) {
            pedido.setFechaActualizacion(timestamp.toLocalDateTime());
        }

        return pedido;
    }

    private PedidoProducto mapPedidoProducto(ResultSet rs) throws SQLException {
        PedidoProducto producto = new PedidoProducto();
        producto.setId(rs.getLong("id"));
        producto.setPedidoId(rs.getLong("pedido_id"));
        producto.setProductoId(rs.getLong("producto_id"));
        producto.setProductoNombre(rs.getString("producto_nombre"));
        producto.setCantidad(rs.getInt("cantidad"));
        producto.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        producto.setSubtotal(rs.getBigDecimal("subtotal"));
        return producto;
    }
}