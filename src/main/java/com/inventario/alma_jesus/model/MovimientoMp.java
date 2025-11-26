package com.inventario.alma_jesus.model;

public class MovimientoMp {
    private Long id;
    private Long materiaId;
    private String fecha;                    // ✅ CAMBIADO a String
    private String tipo;
    private Integer cantidad;
    private String usuarioId;
    private String fechaRegistro;           // ✅ CAMBIADO a String

    // Constructor vacío
    public MovimientoMp() {}

    // Constructor completo
    public MovimientoMp(Long id, Long materiaId, String fecha, String tipo,
                        Integer cantidad, String usuarioId, String fechaRegistro) {
        this.id = id;
        this.materiaId = materiaId;
        this.fecha = fecha;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.usuarioId = usuarioId;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMateriaId() { return materiaId; }
    public void setMateriaId(Long materiaId) { this.materiaId = materiaId; }

    public String getFecha() { return fecha; }                    // ✅ String
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getFechaRegistro() { return fechaRegistro; }    // ✅ String
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}