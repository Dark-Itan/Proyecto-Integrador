package com.inventario.alma_jesus.model;

/**
 * Modelo que representa una transacción de venta en el sistema.
 * <p>
 * Esta clase gestiona las ventas de productos terminados a clientes,
 * incluyendo cálculo automático de totales y registro de información
 * de transacción. Los montos se manejan en centavos para evitar
 * problemas de precisión con decimales.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
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

    /**
     * Constructor por defecto.
     * <p>
     * Crea una instancia vacía de venta.
     * Los valores deben establecerse mediante los setters correspondientes.
     * </p>
     */
    public Venta() {}

    /**
     * Constructor principal para crear una nueva venta.
     * <p>
     * Calcula automáticamente el precioTotal basado en cantidad y precioUnitario.
     * </p>
     *
     * @param clienteId Identificador del cliente que realiza la compra
     * @param productoId Identificador del producto vendido
     * @param cantidad Cantidad de unidades vendidas
     * @param precioUnitario Precio por unidad en centavos
     * @param fecha Fecha de la venta en formato "YYYY-MM-DD"
     * @param tipo Tipo de transacción o forma de pago
     * @param usuarioRegistro ID del usuario que registra la venta
     */
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

    /**
     * Obtiene el identificador único de la venta.
     *
     * @return ID de la venta
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único de la venta.
     *
     * @param id ID único de la venta
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el identificador del cliente.
     *
     * @return ID del cliente
     */
    public int getClienteId() {
        return clienteId;
    }

    /**
     * Establece el identificador del cliente.
     *
     * @param clienteId ID del cliente
     */
    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    /**
     * Obtiene el identificador del producto vendido.
     *
     * @return ID del producto
     */
    public int getProductoId() {
        return productoId;
    }

    /**
     * Establece el identificador del producto vendido.
     *
     * @param productoId ID del producto
     */
    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    /**
     * Obtiene el nombre del modelo del producto.
     * <p>
     * Campo de conveniencia para mostrar información
     * sin necesidad de consultar la tabla de productos.
     * </p>
     *
     * @return Nombre del modelo del producto
     */
    public String getProductoModelo() {
        return productoModelo;
    }

    /**
     * Establece el nombre del modelo del producto.
     *
     * @param productoModelo Nombre del modelo
     */
    public void setProductoModelo(String productoModelo) {
        this.productoModelo = productoModelo;
    }

    /**
     * Obtiene la cantidad de unidades vendidas.
     *
     * @return Cantidad vendida
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad de unidades vendidas.
     * <p>
     * Recalcula automáticamente el precioTotal.
     * </p>
     *
     * @param cantidad Cantidad vendida
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.precioTotal = cantidad * precioUnitario; // Recalcular al cambiar cantidad
    }

    /**
     * Obtiene el precio unitario del producto.
     * <p>
     * Representado en centavos para evitar problemas de precisión.
     * Ej: $150.50 se almacena como 15050
     * </p>
     *
     * @return Precio unitario en centavos
     */
    public int getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     * Establece el precio unitario del producto.
     * <p>
     * Recalcula automáticamente el precioTotal.
     * </p>
     *
     * @param precioUnitario Precio unitario en centavos
     */
    public void setPrecioUnitario(int precioUnitario) {
        this.precioUnitario = precioUnitario;
        this.precioTotal = cantidad * precioUnitario; // Recalcular al cambiar precio
    }

    /**
     * Obtiene el precio total de la venta.
     * <p>
     * Calculado como: cantidad × precioUnitario.
     * También representado en centavos.
     * </p>
     *
     * @return Precio total en centavos
     */
    public int getPrecioTotal() {
        return precioTotal;
    }

    /**
     * Establece el precio total de la venta.
     * <p>
     * Usar con cuidado, ya que normalmente se calcula automáticamente.
     * Solo usar para cargar datos existentes.
     * </p>
     *
     * @param precioTotal Precio total en centavos
     */
    public void setPrecioTotal(int precioTotal) {
        this.precioTotal = precioTotal;
    }

    /**
     * Obtiene la fecha en que se realizó la venta.
     * <p>
     * Formato: "YYYY-MM-DD"
     * </p>
     *
     * @return Fecha de la venta
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha en que se realizó la venta.
     *
     * @param fecha Fecha en formato "YYYY-MM-DD"
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el tipo de transacción o forma de pago.
     * <p>
     * Ejemplos:
     * - "EFECTIVO"
     * - "TARJETA_CREDITO"
     * - "TARJETA_DEBITO"
     * - "TRANSFERENCIA"
     * - "CREDITO" (a cuenta)
     * </p>
     *
     * @return Tipo de transacción
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de transacción o forma de pago.
     *
     * @param tipo Tipo de transacción
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene el ID del usuario que registró la venta.
     *
     * @return ID del usuario registrador
     */
    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    /**
     * Establece el ID del usuario que registró la venta.
     *
     * @param usuarioRegistro ID del usuario
     */
    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    /**
     * Obtiene la fecha y hora del registro en el sistema.
     * <p>
     * Formato: "YYYY-MM-DD HH:MM:SS"
     * </p>
     *
     * @return Fecha y hora de registro
     */
    public String getFechaRegistro() {
        return fechaRegistro;
    }

    /**
     * Establece la fecha y hora del registro en el sistema.
     *
     * @param fechaRegistro Fecha en formato de cadena
     */
    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}