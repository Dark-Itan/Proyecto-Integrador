package com.inventario.alma_jesus.model;

import java.math.BigDecimal;

public class EstadisticaVenta {
    private String periodo;
    private BigDecimal cantidad;
    private BigDecimal monto;

    public EstadisticaVenta() {}

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
}