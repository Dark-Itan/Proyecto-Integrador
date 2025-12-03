package com.inventario.alma_jesus.model;

import java.time.LocalDate;

/**
 * Modelo que representa una tarea asignada a un trabajador.
 * <p>
 * Esta clase gestiona las actividades de producción asignadas a los trabajadores
 * del taller, permitiendo seguimiento de progreso, fechas y estado de cada tarea.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class Tarea {
    private Long id;
    private String asunto;
    private String detalles;
    private LocalDate fechaAsignacion;
    private LocalDate fechaEntrega;
    private Integer cantidadFiguras;
    private String estado;
    private Boolean activo;
    private String creadoPor;
    private String fechaCreacion;
    private String trabajadorId;

    /**
     * Constructor por defecto.
     * <p>
     * Crea una instancia vacía de tarea.
     * Los valores deben establecerse mediante los setters correspondientes.
     * </p>
     */
    public Tarea() {}

    /**
     * Constructor principal para crear una tarea.
     *
     * @param id Identificador único de la tarea
     * @param asunto Título o descripción breve de la tarea
     * @param detalles Descripción detallada de la tarea
     * @param fechaAsignacion Fecha en que se asigna la tarea
     * @param fechaEntrega Fecha límite para completar la tarea
     * @param cantidadFiguras Cantidad de piezas o figuras a producir
     * @param estado Estado actual de la tarea
     * @param activo Indica si la tarea está activa en el sistema
     * @param creadoPor ID del usuario que creó la tarea
     * @param fechaCreacion Fecha de creación del registro
     * @param trabajadorId ID del trabajador asignado a la tarea
     */
    public Tarea(Long id, String asunto, String detalles, LocalDate fechaAsignacion,
                 LocalDate fechaEntrega, Integer cantidadFiguras, String estado,
                 Boolean activo, String creadoPor, String fechaCreacion, String trabajadorId) {
        this.id = id;
        this.asunto = asunto;
        this.detalles = detalles;
        this.fechaAsignacion = fechaAsignacion;
        this.fechaEntrega = fechaEntrega;
        this.cantidadFiguras = cantidadFiguras;
        this.estado = estado;
        this.activo = activo;
        this.creadoPor = creadoPor;
        this.fechaCreacion = fechaCreacion;
        this.trabajadorId = trabajadorId;
    }

    // Getters y Setters

    /**
     * Obtiene el identificador único de la tarea.
     *
     * @return ID de la tarea
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único de la tarea.
     *
     * @param id ID único de la tarea
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el título o asunto de la tarea.
     * <p>
     * Breve descripción que identifica la tarea.
     * Ejemplo: "Cortar piezas para mesa de centro"
     * </p>
     *
     * @return Asunto de la tarea
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Establece el título o asunto de la tarea.
     *
     * @param asunto Título o descripción breve
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /**
     * Obtiene los detalles completos de la tarea.
     * <p>
     * Incluye especificaciones técnicas, materiales a usar,
     * instrucciones específicas y cualquier detalle relevante.
     * </p>
     *
     * @return Detalles de la tarea
     */
    public String getDetalles() {
        return detalles;
    }

    /**
     * Establece los detalles completos de la tarea.
     *
     * @param detalles Descripción detallada
     */
    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    /**
     * Obtiene la fecha en que se asignó la tarea.
     *
     * @return Fecha de asignación
     */
    public LocalDate getFechaAsignacion() {
        return fechaAsignacion;
    }

    /**
     * Establece la fecha en que se asignó la tarea.
     *
     * @param fechaAsignacion Fecha de asignación
     */
    public void setFechaAsignacion(LocalDate fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    /**
     * Obtiene la fecha límite para completar la tarea.
     *
     * @return Fecha de entrega estimada
     */
    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    /**
     * Establece la fecha límite para completar la tarea.
     *
     * @param fechaEntrega Fecha de entrega
     */
    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    /**
     * Obtiene la cantidad de piezas o figuras a producir.
     * <p>
     * Representa el volumen de trabajo en términos de unidades
     * a fabricar, ensamblar o procesar.
     * </p>
     *
     * @return Cantidad de piezas/fiuras
     */
    public Integer getCantidadFiguras() {
        return cantidadFiguras;
    }

    /**
     * Establece la cantidad de piezas o figuras a producir.
     *
     * @param cantidadFiguras Cantidad de unidades
     */
    public void setCantidadFiguras(Integer cantidadFiguras) {
        this.cantidadFiguras = cantidadFiguras;
    }

    /**
     * Obtiene el estado actual de la tarea.
     * <p>
     * Estados posibles:
     * - "PENDIENTE": Tarea creada pero no iniciada
     * - "EN_PROGRESO": Tarea en proceso de ejecución
     * - "COMPLETADA": Tarea finalizada satisfactoriamente
     * - "VERIFICADA": Tarea completada y verificada por supervisor
     * - "CANCELADA": Tarea cancelada o descartada
     * </p>
     *
     * @return Estado actual
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado actual de la tarea.
     *
     * @param estado Estado de la tarea
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Indica si la tarea está activa en el sistema.
     * <p>
     * Las tareas inactivas no aparecen en listados generales
     * pero se mantienen para historial y reportes.
     * </p>
     *
     * @return true si está activa, false si está inactiva
     */
    public Boolean getActivo() {
        return activo;
    }

    /**
     * Establece el estado de actividad de la tarea.
     *
     * @param activo Estado de actividad
     */
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    /**
     * Obtiene el ID del usuario que creó la tarea.
     * <p>
     * Generalmente un supervisor, jefe de producción o administrador.
     * </p>
     *
     * @return ID del usuario creador
     */
    public String getCreadoPor() {
        return creadoPor;
    }

    /**
     * Establece el ID del usuario que creó la tarea.
     *
     * @param creadoPor ID del usuario
     */
    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    /**
     * Obtiene la fecha de creación del registro.
     * <p>
     * Formato común: "YYYY-MM-DD HH:MM:SS"
     * </p>
     *
     * @return Fecha de creación
     */
    public String getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece la fecha de creación del registro.
     *
     * @param fechaCreacion Fecha en formato de cadena
     */
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Obtiene el ID del trabajador asignado a la tarea.
     *
     * @return ID del trabajador responsable
     */
    public String getTrabajadorId() {
        return trabajadorId;
    }

    /**
     * Establece el ID del trabajador asignado a la tarea.
     *
     * @param trabajadorId ID del trabajador
     */
    public void setTrabajadorId(String trabajadorId) {
        this.trabajadorId = trabajadorId;
    }
}