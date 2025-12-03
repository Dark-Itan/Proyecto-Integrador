package com.inventario.alma_jesus.model;

/**
 * Modelo que representa estadísticas de reparaciones por período y tipo.
 * <p>
 * Esta clase se utiliza para generar reportes de volumen de reparaciones,
 * permitiendo analizar tendencias, demanda por tipo de servicio y
 * distribución de carga de trabajo en el taller.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class EstadisticaReparacion {
    private String periodo;
    private Integer cantidadReparaciones;
    private String tipoReparacion;

    /**
     * Constructor por defecto.
     * <p>
     * Crea una instancia vacía de estadísticas de reparación.
     * Los valores deben establecerse mediante los setters correspondientes.
     * </p>
     */
    public EstadisticaReparacion() {}

    /**
     * Obtiene el período de tiempo de la estadística.
     * <p>
     * Representa el intervalo de tiempo para el cual se agrupan las reparaciones.
     * Formatos comunes:
     * - "2024-01" para mensual
     * - "2024-W12" para semanal
     * - "2024-Q1" para trimestral
     * - "2024" para anual
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
     * @param periodo Período en formato de cadena (ej: "2024-01", "2024-Q2")
     */
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    /**
     * Obtiene la cantidad de reparaciones realizadas en el período.
     * <p>
     * Representa el volumen total de reparaciones completadas
     * para el período y tipo especificado.
     * </p>
     *
     * @return Número de reparaciones realizadas
     */
    public Integer getCantidadReparaciones() {
        return cantidadReparaciones;
    }

    /**
     * Establece la cantidad de reparaciones realizadas en el período.
     *
     * @param cantidadReparaciones Número de reparaciones completadas
     */
    public void setCantidadReparaciones(Integer cantidadReparaciones) {
        this.cantidadReparaciones = cantidadReparaciones;
    }

    /**
     * Obtiene el tipo o categoría de reparación.
     * <p>
     * Clasifica las reparaciones según su naturaleza o complejidad.
     * Ejemplos comunes:
     * - "ELECTRÓNICA"
     * - "MECÁNICA"
     * - "CARPINTERÍA"
     * - "URGENTE"
     * - "GARANTÍA"
     * </p>
     *
     * @return Tipo o categoría de reparación
     */
    public String getTipoReparacion() {
        return tipoReparacion;
    }

    /**
     * Establece el tipo o categoría de reparación.
     *
     * @param tipoReparacion Categoría de la reparación
     */
    public void setTipoReparacion(String tipoReparacion) {
        this.tipoReparacion = tipoReparacion;
    }
}