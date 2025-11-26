package com.inventario.alma_jesus.model;

import java.time.LocalDate;

public class HistorialReparacion {
    private Long id;
    private Long reparacionId;
    private String fecha;
    private String estado;
    private String notas;
    private String usuarioId;
    private String fechaRegistro;

    // Constructor vacío
    public HistorialReparacion() {}

    // Constructor completo
    public HistorialReparacion(Long id, Long reparacionId, String fecha, String estado,
                               String notas, String usuarioId, String fechaRegistro) {
        this.id = id;
        this.reparacionId = reparacionId;
        this.fecha = fecha;
        this.estado = estado;
        this.notas = notas;
        this.usuarioId = usuarioId;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getReparacionId() { return reparacionId; }
    public void setReparacionId(Long reparacionId) { this.reparacionId = reparacionId; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}