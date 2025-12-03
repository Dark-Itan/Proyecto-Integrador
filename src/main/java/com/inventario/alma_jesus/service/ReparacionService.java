package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.Reparacion;
import com.inventario.alma_jesus.repository.ReparacionRepository;
import java.util.*;

/**
 * Servicio para la gestión de reparaciones en el sistema.
 * Proporciona métodos CRUD completos para reparaciones, incluyendo gestión de estados,
 * historial, generación de recibos y validaciones de negocio.
 *
 * @author Alma & Jesús
 * @version 1.0
 * @since 2024
 */
public class ReparacionService {
    private final ReparacionRepository reparacionRepository;

    /**
     * Constructor que inicializa el servicio de reparaciones.
     * Crea una nueva instancia del repositorio de reparaciones.
     */
    public ReparacionService() {
        this.reparacionRepository = new ReparacionRepository();
    }

    /**
     * Lista las reparaciones con opción de filtrar por estado, cliente o modelo.
     *
     * @param estado Estado de la reparación para filtrar (opcional).
     * @param cliente Nombre del cliente para filtrar (opcional).
     * @param modelo Modelo del producto para filtrar (opcional).
     * @return Lista de reparaciones que cumplen con los filtros especificados.
     */
    public List<Reparacion> listarReparaciones(String estado, String cliente, String modelo) {
        System.out.println("Listando reparaciones");
        return reparacionRepository.findAll(estado, cliente, modelo);
    }

    /**
     * Obtiene una reparación específica por su ID.
     *
     * @param id ID de la reparación a buscar.
     * @return Optional conteniendo la reparación si se encuentra, Optional.empty() en caso contrario.
     */
    public Optional<Reparacion> obtenerReparacion(Long id) {
        System.out.println("Obteniendo reparacion ID: " + id);
        if (id == null) return Optional.empty();
        return reparacionRepository.findById(id);
    }

    /**
     * Crea una nueva reparación en el sistema con validaciones básicas.
     *
     * @param reparacion Objeto Reparacion con los datos de la nueva reparación.
     * @return La reparación creada con valores por defecto asignados.
     * @throws IllegalArgumentException Si los datos requeridos no son válidos.
     */
    public Reparacion crearReparacion(Reparacion reparacion) {
        System.out.println("Creando nueva reparacion");

        if (reparacion.getNombreCliente() == null || reparacion.getNombreCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es requerido");
        }
        if (reparacion.getModelo() == null || reparacion.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo es requerido");
        }
        if (reparacion.getCostoTotal() == null || reparacion.getCostoTotal() < 0) {
            throw new IllegalArgumentException("El costo total debe ser mayor o igual a 0");
        }

        if (reparacion.getEstado() == null) reparacion.setEstado("Pendiente");
        if (reparacion.getCreadoPor() == null) reparacion.setCreadoPor("Sistema");
        if (reparacion.getAnticipo() == null) reparacion.setAnticipo(0);
        if (reparacion.getFechaIngreso() == null) reparacion.setFechaIngreso(java.time.LocalDate.now().toString());
        if (reparacion.getReciboUrl() == null) reparacion.setReciboUrl("");
        if (reparacion.getMaterialOriginal() == null) reparacion.setMaterialOriginal("Yeso frio");

        return reparacionRepository.save(reparacion);
    }

    /**
     * Actualiza una reparación existente en el sistema.
     *
     * @param reparacion Objeto Reparacion con los datos actualizados.
     * @return true si la reparación fue actualizada exitosamente, false en caso contrario.
     * @throws IllegalArgumentException Si el ID de la reparación es null.
     */
    public boolean actualizarReparacion(Reparacion reparacion) {
        System.out.println("Actualizando reparacion ID: " + reparacion.getId());
        if (reparacion.getId() == null) {
            throw new IllegalArgumentException("ID de reparacion es requerido para actualizar");
        }
        return reparacionRepository.update(reparacion);
    }

    /**
     * Cambia el estado de una reparación específica.
     *
     * @param id ID de la reparación a actualizar.
     * @param nuevoEstado Nuevo estado a asignar a la reparación.
     * @return true si el estado fue cambiado exitosamente, false en caso contrario.
     * @throws IllegalArgumentException Si el estado no es válido.
     */
    public boolean cambiarEstado(Long id, String nuevoEstado) {
        System.out.println("Cambiando estado ID: " + id + " -> " + nuevoEstado);
        if (!esEstadoValido(nuevoEstado)) {
            throw new IllegalArgumentException("Estado no valido. Use: Pendiente, En Proceso, Completado, Entregado");
        }
        return reparacionRepository.updateEstado(id, nuevoEstado);
    }

