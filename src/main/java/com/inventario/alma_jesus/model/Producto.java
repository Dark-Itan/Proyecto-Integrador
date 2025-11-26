package com.inventario.alma_jesus.model;

public class Producto {
    private int id;
    private String modelo;
    private String color;
    private int precio;
    private int stock;
    private String tamaño;
    private String imagenUrl;
    private boolean activo;
    private String creadoPor;
    private String fechaCreacion;
    private String tipo;

    public Producto() {}

    public Producto(String modelo, String color, int precio, int stock,
                    String tamaño, String creadoPor, String tipo) {
        this.modelo = modelo;
        this.color = color;
        this.precio = precio;
        this.stock = stock;
        this.tamaño = tamaño;
        this.creadoPor = creadoPor;
        this.tipo = tipo;
        this.activo = true;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public int getPrecio() { return precio; }
    public void setPrecio(int precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getTamaño() { return tamaño; }
    public void setTamaño(String tamaño) { this.tamaño = tamaño; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }
    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}