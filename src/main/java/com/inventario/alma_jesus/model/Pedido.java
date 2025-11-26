package com.inventario.alma_jesus.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Pedido {
    private Long id;
    private String clienteNombre;
    private String clienteContacto;
    private String fechaEntrega;
    private String notas;
    private String etapa;
    private BigDecimal total;
    private BigDecimal anticipo;
    private Integer totalCantidad;
    private String resumenProducto;
    private String creadoPor;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private List<PedidoProducto> productos;

    public Pedido() {}

    public Pedido(Long id, String clienteNombre, String clienteContacto, String fechaEntrega,
                  String notas, String etapa, BigDecimal total, BigDecimal anticipo,
                  Integer totalCantidad, String resumenProducto, String creadoPor) {
        this.id = id;
        this.clienteNombre = clienteNombre;
        this.clienteContacto = clienteContacto;
        this.fechaEntrega = fechaEntrega;
        this.notas = notas;
        this.etapa = etapa;
        this.total = total;
        this.anticipo = anticipo;
        this.totalCantidad = totalCantidad;
        this.resumenProducto = resumenProducto;
        this.creadoPor = creadoPor;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getClienteContacto() { return clienteContacto; }
    public void setClienteContacto(String clienteContacto) { this.clienteContacto = clienteContacto; }

    public String getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getEtapa() { return etapa; }
    public void setEtapa(String etapa) { this.etapa = etapa; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public BigDecimal getAnticipo() { return anticipo; }
    public void setAnticipo(BigDecimal anticipo) { this.anticipo = anticipo; }

    public Integer getTotalCantidad() { return totalCantidad; }
    public void setTotalCantidad(Integer totalCantidad) { this.totalCantidad = totalCantidad; }

    public String getResumenProducto() { return resumenProducto; }
    public void setResumenProducto(String resumenProducto) { this.resumenProducto = resumenProducto; }

    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public List<PedidoProducto> getProductos() { return productos; }
    public void setProductos(List<PedidoProducto> productos) { this.productos = productos; }
}