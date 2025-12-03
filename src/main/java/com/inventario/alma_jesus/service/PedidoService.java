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

/**
 * Servicio para la gestión de pedidos de fabricación.
 * <p>
 * Esta clase contiene la lógica de negocio para todas las operaciones relacionadas
 * con pedidos, incluyendo creación, consulta, actualización de etapas y eliminación.
 * También maneja la integración automática con el módulo de ventas cuando los
 * pedidos son finalizados.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see PedidoRepository
 * @see VentaRepository
 * @see Pedido
 * @see PedidoProducto
 */
public class PedidoService {
    private final PedidoRepository repository;

    /**
     * Constructor del servicio.
     *
     * @param repository Repositorio de pedidos a utilizar
     */
    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    /**
     * Lista todos los pedidos del sistema.
     *
     * @return Lista de todos los {@link Pedido} ordenados por fecha de creación descendente
     * @throws RuntimeException Si ocurre un error al acceder a la base de datos
     * @see PedidoRepository#findAll()
     */
    public List<Pedido> listarPedidos() {
        try {
            return repository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pedidos: " + e.getMessage(), e);
        }
    }

    /**
     * Lista pedidos filtrados por una fecha específica.
     * <p>
     * Util para reportes diarios y seguimiento de producción por día.
     * </p>
     *
     * @param fecha Fecha en formato "YYYY-MM-DD" para filtrar los pedidos
     * @return Lista de {@link Pedido} creados en la fecha especificada
     * @throws RuntimeException Si:
     *         <ul>
     *           <li>La fecha es nula, vacía o tiene formato inválido</li>
     *           <li>Ocurre un error al acceder a la base de datos</li>
     *         </ul>
     * @see PedidoRepository#findByFecha(String)
     */
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

    /**
     * Obtiene un pedido específico por su ID.
     *
     * @param id ID del pedido a obtener
     * @return El {@link Pedido} encontrado con todos sus productos asociados
     * @throws RuntimeException Si:
     *         <ul>
     *           <li>No se encuentra el pedido con el ID especificado</li>
     *           <li>Ocurre un error al acceder a la base de datos</li>
     *         </ul>
     * @see PedidoRepository#findById(Long)
     */
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

    /**
     * Crea un nuevo pedido en el sistema.
     * <p>
     * Valida los datos del pedido, calcula totales automáticamente, genera
     * un resumen descriptivo y persiste tanto el pedido como sus productos.
     * </p>
     *
     * @param pedido Objeto {@link Pedido} con los datos del nuevo pedido
     * @return El {@link Pedido} creado con su ID generado y productos asociados
     * @throws RuntimeException Si:
     *         <ul>
     *           <li>El nombre del cliente es nulo o vacío</li>
     *           <li>El pedido no tiene productos</li>
     *           <li>El total es nulo o menor/igual a cero</li>
     *           <li>Ocurre un error al acceder a la base de datos</li>
     *         </ul>
     * @see PedidoRepository#create(Pedido)
     * @see PedidoRepository#createProductos(Long, List)
     */
    public Pedido crearPedido(Pedido pedido) {
        try {
            // Validaciones básicas
            if (pedido.getClienteNombre() == null || pedido.getClienteNombre().trim().isEmpty()) {
                throw new RuntimeException("El nombre del cliente es requerido");
            }
            if (pedido.getProductos() == null || pedido.getProductos().isEmpty()) {
                throw new RuntimeException("Debe agregar al menos un producto al pedido");
            }
            if (pedido.getTotal() == null || pedido.getTotal().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("El total del pedido debe ser mayor a cero");
            }

            // Calcular cantidad total
            int totalCantidad = pedido.getProductos().stream()
                    .mapToInt(PedidoProducto::getCantidad)
                    .sum();
            pedido.setTotalCantidad(totalCantidad);

            // Generar resumen descriptivo del producto
            String primerProducto = pedido.getProductos().get(0).getProductoNombre();
            if (pedido.getProductos().size() > 1) {
                pedido.setResumenProducto(primerProducto.substring(0, Math.min(primerProducto.length(), 30)) +
                        "... (+" + (pedido.getProductos().size() - 1) + " items)");
            } else {
                pedido.setResumenProducto(primerProducto);
            }

            // Valores por defecto
            if (pedido.getEtapa() == null) {
                pedido.setEtapa("Pendiente por realizar");
            }

            if (pedido.getCreadoPor() == null) {
                pedido.setCreadoPor("admin");
            }

            // Persistir pedido y productos
            Long pedidoId = repository.create(pedido);
            repository.createProductos(pedidoId, pedido.getProductos());

            return obtenerPedido(pedidoId);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear pedido: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza la etapa de un pedido existente.
     * <p>
     * Cambia la etapa actual del pedido y registra el cambio en el historial.
     * Cuando la nueva etapa es "Finalizado", se activa automáticamente la
     * creación de una venta asociada al pedido.
     * </p>
     *
     * @param id ID del pedido a actualizar
     * @param nuevaEtapa Nueva etapa a asignar al pedido
     * @param notas Notas u observaciones sobre el cambio de etapa
     * @return El {@link Pedido} actualizado
     * @throws RuntimeException Si:
     *         <ul>
     *           <li>No se encuentra el pedido con el ID especificado</li>
     *           <li>No se puede actualizar la etapa</li>
     *           <li>Ocurre un error al acceder a la base de datos</li>
     *         </ul>
     * @see PedidoRepository#updateEtapa(Long, String, String)
     * @see #crearVentaDesdePedido(Long)
     */
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

            // Integración automática con ventas cuando el pedido se finaliza
            if ("Finalizado".equals(nuevaEtapa)) {
                crearVentaDesdePedido(id);
            }

            return obtenerPedido(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar etapa: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un pedido del sistema (eliminación en cascada).
     * <p>
     * Elimina tanto el pedido como todos sus productos asociados.
     * Esta operación es irreversible.
     * </p>
     *
     * @param id ID del pedido a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     * @throws RuntimeException Si:
     *         <ul>
     *           <li>No se encuentra el pedido con el ID especificado</li>
     *           <li>Ocurre un error al acceder a la base de datos</li>
     *         </ul>
     * @see PedidoRepository#delete(Long)
     */
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

    /**
     * Crea automáticamente una venta cuando un pedido es finalizado.
     * <p>
     * Método privado que se ejecuta cuando un pedido alcanza la etapa "Finalizado".
     * Toma el primer producto del pedido y crea una venta asociada para
     * integrar con el módulo de ventas del sistema.
     * </p>
     * <p>
     * <strong>Nota:</strong> Actualmente usa un cliente por defecto (ID: 1)
     * ya que el modelo Pedido no incluye clienteId. En una implementación
     * completa, el modelo debería incluir esta información.
     * </p>
     *
     * @param pedidoId ID del pedido finalizado
     * @throws RuntimeException Si ocurre un error durante la creación de la venta
     * @see VentaRepository#crearVenta(Venta)
     */
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

            // Usar clienteId por defecto (1) ya que el modelo Pedido no tiene clienteId
            // En una implementación real, necesitarías agregar clienteId al modelo Pedido
            int clienteIdDefault = 1; // Cliente genérico

            // Crear la venta según el modelo Venta actual
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

            // Guardar la venta usando el repositorio de ventas
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