package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.Recetario;
import com.inventario.alma_jesus.repository.RecetarioRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestión de recetas de fabricación.
 * <p>
 * Esta clase contiene la lógica de negocio para todas las operaciones relacionadas
 * con recetas del sistema, que definen los procesos estandarizados para fabricar
 * productos. Las recetas incluyen instrucciones técnicas, tiempos de fabricación,
 * materiales requeridos y herramientas necesarias.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see RecetarioRepository
 * @see Recetario
 */
public class RecetarioService {
    private final RecetarioRepository repository;

    /**
     * Constructor del servicio.
     *
     * @param repository Repositorio de recetas a utilizar
     */
    public RecetarioService(RecetarioRepository repository) {
        this.repository = repository;
    }

    /**
     * Lista todas las recetas del sistema.
     *
     * @return Lista de todas las {@link Recetario} disponibles
     * @throws RuntimeException Si ocurre un error al acceder a la base de datos
     * @see RecetarioRepository#findAll()
     */
    public List<Recetario> listarRecetas() {
        try {
            return repository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar recetas: " + e.getMessage(), e);
        }
    }

    /**
     * Lista únicamente las recetas disponibles para trabajadores.
     * <p>
     * Este método está diseñado específicamente para trabajadores,
     * quienes deben visualizar todas las recetas creadas por los administradores.
     * Utiliza el mismo repositorio que {@link #listarRecetas()} pero puede
     * ser extendido para incluir lógica de filtrado específica en el futuro.
     * </p>
     *
     * @return Lista de {@link Recetario} disponibles para trabajadores
     * @throws RuntimeException Si ocurre un error al acceder a la base de datos
     */
    public List<Recetario> listarRecetasParaTrabajador() {
        try {
            return repository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar recetas para trabajador: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene una receta específica por su ID.
     *
     * @param id ID de la receta a obtener
     * @return La {@link Recetario} encontrada con todos sus detalles
     * @throws RuntimeException Si:
     *         <ul>
     *           <li>No se encuentra la receta con el ID especificado</li>
     *           <li>Ocurre un error al acceder a la base de datos</li>
     *         </ul>
     * @see RecetarioRepository#findById(Long)
     */
    public Recetario obtenerReceta(Long id) {
        try {
            Recetario receta = repository.findById(id);
            if (receta == null) {
                throw new RuntimeException("Receta no encontrada con ID: " + id);
            }
            return receta;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener receta: " + e.getMessage(), e);
        }
    }

    /**
     * Crea una nueva receta en el sistema.
     * <p>
     * Valida los datos mínimos requeridos para una receta y la persiste
     * en la base de datos. Una receta básica debe incluir al menos tiempo
     * de fabricación e instrucciones técnicas.
     * </p>
     *
     * @param receta Objeto {@link Recetario} con los datos de la nueva receta
     * @return La {@link Recetario} creada con su ID generado
     * @throws RuntimeException Si:
     *         <ul>
     *           <li>El tiempo de fabricación es nulo o vacío</li>
     *           <li>Las instrucciones son nulas o vacías</li>
     *           <li>Ocurre un error al acceder a la base de datos</li>
     *         </ul>
     * @see RecetarioRepository#create(Recetario)
     */
    public Recetario crearReceta(Recetario receta) {
        try {
            // Validaciones mínimas
            if (receta.getTiempoFabricacion() == null || receta.getTiempoFabricacion().trim().isEmpty()) {
                throw new RuntimeException("El tiempo de fabricación es requerido");
            }
            if (receta.getInstrucciones() == null || receta.getInstrucciones().trim().isEmpty()) {
                throw new RuntimeException("Las instrucciones son requeridas");
            }

            Long recetaId = repository.create(receta);
            return obtenerReceta(recetaId);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear receta: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza una receta existente.
     * <p>
     * Valida que la receta exista, establece el ID correcto y actualiza
     * todos los campos editables de la receta.
     * </p>
     *
     * @param id ID de la receta a actualizar
     * @param receta Objeto {@link Recetario} con los datos actualizados
     * @return La {@link Recetario} actualizada
     * @throws RuntimeException Si:
     *         <ul>
     *           <li>No se encuentra la receta con el ID especificado</li>
     *           <li>No se puede actualizar la receta en la base de datos</li>
     *           <li>Ocurre un error al acceder a la base de datos</li>
     *         </ul>
     * @see RecetarioRepository#update(Recetario)
     */
    public Recetario actualizarReceta(Long id, Recetario receta) {
        try {
            Recetario existente = repository.findById(id);
            if (existente == null) {
                throw new RuntimeException("Receta no encontrada con ID: " + id);
            }

            receta.setId(id);
            boolean updated = repository.update(receta);
            if (!updated) {
                throw new RuntimeException("No se pudo actualizar la receta");
            }

            return obtenerReceta(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar receta: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina una receta del sistema.
     * <p>
     * Realiza una eliminación física de la receta y sus relaciones asociadas.
     * Esta operación es irreversible y elimina también la relación con
     * materiales de la receta.
     * </p>
     *
     * @param id ID de la receta a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     * @throws RuntimeException Si:
     *         <ul>
     *           <li>No se encuentra la receta con el ID especificado</li>
     *           <li>Ocurre un error al acceder a la base de datos</li>
     *         </ul>
     * @see RecetarioRepository#delete(Long)
     */
    public boolean eliminarReceta(Long id) {
        try {
            Recetario existente = repository.findById(id);
            if (existente == null) {
                throw new RuntimeException("Receta no encontrada con ID: " + id);
            }

            return repository.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar receta: " + e.getMessage(), e);
        }
    }
}