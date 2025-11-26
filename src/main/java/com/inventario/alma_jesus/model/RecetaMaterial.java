package com.inventario.alma_jesus.model;

import java.math.BigDecimal;

public class RecetaMaterial {
    private Long id;
    private Long recetaId;
    private Long materiaId;
    private BigDecimal cantidad;
    private String unidad;
    private String nombre;

    public RecetaMaterial() {}

    public RecetaMaterial(Long id, Long recetaId, Long materiaId, BigDecimal cantidad, String unidad) {
        this.id = id;
        this.recetaId = recetaId;
        this.materiaId = materiaId;
        this.cantidad = cantidad;
        this.unidad = unidad;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRecetaId() { return recetaId; }
    public void setRecetaId(Long recetaId) { this.recetaId = recetaId; }

    public Long getMateriaId() { return materiaId; }
    public void setMateriaId(Long materiaId) { this.materiaId = materiaId; }

    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}