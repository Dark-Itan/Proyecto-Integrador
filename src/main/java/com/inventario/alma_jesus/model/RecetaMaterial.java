package com.inventario.alma_jesus.model;

import java.math.BigDecimal;

/**
 * Modelo que representa un material requerido en una receta de producción.
 * <p>
 * Esta clase establece la relación entre recetas y materiales, especificando
 * las cantidades exactas de cada material necesario para fabricar un producto
 * según la receta correspondiente.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class RecetaMaterial {
    /**
     * Identificador único del registro receta-material.
     */
    private Long id;

    /**
     * ID de la receta a la que pertenece este material.
     */
    private Long recetaId;

    /**
     * ID del material requerido en la receta.
     */
    private Long materiaId;

    /**
     * Cantidad exacta del material necesaria para la receta.
     */
    private BigDecimal cantidad;

    /**
     * Unidad de medida para la cantidad especificada (ej: "kg", "pzas", "m").
     */
    private String unidad;

    /**
     * Nombre del material (campo adicional para facilitar visualización).
     */
    private String nombre;

    /**
     * Constructor por defecto.
     * Crea una instancia de RecetaMaterial sin inicializar atributos.
     */
    public RecetaMaterial() {}

    /**
     * Constructor con parámetros principales.
     *
     * @param id Identificador único
     * @param recetaId ID de la receta
     * @param materiaId ID del material
     * @param cantidad Cantidad requerida
     * @param unidad Unidad de medida
     */
    public RecetaMaterial(Long id, Long recetaId, Long materiaId, BigDecimal cantidad, String unidad) {
        this.id = id;
        this.recetaId = recetaId;
        this.materiaId = materiaId;
        this.cantidad = cantidad;
        this.unidad = unidad;
    }

    /**
     * Obtiene el identificador único del registro.
     *
     * @return El ID del registro receta-material
     */
    public Long getId() { return id; }

    /**
     * Establece el identificador único del registro.
     *
     * @param id El ID a asignar
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Obtiene el ID de la receta relacionada.
     *
     * @return El ID de la receta
     */
    public Long getRecetaId() { return recetaId; }

    /**
     * Establece el ID de la receta relacionada.
     *
     * @param recetaId El ID de la receta a asignar
     */
    public void setRecetaId(Long recetaId) { this.recetaId = recetaId; }

    /**
     * Obtiene el ID del material requerido.
     *
     * @return El ID del material
     */
    public Long getMateriaId() { return materiaId; }

    /**
     * Establece el ID del material requerido.
     *
     * @param materiaId El ID del material a asignar
     */
    public void setMateriaId(Long materiaId) { this.materiaId = materiaId; }

    /**
     * Obtiene la cantidad del material requerida.
     *
     * @return La cantidad requerida
     */
    public BigDecimal getCantidad() { return cantidad; }

    /**
     * Establece la cantidad del material requerida.
     *
     * @param cantidad La cantidad a asignar
     */
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

    /**
     * Obtiene la unidad de medida del material.
     *
     * @return La unidad de medida
     */
    public String getUnidad() { return unidad; }

    /**
     * Establece la unidad de medida del material.
     *
     * @param unidad La unidad de medida a asignar
     */
    public void setUnidad(String unidad) { this.unidad = unidad; }

    /**
     * Obtiene el nombre del material.
     *
     * @return El nombre del material
     */
    public String getNombre() { return nombre; }

    /**
     * Establece el nombre del material.
     *
     * @param nombre El nombre del material a asignar
     */
    public void setNombre(String nombre) { this.nombre = nombre; }
}