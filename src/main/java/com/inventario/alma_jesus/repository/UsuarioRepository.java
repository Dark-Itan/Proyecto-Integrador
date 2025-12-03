package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de operaciones CRUD de la entidad Usuario en la base de datos.
 * Proporciona métodos para autenticación, consulta y administración de usuarios del sistema.
 *
 * @author Alma & Jesús
 * @version 1.0
 * @since 2024
 */
public class UsuarioRepository {

    /**
     * Busca un usuario activo por su nombre de usuario (username).
     *
     * @param username Nombre de usuario a buscar.
     * @return Optional conteniendo el Usuario si se encuentra y está activo,
     *         Optional.empty() en caso contrario.
     */
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

    /**
     * Recupera todos los usuarios activos del sistema.
     *
     * @return Lista de objetos Usuario que representan a todos los usuarios activos.
     */
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

    /**
     * Busca un usuario activo por su identificador único.
     *
     * @param id Identificador único del usuario a buscar.
     * @return Optional conteniendo el Usuario si se encuentra y está activo,
     *         Optional.empty() en caso contrario.
     */
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

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param usuario Objeto Usuario con los datos del nuevo usuario.
     * @return true si el usuario fue creado exitosamente, false en caso contrario.
     */
    public boolean crearUsuario(Usuario usuario) {
        // CORRECCIÓN: La consulta SQL tenía un error - faltaban los placeholders
        String sql = "INSERT INTO usuario (id, username, password, rol, email, activo) VALUES (?, ?, ?, ?, ?, ?)";

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
            // Manejo específico para errores de duplicación (código 1062 para MySQL)
            if (e.getErrorCode() == 1062) {
                System.out.println("Repository: Usuario ya existe - " + usuario.getUsername());
            } else {
                System.out.println("Error en UsuarioRepository.crearUsuario: " + e.getMessage());
            }
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cambia la contraseña de un usuario activo.
     *
     * @param id Identificador único del usuario.
     * @param nuevaPassword Nueva contraseña a establecer.
     * @return true si la contraseña fue cambiada exitosamente, false en caso contrario.
     */
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

    /**
     * Realiza una eliminación lógica de un usuario, estableciendo su estado como inactivo.
     *
     * @param id Identificador único del usuario a eliminar.
     * @return true si el usuario fue marcado como inactivo exitosamente, false en caso contrario.
     */
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