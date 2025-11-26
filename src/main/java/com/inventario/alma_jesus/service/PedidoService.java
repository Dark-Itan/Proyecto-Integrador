package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.Pedido;
import com.inventario.alma_jesus.model.PedidoProducto;
import com.inventario.alma_jesus.repository.PedidoRepository;

import java.sql.SQLException;
import java.util.List;

public class PedidoService {
    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    public List<Pedido> listarPedidos() {
        try {
            return repository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pedidos: " + e.getMessage(), e);
        }
    }

    //  NUEVO MÉTODO: Listar pedidos por fecha
    public List<Pedido> listarPedidosPorFecha(String fecha) {
        try {
            // Validar que la fecha no esté vacía
            if (fecha == null || fecha.trim().isEmpty()) {
                throw new RuntimeException("La fecha es requerida");
            }

            // Validar formato básico de fecha (YYYY-MM-DD)
            if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
                throw new RuntimeException("Formato de fecha inválido. Use YYYY-MM-DD");
            }

            return repository.findByFecha(fecha);
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pedidos por fecha: " + e.getMessage(), e);
        }
    }

    public Pedido obtenerPedido(Long id) {
        try {
            Pedido pedido = repository.findById(id);
            if (pedido == null) {
                throw new RuntimeException("Pedido no encontrado con ID: " + id);
            }
            return pedido;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener pedido: " + e.getMessage(), e);
        }
    }

    public Pedido crearPedido(Pedido pedido) {
        try {
            if (pedido.getClienteNombre() == null || pedido.getClienteNombre().trim().isEmpty()) {
                throw new RuntimeException("El nombre del cliente es requerido");
            }
            if (pedido.getProductos() == null || pedido.getProductos().isEmpty()) {
                throw new RuntimeException("Debe agregar al menos un producto al pedido");
            }
            if (pedido.getTotal() == null || pedido.getTotal().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("El total del pedido debe ser mayor a cero");
            }

            // Calcular total cantidad
            int totalCantidad = pedido.getProductos().stream()
                    .mapToInt(PedidoProducto::getCantidad)
                    .sum();
            pedido.setTotalCantidad(totalCantidad);

            // Generar resumen de producto
            String primerProducto = pedido.getProductos().get(0).getProductoNombre();
            if (pedido.getProductos().size() > 1) {
                pedido.setResumenProducto(primerProducto.substring(0, Math.min(primerProducto.length(), 30)) +
                        "... (+" + (pedido.getProductos().size() - 1) + " items)");
            } else {
                pedido.setResumenProducto(primerProducto);
            }

            // Establecer etapa por defecto
            if (pedido.getEtapa() == null) {
                pedido.setEtapa("Pendiente por realizar");
            }

            // Establecer creador por defecto
            if (pedido.getCreadoPor() == null) {
                pedido.setCreadoPor("admin");
            }

            Long pedidoId = repository.create(pedido);
            repository.createProductos(pedidoId, pedido.getProductos());

            return obtenerPedido(pedidoId);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear pedido: " + e.getMessage(), e);
        }
    }

    public Pedido actualizarEtapa(Long id, String nuevaEtapa, String notas) {
        try {
            Pedido existente = repository.findById(id);
            if (existente == null) {
                throw new RuntimeException("Pedido no encontrado con ID: " + id);
            }

            boolean updated = repository.updateEtapa(id, nuevaEtapa, notas);
            if (!updated) {
                throw new RuntimeException("No se pudo actualizar la etapa del pedido");
            }

            return obtenerPedido(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar etapa: " + e.getMessage(), e);
        }
    }

    public boolean eliminarPedido(Long id) {
        try {
            Pedido existente = repository.findById(id);
            if (existente == null) {
                throw new RuntimeException("Pedido no encontrado con ID: " + id);
            }

            return repository.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar pedido: " + e.getMessage(), e);
        }
    }
}