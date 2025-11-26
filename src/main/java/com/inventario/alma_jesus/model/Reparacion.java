package com.inventario.alma_jesus.model;

public class Reparacion {
    private Long id;
    private Long clienteId;
    private String nombreCliente;
    private String contacto;
    private String modelo;
    private String materialOriginal;
    private String condicion;
    private String materialesUsados;
    private Integer costoTotal;
    private Integer anticipo;
    private Integer cantidadPiezas;
    private String fechaIngreso;
    private String fechaEntrega;
    private String estado;
    private String prioridadCliente;
    private String notas;
    private String trabajadorAsignado;
    private String imagenUrl;
    private String reciboUrl; // NUEVO CAMPO
    private String creadoPor;
    private Boolean activo;
    private String fechaRegistro;

    // Constructores
    public Reparacion() {}

    public Reparacion(String nombreCliente, String contacto, String modelo,
                      String materialOriginal, String condicion, String materialesUsados,
                      Integer costoTotal, Integer anticipo, Integer cantidadPiezas,
                      String fechaEntrega, String notas) {
        this.nombreCliente = nombreCliente;
        this.contacto = contacto;
        this.modelo = modelo;
        this.materialOriginal = materialOriginal;
        this.condicion = condicion;
        this.materialesUsados = materialesUsados;
        this.costoTotal = costoTotal;
        this.anticipo = anticipo;
        this.cantidadPiezas = cantidadPiezas;
        this.fechaEntrega = fechaEntrega;
        this.notas = notas;
        this.estado = "Pendiente";
        this.prioridadCliente = "Media";
        this.activo = true;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getMaterialOriginal() { return materialOriginal; }
    public void setMaterialOriginal(String materialOriginal) { this.materialOriginal = materialOriginal; }

    public String getCondicion() { return condicion; }
    public void setCondicion(String condicion) { this.condicion = condicion; }

    public String getMaterialesUsados() { return materialesUsados; }
    public void setMaterialesUsados(String materialesUsados) { this.materialesUsados = materialesUsados; }

    public Integer getCostoTotal() { return costoTotal; }
    public void setCostoTotal(Integer costoTotal) { this.costoTotal = costoTotal; }

    public Integer getAnticipo() { return anticipo; }
    public void setAnticipo(Integer anticipo) { this.anticipo = anticipo; }

    public Integer getCantidadPiezas() { return cantidadPiezas; }
    public void setCantidadPiezas(Integer cantidadPiezas) { this.cantidadPiezas = cantidadPiezas; }

    public String getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(String fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public String getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getPrioridadCliente() { return prioridadCliente; }
    public void setPrioridadCliente(String prioridadCliente) { this.prioridadCliente = prioridadCliente; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getTrabajadorAsignado() { return trabajadorAsignado; }
    public void setTrabajadorAsignado(String trabajadorAsignado) { this.trabajadorAsignado = trabajadorAsignado; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getReciboUrl() { return reciboUrl; } // NUEVO GETTER
    public void setReciboUrl(String reciboUrl) { this.reciboUrl = reciboUrl; } // NUEVO SETTER

    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    // Método para calcular saldo pendiente
    public Integer getSaldoPendiente() {
        if (costoTotal == null) return 0;
        Integer abono = anticipo != null ? anticipo : 0;
        return Math.max(0, costoTotal - abono);
    }
}