package com.inventario.alma_jesus.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MaterialUtilizado {
    private Long id;
    private TipoDocumento tipoDocumento;
    private Long documentoId;
    private Long materiaId;
    private Integer cantidad;
    private Integer costoUnitario;
    private LocalDate fecha;
    private String usuarioId;
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    public enum TipoDocumento {
        reparacion, pedido
    }

    public MaterialUtilizado() {}

    public MaterialUtilizado(TipoDocumento tipoDocumento, Long documentoId, Long materiaId,
                             Integer cantidad, Integer costoUnitario, LocalDate fecha, String usuarioId) {
        this.tipoDocumento = tipoDocumento;
        this.documentoId = documentoId;
        this.materiaId = materiaId;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.fecha = fecha;
        this.usuarioId = usuarioId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoDocumento getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(TipoDocumento tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public Long getDocumentoId() { return documentoId; }
    public void setDocumentoId(Long documentoId) { this.documentoId = documentoId; }

    public Long getMateriaId() { return materiaId; }
    public void setMateriaId(Long materiaId) { this.materiaId = materiaId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Integer getCostoUnitario() { return costoUnitario; }
    public void setCostoUnitario(Integer costoUnitario) { this.costoUnitario = costoUnitario; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
