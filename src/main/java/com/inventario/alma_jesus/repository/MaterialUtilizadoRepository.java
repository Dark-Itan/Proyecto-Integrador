package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.MaterialUtilizado;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para la gestión de materiales utilizados en documentos.
 * <p>
 * Esta clase proporciona operaciones CRUD y consultas específicas
 * para los registros de materiales utilizados en reparaciones y pedidos.
 * Utiliza un pool de conexiones HikariCP para eficiencia.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class MaterialUtilizadoRepository {
    private final HikariDataSource dataSource;

    /**
     * Constructor del repositorio.
     *
     * @param dataSource Fuente de datos configurada con HikariCP
     */
    public MaterialUtilizadoRepository(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Busca materiales utilizados por tipo de documento y ID de documento.
     * <p>
     * Retorna los materiales ordenados por fecha de registro descendente
     * (los más recientes primero).
     * </p>
     *
     * @param tipoDocumento Tipo de documento (reparacion o pedido)
     * @param documentoId ID del documento relacionado
     * @return Lista de {@link MaterialUtilizado} encontrados
     * @throws RuntimeException Si ocurre un error en la consulta SQL
     */
    public List<MaterialUtilizado> findByTipoDocumentoAndDocumentoIdOrderByFechaRegistroDesc(
            MaterialUtilizado.TipoDocumento tipoDocumento, Long documentoId) {
        List<MaterialUtilizado> materiales = new ArrayList<>();
        String sql = "SELECT * FROM materialutilizado WHERE tipo_documento = ? AND documento_id = ? ORDER BY fecha_registro DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipoDocumento.name());
            stmt.setLong(2, documentoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    materiales.add(mapToMaterialUtilizado(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener materiales utilizados", e);
        }
        return materiales;
    }

    /**
     * Busca materiales utilizados por tipo de documento y ID de documento.
     * <p>
     * Método de conveniencia que delega en {@link #findByTipoDocumentoAndDocumentoIdOrderByFechaRegistroDesc}.
     * </p>
     *
     * @param tipoDocumento Tipo de documento (reparacion o pedido)
     * @param documentoId ID del documento relacionado
     * @return Lista de {@link MaterialUtilizado} encontrados
     * @see #findByTipoDocumentoAndDocumentoIdOrderByFechaRegistroDesc
     */
    public List<MaterialUtilizado> findByTipoDocumentoAndDocumentoId(
            MaterialUtilizado.TipoDocumento tipoDocumento, Long documentoId) {
        return findByTipoDocumentoAndDocumentoIdOrderByFechaRegistroDesc(tipoDocumento, documentoId);
    }

    /**
     * Calcula el costo total de materiales utilizados en una reparación.
     * <p>
     * Suma el producto de cantidad × costo_unitario para todos los materiales
     * asociados a una reparación específica.
     * </p>
     *
     * @param reparacionId ID de la reparación
     * @return Costo total en centavos, o 0 si no hay materiales
     * @throws RuntimeException Si ocurre un error en la consulta SQL
     */
    public Integer calcularCostoTotalMaterialesPorReparacion(Long reparacionId) {
        String sql = "SELECT COALESCE(SUM(cantidad * costo_unitario), 0) FROM materialutilizado " +
                "WHERE tipo_documento = 'reparacion' AND documento_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, reparacionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al calcular costo de materiales", e);
        }
        return 0;
    }

    /**
     * Verifica si existen materiales utilizados para un documento específico.
     * <p>
     * Útil para determinar si un documento tiene materiales asociados
     * antes de permitir ciertas operaciones.
     * </p>
     *
     * @param tipoDocumento Tipo de documento a verificar
     * @param documentoId ID del documento a verificar
     * @return true si existen materiales asociados, false en caso contrario
     * @throws RuntimeException Si ocurre un error en la consulta SQL
     */
    public boolean existsByTipoDocumentoAndDocumentoId(MaterialUtilizado.TipoDocumento tipoDocumento, Long documentoId) {
        String sql = "SELECT COUNT(*) FROM materialutilizado WHERE tipo_documento = ? AND documento_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipoDocumento.name());
            stmt.setLong(2, documentoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar materiales utilizados", e);
        }
        return false;
    }

    /**
     * Guarda un nuevo registro de material utilizado.
     * <p>
     * Inserta el registro en la base de datos y establece el ID generado
     * en el objeto material.
     * </p>
     *
     * @param material Objeto {@link MaterialUtilizado} a guardar
     * @return El mismo objeto con el ID generado establecido
     * @throws RuntimeException Si ocurre un error en la inserción SQL
     * @throws IllegalArgumentException Si algún campo requerido es nulo
     */
    public MaterialUtilizado save(MaterialUtilizado material) {
        String sql = "INSERT INTO materialutilizado (tipo_documento, documento_id, materia_id, " +
                "cantidad, costo_unitario, fecha, usuario_id, fecha_registro) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, material.getTipoDocumento().name());
            stmt.setLong(2, material.getDocumentoId());
            stmt.setLong(3, material.getMateriaId());
            stmt.setInt(4, material.getCantidad());
            stmt.setInt(5, material.getCostoUnitario());
            stmt.setDate(6, Date.valueOf(material.getFecha()));
            stmt.setString(7, material.getUsuarioId());
            stmt.setTimestamp(8, Timestamp.valueOf(material.getFechaRegistro()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear material utilizado, ninguna fila afectada");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    material.setId(generatedKeys.getLong(1));
                }
            }

            return material;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar material utilizado", e);
        }
    }

    /**
     * Elimina un registro de material utilizado por su ID.
     * <p>
     * Realiza una eliminación física del registro en la base de datos.
     * Use con precaución ya que no hay recuperación de datos.
     * </p>
     *
     * @param materialId ID del material utilizado a eliminar
     * @throws RuntimeException Si ocurre un error en la eliminación SQL
     */
    public void deleteById(Long materialId) {
        String sql = "DELETE FROM materialutilizado WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, materialId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar material utilizado", e);
        }
    }

    /**
     * Verifica si existe un material utilizado por su ID.
     *
     * @param materialId ID del material a verificar
     * @return true si existe, false en caso contrario
     * @throws RuntimeException Si ocurre un error en la consulta SQL
     */
    public boolean existsById(Long materialId) {
        String sql = "SELECT COUNT(*) FROM materialutilizado WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, materialId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar material utilizado", e);
        }
        return false;
    }

    /**
     * Mapea un ResultSet a un objeto MaterialUtilizado.
     * <p>
     * Método privado auxiliar para convertir filas de base de datos
     * a objetos del dominio.
     * </p>
     *
     * @param rs ResultSet con los datos del material
     * @return Objeto {@link MaterialUtilizado} mapeado
     * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet
     */
    private MaterialUtilizado mapToMaterialUtilizado(ResultSet rs) throws SQLException {
        MaterialUtilizado material = new MaterialUtilizado();
        material.setId(rs.getLong("id"));
        material.setTipoDocumento(MaterialUtilizado.TipoDocumento.valueOf(rs.getString("tipo_documento")));
        material.setDocumentoId(rs.getLong("documento_id"));
        material.setMateriaId(rs.getLong("materia_id"));
        material.setCantidad(rs.getInt("cantidad"));
        material.setCostoUnitario(rs.getInt("costo_unitario"));
        material.setFecha(rs.getDate("fecha").toLocalDate());
        material.setUsuarioId(rs.getString("usuario_id"));
        material.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());

        return material;
    }
}