package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.Tarea;
import com.inventario.alma_jesus.repository.TareaRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para la gestión de tareas de producción.
 * <p>
 * Esta clase contiene la lógica de negocio para todas las operaciones relacionadas
 * con tareas asignadas a trabajadores del taller. Incluye validaciones de datos,
 * control de estados, y manejo del ciclo de vida completo de las tareas.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see TareaRepository
 * @see Tarea
 */
public class TareaService {
    private final TareaRepository tareaRepository = new TareaRepository();

    /**
     * Lista tareas con opciones de filtrado.
     *
     * @param buscar Texto opcional para buscar en asunto o detalles de tareas
     * @param estado Estado opcional para filtrar tareas ("TODAS" para incluir todos)
     * @return Lista de {@link Tarea} que cumplen con los criterios de filtrado
     * @see TareaRepository#findAll(String, String)
     */
    public List<Tarea> listarTareas(String buscar, String estado) {
        return tareaRepository.findAll(buscar, estado);
    }

    /**
     * Lista tareas asignadas a un trabajador específico.
     *
     * @param trabajadorId ID del trabajador para el cual listar tareas
     * @param estado Estado opcional para filtrar tareas ("TODAS" para incluir todos)
     * @return Lista de {@link Tarea} asignadas al trabajador especificado
     * @see TareaRepository#findByTrabajadorId(String, String)
     */
    public List<Tarea> listarTareasPorTrabajador(String trabajadorId, String estado) {
        return tareaRepository.findByTrabajadorId(trabajadorId, estado);
    }

    /**
     * Obtiene una tarea específica por su ID.
     *
     * @param id ID de la tarea a obtener
     * @return La {@link Tarea} encontrada
     * @throws RuntimeException Si no se encuentra ninguna tarea con el ID proporcionado
     * @see TareaRepository#findById(Long)
     */
    public Tarea obtenerTarea(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada ID: " + id));
    }

    /**
     * Crea una nueva tarea en el sistema.
     * <p>
     * Valida los datos de la tarea, establece valores por defecto y la persiste
     * en la base de datos.
     * </p>
     *
     * @param tarea Objeto {@link Tarea} con los datos de la nueva tarea
     * @return La {@link Tarea} creada con su ID generado
     * @throws RuntimeException Si la tarea no pasa las validaciones
     * @see #validarTarea(Tarea)
     * @see TareaRepository#save(Tarea)
     */
    public Tarea crearTarea(Tarea tarea) {
        validarTarea(tarea);

        // Valores por defecto
        if (tarea.getEstado() == null) {
            tarea.setEstado("PENDIENTE");
        }
        if (tarea.getActivo() == null) {
            tarea.setActivo(true);
        }

        return tareaRepository.save(tarea);
    }

    /**
     * Actualiza el estado de una tarea existente.
     * <p>
     * Permite transicionar una tarea entre los estados válidos del flujo de trabajo.
     * Los estados válidos son: PENDIENTE, EN_PROCESO, COMPLETADA.
     * </p>
     *
     * @param id ID de la tarea a actualizar
     * @param nuevoEstado Nuevo estado a asignar a la tarea
     * @return true si la actualización fue exitosa, false en caso contrario
     * @throws RuntimeException Si el nuevo estado no es válido
     * @see #esEstadoValido(String)
     * @see TareaRepository#updateEstado(Long, String)
     */
    public boolean actualizarEstado(Long id, String nuevoEstado) {
        if (!esEstadoValido(nuevoEstado)) {
            throw new RuntimeException("Estado inválido. Debe ser: PENDIENTE, EN_PROCESO o COMPLETADA");
        }

        return tareaRepository.updateEstado(id, nuevoEstado);
    }

    /**
     * Elimina una tarea del sistema (eliminación lógica).
     * <p>
     * Realiza una eliminación lógica estableciendo el campo 'activo' a false.
     * La tarea permanece en la base de datos para fines de historial y reportes.
     * </p>
     *
     * @param id ID de la tarea a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     * @see TareaRepository#delete(Long)
     */
    public boolean eliminarTarea(Long id) {
        return tareaRepository.delete(id);
    }

    /**
     * Actualiza todos los datos de una tarea existente (excepto estado).
     * <p>
     * Permite modificar todos los campos editables de una tarea, excluyendo
     * el estado que debe actualizarse mediante {@link #actualizarEstado}.
     * </p>
     *
     * @param tarea Objeto {@link Tarea} con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     * @throws RuntimeException Si la tarea no pasa las validaciones
     * @see #validarTarea(Tarea)
     * @see TareaRepository#update(Tarea)
     */
    public boolean actualizarTarea(Tarea tarea) {
        validarTarea(tarea);
        return tareaRepository.update(tarea);
    }

    /**
     * Valida los datos de una tarea antes de persistirla.
     * <p>
     * Verifica que todos los campos requeridos estén presentes y tengan
     * valores válidos. Lanza excepciones descriptivas para cada caso de error.
     * </p>
     *
     * @param tarea Tarea a validar
     * @throws RuntimeException Si algún campo requerido es inválido:
     *         <ul>
     *           <li>Asunto nulo o vacío</li>
     *           <li>Detalles nulos o vacíos</li>
     *           <li>Fecha de asignación nula</li>
     *           <li>Fecha de entrega nula</li>
     *           <li>Fecha de entrega anterior a fecha de asignación</li>
     *           <li>Cantidad de figuras negativa (si se proporciona)</li>
     *           <li>Creador nulo o vacío</li>
     *           <li>Trabajador ID nulo</li>
     *         </ul>
     */
    private void validarTarea(Tarea tarea) {
        if (tarea.getAsunto() == null || tarea.getAsunto().trim().isEmpty()) {
            throw new RuntimeException("El asunto de la tarea es requerido");
        }
        if (tarea.getDetalles() == null || tarea.getDetalles().trim().isEmpty()) {
            throw new RuntimeException("Los detalles de la tarea son requeridos");
        }
        if (tarea.getFechaAsignacion() == null) {
            throw new RuntimeException("La fecha de asignación es requerida");
        }
        if (tarea.getFechaEntrega() == null) {
            throw new RuntimeException("La fecha de entrega es requerida");
        }
        if (tarea.getFechaEntrega().isBefore(tarea.getFechaAsignacion())) {
            throw new RuntimeException("La fecha de entrega no puede ser anterior a la fecha de asignación");
        }
        if (tarea.getCantidadFiguras() != null && tarea.getCantidadFiguras() < 0) {
            throw new RuntimeException("La cantidad de figuras debe ser un número positivo");
        }
        if (tarea.getCreadoPor() == null || tarea.getCreadoPor().trim().isEmpty()) {
            throw new RuntimeException("El creador de la tarea es requerido");
        }
        if (tarea.getTrabajadorId() == null) {
            throw new RuntimeException("El ID del trabajador es requerido");
        }
    }

    /**
     * Verifica si un estado es válido para el flujo de trabajo de tareas.
     * <p>
     * Los estados válidos están predefinidos y representan el ciclo de vida
     * estándar de una tarea en el sistema.
     * </p>
     *
     * @param estado Estado a verificar
     * @return true si el estado es válido, false en caso contrario
     */
    private boolean esEstadoValido(String estado) {
        return estado != null &&
                (estado.equals("PENDIENTE") || estado.equals("EN_PROCESO") || estado.equals("COMPLETADA"));
    }
}