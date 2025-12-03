package com.inventario.alma_jesus.repository;

import com.inventario.alma_jesus.model.MovimientoMp;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para la gestión de movimientos de materia prima.
 * <p>
 * Esta clase maneja el historial de movimientos (entradas, salidas, ajustes)
 * de materiales en el sistema de inventario. Cada movimiento registra cambios
 * en el stock de materias primas con fines de trazabilidad y control.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class MovimientoMpRepository {
    private final Connection connection;

    /**
     * Constructor del repositorio.
     * <p>
     * Establece la conexión a la base de datos utilizando {@link DatabaseConnection}.
     * Lanza una excepción si no puede establecer la conexión.
     * </p>
     *
     * @throws RuntimeException Si ocurre un error al conectar con la base de datos
     */
    public MovimientoMpRepository() {
        try {
            this.connection = DatabaseConnection.getConnection();
            System.out.println("[MOVIMIENTO-REPO] Conectado a la base de datos");
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }

    /**
     * Obtiene el historial de movimientos para un material específico.
     * <p>
     * Retorna todos los movimientos registrados para un material,
     * ordenados por fecha descendente (los más recientes primero).
     * Incluye movimientos de tipo "entrada", "salida", "consumo", "ajuste", etc.
     * </p>
     *
     * @param materiaId ID del material para el cual obtener el historial
     * @return Lista de {@link MovimientoMp} ordenados por fecha descendente
     * @throws RuntimeException Si ocurre un error en la consulta SQL
     * @see MovimientoMp
     */
    public List<MovimientoMp> findByMateriaId(Long materiaId) {
        System.out.println(" [MOVIMIENTO-REPO] Obteniendo historial para material ID: " + materiaId);
        List<MovimientoMp> movimientos = new ArrayList<>();
        // Nota: El nombre de la tabla es "movimientomp" (en minúsculas)
        String sql = "SELECT * FROM movimientomp WHERE materia_id = ? ORDER BY fecha DESC, fecha_registro DESC";

        System.out.println("[MOVIMIENTO-REPO] SQL: " + sql);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, materiaId);

            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                movimientos.add(mapResultSetToMovimiento(rs));
                count++;
            }
            System.out.println("[MOVIMIENTO-REPO] Encontrados " + count + " movimientos");
        } catch (SQLException e) {
            System.out.println("[MOVIMIENTO-REPO] Error al obtener historial: " + e.getMessage());
            throw new RuntimeException("Error al obtener historial para material ID: " + materiaId, e);
        }
        return movimientos;
    }

    /**
     * Registra un nuevo movimiento de materia prima.
     * <p>
     * Inserta un registro de movimiento en la base de datos y establece
     * el ID generado en el objeto movimiento. Los tipos de movimiento
     * comunes incluyen:
     * - "entrada": Compra o ingreso de material
     * - "salida": Venta o retiro de material
     * - "consumo": Uso en producción
     * - "ajuste": Corrección manual de inventario
     * </p>
     *
     * @param movimiento Objeto {@link MovimientoMp} a registrar
     * @return El mismo objeto con el ID generado establecido
     * @throws RuntimeException Si ocurre un error en la inserción SQL
     * @throws IllegalArgumentException Si algún campo requerido es nulo o inválido
     */
    public MovimientoMp save(MovimientoMp movimiento) {
        System.out.println("[MOVIMIENTO-REPO] Registrando movimiento - Material: " + movimiento.getMateriaId() +
                ", Tipo: " + movimiento.getTipo() + ", Cantidad: " + movimiento.getCantidad());
        // Nota: El nombre de la tabla es "movimientomp" (en minúsculas)
        String sql = "INSERT INTO movimientomp (materia_id, fecha, tipo, cantidad, usuario_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        System.out.println("[MOVIMIENTO-REPO] SQL: " + sql);

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, movimiento.getMateriaId());
            stmt.setString(2, movimiento.getFecha());
            stmt.setString(3, movimiento.getTipo());
            stmt.setInt(4, movimiento.getCantidad());
            stmt.setString(5, movimiento.getUsuarioId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("[MOVIMIENTO-REPO] Error: ninguna fila afectada");
                throw new SQLException("Error al registrar movimiento, ninguna fila afectada");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    movimiento.setId(generatedKeys.getLong(1));
                    System.out.println("[MOVIMIENTO-REPO] Movimiento registrado con ID: " + movimiento.getId());
                }
            }
            return movimiento;
        } catch (SQLException e) {
            System.out.println("❌ [MOVIMIENTO-REPO] Error al registrar movimiento: " + e.getMessage());
            throw new RuntimeException("Error al registrar movimiento: " + e.getMessage(), e);
        }
    }

    /**
     * Mapea un ResultSet a un objeto MovimientoMp.
     * <p>
     * Método privado auxiliar para convertir filas de base de datos
     * a objetos del dominio. Extrae todos los campos de la tabla
     * "movimientomp" y los asigna al objeto correspondiente.
     * </p>
     *
     * @param rs ResultSet con los datos del movimiento
     * @return Objeto {@link MovimientoMp} mapeado
     * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet
     */
    private MovimientoMp mapResultSetToMovimiento(ResultSet rs) throws SQLException {
        MovimientoMp movimiento = new MovimientoMp();
        movimiento.setId(rs.getLong("id"));
        movimiento.setMateriaId(rs.getLong("materia_id"));
        movimiento.setFecha(rs.getString("fecha"));
        movimiento.setTipo(rs.getString("tipo"));
        movimiento.setCantidad(rs.getInt("cantidad"));
        movimiento.setUsuarioId(rs.getString("usuario_id"));
        movimiento.setFechaRegistro(rs.getString("fecha_registro"));
        return movimiento;
    }
}