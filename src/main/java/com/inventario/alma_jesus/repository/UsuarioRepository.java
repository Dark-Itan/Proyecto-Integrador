package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository {

    public Optional<Usuario> findByUsername(String username) {
        String sql = "SELECT id, username, password, rol, email, activo, fecha_creacion FROM usuario WHERE username = ? AND activo = true";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getString("id"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                usuario.setRol(rs.getString("rol"));
                usuario.setEmail(rs.getString("email"));
                usuario.setActivo(rs.getBoolean("activo"));
                usuario.setFechaCreacion(rs.getString("fecha_creacion"));

                return Optional.of(usuario);
            }
        } catch (SQLException e) {
            System.out.println("Error en UsuarioRepository.findByUsername: " + e.getMessage());
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, username, password, rol, email, activo, fecha_creacion FROM usuario WHERE activo = true";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getString("id"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                usuario.setRol(rs.getString("rol"));
                usuario.setEmail(rs.getString("email"));
                usuario.setActivo(rs.getBoolean("activo"));
                usuario.setFechaCreacion(rs.getString("fecha_creacion"));

                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println("Error en UsuarioRepository.findAll: " + e.getMessage());
            e.printStackTrace();
        }

        return usuarios;
    }

    public Optional<Usuario> findById(String id) {
        String sql = "SELECT id, username, password, rol, email, activo, fecha_creacion FROM usuario WHERE id = ? AND activo = true";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getString("id"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                usuario.setRol(rs.getString("rol"));
                usuario.setEmail(rs.getString("email"));
                usuario.setActivo(rs.getBoolean("activo"));
                usuario.setFechaCreacion(rs.getString("fecha_creacion"));

                return Optional.of(usuario);
            }
        } catch (SQLException e) {
            System.out.println("Error en UsuarioRepository.findById: " + e.getMessage());
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public boolean crearUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario (id, username, password, rol, email, activo) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getId());
            stmt.setString(2, usuario.getUsername());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getRol());
            stmt.setString(5, usuario.getEmail());
            stmt.setBoolean(6, usuario.isActivo());

            int filasAfectadas = stmt.executeUpdate();
            boolean exito = filasAfectadas > 0;

            System.out.println("Repository: Usuario creado - " + usuario.getUsername() + " - Exito: " + exito);
            return exito;

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) { // Código de error MySQL para DUPLICATE ENTRY
                System.out.println("Repository: Usuario ya existe - " + usuario.getUsername());
            } else {
                System.out.println("Error en UsuarioRepository.crearUsuario: " + e.getMessage());
            }
            e.printStackTrace();
            return false;
        }
    }

    public boolean cambiarPassword(String id, String nuevaPassword) {
        String sql = "UPDATE usuario SET password = ? WHERE id = ? AND activo = true";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevaPassword);
            stmt.setString(2, id);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error en UsuarioRepository.cambiarPassword: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarUsuario(String id) {
        String sql = "UPDATE usuario SET activo = false WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error en UsuarioRepository.eliminarUsuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}