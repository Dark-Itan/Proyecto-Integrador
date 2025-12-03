package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.MaterialUtilizado;
import com.inventario.alma_jesus.repository.MaterialUtilizadoRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para la gestión de materiales utilizados en reparaciones y pedidos.
 * <p>
 * Esta clase contiene la lógica de negocio relacionada con el registro,
 * consulta y cálculo de costos de materiales utilizados en documentos del sistema
 * (reparaciones y pedidos). Actúa como intermediario entre los controladores
 * y el repositorio, aplicando reglas de negocio y validaciones.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see MaterialUtilizadoRepository
 * @see MaterialUtilizado
 */
public class MaterialUtilizadoService {

    /**
     * Logger para registrar eventos y errores del servicio.
     */
    private static final Logger logger = LoggerFactory.getLogger(MaterialUtilizadoService.class);

    /**
     * Repositorio para operaciones de persistencia de materiales utilizados.
     */
    private final MaterialUtilizadoRepository materialUtilizadoRepository;

    /**
     * Constructor del servicio.
     * <p>
     * Inicializa el servicio con una fuente de datos HikariCP para la conexión
     * a la base de datos. Esta configuración permite un manejo eficiente de
     * conexiones mediante pooling.
     * </p>
     *
     * @param dataSource Fuente de datos configurada con HikariCP
     */
    public MaterialUtilizadoService(HikariDataSource dataSource) {
        this.materialUtilizadoRepository = new MaterialUtilizadoRepository(dataSource);
    }

    /**
     * Registra el uso de un material en una reparación.
     * <p>
     * Crea un nuevo registro de material utilizado, asignándolo automáticamente
     * al tipo de documento "reparacion" y estableciendo la fecha actual.
     * Este registro es fundamental para el cálculo de costos y control de inventario.
     * </p>
     *
     * @param reparacionId ID de la reparación donde se utilizó el material
     * @param materiaId ID del material consumido
     * @param cantidad Cantidad utilizada del material (unidades, kg, etc.)
     * @param costoUnitario Costo unitario del material al momento del consumo (en centavos)
     * @param usuarioId ID del usuario que registra el consumo
     * @return El {@link MaterialUtilizado} registrado con su ID generado
     * @throws IllegalArgumentException Si algún parámetro requerido es nulo o inválido
     * @throws RuntimeException Si ocurre un error en la persistencia
     * @see MaterialUtilizadoRepository#save(MaterialUtilizado)
     */
    public MaterialUtilizado registrarMaterialUtilizado(Long reparacionId, Long materiaId,
                                                        Integer cantidad, Integer costoUnitario, String usuarioId) {
        logger.info("📦 Registrando material utilizado - Reparación: {}, Material: {}, Cantidad: {}",
                reparacionId, materiaId, cantidad);

        MaterialUtilizado material = new MaterialUtilizado();
        material.setTipoDocumento(MaterialUtilizado.TipoDocumento.reparacion);
        material.setDocumentoId(reparacionId);
        material.setMateriaId(materiaId);
        material.setCantidad(cantidad);
        material.setCostoUnitario(costoUnitario);
        material.setFecha(LocalDate.now());
        material.setUsuarioId(usuarioId);

        MaterialUtilizado materialGuardado = materialUtilizadoRepository.save(material);
        logger.info("✅ Material registrado exitosamente - ID: {}", materialGuardado.getId());

        return materialGuardado;
    }

    /**
     * Obtiene los materiales utilizados en una reparación específica.
     * <p>
     * Retorna la lista completa de materiales consumidos en una reparación,
     * ordenados por fecha de registro descendente (los más recientes primero).
     * Útil para revisar el historial de consumo y generar reportes de costos.
     * </p>
     *
     * @param reparacionId ID de la reparación para la cual obtener los materiales
     * @return Lista de {@link MaterialUtilizado} asociados a la reparación,
     *         o lista vacía si no hay materiales registrados
     * @see MaterialUtilizadoRepository#findByTipoDocumentoAndDocumentoIdOrderByFechaRegistroDesc(
     *      MaterialUtilizado.TipoDocumento, Long)
     */
    public List<MaterialUtilizado> obtenerMaterialesPorReparacion(Long reparacionId) {
        logger.info("📦 Obteniendo materiales utilizados para reparación ID: {}", reparacionId);

        List<MaterialUtilizado> materiales = materialUtilizadoRepository
                .findByTipoDocumentoAndDocumentoIdOrderByFechaRegistroDesc(
                        MaterialUtilizado.TipoDocumento.reparacion, reparacionId);

        logger.info("✅ Se encontraron {} materiales para la reparación", materiales.size());
        return materiales;
    }

    /**
     * Calcula el costo total de materiales utilizados en una reparación.
     * <p>
     * Suma el producto de cantidad × costo_unitario para todos los materiales
     * asociados a una reparación. El resultado está expresado en centavos
     * para evitar problemas de precisión con decimales.
     * </p>
     *
     * @param reparacionId ID de la reparación para calcular el costo
     * @return Costo total en centavos, o 0 si no hay materiales registrados
     * @see MaterialUtilizadoRepository#calcularCostoTotalMaterialesPorReparacion(Long)
     */
    public Integer calcularCostoMaterialesReparacion(Long reparacionId) {
        Integer costoTotal = materialUtilizadoRepository.calcularCostoTotalMaterialesPorReparacion(reparacionId);
        logger.info("💰 Costo total de materiales para reparación {}: ${}",
                reparacionId, costoTotal / 100.0);
        return costoTotal;
    }

    /**
     * Elimina un registro de material utilizado.
     * <p>
     * Realiza una eliminación física del registro de la base de datos.
     * Útil para corregir registros erróneos o duplicados. Se recomienda
     * usar con precaución ya que esta operación no es reversible.
     * </p>
     *
     * @param materialId ID del registro de material utilizado a eliminar
     * @return true si la eliminación fue exitosa, false si no se encontró el registro
     * @see MaterialUtilizadoRepository#existsById(Long)
     * @see MaterialUtilizadoRepository#deleteById(Long)
     */
    public boolean eliminarMaterialUtilizado(Long materialId) {
        logger.info("🗑️ Eliminando material utilizado ID: {}", materialId);

        if (materialUtilizadoRepository.existsById(materialId)) {
            materialUtilizadoRepository.deleteById(materialId);
            logger.info("✅ Material eliminado exitosamente");
            return true;
        }

        logger.warn("⚠️ Material con ID {} no encontrado", materialId);
        return false;
    }
}