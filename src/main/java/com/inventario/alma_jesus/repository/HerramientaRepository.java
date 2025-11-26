package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.Herramienta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HerramientaRepository {
    private final Connection connection;

    public HerramientaRepository() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }

    // Listar todas las herramientas activas con filtros
    public List<Herramienta> findAll(String buscar, String estatus) {
        System.out.println("🔧 [HERRAMIENTA] Listando herramientas - Buscar: " + buscar + ", Estatus: " + estatus);
        List<Herramienta> herramientas = new ArrayList<>();
        //  CAMBIADO: "Herramienta" por "herramienta"
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

    // Buscar por ID o Nombre
    public Optional<Herramienta> findByIdOrNombre(String idOrNombre) {
        System.out.println("🔧 [HERRAMIENTA] Buscando herramienta: " + idOrNombre);
        // CAMBIADO: "Herramienta" por "herramienta"
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

    // Crear nueva herramienta
    public Herramienta save(Herramienta herramienta) {
        System.out.println("🔧 [HERRAMIENTA] Creando herramienta: " + herramienta.getNombre());
        // CAMBIADO: "Herramienta" por "herramienta"
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

    // Actualizar stock
    public boolean updateStock(Long id, Integer nuevaCantidad) {
        // CAMBIADO: "Herramienta" por "herramienta"
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

    // Tomar/Asignar herramienta
    public boolean asignarHerramienta(Long id, String usuarioAsignado, String asignadoPor) {
        // CAMBIADO: "Herramienta" por "herramienta"
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

    // Devolver herramienta
    public boolean devolverHerramienta(Long id) {
        // CAMBIADO: "Herramienta" por "herramienta"
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

    // Eliminación lógica
    public boolean delete(Long id) {
        //  CAMBIADO: "Herramienta" por "herramienta"
        String sql = "UPDATE herramienta SET activo = false, fecha_actualizacion = CURRENT_TIMESTAMP WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar herramienta ID: " + id, e);
        }
    }

    // Métodos auxiliares
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