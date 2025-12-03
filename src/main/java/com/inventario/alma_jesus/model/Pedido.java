package com.inventario.alma_jesus.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Modelo que representa un pedido de fabricación en el sistema.
 * <p>
 * Esta clase gestiona pedidos de clientes para productos personalizados o estándar,
 * incluyendo seguimiento de etapas de producción, pagos y detalles del cliente.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
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

    /**
     * Constructor por defecto.
     * <p>
     * Crea una instancia vacía de pedido.
     * Los valores deben establecerse mediante los setters correspondientes.
     * </p>
     */
    public Pedido() {}

    /**
     * Constructor principal para crear un pedido.
     * <p>
     * Este constructor excluye las fechas de creación/actualización
     * y la lista de productos que se gestionan por separado.
     * </p>
     *
     * @param id Identificador único del pedido
     * @param clienteNombre Nombre completo del cliente
     * @param clienteContacto Teléfono o email de contacto del cliente
     * @param fechaEntrega Fecha estimada de entrega en formato "YYYY-MM-DD"
     * @param notas Notas adicionales o especificaciones del pedido
     * @param etapa Etapa actual del proceso de producción
     * @param total Monto total del pedido
     * @param anticipo Anticipo o pago inicial recibido
     * @param totalCantidad Cantidad total de productos en el pedido
     * @param resumenProducto Descripción resumida de los productos solicitados
     * @param creadoPor ID del usuario que creó el pedido
     */
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

    /**
     * Obtiene el identificador único del pedido.
     *
     * @return ID del pedido
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del pedido.
     *
     * @param id ID único del pedido
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre completo del cliente.
     *
     * @return Nombre del cliente
     */
    public String getClienteNombre() {
        return clienteNombre;
    }

    /**
     * Establece el nombre completo del cliente.
     *
     * @param clienteNombre Nombre del cliente
     */
    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    /**
     * Obtiene la información de contacto del cliente.
     * <p>
     * Puede ser teléfono, email o ambos, según la preferencia del cliente.
     * </p>
     *
     * @return Información de contacto
     */
    public String getClienteContacto() {
        return clienteContacto;
    }

    /**
     * Establece la información de contacto del cliente.
     *
     * @param clienteContacto Teléfono o email del cliente
     */
    public void setClienteContacto(String clienteContacto) {
        this.clienteContacto = clienteContacto;
    }

    /**
     * Obtiene la fecha estimada de entrega.
     * <p>
     * Formato esperado: "YYYY-MM-DD"
     * </p>
     *
     * @return Fecha de entrega estimada
     */
    public String getFechaEntrega() {
        return fechaEntrega;
    }

    /**
     * Establece la fecha estimada de entrega.
     *
     * @param fechaEntrega Fecha en formato "YYYY-MM-DD"
     */
    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    /**
     * Obtiene las notas o especificaciones adicionales del pedido.
     * <p>
     * Incluye detalles como preferencias del cliente, colores, medidas
     * personalizadas, o instrucciones especiales de fabricación.
     * </p>
     *
     * @return Notas del pedido
     */
    public String getNotas() {
        return notas;
    }

    /**
     * Establece las notas o especificaciones adicionales del pedido.
     *
     * @param notas Notas y especificaciones
     */
    public void setNotas(String notas) {
        this.notas = notas;
    }

    /**
     * Obtiene la etapa actual del proceso de producción.
     * <p>
     * Etapas posibles:
     * - "PENDIENTE": Pedido recibido, sin iniciar producción
     * - "DISEÑO": En fase de diseño o planos
     * - "CORTE": En proceso de corte de materiales
     * - "ENSAMBLAJE": En ensamblaje de piezas
     * - "ACABADO": Aplicación de acabados (barniz, pintura)
     * - "INSTALACION": Instalación en sitio del cliente
     * - "FINALIZADO": Pedido completado y entregado
     * - "CANCELADO": Pedido cancelado por el cliente
     * </p>
     *
     * @return Etapa actual del pedido
     */
    public String getEtapa() {
        return etapa;
    }

    /**
     * Establece la etapa actual del proceso de producción.
     *
     * @param etapa Etapa del pedido
     */
    public void setEtapa(String etapa) {
        this.etapa = etapa;
    }

    /**
     * Obtiene el monto total del pedido.
     * <p>
     * Usa {@link BigDecimal} para precisión en cálculos monetarios.
     * </p>
     *
     * @return Monto total del pedido
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * Establece el monto total del pedido.
     *
     * @param total Monto total
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * Obtiene el anticipo o pago inicial recibido.
     * <p>
     * Generalmente se requiere para iniciar la producción.
     * </p>
     *
     * @return Monto del anticipo
     */
    public BigDecimal getAnticipo() {
        return anticipo;
    }

    /**
     * Establece el anticipo o pago inicial recibido.
     *
     * @param anticipo Monto del anticipo
     */
    public void setAnticipo(BigDecimal anticipo) {
        this.anticipo = anticipo;
    }

    /**
     * Obtiene la cantidad total de productos en el pedido.
     * <p>
     * Suma de todas las cantidades de productos individuales.
     * </p>
     *
     * @return Cantidad total de productos
     */
    public Integer getTotalCantidad() {
        return totalCantidad;
    }

    /**
     * Establece la cantidad total de productos en el pedido.
     *
     * @param totalCantidad Cantidad total
     */
    public void setTotalCantidad(Integer totalCantidad) {
        this.totalCantidad = totalCantidad;
    }

    /**
     * Obtiene un resumen descriptivo de los productos solicitados.
     * <p>
     * Texto breve que describe el pedido, útil para vistas rápidas
     * sin cargar la lista completa de productos.
     * </p>
     *
     * @return Resumen de productos
     */
    public String getResumenProducto() {
        return resumenProducto;
    }

    /**
     * Establece un resumen descriptivo de los productos solicitados.
     *
     * @param resumenProducto Descripción resumida
     */
    public void setResumenProducto(String resumenProducto) {
        this.resumenProducto = resumenProducto;
    }

    /**
     * Obtiene el ID del usuario que creó el pedido.
     *
     * @return ID del usuario creador
     */
    public String getCreadoPor() {
        return creadoPor;
    }

    /**
     * Establece el ID del usuario que creó el pedido.
     *
     * @param creadoPor ID del usuario
     */
    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    /**
     * Obtiene la fecha y hora de creación del pedido.
     *
     * @return Fecha de creación
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece la fecha y hora de creación del pedido.
     * <p>
     * Normalmente se establece automáticamente por la base de datos.
     * </p>
     *
     * @param fechaCreacion Fecha de creación
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Obtiene la fecha y hora de la última actualización.
     *
     * @return Fecha de última actualización
     */
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    /**
     * Establece la fecha y hora de la última actualización.
     * <p>
     * Se actualiza automáticamente al modificar el pedido.
     * </p>
     *
     * @param fechaActualizacion Fecha de actualización
     */
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    /**
     * Obtiene la lista de productos incluidos en el pedido.
     * <p>
     * Cada elemento es un {@link PedidoProducto} con detalles
     * específicos de cantidad, precio y especificaciones.
     * </p>
     *
     * @return Lista de productos del pedido
     */
    public List<PedidoProducto> getProductos() {
        return productos;
    }

    /**
     * Establece la lista de productos incluidos en el pedido.
     *
     * @param productos Lista de productos
     */
    public void setProductos(List<PedidoProducto> productos) {
        this.productos = productos;
    }
}