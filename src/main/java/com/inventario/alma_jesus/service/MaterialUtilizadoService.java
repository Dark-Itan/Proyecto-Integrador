package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.MaterialUtilizado;
import com.inventario.alma_jesus.repository.MaterialUtilizadoRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class MaterialUtilizadoService {

    private static final Logger logger = LoggerFactory.getLogger(MaterialUtilizadoService.class);

    private final MaterialUtilizadoRepository materialUtilizadoRepository;

    public MaterialUtilizadoService(HikariDataSource dataSource) {
        this.materialUtilizadoRepository = new MaterialUtilizadoRepository(dataSource);
    }

    // Registrar material utilizado en reparación
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

    // Obtener materiales utilizados en una reparación
    public List<MaterialUtilizado> obtenerMaterialesPorReparacion(Long reparacionId) {
        logger.info("📦 Obteniendo materiales utilizados para reparación ID: {}", reparacionId);

        List<MaterialUtilizado> materiales = materialUtilizadoRepository
                .findByTipoDocumentoAndDocumentoIdOrderByFechaRegistroDesc(
                        MaterialUtilizado.TipoDocumento.reparacion, reparacionId);

        logger.info("✅ Se encontraron {} materiales para la reparación", materiales.size());
        return materiales;
    }

    // Calcular costo total de materiales por reparación
    public Integer calcularCostoMaterialesReparacion(Long reparacionId) {
        Integer costoTotal = materialUtilizadoRepository.calcularCostoTotalMaterialesPorReparacion(reparacionId);
        logger.info("💰 Costo total de materiales para reparación {}: ${}", reparacionId, costoTotal / 100.0);
        return costoTotal;
    }

    // Eliminar material utilizado
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