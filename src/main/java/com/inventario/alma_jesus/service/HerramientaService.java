package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.Herramienta;
import com.inventario.alma_jesus.repository.HerramientaRepository;
import java.util.List;
import java.util.Optional;

public class HerramientaService {
    private final HerramientaRepository herramientaRepository = new HerramientaRepository();

    // Endpoint 18: Listar herramientas
    public List<Herramienta> listarHerramientas(String buscar, String estatus) {
        return herramientaRepository.findAll(buscar, estatus);
    }

    // Endpoint 19: Obtener herramienta por ID o nombre
    public Herramienta obtenerPorIdONombre(String idONombre) {
        Optional<Herramienta> herramienta = herramientaRepository.findByIdOrNombre(idONombre);
        return herramienta.orElseThrow(() ->
                new RuntimeException("Herramienta no encontrada: " + idONombre));
    }

    // Endpoint 20: Crear herramienta
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

    // Endpoint 21: Actualizar stock
    public boolean actualizarStock(String idONombre, Integer nuevaCantidad) {
        if (nuevaCantidad == null || nuevaCantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser un número positivo");
        }

        Herramienta herramienta = obtenerPorIdONombre(idONombre);
        return herramientaRepository.updateStock(herramienta.getId(), nuevaCantidad);
    }

    // Endpoint 22: Tomar herramienta
    public boolean tomarHerramienta(String idONombre, String usuarioAsignado, String asignadoPor) {
        if (usuarioAsignado == null || usuarioAsignado.trim().isEmpty()) {
            throw new RuntimeException("El usuario asignado es requerido");
        }

        Herramienta herramienta = obtenerPorIdONombre(idONombre);
        return herramientaRepository.asignarHerramienta(herramienta.getId(), usuarioAsignado, asignadoPor);
    }

    // Endpoint 23: Devolver herramienta
    public boolean devolverHerramienta(String idONombre) {
        Herramienta herramienta = obtenerPorIdONombre(idONombre);
        return herramientaRepository.devolverHerramienta(herramienta.getId());
    }

    // Endpoint 24: Eliminar herramienta
    public boolean eliminarHerramienta(String idONombre) {
        Herramienta herramienta = obtenerPorIdONombre(idONombre);
        return herramientaRepository.delete(herramienta.getId());
    }
}