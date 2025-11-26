package com.inventario.alma_jesus.model;

import java.time.LocalDate;

public class Tarea {
    private Long id;
    private String asunto;
    private String detalles;
    private LocalDate fechaAsignacion;
    private LocalDate fechaEntrega;
    private Integer cantidadFiguras;
    private String estado;
    private Boolean activo;
    private String creadoPor;
    private String fechaCreacion;
    private Long trabajadorId; // NUEVO CAMPO

    public Tarea() {}

    public Tarea(Long id, String asunto, String detalles, LocalDate fechaAsignacion,
                 LocalDate fechaEntrega, Integer cantidadFiguras, String estado,
                 Boolean activo, String creadoPor, String fechaCreacion, Long trabajadorId) {
        this.id = id;
        this.asunto = asunto;
        this.detalles = detalles;
        this.fechaAsignacion = fechaAsignacion;
        this.fechaEntrega = fechaEntrega;
        this.cantidadFiguras = cantidadFiguras;
        this.estado = estado;
        this.activo = activo;
        this.creadoPor = creadoPor;
        this.fechaCreacion = fechaCreacion;
        this.trabajadorId = trabajadorId;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getDetalles() { return detalles; }
    public void setDetalles(String detalles) { this.detalles = detalles; }

    public LocalDate getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(LocalDate fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }

    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public Integer getCantidadFiguras() { return cantidadFiguras; }
    public void setCantidadFiguras(Integer cantidadFiguras) { this.cantidadFiguras = cantidadFiguras; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Long getTrabajadorId() { return trabajadorId; }
    public void setTrabajadorId(Long trabajadorId) { this.trabajadorId = trabajadorId; }
}