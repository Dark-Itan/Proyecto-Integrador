package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.MateriaPrima;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MateriaPrimaRepository {
    private final Connection connection;

    public MateriaPrimaRepository() {
        try {
            this.connection = DatabaseConnection.getConnection();
            System.out.println("[MATERIAL-REPO] Conectado a la base de datos");
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }

    // Endpoint 25: Listar materiales
    public List<MateriaPrima> findAll(String buscar, String categoria) {
        System.out.println("[MATERIAL-REPO] Listando materiales - Buscar: '" + buscar + "', Categoría: '" + categoria + "'");
        List<MateriaPrima> materiales = new ArrayList<>();
        // CAMBIADO: "MateriaPrima" por "materiaprima"
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

    // Endpoint 26: Obtener material por ID
    public Optional<MateriaPrima> findById(Long id) {
        System.out.println("[MATERIAL-REPO] Buscando material por ID: " + id);
        // CAMBIADO: "MateriaPrima" por "materiaprima"
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

    // Endpoint 27: Crear material
    public MateriaPrima save(MateriaPrima material) {
        System.out.println("[MATERIAL-REPO] Creando nuevo material: " + material.getNombre());
        //  CAMBIADO: "MateriaPrima" por "materiaprima"
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

    // Endpoint 28: Editar material completo
    public boolean update(MateriaPrima material) {
        System.out.println("[MATERIAL-REPO] Actualizando material ID: " + material.getId());
        // CAMBIADO: "MateriaPrima" por "materiaprima"
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

    // Endpoint 29: Actualizar stock
    public boolean updateStock(Long id, Integer nuevaCantidad) {
        System.out.println("[MATERIAL-REPO] Actualizando stock material ID: " + id + " -> " + nuevaCantidad);
        // CAMBIADO: "MateriaPrima" por "materiaprima"
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

    // Endpoint 31: Eliminar material (lógico)
    public boolean delete(Long id) {
        System.out.println("[MATERIAL-REPO] Eliminando material ID: " + id + " (lógico)");
        // CAMBIADO: "MateriaPrima" por "materiaprima"
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

    // Métodos auxiliares
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