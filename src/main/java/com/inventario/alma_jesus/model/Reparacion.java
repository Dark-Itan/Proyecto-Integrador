package com.inventario.alma_jesus.model;

/**
 * Modelo que representa una reparación en el sistema.
 * <p>
 * Esta clase gestiona toda la información relacionada con las reparaciones
 * de clientes, incluyendo datos del cliente, detalles del artículo, costos,
 * fechas y seguimiento del estado del trabajo.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class Reparacion {
    /**
     * Identificador único de la reparación.
     */
    private Long id;

    /**
     * ID del cliente registrado en el sistema (opcional).
     */
    private Long clienteId;

    /**
     * Nombre completo del cliente que solicita la reparación.
     */
    private String nombreCliente;

    /**
     * Información de contacto del cliente (teléfono, email).
     */
    private String contacto;

    /**
     * Modelo o tipo del artículo a reparar.
     */
    private String modelo;

    /**
     * Material original del artículo.
     */
    private String materialOriginal;

    /**
     * Condición actual del artículo al momento de ingreso.
     */
    private String condicion;

    /**
     * Costo total estimado de la reparación.
     */
    private Integer costoTotal;

    /**
     * Anticipo o pago inicial recibido.
     */
    private Integer anticipo;

    /**
     * Fecha en que el artículo ingresó al taller (formato: YYYY-MM-DD).
     */
    private String fechaIngreso;

    /**
     * Fecha estimada o acordada para la entrega (formato: YYYY-MM-DD).
     */
    private String fechaEntrega;

    /**
     * Estado actual de la reparación (Pendiente, En Proceso, Completada, Entregada, Cancelada).
     */
    private String estado;

    /**
     * Notas adicionales sobre la reparación.
     */
    private String notas;

    /**
     * URL o ruta de la imagen del artículo.
     */
    private String imagenUrl;

    /**
     * URL o ruta del recibo generado.
     */
    private String reciboUrl;

    /**
     * ID del usuario que creó el registro de reparación.
     */
    private String creadoPor;

    /**
     * Indica si la reparación está activa en el sistema.
     */
    private Boolean activo;

    /**
     * Fecha y hora de registro en el sistema.
     */
    private String fechaRegistro;

    /**
     * Constructor por defecto.
     * Crea una instancia de Reparacion sin inicializar atributos.
     */
    public Reparacion() {}

    /**
     * Constructor para creación rápida de reparaciones.
     *
     * @param nombreCliente Nombre del cliente
     * @param contacto Información de contacto
     * @param modelo Modelo del artículo
     * @param materialOriginal Material original
     * @param condicion Condición actual
     * @param costoTotal Costo estimado
     * @param anticipo Pago inicial
     * @param fechaEntrega Fecha de entrega estimada
     * @param notas Notas adicionales
     */
    public Reparacion(String nombreCliente, String contacto, String modelo,
                      String materialOriginal, String condicion,
                      Integer costoTotal, Integer anticipo, String fechaEntrega, String notas) {
        this.nombreCliente = nombreCliente;
        this.contacto = contacto;
        this.modelo = modelo;
        this.materialOriginal = materialOriginal;
        this.condicion = condicion;
        this.costoTotal = costoTotal;
        this.anticipo = anticipo;
        this.fechaEntrega = fechaEntrega;
        this.notas = notas;
        this.estado = "Pendiente";
        this.activo = true;
    }

    /**
     * Obtiene el identificador único de la reparación.
     *
     * @return El ID de la reparación
     */
    public Long getId() { return id; }

    /**
     * Establece el identificador único de la reparación.
     *
     * @param id El ID a asignar
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Obtiene el ID del cliente registrado.
     *
     * @return El ID del cliente
     */
    public Long getClienteId() { return clienteId; }

    /**
     * Establece el ID del cliente registrado.
     *
     * @param clienteId El ID del cliente a asignar
     */
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    /**
     * Obtiene el nombre completo del cliente.
     *
     * @return El nombre del cliente
     */
    public String getNombreCliente() { return nombreCliente; }

    /**
     * Establece el nombre completo del cliente.
     *
     * @param nombreCliente El nombre del cliente a asignar
     */
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    /**
     * Obtiene la información de contacto del cliente.
     *
     * @return La información de contacto
     */
    public String getContacto() { return contacto; }

    /**
     * Establece la información de contacto del cliente.
     *
     * @param contacto La información de contacto a asignar
     */
    public void setContacto(String contacto) { this.contacto = contacto; }

    /**
     * Obtiene el modelo o tipo del artículo.
     *
     * @return El modelo del artículo
     */
    public String getModelo() { return modelo; }

    /**
     * Establece el modelo o tipo del artículo.
     *
     * @param modelo El modelo del artículo a asignar
     */
    public void setModelo(String modelo) { this.modelo = modelo; }

    /**
     * Obtiene el material original del artículo.
     *
     * @return El material original
     */
    public String getMaterialOriginal() { return materialOriginal; }

    /**
     * Establece el material original del artículo.
     *
     * @param materialOriginal El material original a asignar
     */
    public void setMaterialOriginal(String materialOriginal) { this.materialOriginal = materialOriginal; }

    /**
     * Obtiene la condición actual del artículo.
     *
     * @return La condición del artículo
     */
    public String getCondicion() { return condicion; }

    /**
     * Establece la condición actual del artículo.
     *
     * @param condicion La condición del artículo a asignar
     */
    public void setCondicion(String condicion) { this.condicion = condicion; }

    /**
     * Obtiene el costo total estimado de la reparación.
     *
     * @return El costo total
     */
    public Integer getCostoTotal() { return costoTotal; }

    /**
     * Establece el costo total estimado de la reparación.
     *
     * @param costoTotal El costo total a asignar
     */
    public void setCostoTotal(Integer costoTotal) { this.costoTotal = costoTotal; }

    /**
     * Obtiene el anticipo o pago inicial recibido.
     *
     * @return El anticipo recibido
     */
    public Integer getAnticipo() { return anticipo; }

    /**
     * Establece el anticipo o pago inicial recibido.
     *
     * @param anticipo El anticipo a asignar
     */
    public void setAnticipo(Integer anticipo) { this.anticipo = anticipo; }

    /**
     * Obtiene la fecha de ingreso al taller.
     *
     * @return La fecha de ingreso (formato: YYYY-MM-DD)
     */
    public String getFechaIngreso() { return fechaIngreso; }

    /**
     * Establece la fecha de ingreso al taller.
     *
     * @param fechaIngreso La fecha de ingreso a asignar (formato: YYYY-MM-DD)
     */
    public void setFechaIngreso(String fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    /**
     * Obtiene la fecha estimada de entrega.
     *
     * @return La fecha de entrega (formato: YYYY-MM-DD)
     */
    public String getFechaEntrega() { return fechaEntrega; }

    /**
     * Establece la fecha estimada de entrega.
     *
     * @param fechaEntrega La fecha de entrega a asignar (formato: YYYY-MM-DD)
     */
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    /**
     * Obtiene el estado actual de la reparación.
     *
     * @return El estado de la reparación
     */
    public String getEstado() { return estado; }

    /**
     * Establece el estado actual de la reparación.
     *
     * @param estado El estado a asignar
     */
    public void setEstado(String estado) { this.estado = estado; }

    /**
     * Obtiene las notas adicionales sobre la reparación.
     *
     * @return Las notas de la reparación
     */
    public String getNotas() { return notas; }

    /**
     * Establece las notas adicionales sobre la reparación.
     *
     * @param notas Las notas a asignar
     */
    public void setNotas(String notas) { this.notas = notas; }

    /**
     * Obtiene la URL de la imagen del artículo.
     *
     * @return La URL de la imagen
     */
    public String getImagenUrl() { return imagenUrl; }

    /**
     * Establece la URL de la imagen del artículo.
     *
     * @param imagenUrl La URL de la imagen a asignar
     */
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    /**
     * Obtiene la URL del recibo generado.
     *
     * @return La URL del recibo
     */
    public String getReciboUrl() { return reciboUrl; }

    /**
     * Establece la URL del recibo generado.
     *
     * @param reciboUrl La URL del recibo a asignar
     */
    public void setReciboUrl(String reciboUrl) { this.reciboUrl = reciboUrl; }

    /**
     * Obtiene el usuario creador de la reparación.
     *
     * @return El ID del usuario creador
     */
    public String getCreadoPor() { return creadoPor; }

    /**
     * Establece el usuario creador de la reparación.
     *
     * @param creadoPor El ID del usuario creador a asignar
     */
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }

    /**
     * Verifica si la reparación está activa.
     *
     * @return true si está activa, false en caso contrario
     */
    public Boolean getActivo() { return activo; }

    /**
     * Establece el estado de actividad de la reparación.
     *
     * @param activo El estado de actividad a asignar
     */
    public void setActivo(Boolean activo) { this.activo = activo; }

    /**
     * Obtiene la fecha de registro en el sistema.
     *
     * @return La fecha de registro
     */
    public String getFechaRegistro() { return fechaRegistro; }

    /**
     * Establece la fecha de registro en el sistema.
     *
     * @param fechaRegistro La fecha de registro a asignar
     */
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    /**
     * Calcula el saldo pendiente de pago.
     * <p>
     * Resta el anticipo del costo total para obtener el saldo que aún debe pagar el cliente.
     * </p>
     *
     * @return El saldo pendiente (nunca negativo)
     */
    public Integer getSaldoPendiente() {
        if (costoTotal == null) return 0;
        Integer abono = anticipo != null ? anticipo : 0;
        return Math.max(0, costoTotal - abono);
    }
}