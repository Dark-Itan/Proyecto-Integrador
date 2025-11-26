package com.inventario.alma_jesus.model;

import java.time.LocalDateTime;

public class MateriaPrima {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer cantidad;
    private String unidad;
    private Integer stockMinimo;
    private Double costo;
    private String categoria;
    private Boolean activo;
    private String creadoPor;
    private LocalDateTime fechaCreacion;

    public MateriaPrima() {}

    // Constructor completo
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

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }

    public Double getCosto() { return costo; }
    public void setCosto(Double costo) { this.costo = costo; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}