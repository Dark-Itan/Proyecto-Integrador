package com.inventario.alma_jesus.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modelo que representa el consumo de materiales en documentos del sistema.
 * <p>
 * Esta clase registra el uso de materiales en reparaciones y pedidos,
 * permitiendo llevar un control preciso de inventario y costos asociados
 * a cada documento generado en el sistema.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class MaterialUtilizado {
    private Long id;
    private TipoDocumento tipoDocumento;
    private Long documentoId;
    private Long materiaId;
    private Integer cantidad;
    private Integer costoUnitario;
    private LocalDate fecha;
    private String usuarioId;
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    /**
     * Enumeración que define los tipos de documentos que pueden consumir materiales.
     */
    public enum TipoDocumento {
        /** Documento de reparación realizada en el taller */
        reparacion,

        /** Documento de pedido de fabricación */
        pedido
    }

    /**
     * Constructor por defecto.
     * <p>
     * Inicializa con fechaRegistro automática a la fecha/hora actual.
     * </p>
     */
    public MaterialUtilizado() {}

    /**
     * Constructor principal para crear un registro de material utilizado.
     *
     * @param tipoDocumento Tipo de documento que consume el material
     * @param documentoId ID del documento (reparación o pedido)
     * @param materiaId ID del material consumido
     * @param cantidad Cantidad utilizada del material
     * @param costoUnitario Costo unitario del material al momento del consumo
     * @param fecha Fecha en que se consumió el material
     * @param usuarioId ID del usuario que registró el consumo
     */
    public MaterialUtilizado(TipoDocumento tipoDocumento, Long documentoId, Long materiaId,
                             Integer cantidad, Integer costoUnitario, LocalDate fecha, String usuarioId) {
        this.tipoDocumento = tipoDocumento;
        this.documentoId = documentoId;
        this.materiaId = materiaId;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.fecha = fecha;
        this.usuarioId = usuarioId;
    }

    // Getters y Setters

    /**
     * Obtiene el identificador único del registro.
     *
     * @return ID del registro de material utilizado
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del registro.
     *
     * @param id ID único del registro
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el tipo de documento que consume el material.
     *
     * @return Tipo de documento ({@link TipoDocumento})
     */
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Establece el tipo de documento que consume el material.
     *
     * @param tipoDocumento Tipo de documento
     */
    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * Obtiene el ID del documento (reparación o pedido).
     *
     * @return ID del documento relacionado
     */
    public Long getDocumentoId() {
        return documentoId;
    }

    /**
     * Establece el ID del documento (reparación o pedido).
     *
     * @param documentoId ID del documento
     */
    public void setDocumentoId(Long documentoId) {
        this.documentoId = documentoId;
    }

    /**
     * Obtiene el ID del material consumido.
     *
     * @return ID del material
     */
    public Long getMateriaId() {
        return materiaId;
    }

    /**
     * Establece el ID del material consumido.
     *
     * @param materiaId ID del material
     */
    public void setMateriaId(Long materiaId) {
        this.materiaId = materiaId;
    }

    /**
     * Obtiene la cantidad de material consumido.
     * <p>
     * La cantidad debe especificarse en la unidad de medida
     * correspondiente al material (ej: piezas, metros, litros).
     * </p>
     *
     * @return Cantidad consumida
     */
    public Integer getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad de material consumido.
     *
     * @param cantidad Cantidad consumida
     */
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el costo unitario del material al momento del consumo.
     * <p>
     * Representado en centavos o la mínima unidad monetaria
     * para evitar problemas de precisión con decimales.
     * </p>
     *
     * @return Costo unitario en centavos
     */
    public Integer getCostoUnitario() {
        return costoUnitario;
    }

    /**
     * Establece el costo unitario del material al momento del consumo.
     *
     * @param costoUnitario Costo unitario en centavos
     */
    public void setCostoUnitario(Integer costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    /**
     * Obtiene la fecha en que se consumió el material.
     *
     * @return Fecha de consumo
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha en que se consumió el material.
     *
     * @param fecha Fecha de consumo
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el ID del usuario que registró el consumo.
     *
     * @return ID del usuario responsable
     */
    public String getUsuarioId() {
        return usuarioId;
    }

    /**
     * Establece el ID del usuario que registró el consumo.
     *
     * @param usuarioId ID del usuario
     */
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * Obtiene la fecha y hora del registro en el sistema.
     * <p>
     * Se establece automáticamente al crear el registro.
     * </p>
     *
     * @return Fecha y hora de registro
     */
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    /**
     * Establece la fecha y hora del registro en el sistema.
     * <p>
     * Normalmente solo se usa para cargar registros existentes.
     * </p>
     *
     * @param fechaRegistro Fecha y hora de registro
     */
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}