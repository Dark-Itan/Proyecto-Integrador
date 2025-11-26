package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.Tarea;
import com.inventario.alma_jesus.repository.TareaRepository;
import java.time.LocalDate;
import java.util.List;

public class TareaService {
    private final TareaRepository tareaRepository = new TareaRepository();

    // Endpoint 50: Listar tareas (admin)
    public List<Tarea> listarTareas(String buscar, String estado) {
        System.out.println("[TAREA-SERVICE] Listando tareas - Buscar: '" + buscar + "', Estado: '" + estado + "'");
        return tareaRepository.findAll(buscar, estado);
    }

    // NUEVO: Listar tareas por trabajador
    public List<Tarea> listarTareasPorTrabajador(Long trabajadorId, String estado) {
        System.out.println("[TAREA-SERVICE] Listando tareas para trabajador: " + trabajadorId + ", estado: " + estado);
        return tareaRepository.findByTrabajadorId(trabajadorId, estado);
    }

    // Endpoint 51: Obtener tarea
    public Tarea obtenerTarea(Long id) {
        System.out.println("[TAREA-SERVICE] Obteniendo tarea ID: " + id);
        return tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada ID: " + id));
    }

    // Endpoint 52: Crear tarea
    public Tarea crearTarea(Tarea tarea) {
        System.out.println("[TAREA-SERVICE] Creando nueva tarea: " + tarea.getAsunto());
        validarTarea(tarea);

        if (tarea.getEstado() == null) {
            tarea.setEstado("PENDIENTE");
        }
        if (tarea.getActivo() == null) {
            tarea.setActivo(true);
        }

        Tarea tareaCreada = tareaRepository.save(tarea);
        System.out.println("[TAREA-SERVICE] Tarea creada exitosamente: " + tareaCreada.getAsunto());
        return tareaCreada;
    }

    // Endpoint 53: Actualizar estado
    public boolean actualizarEstado(Long id, String nuevoEstado) {
        System.out.println("[TAREA-SERVICE] Actualizando estado tarea ID: " + id + " -> " + nuevoEstado);

        if (!esEstadoValido(nuevoEstado)) {
            throw new RuntimeException("Estado inválido. Debe ser: PENDIENTE, EN_PROCESO o COMPLETADA");
        }

        boolean actualizado = tareaRepository.updateEstado(id, nuevoEstado);
        if (actualizado) {
            System.out.println("[TAREA-SERVICE] Estado actualizado exitosamente");
        } else {
            System.out.println("[TAREA-SERVICE] No se pudo actualizar el estado");
        }
        return actualizado;
    }

    // Endpoint 54: Eliminar tarea
    public boolean eliminarTarea(Long id) {
        System.out.println("[TAREA-SERVICE] Eliminando tarea ID: " + id);
        return tareaRepository.delete(id);
    }

    // Método adicional: Actualizar tarea completa
    public boolean actualizarTarea(Tarea tarea) {
        System.out.println("[TAREA-SERVICE] Actualizando tarea ID: " + tarea.getId());
        validarTarea(tarea);
        return tareaRepository.update(tarea);
    }

    // Validaciones
    private void validarTarea(Tarea tarea) {
        System.out.println("[TAREA-SERVICE] Validando tarea: " + tarea.getAsunto());

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

        System.out.println("[TAREA-SERVICE] Tarea validada correctamente");
    }

    private boolean esEstadoValido(String estado) {
        return estado != null &&
                (estado.equals("PENDIENTE") || estado.equals("EN_PROCESO") || estado.equals("COMPLETADA"));
    }
}