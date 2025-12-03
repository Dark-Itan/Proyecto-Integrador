package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.MateriaPrima;
import com.inventario.alma_jesus.model.MovimientoMp;
import com.inventario.alma_jesus.repository.MateriaPrimaRepository;
import com.inventario.alma_jesus.repository.MovimientoMpRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para la gestión de materia prima en el inventario.
 * <p>
 * Esta clase contiene la lógica de negocio para todas las operaciones relacionadas
 * con materia prima, incluyendo creación, actualización, control de stock y
 * registro de movimientos. Mantiene la trazabilidad completa de los materiales
 * mediante un sistema de movimientos que registra todas las entradas y salidas.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see MateriaPrimaRepository
 * @see MovimientoMpRepository
 * @see MateriaPrima
 * @see MovimientoMp
 */
public class MateriaPrimaService {
    private final MateriaPrimaRepository materiaRepository = new MateriaPrimaRepository();
    private final MovimientoMpRepository movimientoRepository = new MovimientoMpRepository();

    /**
     * Lista materiales de inventario con opciones de filtrado.
     * <p>
     * Retorna todas las materias primas activas en el sistema, con posibilidad
     * de filtrar por texto de búsqueda (nombre o descripción) y por categoría.
     * </p>
     *
     * @param buscar Texto opcional para buscar en nombre o descripción (ignora mayúsculas/minúsculas)
     * @param categoria Categoría opcional para filtrar materiales
     * @return Lista de {@link MateriaPrima} que cumplen con los criterios de filtrado
     * @see MateriaPrimaRepository#findAll(String, String)
     */
    public List<MateriaPrima> listarMateriales(String buscar, String categoria) {
        System.out.println(" [MATERIAL-SERVICE] Listando materiales - Buscar: '" + buscar + "', Categoría: '" + categoria + "'");
        return materiaRepository.findAll(buscar, categoria);
    }

