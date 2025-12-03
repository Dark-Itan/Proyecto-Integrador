package com.inventario.alma_jesus.model;

import java.time.LocalDateTime;

/**
 * Modelo que representa una herramienta en el sistema de inventario.
 * <p>
 * Esta clase gestiona el control de herramientas del taller, incluyendo
 * asignación a usuarios, disponibilidad, y seguimiento de uso.
 * Permite llevar un inventario preciso de todas las herramientas disponibles.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class Herramienta {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer cantidadTotal;
    private Integer cantidadDisponible;
    private String estatus;
    private String usuarioAsignado;
    private String asignadoPor;
    private LocalDateTime fechaAsignacion;
    private Boolean activo;
    private String creadoPor;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    /**
     * Constructor por defecto.
     * <p>
     * Crea una instancia vacía de herramienta.
     * Los valores deben establecerse mediante los setters correspondientes.
     * </p>
     */
    public Herramienta() {}

    /**
     * Constructor principal para crear una herramienta.
     * <p>
     * Este constructor excluye las fechas de creación y actualización
     * que son manejadas automáticamente por la base de datos.
     * </p>
     *
     * @param id Identificador único de la herramienta
     * @param nombre Nombre de la herramienta
     * @param descripcion Descripción detallada de la herramienta
     * @param cantidadTotal Cantidad total disponible en inventario
     * @param cantidadDisponible Cantidad actualmente disponible para asignación
     * @param estatus Estado actual de la herramienta
     * @param usuarioAsignado ID del usuario al que está asignada
     * @param asignadoPor ID del usuario que realizó la asignación
     * @param activo Indica si la herramienta está activa en el sistema
     * @param creadoPor ID del usuario que creó el registro
     */
    public Herramienta(Long id, String nombre, String descripcion, Integer cantidadTotal,
                       Integer cantidadDisponible, String estatus, String usuarioAsignado,
                       String asignadoPor, Boolean activo, String creadoPor) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidadTotal = cantidadTotal;
        this.cantidadDisponible = cantidadDisponible;
        this.estatus = estatus;
        this.usuarioAsignado = usuarioAsignado;
        this.asignadoPor = asignadoPor;
        this.activo = activo;
        this.creadoPor = creadoPor;
    }

    // Getters y Setters

    /**
     * Obtiene el identificador único de la herramienta.
     *
     * @return ID de la herramienta
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único de la herramienta.
     *
     * @param id ID único de la herramienta
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la herramienta.
     *
     * @return Nombre de la herramienta
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la herramienta.
     *
     * @param nombre Nombre descriptivo de la herramienta
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción detallada de la herramienta.
     *
     * @return Descripción de la herramienta
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción detallada de la herramienta.
     *
     * @param descripcion Descripción que incluye especificaciones técnicas
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la cantidad total de esta herramienta en inventario.
     *
     * @return Cantidad total en stock
     */
    public Integer getCantidadTotal() {
        return cantidadTotal;
    }

    /**
     * Establece la cantidad total de esta herramienta en inventario.
     *
     * @param cantidadTotal Cantidad total disponible
     */
    public void setCantidadTotal(Integer cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    /**
     * Obtiene la cantidad actualmente disponible para asignación.
     * <p>
     * Se calcula como: cantidadTotal - cantidadAsignada
     * </p>
     *
     * @return Cantidad disponible
     */
    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    /**
     * Establece la cantidad actualmente disponible para asignación.
     *
     * @param cantidadDisponible Cantidad disponible
     */
    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    /**
     * Obtiene el estado actual de la herramienta.
     * <p>
     * Estados posibles:
     * - "DISPONIBLE"
     * - "ASIGNADA"
     * - "EN_MANTENIMIENTO"
     * - "DANADA"
     * - "PERDIDA"
     * </p>
     *
     * @return Estado actual
     */
    public String getEstatus() {
        return estatus;
    }

    /**
     * Establece el estado actual de la herramienta.
     *
     * @param estatus Estado de la herramienta
     */
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    /**
     * Obtiene el ID del usuario al que está asignada la herramienta.
     *
     * @return ID del usuario asignado, o null si no está asignada
     */
    public String getUsuarioAsignado() {
        return usuarioAsignado;
    }

    /**
     * Establece el ID del usuario al que está asignada la herramienta.
     *
     * @param usuarioAsignado ID del usuario
     */
    public void setUsuarioAsignado(String usuarioAsignado) {
        this.usuarioAsignado = usuarioAsignado;
    }

    /**
     * Obtiene el ID del usuario que realizó la asignación.
     *
     * @return ID del usuario que asignó la herramienta
     */
    public String getAsignadoPor() {
        return asignadoPor;
    }

    /**
     * Establece el ID del usuario que realizó la asignación.
     *
     * @param asignadoPor ID del usuario asignador
     */
    public void setAsignadoPor(String asignadoPor) {
        this.asignadoPor = asignadoPor;
    }

    /**
     * Obtiene la fecha y hora de la última asignación.
     *
     * @return Fecha de asignación
     */
    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    /**
     * Establece la fecha y hora de la última asignación.
     *
     * @param fechaAsignacion Fecha de asignación
     */
    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    /**
     * Indica si la herramienta está activa en el sistema.
     * <p>
     * Las herramientas inactivas no aparecen en búsquedas
     * pero se mantienen en el historial.
     * </p>
     *
     * @return true si está activa, false si está inactiva
     */
    public Boolean getActivo() {
        return activo;
    }

    /**
     * Establece el estado de actividad de la herramienta.
     *
     * @param activo Estado de actividad
     */
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    /**
     * Obtiene el ID del usuario que creó el registro.
     *
     * @return ID del usuario creador
     */
    public String getCreadoPor() {
        return creadoPor;
    }

    /**
     * Establece el ID del usuario que creó el registro.
     *
     * @param creadoPor ID del usuario creador
     */
    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    /**
     * Obtiene la fecha y hora de creación del registro.
     *
     * @return Fecha de creación
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece la fecha y hora de creación del registro.
     * <p>
     * Normalmente se establece automáticamente por la base de datos.
     * </p>
     *
     * @param fechaCreacion Fecha de creación
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Obtiene la fecha y hora de la última actualización.
     *
     * @return Fecha de última actualización
     */
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    /**
     * Establece la fecha y hora de la última actualización.
     * <p>
     * Normalmente se actualiza automáticamente al modificar el registro.
     * </p>
     *
     * @param fechaActualizacion Fecha de actualización
     */
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}