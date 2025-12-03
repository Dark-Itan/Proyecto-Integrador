package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.Herramienta;
import com.inventario.alma_jesus.repository.HerramientaRepository;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de herramientas del taller.
 * <p>
 * Esta clase contiene la lógica de negocio para las operaciones relacionadas
 * con herramientas, incluyendo validaciones, reglas de negocio y coordinación
 * con el repositorio de datos. Maneja el ciclo completo de vida de las
 * herramientas en el inventario del taller.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see HerramientaRepository
 * @see Herramienta
 */
public class HerramientaService {
    private final HerramientaRepository herramientaRepository = new HerramientaRepository();

    /**
     * Lista herramientas con opciones de filtrado.
     * <p>
     * Retorna todas las herramientas activas en el sistema, con posibilidad
     * de filtrar por texto de búsqueda (nombre o descripción) y por estatus.
     * </p>
     *
     * @param buscar Texto opcional para buscar en nombre o descripción (ignora mayúsculas/minúsculas)
     * @param estatus Estatus opcional para filtrar (ej: "DISPONIBLE", "ASIGNADA", "EN_MANTENIMIENTO")
     * @return Lista de {@link Herramienta} que cumplen con los criterios de filtrado
     * @see HerramientaRepository#findAll(String, String)
     */
    public List<Herramienta> listarHerramientas(String buscar, String estatus) {
        return herramientaRepository.findAll(buscar, estatus);
    }

    /**
     * Obtiene una herramienta por su ID o nombre.
     * <p>
     * Busca una herramienta activa utilizando su ID numérico o su nombre.
     * Este método proporciona flexibilidad para las búsquedas desde la interfaz.
     * </p>
     *
     * @param idONombre ID numérico de la herramienta o nombre exacto
     * @return La {@link Herramienta} encontrada
     * @throws RuntimeException Si no se encuentra ninguna herramienta con el ID o nombre proporcionado
     * @see HerramientaRepository#findByIdOrNombre(String)
     */
    public Herramienta obtenerPorIdONombre(String idONombre) {
        Optional<Herramienta> herramienta = herramientaRepository.findByIdOrNombre(idONombre);
        return herramienta.orElseThrow(() ->
                new RuntimeException("Herramienta no encontrada: " + idONombre));
    }

    /**
     * Crea una nueva herramienta en el inventario.
     * <p>
     * Valida los datos requeridos y crea un nuevo registro de herramienta.
     * La cantidad disponible se establece inicialmente igual a la cantidad total.
     * </p>
     *
     * @param herramienta Objeto {@link Herramienta} con los datos de la nueva herramienta
     * @return La {@link Herramienta} creada con su ID generado
     * @throws RuntimeException Si:
     *         <ul>
     *           <li>El nombre es nulo o vacío</li>
     *           <li>La cantidad total es nula o menor/igual a cero</li>
     *         </ul>
     * @see HerramientaRepository#save(Herramienta)
     */
    public Herramienta crearHerramienta(Herramienta herramienta) {
        // Validaciones
        if (herramienta.getNombre() == null || herramienta.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la herramienta es requerido");
        }
        if (herramienta.getCantidadTotal() == null || herramienta.getCantidadTotal() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        return herramientaRepository.save(herramienta);
    }

    /**
     * Actualiza el stock de una herramienta.
     * <p>
     * Modifica la cantidad total y disponible de una herramienta existente.
     * Útil para ajustes de inventario o correcciones de stock.
     * </p>
     *
     * @param idONombre ID numérico o nombre de la herramienta a actualizar
     * @param nuevaCantidad Nueva cantidad total para la herramienta (debe ser positiva)
     * @return true si la actualización fue exitosa, false en caso contrario
     * @throws RuntimeException Si:
     *         <ul>
     *           <li>La nueva cantidad es nula o menor/igual a cero</li>
     *           <li>No se encuentra la herramienta especificada</li>
     *         </ul>
     * @see HerramientaRepository#updateStock(Long, Integer)
     */
    public boolean actualizarStock(String idONombre, Integer nuevaCantidad) {
        if (nuevaCantidad == null || nuevaCantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser un número positivo");
        }

        Herramienta herramienta = obtenerPorIdONombre(idONombre);
        return herramientaRepository.updateStock(herramienta.getId(), nuevaCantidad);
    }

    /**
     * Asigna una herramienta a un usuario (retiro del inventario).
     * <p>
     * Reduce la cantidad disponible de la herramienta y registra la asignación
     * a un usuario específico. Actualiza el estatus a "ASIGNADA" si corresponde.
     * </p>
     *
     * @param idONombre ID numérico o nombre de la herramienta a tomar
     * @param usuarioAsignado ID del usuario que toma la herramienta
     * @param asignadoPor ID del usuario que realiza la asignación (generalmente un supervisor)
     * @return true si la asignación fue exitosa, false en caso contrario
     * @throws RuntimeException Si:
     *         <ul>
     *           <li>El usuario asignado es nulo o vacío</li>
     *           <li>No se encuentra la herramienta especificada</li>
     *           <li>No hay suficiente stock disponible</li>
     *         </ul>
     * @see HerramientaRepository#asignarHerramienta(Long, String, String)
     */
    public boolean tomarHerramienta(String idONombre, String usuarioAsignado, String asignadoPor) {
        if (usuarioAsignado == null || usuarioAsignado.trim().isEmpty()) {
            throw new RuntimeException("El usuario asignado es requerido");
        }

        Herramienta herramienta = obtenerPorIdONombre(idONombre);
        return herramientaRepository.asignarHerramienta(herramienta.getId(), usuarioAsignado, asignadoPor);
    }

    /**
     * Devuelve una herramienta al inventario.
     * <p>
     * Incrementa la cantidad disponible de la herramienta y actualiza el estatus
     * a "DISPONIBLE" si corresponde. Limpia la información de asignación.
     * </p>
     *
     * @param idONombre ID numérico o nombre de la herramienta a devolver
     * @return true si la devolución fue exitosa, false en caso contrario
     * @throws RuntimeException Si no se encuentra la herramienta especificada
     * @see HerramientaRepository#devolverHerramienta(Long)
     */
    public boolean devolverHerramienta(String idONombre) {
        Herramienta herramienta = obtenerPorIdONombre(idONombre);
        return herramientaRepository.devolverHerramienta(herramienta.getId());
    }

    /**
     * Elimina una herramienta del sistema (eliminación lógica).
     * <p>
     * Realiza una eliminación lógica estableciendo el campo 'activo' a false.
     * La herramienta permanece en la base de datos para historial, pero no
     * aparece en búsquedas ni puede ser asignada.
     * </p>
     *
     * @param idONombre ID numérico o nombre de la herramienta a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     * @throws RuntimeException Si no se encuentra la herramienta especificada
     * @see HerramientaRepository#delete(Long)
     */
    public boolean eliminarHerramienta(String idONombre) {
        Herramienta herramienta = obtenerPorIdONombre(idONombre);
        return herramientaRepository.delete(herramienta.getId());
    }
}