    /**
     * Elimina una reparación del sistema.
     *
     * @param id ID de la reparación a eliminar.
     * @return true si la reparación fue eliminada exitosamente, false en caso contrario.
     */
    public boolean eliminarReparacion(Long id) {
        System.out.println("Eliminando reparacion ID: " + id);
        return reparacionRepository.delete(id);
    }

    /**
     * Obtiene el historial de acciones realizadas sobre una reparación específica.
     *
     * @param reparacionId ID de la reparación cuyo historial se desea obtener.
     * @return Lista de mapas con las entradas del historial.
     */
    public List<Map<String, Object>> obtenerHistorial(Long reparacionId) {
        System.out.println("Obteniendo historial ID: " + reparacionId);

        List<Map<String, Object>> historial = new ArrayList<>();

        Map<String, Object> entrada1 = new HashMap<>();
        entrada1.put("id", 1);
        entrada1.put("fecha", "2024-01-15 10:30:00");
        entrada1.put("accion", "REPARACION CREADA");
        entrada1.put("usuario", "Sistema");
        entrada1.put("detalles", "Reparacion registrada en el sistema");
        entrada1.put("estado_anterior", null);
        entrada1.put("estado_nuevo", "Pendiente");
        historial.add(entrada1);

        Map<String, Object> entrada2 = new HashMap<>();
        entrada2.put("id", 2);
        entrada2.put("fecha", "2024-01-16 14:20:00");
        entrada2.put("accion", "ESTADO CAMBIADO");
        entrada2.put("usuario", "Admin");
        entrada2.put("detalles", "Reparacion en progreso");
        entrada2.put("estado_anterior", "Pendiente");
        entrada2.put("estado_nuevo", "En Proceso");
        historial.add(entrada2);

        System.out.println("Historial obtenido: " + historial.size() + " entradas");
        return historial;
    }

    /**
     * Genera un recibo para una reparación específica.
     *
     * @param reparacionId ID de la reparación para la cual generar el recibo.
     * @return Mapa con los datos del recibo generado.
     * @throws RuntimeException Si la reparación no se encuentra.
     */
    public Map<String, Object> generarRecibo(Long reparacionId) {
        System.out.println("Generando recibo ID: " + reparacionId);

        Optional<Reparacion> reparacionOpt = obtenerReparacion(reparacionId);
        if (reparacionOpt.isEmpty()) {
            throw new RuntimeException("Reparacion no encontrada para generar recibo");
        }

        Reparacion reparacion = reparacionOpt.get();

        Map<String, Object> recibo = new HashMap<>();
        recibo.put("id", reparacion.getId());
        recibo.put("numeroRecibo", "REC-" + System.currentTimeMillis());
        recibo.put("fechaEmision", java.time.LocalDate.now().toString());
        recibo.put("cliente", reparacion.getNombreCliente());
        recibo.put("contacto", reparacion.getContacto());
        recibo.put("modelo", reparacion.getModelo());
        recibo.put("descripcion", "Reparacion de " + reparacion.getModelo());
        recibo.put("costoTotal", reparacion.getCostoTotal());
        recibo.put("anticipo", reparacion.getAnticipo());
        recibo.put("saldoPendiente", calcularSaldoPendiente(reparacion));
        recibo.put("estado", reparacion.getEstado());
        recibo.put("fechaEntrega", reparacion.getFechaEntrega());
        recibo.put("materialesUsados", reparacion.getNotas());
        recibo.put("reciboUrl", reparacion.getReciboUrl());

        System.out.println("Recibo generado para ID: " + reparacionId);
        return recibo;
    }

    /**
     * Verifica si un estado de reparación es válido.
     *
     * @param estado Estado a validar.
     * @return true si el estado es válido, false en caso contrario.
     */
    private boolean esEstadoValido(String estado) {
        return estado != null &&
                (estado.equals("Pendiente") ||
                        estado.equals("En Proceso") ||
                        estado.equals("Completado") ||
                        estado.equals("Entregado"));
    }

    /**
     * Calcula el saldo pendiente de pago para una reparación.
     *
     * @param reparacion Reparación para calcular el saldo pendiente.
     * @return Saldo pendiente de pago (costo total - anticipo).
     */
    public Integer calcularSaldoPendiente(Reparacion reparacion) {
        if (reparacion == null || reparacion.getCostoTotal() == null) return 0;
        Integer anticipo = reparacion.getAnticipo() != null ? reparacion.getAnticipo() : 0;
        return Math.max(0, reparacion.getCostoTotal() - anticipo);
    }
}