    /**
     * Obtiene una materia prima específica por su ID.
     *
     * @param id ID de la materia prima a obtener
     * @return La {@link MateriaPrima} encontrada
     * @throws RuntimeException Si no se encuentra ninguna materia prima con el ID proporcionado
     * @see MateriaPrimaRepository#findById(Long)
     */
    public MateriaPrima obtenerMaterial(Long id) {
        System.out.println(" [MATERIAL-SERVICE] Obteniendo material ID: " + id);
        return materiaRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("❌ [MATERIAL-SERVICE] Material no encontrado ID: " + id);
                    return new RuntimeException("Material no encontrado ID: " + id);
                });
    }

    /**
     * Crea una nueva materia prima en el inventario.
     * <p>
     * Valida los datos del material, lo persiste en la base de datos y registra
     * automáticamente un movimiento inicial de tipo "entrada" si la cantidad
     * inicial es mayor a cero.
     * </p>
     *
     * @param material Objeto {@link MateriaPrima} con los datos del nuevo material
     * @return La {@link MateriaPrima} creada con su ID generado
     * @throws RuntimeException Si el material no pasa las validaciones
     * @see #validarMaterial(MateriaPrima)
     * @see MateriaPrimaRepository#save(MateriaPrima)
     */
    public MateriaPrima crearMaterial(MateriaPrima material) {
        System.out.println(" [MATERIAL-SERVICE] Creando nuevo material: " + material.getNombre());
        validarMaterial(material);
        MateriaPrima materialCreado = materiaRepository.save(material);

        // Registrar movimiento inicial si hay cantidad
        if (materialCreado.getCantidad() > 0) {
            System.out.println(" [MATERIAL-SERVICE] Registrando movimiento inicial para: " + materialCreado.getNombre());
            MovimientoMp movimiento = new MovimientoMp();
            movimiento.setMateriaId(materialCreado.getId());
            movimiento.setFecha(LocalDate.now().toString());
            movimiento.setTipo("entrada");
            movimiento.setCantidad(materialCreado.getCantidad());
            movimiento.setUsuarioId(materialCreado.getCreadoPor());
            movimientoRepository.save(movimiento);
        }

        System.out.println(" [MATERIAL-SERVICE] Material creado exitosamente: " + materialCreado.getNombre());
        return materialCreado;
    }

    /**
     * Edita completamente una materia prima existente.
     * <p>
     * Actualiza todos los campos editables de un material. La cantidad
     * debe actualizarse mediante {@link #actualizarStock} para mantener
     * la trazabilidad mediante movimientos.
     * </p>
     *
     * @param material Objeto {@link MateriaPrima} con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     * @throws RuntimeException Si el material no pasa las validaciones
     * @see #validarMaterial(MateriaPrima)
     * @see MateriaPrimaRepository#update(MateriaPrima)
     */
    public boolean editarMaterial(MateriaPrima material) {
        System.out.println(" [MATERIAL-SERVICE] Editando material ID: " + material.getId());
        validarMaterial(material);
        return materiaRepository.update(material);
    }

    /**
     * Actualiza el stock de una materia prima y registra el movimiento correspondiente.
     * <p>
     * Calcula la diferencia entre la cantidad anterior y la nueva, y registra
     * un movimiento de tipo "entrada" (si aumentó) o "salida" (si disminuyó).
     * Mantiene la trazabilidad completa de los cambios en el inventario.
     * </p>
     *
     * @param id ID del material cuyo stock se actualizará
     * @param nuevaCantidad Nueva cantidad de stock
     * @param usuarioId ID del usuario que realiza la actualización
     * @param nota Nota u observación sobre la actualización (actualmente no se usa, reservado para futuro)
     * @return true si la actualización fue exitosa, false en caso contrario
     * @throws RuntimeException Si la nueva cantidad es inválida (nula o negativa)
     * @see MateriaPrimaRepository#updateStock(Long, Integer)
     * @see MovimientoMpRepository#save(MovimientoMp)
     */
    public boolean actualizarStock(Long id, Integer nuevaCantidad, String usuarioId, String nota) {
        System.out.println(" [MATERIAL-SERVICE] Actualizando stock material ID: " + id + " -> " + nuevaCantidad);
        if (nuevaCantidad == null || nuevaCantidad < 0) {
            System.out.println(" [MATERIAL-SERVICE] Cantidad inválida: " + nuevaCantidad);
            throw new RuntimeException("La cantidad debe ser un número positivo");
        }

        MateriaPrima material = obtenerMaterial(id);
        Integer diferencia = nuevaCantidad - material.getCantidad();
        System.out.println(" [MATERIAL-SERVICE] Diferencia de stock: " + diferencia);

        boolean actualizado = materiaRepository.updateStock(id, nuevaCantidad);

        if (actualizado && diferencia != 0) {
            System.out.println("[MATERIAL-SERVICE] Registrando movimiento - Tipo: " + (diferencia > 0 ? "entrada" : "salida") + ", Cantidad: " + Math.abs(diferencia));
            // Registrar movimiento
            MovimientoMp movimiento = new MovimientoMp();
            movimiento.setMateriaId(id);
            movimiento.setFecha(LocalDate.now().toString());
            movimiento.setTipo(diferencia > 0 ? "entrada" : "salida");
            movimiento.setCantidad(Math.abs(diferencia));
            movimiento.setUsuarioId(usuarioId);
            movimientoRepository.save(movimiento);
        }

        System.out.println("[MATERIAL-SERVICE] Stock actualizado exitosamente");
        return actualizado;
    }

    /**
     * Obtiene el historial de movimientos de una materia prima.
     * <p>
     * Retorna todos los movimientos (entradas, salidas, ajustes) registrados
     * para un material específico, ordenados por fecha descendente.
     * </p>
     *
     * @param materiaId ID de la materia prima para la cual obtener el historial
     * @return Lista de {@link MovimientoMp} ordenados por fecha descendente
     * @see MovimientoMpRepository#findByMateriaId(Long)
     */
    public List<MovimientoMp> obtenerHistorial(Long materiaId) {
        System.out.println(" [MATERIAL-SERVICE] Obteniendo historial para material ID: " + materiaId);
        return movimientoRepository.findByMateriaId(materiaId);
    }

    /**
     * Elimina una materia prima del inventario (eliminación lógica).
     * <p>
     * Realiza una eliminación lógica estableciendo el campo 'activo' a false.
     * El material permanece en la base de datos para fines de historial y
     * sus movimientos asociados se mantienen intactos.
     * </p>
     *
     * @param id ID del material a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     * @see MateriaPrimaRepository#delete(Long)
     */
    public boolean eliminarMaterial(Long id) {
        System.out.println(" [MATERIAL-SERVICE] Eliminando material ID: " + id);
        return materiaRepository.delete(id);
    }

    /**
     * Valida los datos de una materia prima.
     * <p>
     * Verifica que todos los campos requeridos estén presentes y tengan
     * valores válidos. Lanza excepciones descriptivas para cada caso de error.
     * </p>
     *
     * @param material Materia prima a validar
     * @throws RuntimeException Si algún campo requerido es inválido:
     *         <ul>
     *           <li>Nombre nulo o vacío</li>
     *           <li>Cantidad negativa</li>
     *           <li>Stock mínimo negativo</li>
     *           <li>Costo negativo (tipo Double)</li>
     *           <li>Unidad nula o vacía</li>
     *           <li>Categoría nula o vacía</li>
     *         </ul>
     */
    private void validarMaterial(MateriaPrima material) {
        System.out.println(" [MATERIAL-SERVICE] Validando material: " + material.getNombre());
        if (material.getNombre() == null || material.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del material es requerido");
        }
        if (material.getCantidad() == null || material.getCantidad() < 0) {
            throw new RuntimeException("La cantidad debe ser un número positivo");
        }
        if (material.getStockMinimo() == null || material.getStockMinimo() < 0) {
            throw new RuntimeException("El stock mínimo debe ser un número positivo");
        }
        if (material.getCosto() == null || material.getCosto() < 0) { //  Tipo Double
            throw new RuntimeException("El costo debe ser un número positivo");
        }
        if (material.getUnidad() == null || material.getUnidad().trim().isEmpty()) {
            throw new RuntimeException("La unidad es requerida");
        }
        if (material.getCategoria() == null || material.getCategoria().trim().isEmpty()) {
            throw new RuntimeException("La categoría es requerida");
        }
        System.out.println("[MATERIAL-SERVICE] Material validado correctamente");
    }
}