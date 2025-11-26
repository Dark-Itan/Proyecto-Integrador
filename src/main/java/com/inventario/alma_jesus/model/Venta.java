package com.inventario.alma_jesus.model;

public class Venta {
    private int id;
    private int clienteId;
    private int productoId;
    private String productoModelo;
    private int cantidad;
    private int precioUnitario;
    private int precioTotal;
    private String fecha;
    private String tipo;
    private String usuarioRegistro;
    private String fechaRegistro;

    // Constructores
    public Venta() {}

    public Venta(int clienteId, int productoId, int cantidad, int precioUnitario,
                 String fecha, String tipo, String usuarioRegistro) {
        this.clienteId = clienteId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.precioTotal = cantidad * precioUnitario; // Calculado automáticamente
        this.fecha = fecha;
        this.tipo = tipo;
        this.usuarioRegistro = usuarioRegistro;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public String getProductoModelo() { return productoModelo; }
    public void setProductoModelo(String productoModelo) { this.productoModelo = productoModelo; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.precioTotal = cantidad * precioUnitario; // Recalcular al cambiar cantidad
    }

    public int getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(int precioUnitario) {
        this.precioUnitario = precioUnitario;
        this.precioTotal = cantidad * precioUnitario; // Recalcular al cambiar precio
    }

    public int getPrecioTotal() { return precioTotal; }
    public void setPrecioTotal(int precioTotal) { this.precioTotal = precioTotal; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getUsuarioRegistro() { return usuarioRegistro; }
    public void setUsuarioRegistro(String usuarioRegistro) { this.usuarioRegistro = usuarioRegistro; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}