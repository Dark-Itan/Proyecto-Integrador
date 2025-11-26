package com.inventario.alma_jesus.model;

import java.math.BigDecimal;

public class EstadisticaMaterial {
    private String periodo;
    private String material;
    private BigDecimal cantidadConsumida;

    public EstadisticaMaterial() {}

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    public BigDecimal getCantidadConsumida() { return cantidadConsumida; }
    public void setCantidadConsumida(BigDecimal cantidadConsumida) { this.cantidadConsumida = cantidadConsumida; }
}