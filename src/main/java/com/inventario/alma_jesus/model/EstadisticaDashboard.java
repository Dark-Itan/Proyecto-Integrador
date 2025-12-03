package com.inventario.alma_jesus.model;

import java.math.BigDecimal;

/**
 * Modelo que representa las estadísticas del dashboard del sistema.
 * <p>
 * Esta clase contiene los datos estadísticos principales que se muestran
 * en el dashboard administrativo para la toma de decisiones.
 * Todos los valores monetarios usan {@link BigDecimal} para garantizar
 * precisión en cálculos financieros.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class EstadisticaDashboard {
    private BigDecimal totalVentas;
    private Integer totalReparaciones;
    private BigDecimal materialesUtilizados;
    private Integer clientesActivos;
    private Integer pedidosPendientes;

    /**
     * Constructor por defecto.
     * <p>
     * Inicializa un objeto vacío de estadísticas del dashboard.
     * Los valores deberán establecerse mediante los setters correspondientes.
     * </p>
     */
    public EstadisticaDashboard() {}

    /**
     * Obtiene el total de ventas del período.
     *
     * @return Monto total de ventas en formato BigDecimal para precisión financiera
     */
    public BigDecimal getTotalVentas() {
        return totalVentas;
    }

    /**
     * Establece el total de ventas del período.
     *
     * @param totalVentas Monto total de ventas, debe usar BigDecimal para precisión
     */
    public void setTotalVentas(BigDecimal totalVentas) {
        this.totalVentas = totalVentas;
    }

    /**
     * Obtiene el número total de reparaciones realizadas.
     *
     * @return Cantidad de reparaciones completadas en el período
     */
    public Integer getTotalReparaciones() {
        return totalReparaciones;
    }

    /**
     * Establece el número total de reparaciones realizadas.
     *
     * @param totalReparaciones Cantidad de reparaciones completadas
     */
    public void setTotalReparaciones(Integer totalReparaciones) {
        this.totalReparaciones = totalReparaciones;
    }

    /**
     * Obtiene el valor total de materiales utilizados en producción.
     *
     * @return Valor de materiales utilizados en formato BigDecimal
     */
    public BigDecimal getMaterialesUtilizados() {
        return materialesUtilizados;
    }

    /**
     * Establece el valor total de materiales utilizados en producción.
     *
     * @param materialesUtilizados Valor de materiales consumidos
     */
    public void setMaterialesUtilizados(BigDecimal materialesUtilizados) {
        this.materialesUtilizados = materialesUtilizados;
    }

    /**
     * Obtiene el número de clientes activos en el sistema.
     * <p>
     * Se considera cliente activo aquel que ha realizado al menos
     * una transacción en los últimos 30 días.
     * </p>
     *
     * @return Cantidad de clientes activos
     */
    public Integer getClientesActivos() {
        return clientesActivos;
    }

    /**
     * Establece el número de clientes activos en el sistema.
     *
     * @param clientesActivos Cantidad de clientes activos
     */
    public void setClientesActivos(Integer clientesActivos) {
        this.clientesActivos = clientesActivos;
    }

    /**
     * Obtiene el número de pedidos pendientes de atención.
     * <p>
     * Incluye pedidos en estados: PENDIENTE, EN_PROCESO, y ESPERA_DE_MATERIALES.
     * </p>
     *
     * @return Cantidad de pedidos pendientes
     */
    public Integer getPedidosPendientes() {
        return pedidosPendientes;
    }

    /**
     * Establece el número de pedidos pendientes de atención.
     *
     * @param pedidosPendientes Cantidad de pedidos pendientes
     */
    public void setPedidosPendientes(Integer pedidosPendientes) {
        this.pedidosPendientes = pedidosPendientes;
    }
}