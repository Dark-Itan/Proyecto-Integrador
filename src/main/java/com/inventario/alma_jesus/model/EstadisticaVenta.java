package com.inventario.alma_jesus.model;

import java.math.BigDecimal;

/**
 * Modelo que representa estadísticas de ventas por período.
 * <p>
 * Esta clase se utiliza para almacenar y transportar datos estadísticos
 * de ventas agrupadas por período (día, semana, mes, año), incluyendo
 * tanto la cantidad de ventas como el monto total generado.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class EstadisticaVenta {
    /**
     * Período al que corresponden las estadísticas (ej: "2024-11", "Semana 45", "Lunes").
     */
    private String periodo;

    /**
     * Cantidad total de ventas realizadas en el período.
     */
    private BigDecimal cantidad;

    /**
     * Monto total generado por las ventas en el período.
     */
    private BigDecimal monto;

    /**
     * Constructor por defecto.
     * Inicializa un objeto EstadisticaVenta con valores nulos.
     */
    public EstadisticaVenta() {}

    /**
     * Obtiene el período de las estadísticas.
     *
     * @return El período al que corresponden las estadísticas
     */
    public String getPeriodo() { return periodo; }

    /**
     * Establece el período de las estadísticas.
     *
     * @param periodo El período a asignar (ej: "2024-11", "Semana 45")
     */
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    /**
     * Obtiene la cantidad total de ventas.
     *
     * @return La cantidad de ventas en el período
     */
    public BigDecimal getCantidad() { return cantidad; }

    /**
     * Establece la cantidad total de ventas.
     *
     * @param cantidad La cantidad de ventas a asignar
     */
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

    /**
     * Obtiene el monto total generado.
     *
     * @return El monto total en el período
     */
    public BigDecimal getMonto() { return monto; }

    /**
     * Establece el monto total generado.
     *
     * @param monto El monto total a asignar
     */
    public void setMonto(BigDecimal monto) { this.monto = monto; }
}