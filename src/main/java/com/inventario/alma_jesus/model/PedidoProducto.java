package com.inventario.alma_jesus.model;

import java.math.BigDecimal;

/**
 * Modelo que representa un producto dentro de un pedido.
 * <p>
 * Esta clase establece la relación entre pedidos y productos, almacenando
 * información sobre los productos incluidos en cada pedido, incluyendo
 * cantidades, precios y cálculos de subtotales.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class PedidoProducto {
    /**
     * Identificador único del registro pedido-producto.
     */
    private Long id;

    /**
     * ID del pedido al que pertenece este producto.
     */
    private Long pedidoId;

    /**
     * ID del producto incluido en el pedido.
     */
    private Long productoId;

    /**
     * Nombre del producto en el momento de crear el pedido.
     */
    private String productoNombre;

    /**
     * Cantidad del producto solicitada en el pedido.
     */
    private Integer cantidad;

    /**
     * Precio unitario del producto en el momento del pedido.
     */
    private BigDecimal precioUnitario;

    /**
     * Subtotal calculado para este producto (cantidad × precio unitario).
     */
    private BigDecimal subtotal;

    /**
     * Constructor por defecto.
     * Crea una instancia de PedidoProducto sin inicializar atributos.
     */
    public PedidoProducto() {}

    /**
     * Constructor completo con todos los atributos.
     *
     * @param id Identificador único
     * @param pedidoId ID del pedido
     * @param productoId ID del producto
     * @param productoNombre Nombre del producto
     * @param cantidad Cantidad solicitada
     * @param precioUnitario Precio unitario
     * @param subtotal Subtotal calculado
     */
    public PedidoProducto(Long id, Long pedidoId, Long productoId, String productoNombre,
                          Integer cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    /**
     * Obtiene el identificador único del registro.
     *
     * @return El ID del registro pedido-producto
     */
    public Long getId() { return id; }

    /**
     * Establece el identificador único del registro.
     *
     * @param id El ID a asignar
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Obtiene el ID del pedido relacionado.
     *
     * @return El ID del pedido
     */
    public Long getPedidoId() { return pedidoId; }

    /**
     * Establece el ID del pedido relacionado.
     *
     * @param pedidoId El ID del pedido a asignar
     */
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    /**
     * Obtiene el ID del producto incluido.
     *
     * @return El ID del producto
     */
    public Long getProductoId() { return productoId; }

    /**
     * Establece el ID del producto incluido.
     *
     * @param productoId El ID del producto a asignar
     */
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    /**
     * Obtiene el nombre del producto en el pedido.
     *
     * @return El nombre del producto
     */
    public String getProductoNombre() { return productoNombre; }

    /**
     * Establece el nombre del producto en el pedido.
     *
     * @param productoNombre El nombre del producto a asignar
     */
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    /**
     * Obtiene la cantidad del producto solicitada.
     *
     * @return La cantidad solicitada
     */
    public Integer getCantidad() { return cantidad; }

    /**
     * Establece la cantidad del producto solicitada.
     *
     * @param cantidad La cantidad a asignar
     */
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    /**
     * Obtiene el precio unitario del producto.
     *
     * @return El precio unitario
     */
    public BigDecimal getPrecioUnitario() { return precioUnitario; }

    /**
     * Establece el precio unitario del producto.
     *
     * @param precioUnitario El precio unitario a asignar
     */
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    /**
     * Obtiene el subtotal calculado para este producto.
     *
     * @return El subtotal (cantidad × precio unitario)
     */
    public BigDecimal getSubtotal() { return subtotal; }

    /**
     * Establece el subtotal calculado para este producto.
     *
     * @param subtotal El subtotal a asignar
     */
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}