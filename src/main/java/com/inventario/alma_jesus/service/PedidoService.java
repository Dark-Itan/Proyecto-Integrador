package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.Pedido;
import com.inventario.alma_jesus.model.PedidoProducto;
import com.inventario.alma_jesus.model.Venta;
import com.inventario.alma_jesus.repository.PedidoRepository;
import com.inventario.alma_jesus.repository.VentaRepository;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public List<Pedido> listarPedidosPorFecha(String fecha) {
        try {
            if (fecha == null || fecha.trim().isEmpty()) {
                throw new RuntimeException("La fecha es requerida");
            }

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

            int totalCantidad = pedido.getProductos().stream()
                    .mapToInt(PedidoProducto::getCantidad)
                    .sum();
            pedido.setTotalCantidad(totalCantidad);

            String primerProducto = pedido.getProductos().get(0).getProductoNombre();
            if (pedido.getProductos().size() > 1) {
                pedido.setResumenProducto(primerProducto.substring(0, Math.min(primerProducto.length(), 30)) +
                        "... (+" + (pedido.getProductos().size() - 1) + " items)");
            } else {
                pedido.setResumenProducto(primerProducto);
            }

            if (pedido.getEtapa() == null) {
                pedido.setEtapa("Pendiente por realizar");
            }

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

            // NUEVA FUNCIONALIDAD: Crear venta automáticamente cuando el pedido se finaliza
            if ("Finalizado".equals(nuevaEtapa)) {
                crearVentaDesdePedido(id);
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

    // NUEVO MÉTODO: Crear venta automáticamente desde pedido finalizado
    private void crearVentaDesdePedido(Long pedidoId) {
        try {
            System.out.println("Creando venta desde pedido ID: " + pedidoId);

            // Obtener el pedido completado
            Pedido pedido = obtenerPedido(pedidoId);

            // Verificar que el pedido tenga productos
            if (pedido.getProductos() == null || pedido.getProductos().isEmpty()) {
                System.out.println("❌ Pedido sin productos, no se crea venta");
                return;
            }

            // Tomar el primer producto del pedido para la venta
            PedidoProducto primerProducto = pedido.getProductos().get(0);

            // Verificar datos esenciales
            if (primerProducto.getProductoId() == null) {
                System.out.println("❌ Producto sin ID, no se crea venta");
                return;
            }

            // Formatear fecha actual
            String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            // Usar clienteId por defecto (1) ya que tu modelo no tiene clienteId
            // En una implementación real, necesitarías agregar clienteId al modelo Pedido
            int clienteIdDefault = 1; // Cliente genérico

            // Crear la venta según tu modelo Venta actual
            Venta venta = new Venta();
            venta.setClienteId(clienteIdDefault); // Usar cliente por defecto
            venta.setProductoId(primerProducto.getProductoId().intValue()); // Convertir Long a int
            venta.setCantidad(primerProducto.getCantidad());

            // Convertir BigDecimal a int para precio unitario
            int precioUnitario = primerProducto.getPrecioUnitario() != null ?
                    primerProducto.getPrecioUnitario().intValue() : 0;
            venta.setPrecioUnitario(precioUnitario);

            venta.setFecha(fechaActual);
            venta.setTipo("pedido");
            venta.setUsuarioRegistro("Sistema");

            // El precioTotal se calcula automáticamente en el constructor de Venta

            System.out.println(" Datos de venta a crear:");
            System.out.println("  - Cliente ID: " + venta.getClienteId());
            System.out.println("  - Producto ID: " + venta.getProductoId());
            System.out.println("  - Cantidad: " + venta.getCantidad());
            System.out.println("  - Precio Unitario: " + venta.getPrecioUnitario());
            System.out.println("  - Precio Total: " + venta.getPrecioTotal());
            System.out.println("  - Fecha: " + venta.getFecha());
            System.out.println("  - Tipo: " + venta.getTipo());

            // Guardar la venta usando el método que ya existe en VentaRepository
            VentaRepository ventaRepository = new VentaRepository();
            boolean ventaCreada = ventaRepository.crearVenta(venta);

            if (ventaCreada) {
                System.out.println("Venta creada exitosamente desde pedido ID: " + pedidoId);
                System.out.println("Total de venta: $" + venta.getPrecioTotal());
            } else {
                System.out.println(" Error al crear venta desde pedido ID: " + pedidoId);
            }

        } catch (Exception e) {
            System.err.println("Error al crear venta desde pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }
}