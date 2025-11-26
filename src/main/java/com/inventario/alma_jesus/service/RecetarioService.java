package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.Recetario;
import com.inventario.alma_jesus.repository.RecetarioRepository;

import java.sql.SQLException;
import java.util.List;

public class RecetarioService {
    private final RecetarioRepository repository;

    public RecetarioService(RecetarioRepository repository) {
        this.repository = repository;
    }

    public List<Recetario> listarRecetas() {
        try {
            return repository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar recetas: " + e.getMessage(), e);
        }
    }

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

    public Recetario crearReceta(Recetario receta) {
        try {
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