package com.inventario.alma_jesus.model;

import java.math.BigDecimal;

public class EstadisticaDashboard {
    private BigDecimal totalVentas;
    private Integer totalReparaciones;
    private BigDecimal materialesUtilizados;
    private Integer clientesActivos;
    private Integer pedidosPendientes;

    public EstadisticaDashboard() {}

    public BigDecimal getTotalVentas() { return totalVentas; }
    public void setTotalVentas(BigDecimal totalVentas) { this.totalVentas = totalVentas; }
    public Integer getTotalReparaciones() { return totalReparaciones; }
    public void setTotalReparaciones(Integer totalReparaciones) { this.totalReparaciones = totalReparaciones; }
    public BigDecimal getMaterialesUtilizados() { return materialesUtilizados; }
    public void setMaterialesUtilizados(BigDecimal materialesUtilizados) { this.materialesUtilizados = materialesUtilizados; }
    public Integer getClientesActivos() { return clientesActivos; }
    public void setClientesActivos(Integer clientesActivos) { this.clientesActivos = clientesActivos; }
    public Integer getPedidosPendientes() { return pedidosPendientes; }
    public void setPedidosPendientes(Integer pedidosPendientes) { this.pedidosPendientes = pedidosPendientes; }
}