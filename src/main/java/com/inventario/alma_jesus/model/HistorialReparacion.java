package com.inventario.alma_jesus.model;

/**
 * Modelo que representa el historial de cambios de estado en una reparación.
 * <p>
 * Esta clase registra todos los cambios de estado que sufre una reparación a lo largo
 * de su ciclo de vida, incluyendo fechas, estados anteriores/nuevos, notas y usuarios
 * responsables de cada cambio.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class HistorialReparacion {
    /**
     * Identificador único del registro en el historial.
     */
    private Long id;

    /**
     * ID de la reparación a la que pertenece este historial.
     */
    private Long reparacionId;

    /**
     * Fecha en que se realizó el cambio de estado (formato: YYYY-MM-DD).
     */
    private String fecha;

    /**
     * Nuevo estado asignado a la reparación.
     * Valores posibles: RECEPCION, DIAGNOSTICO, REPARACION, PRUEBA, TERMINADO, ENTREGADO, CANCELADO.
     */
    private String estado;

    /**
     * Notas adicionales sobre el cambio de estado.
     */
    private String notas;

    /**
     * ID del usuario que realizó el cambio de estado.
     */
    private String usuarioId;

    /**
     * Fecha y hora exacta en que se registró el cambio en el sistema.
     */
    private String fechaRegistro;

    /**
     * Constructor por defecto.
     * Crea una instancia de HistorialReparacion sin inicializar atributos.
     */
    public HistorialReparacion() {}

    /**
     * Constructor completo con todos los parámetros.
     *
     * @param id Identificador único del historial
     * @param reparacionId ID de la reparación relacionada
     * @param fecha Fecha del cambio de estado
     * @param estado Nuevo estado asignado
     * @param notas Notas adicionales del cambio
     * @param usuarioId ID del usuario que realizó el cambio
     * @param fechaRegistro Fecha de registro en el sistema
     */
    public HistorialReparacion(Long id, Long reparacionId, String fecha, String estado,
                               String notas, String usuarioId, String fechaRegistro) {
        this.id = id;
        this.reparacionId = reparacionId;
        this.fecha = fecha;
        this.estado = estado;
        this.notas = notas;
        this.usuarioId = usuarioId;
        this.fechaRegistro = fechaRegistro;
    }

    /**
     * Obtiene el identificador único del registro de historial.
     *
     * @return El ID del historial
     */
    public Long getId() { return id; }

    /**
     * Establece el identificador único del registro de historial.
     *
     * @param id El ID a asignar
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Obtiene el ID de la reparación relacionada.
     *
     * @return El ID de la reparación
     */
    public Long getReparacionId() { return reparacionId; }

    /**
     * Establece el ID de la reparación relacionada.
     *
     * @param reparacionId El ID de la reparación a asignar
     */
    public void setReparacionId(Long reparacionId) { this.reparacionId = reparacionId; }

    /**
     * Obtiene la fecha del cambio de estado.
     *
     * @return La fecha del cambio (formato: YYYY-MM-DD)
     */
    public String getFecha() { return fecha; }

    /**
     * Establece la fecha del cambio de estado.
     *
     * @param fecha La fecha a asignar (formato: YYYY-MM-DD)
     */
    public void setFecha(String fecha) { this.fecha = fecha; }

    /**
     * Obtiene el nuevo estado asignado.
     *
     * @return El estado de la reparación
     */
    public String getEstado() { return estado; }

    /**
     * Establece el nuevo estado asignado.
     *
     * @param estado El estado a asignar
     */
    public void setEstado(String estado) { this.estado = estado; }

    /**
     * Obtiene las notas adicionales del cambio.
     *
     * @return Las notas del cambio de estado
     */
    public String getNotas() { return notas; }

    /**
     * Establece las notas adicionales del cambio.
     *
     * @param notas Las notas a asignar
     */
    public void setNotas(String notas) { this.notas = notas; }

    /**
     * Obtiene el ID del usuario que realizó el cambio.
     *
     * @return El ID del usuario
     */
    public String getUsuarioId() { return usuarioId; }

    /**
     * Establece el ID del usuario que realizó el cambio.
     *
     * @param usuarioId El ID del usuario a asignar
     */
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    /**
     * Obtiene la fecha de registro en el sistema.
     *
     * @return La fecha y hora de registro
     */
    public String getFechaRegistro() { return fechaRegistro; }

    /**
     * Establece la fecha de registro en el sistema.
     *
     * @param fechaRegistro La fecha y hora de registro a asignar
     */
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}