package com.inventario.alma_jesus.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Modelo que representa un recetario o instrucciones de fabricación.
 * <p>
 * Esta clase almacena las especificaciones técnicas, materiales y procedimientos
 * necesarios para fabricar un producto de manera estandarizada.
 * Sirve como guía de producción para garantizar calidad y consistencia.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
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

    /**
     * Constructor por defecto.
     * <p>
     * Crea una instancia vacía de recetario.
     * Los valores deben establecerse mediante los setters correspondientes.
     * </p>
     */
    public Recetario() {}

    /**
     * Constructor principal para crear un recetario.
     *
     * @param id Identificador único del recetario
     * @param productoId ID del producto asociado a este recetario
     * @param tiempoFabricacion Tiempo estimado para fabricar el producto
     * @param instrucciones Pasos detallados del proceso de fabricación
     * @param notas Observaciones o consideraciones especiales
     * @param herramientas Herramientas requeridas para la fabricación
     * @param creadoPor ID del usuario que creó el recetario
     * @param fechaCreacion Fecha y hora de creación del recetario
     */
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

    /**
     * Obtiene el identificador único del recetario.
     *
     * @return ID del recetario
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del recetario.
     *
     * @param id ID único del recetario
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el ID del producto asociado a este recetario.
     *
     * @return ID del producto
     */
    public Long getProductoId() {
        return productoId;
    }

    /**
     * Establece el ID del producto asociado a este recetario.
     *
     * @param productoId ID del producto
     */
    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    /**
     * Obtiene el tiempo estimado de fabricación.
     * <p>
     * Formato común: "X horas Y minutos" o "D días H horas"
     * Ejemplos: "2 horas 30 minutos", "1 día 4 horas"
     * </p>
     *
     * @return Tiempo de fabricación estimado
     */
    public String getTiempoFabricacion() {
        return tiempoFabricacion;
    }

    /**
     * Establece el tiempo estimado de fabricación.
     *
     * @param tiempoFabricacion Tiempo estimado en formato legible
     */
    public void setTiempoFabricacion(String tiempoFabricacion) {
        this.tiempoFabricacion = tiempoFabricacion;
    }

    /**
     * Obtiene las instrucciones detalladas de fabricación.
     * <p>
     * Contiene pasos secuenciales con detalles técnicos,
     * medidas precisas y puntos de verificación de calidad.
     * </p>
     *
     * @return Instrucciones de fabricación
     */
    public String getInstrucciones() {
        return instrucciones;
    }

    /**
     * Establece las instrucciones detalladas de fabricación.
     *
     * @param instrucciones Pasos y procedimientos
     */
    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    /**
     * Obtiene las notas u observaciones adicionales.
     * <p>
     * Incluye información como precauciones de seguridad,
     * variantes permitidas, o consejos para obtener mejores resultados.
     * </p>
     *
     * @return Notas adicionales
     */
    public String getNotas() {
        return notas;
    }

    /**
     * Establece las notas u observaciones adicionales.
     *
     * @param notas Observaciones y consideraciones
     */
    public void setNotas(String notas) {
        this.notas = notas;
    }

    /**
     * Obtiene la lista de herramientas requeridas.
     * <p>
     * Puede ser una lista separada por comas o un texto descriptivo
     * de las herramientas necesarias para cada paso.
     * </p>
     *
     * @return Herramientas requeridas
     */
    public String getHerramientas() {
        return herramientas;
    }

    /**
     * Establece la lista de herramientas requeridas.
     *
     * @param herramientas Lista de herramientas
     */
    public void setHerramientas(String herramientas) {
        this.herramientas = herramientas;
    }

    /**
     * Obtiene el ID del usuario que creó el recetario.
     * <p>
     * Generalmente un diseñador, ingeniero o supervisor de producción.
     * </p>
     *
     * @return ID del usuario creador
     */
    public String getCreadoPor() {
        return creadoPor;
    }

    /**
     * Establece el ID del usuario que creó el recetario.
     *
     * @param creadoPor ID del usuario
     */
    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    /**
     * Obtiene la fecha y hora de creación del recetario.
     *
     * @return Fecha de creación
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece la fecha y hora de creación del recetario.
     *
     * @param fechaCreacion Fecha de creación
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Obtiene la lista de materiales requeridos para la receta.
     * <p>
     * Cada {@link RecetaMaterial} especifica el tipo de material,
     * cantidad exacta y unidad de medida necesaria.
     * </p>
     *
     * @return Lista de materiales de la receta
     */
    public List<RecetaMaterial> getMateriales() {
        return materiales;
    }

    /**
     * Establece la lista de materiales requeridos para la receta.
     *
     * @param materiales Lista de materiales
     */
    public void setMateriales(List<RecetaMaterial> materiales) {
        this.materiales = materiales;
    }
}