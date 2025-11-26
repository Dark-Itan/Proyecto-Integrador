package com.inventario.alma_jesus.model;

public class EstadisticaReparacion {
    private String periodo;
    private Integer cantidadReparaciones;
    private String tipoReparacion;

    public EstadisticaReparacion() {}

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
    public Integer getCantidadReparaciones() { return cantidadReparaciones; }
    public void setCantidadReparaciones(Integer cantidadReparaciones) { this.cantidadReparaciones = cantidadReparaciones; }
    public String getTipoReparacion() { return tipoReparacion; }
    public void setTipoReparacion(String tipoReparacion) { this.tipoReparacion = tipoReparacion; }
}