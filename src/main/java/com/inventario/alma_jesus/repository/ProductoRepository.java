package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoRepository {

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

    public boolean publicarPrecios() {
        return true;
    }

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