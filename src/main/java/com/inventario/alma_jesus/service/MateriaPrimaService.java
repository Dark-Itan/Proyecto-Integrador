package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.MateriaPrima;
import com.inventario.alma_jesus.model.MovimientoMp;
import com.inventario.alma_jesus.repository.MateriaPrimaRepository;
import com.inventario.alma_jesus.repository.MovimientoMpRepository;
import java.time.LocalDate;
import java.util.List;

public class MateriaPrimaService {
    private final MateriaPrimaRepository materiaRepository = new MateriaPrimaRepository();
    private final MovimientoMpRepository movimientoRepository = new MovimientoMpRepository();

    // Endpoint 25: Listar materiales
    public List<MateriaPrima> listarMateriales(String buscar, String categoria) {
        System.out.println("📦 [MATERIAL-SERVICE] Listando materiales - Buscar: '" + buscar + "', Categoría: '" + categoria + "'");
        return materiaRepository.findAll(buscar, categoria);
    }

    // Endpoint 26: Obtener material
    public MateriaPrima obtenerMaterial(Long id) {
        System.out.println("📦 [MATERIAL-SERVICE] Obteniendo material ID: " + id);
        return materiaRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("❌ [MATERIAL-SERVICE] Material no encontrado ID: " + id);
                    return new RuntimeException("Material no encontrado ID: " + id);
                });
    }

    // Endpoint 27: Crear material
    public MateriaPrima crearMaterial(MateriaPrima material) {
        System.out.println("📦 [MATERIAL-SERVICE] Creando nuevo material: " + material.getNombre());
        validarMaterial(material);
        MateriaPrima materialCreado = materiaRepository.save(material);

        // Registrar movimiento inicial
        if (materialCreado.getCantidad() > 0) {
            System.out.println("📦 [MATERIAL-SERVICE] Registrando movimiento inicial para: " + materialCreado.getNombre());
            MovimientoMp movimiento = new MovimientoMp();
            movimiento.setMateriaId(materialCreado.getId());
            movimiento.setFecha(LocalDate.now().toString());
            movimiento.setTipo("entrada");
            movimiento.setCantidad(materialCreado.getCantidad());
            movimiento.setUsuarioId(materialCreado.getCreadoPor());
            movimientoRepository.save(movimiento);
        }

        System.out.println("📦 [MATERIAL-SERVICE] Material creado exitosamente: " + materialCreado.getNombre());
        return materialCreado;
    }

    // Endpoint 28: Editar material completo
    public boolean editarMaterial(MateriaPrima material) {
        System.out.println("📦 [MATERIAL-SERVICE] Editando material ID: " + material.getId());
        validarMaterial(material);
        return materiaRepository.update(material);
    }

    // Endpoint 29: Actualizar stock
    public boolean actualizarStock(Long id, Integer nuevaCantidad, String usuarioId, String nota) {
        System.out.println("📦 [MATERIAL-SERVICE] Actualizando stock material ID: " + id + " -> " + nuevaCantidad);
        if (nuevaCantidad == null || nuevaCantidad < 0) {
            System.out.println("❌ [MATERIAL-SERVICE] Cantidad inválida: " + nuevaCantidad);
            throw new RuntimeException("La cantidad debe ser un número positivo");
        }

        MateriaPrima material = obtenerMaterial(id);
        Integer diferencia = nuevaCantidad - material.getCantidad();
        System.out.println("📦 [MATERIAL-SERVICE] Diferencia de stock: " + diferencia);

        boolean actualizado = materiaRepository.updateStock(id, nuevaCantidad);

        if (actualizado && diferencia != 0) {
            System.out.println("📦 [MATERIAL-SERVICE] Registrando movimiento - Tipo: " + (diferencia > 0 ? "entrada" : "salida") + ", Cantidad: " + Math.abs(diferencia));
            // Registrar movimiento
            MovimientoMp movimiento = new MovimientoMp();
            movimiento.setMateriaId(id);
            movimiento.setFecha(LocalDate.now().toString());
            movimiento.setTipo(diferencia > 0 ? "entrada" : "salida");
            movimiento.setCantidad(Math.abs(diferencia));
            movimiento.setUsuarioId(usuarioId);
            movimientoRepository.save(movimiento);
        }

        System.out.println("📦 [MATERIAL-SERVICE] Stock actualizado exitosamente");
        return actualizado;
    }

    // Endpoint 30: Ver historial
    public List<MovimientoMp> obtenerHistorial(Long materiaId) {
        System.out.println("📦 [MATERIAL-SERVICE] Obteniendo historial para material ID: " + materiaId);
        return movimientoRepository.findByMateriaId(materiaId);
    }

    // Endpoint 31: Eliminar material
    public boolean eliminarMaterial(Long id) {
        System.out.println("📦 [MATERIAL-SERVICE] Eliminando material ID: " + id);
        return materiaRepository.delete(id);
    }

    // Validaciones - CORREGIDAS para Double
    private void validarMaterial(MateriaPrima material) {
        System.out.println("📦 [MATERIAL-SERVICE] Validando material: " + material.getNombre());
        if (material.getNombre() == null || material.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del material es requerido");
        }
        if (material.getCantidad() == null || material.getCantidad() < 0) {
            throw new RuntimeException("La cantidad debe ser un número positivo");
        }
        if (material.getStockMinimo() == null || material.getStockMinimo() < 0) {
            throw new RuntimeException("El stock mínimo debe ser un número positivo");
        }
        if (material.getCosto() == null || material.getCosto() < 0) { // ✅ Ahora es Double
            throw new RuntimeException("El costo debe ser un número positivo");
        }
        if (material.getUnidad() == null || material.getUnidad().trim().isEmpty()) {
            throw new RuntimeException("La unidad es requerida");
        }
        if (material.getCategoria() == null || material.getCategoria().trim().isEmpty()) {
            throw new RuntimeException("La categoría es requerida");
        }
        System.out.println("📦 [MATERIAL-SERVICE] Material validado correctamente");
    }
}