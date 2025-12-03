package com.inventario.alma_jesus.model;

import java.math.BigDecimal;

/**
 * Modelo que representa estadísticas de consumo de materiales por período.
 * <p>
 * Esta clase se utiliza para generar reportes de consumo de materiales
 * agrupados por período de tiempo (día, semana, mes) y por tipo de material.
 * Es útil para análisis de costos, previsión de compras y control de inventario.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class EstadisticaMaterial {
    private String periodo;
    private String material;
    private BigDecimal cantidadConsumida;

    /**
     * Constructor por defecto.
     * <p>
     * Crea una instancia vacía de estadísticas de material.
     * Los valores deben establecerse mediante los setters correspondientes.
     * </p>
     */
    public EstadisticaMaterial() {}

    /**
     * Obtiene el período de tiempo de la estadística.
     * <p>
     * El formato del período puede variar según el reporte:
     * - "YYYY-MM-DD" para reportes diarios
     * - "YYYY-WW" para reportes semanales
     * - "YYYY-MM" para reportes mensuales
     * </p>
     *
     * @return Período de tiempo de la estadística
     */
    public String getPeriodo() {
        return periodo;
    }

    /**
     * Establece el período de tiempo de la estadística.
     *
     * @param periodo Período en formato de cadena (ej: "2024-01", "2024-12-15")
     */
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    /**
     * Obtiene el nombre o descripción del material.
     * <p>
     * Puede incluir tanto el nombre genérico como especificaciones técnicas
     * según el nivel de detalle requerido en el reporte.
     * </p>
     *
     * @return Nombre o descripción del material
     */
    public String getMaterial() {
        return material;
    }

    /**
     * Establece el nombre o descripción del material.
     *
     * @param material Nombre del material (ej: "Madera de Roble", "Tornillos 3x50mm")
     */
    public void setMaterial(String material) {
        this.material = material;
    }

    /**
     * Obtiene la cantidad consumida del material en el período.
     * <p>
     * Usa {@link BigDecimal} para garantizar precisión en las cantidades,
     * especialmente importante para materiales medidos en unidades fraccionarias.
     * </p>
     *
     * @return Cantidad consumida del material
     */
    public BigDecimal getCantidadConsumida() {
        return cantidadConsumida;
    }

    /**
     * Establece la cantidad consumida del material en el período.
     *
     * @param cantidadConsumida Cantidad consumida, debe usar BigDecimal para precisión
     */
    public void setCantidadConsumida(BigDecimal cantidadConsumida) {
        this.cantidadConsumida = cantidadConsumida;
    }
}