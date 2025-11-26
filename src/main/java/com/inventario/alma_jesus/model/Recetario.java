package com.inventario.alma_jesus.model;

import java.time.LocalDateTime;
import java.util.List;

public class Recetario {
    private Long id;
    private Long productoId;
    private String tiempoFabricacion;
    private String instrucciones;
    private String notas;
    private String herramientas;
    private String creadoPor;
    private LocalDateTime fechaCreacion;
    private List<RecetaMaterial> materiales;

    // Constructores
    public Recetario() {}

    public Recetario(Long id, Long productoId, String tiempoFabricacion, String instrucciones,
                     String notas, String herramientas, String creadoPor, LocalDateTime fechaCreacion) {
        this.id = id;
        this.productoId = productoId;
        this.tiempoFabricacion = tiempoFabricacion;
        this.instrucciones = instrucciones;
        this.notas = notas;
        this.herramientas = herramientas;
        this.creadoPor = creadoPor;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public String getTiempoFabricacion() { return tiempoFabricacion; }
    public void setTiempoFabricacion(String tiempoFabricacion) { this.tiempoFabricacion = tiempoFabricacion; }

    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getHerramientas() { return herramientas; }
    public void setHerramientas(String herramientas) { this.herramientas = herramientas; }

    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public List<RecetaMaterial> getMateriales() { return materiales; }
    public void setMateriales(List<RecetaMaterial> materiales) { this.materiales = materiales; }
}