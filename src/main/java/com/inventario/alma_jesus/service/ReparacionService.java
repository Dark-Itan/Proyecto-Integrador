package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.Reparacion;
import com.inventario.alma_jesus.repository.ReparacionRepository;
import java.util.*;

public class ReparacionService {
    private final ReparacionRepository reparacionRepository;

    public ReparacionService() {
        this.reparacionRepository = new ReparacionRepository();
    }

    public List<Reparacion> listarReparaciones(String estado, String cliente, String modelo) {
        System.out.println("Listando reparaciones");
        return reparacionRepository.findAll(estado, cliente, modelo);
    }

    public Optional<Reparacion> obtenerReparacion(Long id) {
        System.out.println("Obteniendo reparacion ID: " + id);
        if (id == null) return Optional.empty();
        return reparacionRepository.findById(id);
    }

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
        if (reparacion.getPrioridadCliente() == null) reparacion.setPrioridadCliente("Media");
        if (reparacion.getCreadoPor() == null) reparacion.setCreadoPor("Sistema");
        if (reparacion.getAnticipo() == null) reparacion.setAnticipo(0);
        if (reparacion.getCantidadPiezas() == null) reparacion.setCantidadPiezas(1);
        if (reparacion.getFechaIngreso() == null) reparacion.setFechaIngreso(java.time.LocalDate.now().toString());
        if (reparacion.getReciboUrl() == null) reparacion.setReciboUrl(""); // INICIALIZAR reciboUrl

        Reparacion reparacionCreada = reparacionRepository.save(reparacion);

        if (reparacionCreada.getMaterialesUsados() != null && !reparacionCreada.getMaterialesUsados().trim().isEmpty()) {
            procesarMaterialesParaInventario(reparacionCreada.getMaterialesUsados());
        }

        return reparacionCreada;
    }

    public boolean actualizarReparacion(Reparacion reparacion) {
        System.out.println("Actualizando reparacion ID: " + reparacion.getId());
        if (reparacion.getId() == null) {
            throw new IllegalArgumentException("ID de reparacion es requerido para actualizar");
        }
        return reparacionRepository.update(reparacion);
    }

    public boolean cambiarEstado(Long id, String nuevoEstado) {
        System.out.println("Cambiando estado ID: " + id + " -> " + nuevoEstado);
        if (!esEstadoValido(nuevoEstado)) {
            throw new IllegalArgumentException("Estado no valido. Use: Pendiente, En Proceso, Completado, Entregado");
        }
        return reparacionRepository.updateEstado(id, nuevoEstado);
    }

    public boolean eliminarReparacion(Long id) {
        System.out.println("Eliminando reparacion ID: " + id);
        return reparacionRepository.delete(id);
    }

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
        recibo.put("trabajadorAsignado", reparacion.getTrabajadorAsignado());
        recibo.put("materialesUsados", reparacion.getMaterialesUsados());
        recibo.put("reciboUrl", reparacion.getReciboUrl()); // NUEVO CAMPO EN RECIBO

        System.out.println("Recibo generado para ID: " + reparacionId);
        return recibo;
    }

    private boolean esEstadoValido(String estado) {
        return estado != null &&
                (estado.equals("Pendiente") ||
                        estado.equals("En Proceso") ||
                        estado.equals("Completado") ||
                        estado.equals("Entregado"));
    }

    public void procesarMaterialesParaInventario(String materialesUsados) {
        if (materialesUsados == null || materialesUsados.trim().isEmpty()) return;
        System.out.println("Procesando materiales: " + materialesUsados);
    }

    public Integer calcularSaldoPendiente(Reparacion reparacion) {
        if (reparacion == null || reparacion.getCostoTotal() == null) return 0;
        Integer anticipo = reparacion.getAnticipo() != null ? reparacion.getAnticipo() : 0;
        return Math.max(0, reparacion.getCostoTotal() - anticipo);
    }
}