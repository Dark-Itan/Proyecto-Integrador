package com.inventario.alma_jesus.model;

/**
 * Modelo que representa un movimiento de stock de materia prima.
 * <p>
 * Esta clase registra todas las transacciones de inventario relacionadas con
 * materias primas, incluyendo entradas (compras), salidas (consumo) y ajustes
 * de stock, con trazabilidad completa de fechas y usuarios responsables.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class MovimientoMp {
    /**
     * Identificador único del movimiento.
     */
    private Long id;

    /**
     * ID de la materia prima relacionada con este movimiento.
     */
    private Long materiaId;

    /**
     * Fecha en que ocurrió el movimiento (formato: YYYY-MM-DD HH:MM:SS).
     */
    private String fecha;

    /**
     * Tipo de movimiento: ENTRADA (compra), SALIDA (consumo), AJUSTE (corrección).
     */
    private String tipo;

    /**
     * Cantidad involucrada en el movimiento (positiva para entradas, negativa para salidas).
     */
    private Integer cantidad;

    /**
     * ID del usuario que registró el movimiento.
     */
    private String usuarioId;

    /**
     * Fecha y hora exacta en que se registró el movimiento en el sistema.
     */
    private String fechaRegistro;

    /**
     * Constructor por defecto.
     * Crea una instancia de MovimientoMp sin inicializar atributos.
     */
    public MovimientoMp() {}

    /**
     * Constructor completo con todos los atributos.
     *
     * @param id Identificador único
     * @param materiaId ID de la materia prima
     * @param fecha Fecha del movimiento
     * @param tipo Tipo de movimiento (ENTRADA, SALIDA, AJUSTE)
     * @param cantidad Cantidad involucrada
     * @param usuarioId ID del usuario responsable
     * @param fechaRegistro Fecha de registro en sistema
     */
    public MovimientoMp(Long id, Long materiaId, String fecha, String tipo,
                        Integer cantidad, String usuarioId, String fechaRegistro) {
        this.id = id;
        this.materiaId = materiaId;
        this.fecha = fecha;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.usuarioId = usuarioId;
        this.fechaRegistro = fechaRegistro;
    }

    /**
     * Obtiene el identificador único del movimiento.
     *
     * @return El ID del movimiento
     */
    public Long getId() { return id; }

    /**
     * Establece el identificador único del movimiento.
     *
     * @param id El ID a asignar
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Obtiene el ID de la materia prima relacionada.
     *
     * @return El ID de la materia prima
     */
    public Long getMateriaId() { return materiaId; }

    /**
     * Establece el ID de la materia prima relacionada.
     *
     * @param materiaId El ID de la materia prima a asignar
     */
    public void setMateriaId(Long materiaId) { this.materiaId = materiaId; }

    /**
     * Obtiene la fecha en que ocurrió el movimiento.
     *
     * @return La fecha del movimiento (formato: YYYY-MM-DD HH:MM:SS)
     */
    public String getFecha() { return fecha; }

    /**
     * Establece la fecha en que ocurrió el movimiento.
     *
     * @param fecha La fecha a asignar (formato: YYYY-MM-DD HH:MM:SS)
     */
    public void setFecha(String fecha) { this.fecha = fecha; }

    /**
     * Obtiene el tipo de movimiento.
     *
     * @return El tipo de movimiento (ENTRADA, SALIDA, AJUSTE)
     */
    public String getTipo() { return tipo; }

    /**
     * Establece el tipo de movimiento.
     *
     * @param tipo El tipo de movimiento a asignar
     */
    public void setTipo(String tipo) { this.tipo = tipo; }

    /**
     * Obtiene la cantidad involucrada en el movimiento.
     *
     * @return La cantidad (positiva para entradas, negativa para salidas)
     */
    public Integer getCantidad() { return cantidad; }

    /**
     * Establece la cantidad involucrada en el movimiento.
     *
     * @param cantidad La cantidad a asignar
     */
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    /**
     * Obtiene el ID del usuario que registró el movimiento.
     *
     * @return El ID del usuario
     */
    public String getUsuarioId() { return usuarioId; }

    /**
     * Establece el ID del usuario que registró el movimiento.
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