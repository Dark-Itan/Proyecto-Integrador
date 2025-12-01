package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.Tarea;
import com.inventario.alma_jesus.repository.TareaRepository;
import java.time.LocalDate;
import java.util.List;

public class TareaService {
    private final TareaRepository tareaRepository = new TareaRepository();

    public List<Tarea> listarTareas(String buscar, String estado) {
        return tareaRepository.findAll(buscar, estado);
    }

    public List<Tarea> listarTareasPorTrabajador(String trabajadorId, String estado) {
        return tareaRepository.findByTrabajadorId(trabajadorId, estado);
    }

    public Tarea obtenerTarea(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada ID: " + id));
    }

    public Tarea crearTarea(Tarea tarea) {
        validarTarea(tarea);

        if (tarea.getEstado() == null) {
            tarea.setEstado("PENDIENTE");
        }
        if (tarea.getActivo() == null) {
            tarea.setActivo(true);
        }

        return tareaRepository.save(tarea);
    }

    public boolean actualizarEstado(Long id, String nuevoEstado) {
        if (!esEstadoValido(nuevoEstado)) {
            throw new RuntimeException("Estado inválido. Debe ser: PENDIENTE, EN_PROCESO o COMPLETADA");
        }

        return tareaRepository.updateEstado(id, nuevoEstado);
    }

    public boolean eliminarTarea(Long id) {
        return tareaRepository.delete(id);
    }

    public boolean actualizarTarea(Tarea tarea) {
        validarTarea(tarea);
        return tareaRepository.update(tarea);
    }

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

    private boolean esEstadoValido(String estado) {
        return estado != null &&
                (estado.equals("PENDIENTE") || estado.equals("EN_PROCESO") || estado.equals("COMPLETADA"));
    }
}