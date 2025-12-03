package com.inventario.alma_jesus.model;

import java.time.LocalDateTime;

/**
 * Modelo que representa una materia prima o material en el inventario.
 * <p>
 * Esta clase define los atributos y comportamientos de los materiales utilizados
 * en la producción y reparaciones del taller, incluyendo información de stock,
 * costos, categorías y seguimiento de inventario.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class MateriaPrima {
    /**
     * Identificador único de la materia prima.
     */
    private Long id;

    /**
     * Nombre descriptivo del material (ej: "Tornillos 3x20", "Madera de roble").
     */
    private String nombre;

    /**
     * Descripción detallada del material y sus características.
     */
    private String descripcion;

    /**
     * Cantidad actual en stock del material.
     */
    private Integer cantidad;

    /**
     * Unidad de medida del material (ej: "pzas", "kg", "m", "m2", "l").
     */
    private String unidad;

    /**
     * Nivel mínimo de stock para generar alertas de reabastecimiento.
     */
    private Integer stockMinimo;

    /**
     * Costo unitario del material en moneda local.
     */
    private Double costo;

    /**
     * Categoría del material (ej: "Madera", "Metal", "Fijación", "Herrajes").
     */
    private String categoria;

    /**
     * Indica si el material está activo y disponible para uso.
     */
    private Boolean activo;

    /**
     * Identificador del usuario que creó el registro del material.
     */
    private String creadoPor;

    /**
     * Fecha y hora en que se registró el material en el sistema.
     */
    private LocalDateTime fechaCreacion;

    /**
     * Constructor por defecto.
     * Crea una instancia de MateriaPrima sin inicializar atributos.
     */
    public MateriaPrima() {}

    /**
     * Constructor completo con todos los atributos.
     *
     * @param id Identificador único
     * @param nombre Nombre del material
     * @param descripcion Descripción detallada
     * @param cantidad Cantidad en stock
     * @param unidad Unidad de medida
     * @param stockMinimo Nivel mínimo de stock
     * @param costo Costo unitario
     * @param categoria Categoría del material
     * @param activo Estado de actividad
     * @param creadoPor Usuario creador
     * @param fechaCreacion Fecha de registro
     */
    public MateriaPrima(Long id, String nombre, String descripcion, Integer cantidad,
                        String unidad, Integer stockMinimo, Double costo, String categoria,
                        Boolean activo, String creadoPor, LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.stockMinimo = stockMinimo;
        this.costo = costo;
        this.categoria = categoria;
        this.activo = activo;
        this.creadoPor = creadoPor;
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Obtiene el identificador único de la materia prima.
     *
     * @return El ID del material
     */
    public Long getId() { return id; }

    /**
     * Establece el identificador único de la materia prima.
     *
     * @param id El ID a asignar
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Obtiene el nombre descriptivo del material.
     *
     * @return El nombre del material
     */
    public String getNombre() { return nombre; }

    /**
     * Establece el nombre descriptivo del material.
     *
     * @param nombre El nombre a asignar
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Obtiene la descripción detallada del material.
     *
     * @return La descripción del material
     */
    public String getDescripcion() { return descripcion; }

    /**
     * Establece la descripción detallada del material.
     *
     * @param descripcion La descripción a asignar
     */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /**
     * Obtiene la cantidad actual en stock.
     *
     * @return La cantidad disponible
     */
    public Integer getCantidad() { return cantidad; }

    /**
     * Establece la cantidad actual en stock.
     *
     * @param cantidad La cantidad a asignar
     */
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

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
     * Obtiene el nivel mínimo de stock para alertas.
     *
     * @return El stock mínimo
     */
    public Integer getStockMinimo() { return stockMinimo; }

    /**
     * Establece el nivel mínimo de stock para alertas.
     *
     * @param stockMinimo El stock mínimo a asignar
     */
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }

    /**
     * Obtiene el costo unitario del material.
     *
     * @return El costo unitario
     */
    public Double getCosto() { return costo; }

    /**
     * Establece el costo unitario del material.
     *
     * @param costo El costo unitario a asignar
     */
    public void setCosto(Double costo) { this.costo = costo; }

    /**
     * Obtiene la categoría del material.
     *
     * @return La categoría
     */
    public String getCategoria() { return categoria; }

    /**
     * Establece la categoría del material.
     *
     * @param categoria La categoría a asignar
     */
    public void setCategoria(String categoria) { this.categoria = categoria; }

    /**
     * Verifica si el material está activo.
     *
     * @return true si está activo, false en caso contrario
     */
    public Boolean getActivo() { return activo; }

    /**
     * Establece el estado de actividad del material.
     *
     * @param activo El estado de actividad a asignar
     */
    public void setActivo(Boolean activo) { this.activo = activo; }

    /**
     * Obtiene el usuario creador del material.
     *
     * @return El ID del usuario creador
     */
    public String getCreadoPor() { return creadoPor; }

    /**
     * Establece el usuario creador del material.
     *
     * @param creadoPor El ID del usuario creador a asignar
     */
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }

    /**
     * Obtiene la fecha y hora de registro del material.
     *
     * @return La fecha de creación
     */
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    /**
     * Establece la fecha y hora de registro del material.
     *
     * @param fechaCreacion La fecha de creación a asignar
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}