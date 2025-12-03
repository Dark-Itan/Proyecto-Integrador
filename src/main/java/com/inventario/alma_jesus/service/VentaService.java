package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.Venta;
import com.inventario.alma_jesus.repository.VentaRepository;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de ventas del sistema.
 * <p>
 * Esta clase contiene la lógica de negocio para todas las operaciones relacionadas
 * con ventas, incluyendo listado, consulta, registro y eliminación. Maneja la
 * transformación de datos entre el modelo {@link Venta} y el formato de respuesta
 * estructurada requerido por la API.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see VentaRepository
 * @see Venta
 */
public class VentaService {
    private VentaRepository ventaRepository = new VentaRepository();

    /**
     * Lista todas las ventas del sistema con formato estructurado.
     * <p>
     * Retorna todas las ventas registradas, transformadas a un formato de mapa
     * para facilitar su serialización a JSON en las respuestas de la API.
     * </p>
     *
     * @return Mapa con los siguientes elementos:
     *         <ul>
     *           <li>success: boolean indicando si la operación fue exitosa</li>
     *           <li>message: Mensaje descriptivo del resultado</li>
     *           <li>ventas: Lista de ventas formateadas como mapas</li>
     *           <li>total: Cantidad total de ventas listadas</li>
     *         </ul>
     * @throws RuntimeException Si ocurre un error interno no manejado
     * @see VentaRepository#findAll()
     */
    public Map<String, Object> listarVentas() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Venta> ventas = ventaRepository.findAll();

            List<Map<String, Object>> ventasFormateadas = ventas.stream()
                    .map(venta -> {
                        Map<String, Object> ventaMap = new HashMap<>();
                        ventaMap.put("id", venta.getId());
                        ventaMap.put("clienteId", venta.getClienteId());
                        ventaMap.put("productoId", venta.getProductoId());
                        ventaMap.put("productoModelo", venta.getProductoModelo());
                        ventaMap.put("cantidad", venta.getCantidad());
                        ventaMap.put("precioUnitario", venta.getPrecioUnitario());
                        ventaMap.put("precioTotal", venta.getPrecioTotal());
                        ventaMap.put("fecha", venta.getFecha());
                        ventaMap.put("tipo", venta.getTipo());
                        ventaMap.put("usuarioRegistro", venta.getUsuarioRegistro());
                        ventaMap.put("fechaRegistro", venta.getFechaRegistro());
                        return ventaMap;
                    })
                    .collect(Collectors.toList());

            response.put("success", true);
            response.put("message", "Ventas listadas exitosamente");
            response.put("ventas", ventasFormateadas);
            response.put("total", ventasFormateadas.size());

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al listar ventas: " + e.getMessage());
        }

        return response;
    }

    /**
     * Obtiene una venta específica por su ID.
     *
     * @param id ID de la venta a obtener
     * @return Mapa con los siguientes elementos:
     *         <ul>
     *           <li>success: boolean indicando si la operación fue exitosa</li>
     *           <li>message: Mensaje descriptivo del resultado</li>
     *           <li>venta: Detalles de la venta encontrada (solo si success=true)</li>
     *         </ul>
     * @see VentaRepository#findById(int)
     */
    public Map<String, Object> obtenerVentaPorId(int id) {
        Map<String, Object> response = new HashMap<>();

        try {
            var ventaOpt = ventaRepository.findById(id);

            if (ventaOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Venta no encontrada");
                return response;
            }

            Venta venta = ventaOpt.get();

            Map<String, Object> ventaDetallada = new HashMap<>();
            ventaDetallada.put("id", venta.getId());
            ventaDetallada.put("clienteId", venta.getClienteId());
            ventaDetallada.put("productoId", venta.getProductoId());
            ventaDetallada.put("productoModelo", venta.getProductoModelo());
            ventaDetallada.put("cantidad", venta.getCantidad());
            ventaDetallada.put("precioUnitario", venta.getPrecioUnitario());
            ventaDetallada.put("precioTotal", venta.getPrecioTotal());
            ventaDetallada.put("fecha", venta.getFecha());
            ventaDetallada.put("tipo", venta.getTipo());
            ventaDetallada.put("usuarioRegistro", venta.getUsuarioRegistro());
            ventaDetallada.put("fechaRegistro", venta.getFechaRegistro());

            response.put("success", true);
            response.put("message", "Venta encontrada exitosamente");
            response.put("venta", ventaDetallada);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener venta: " + e.getMessage());
        }

        return response;
    }

    /**
     * Registra una nueva venta en el sistema.
     * <p>
     * Valida que todos los campos requeridos estén presentes, crea un objeto
     * {@link Venta} y lo persiste en la base de datos. Incluye verificación
     * de duplicados para evitar ventas repetidas.
     * </p>
     *
     * @param ventaData Mapa con los datos de la nueva venta, debe contener:
     *                  <ul>
     *                    <li>clienteId: ID del cliente (entero)</li>
     *                    <li>productoId: ID del producto (entero)</li>
     *                    <li>cantidad: Cantidad vendida (entero)</li>
     *                    <li>precioUnitario: Precio por unidad en centavos (entero)</li>
     *                    <li>fecha: Fecha de la venta en formato "YYYY-MM-DD" (string)</li>
     *                    <li>tipo: Tipo de transacción (string, ej: "EFECTIVO", "TARJETA")</li>
     *                    <li>usuarioRegistro: ID del usuario que registra la venta (string)</li>
     *                  </ul>
     * @return Mapa con los siguientes elementos:
     *         <ul>
     *           <li>success: boolean indicando si el registro fue exitoso</li>
     *           <li>message: Mensaje descriptivo del resultado</li>
     *         </ul>
     * @throws NumberFormatException Si los campos numéricos no pueden ser convertidos a enteros
     * @see VentaRepository#crearVenta(Venta)
     * @see VentaRepository#findDuplicadas(Venta)
     */
    public Map<String, Object> registrarVenta(Map<String, Object> ventaData) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("Iniciando registro de venta con datos: " + ventaData);

            // Validación de campos requeridos
            if (ventaData.get("clienteId") == null || ventaData.get("productoId") == null ||
                    ventaData.get("cantidad") == null || ventaData.get("precioUnitario") == null ||
                    ventaData.get("fecha") == null || ventaData.get("tipo") == null ||
                    ventaData.get("usuarioRegistro") == null) {

                System.out.println("Campos faltantes en ventaData");
                response.put("success", false);
                response.put("message", "Todos los campos son requeridos");
                return response;
            }

            // Crear objeto Venta a partir de los datos
            Venta nuevaVenta = new Venta();
            nuevaVenta.setClienteId(Integer.parseInt(ventaData.get("clienteId").toString()));
            nuevaVenta.setProductoId(Integer.parseInt(ventaData.get("productoId").toString()));
            nuevaVenta.setCantidad(Integer.parseInt(ventaData.get("cantidad").toString()));
            nuevaVenta.setPrecioUnitario(Integer.parseInt(ventaData.get("precioUnitario").toString()));
            nuevaVenta.setFecha(ventaData.get("fecha").toString());
            nuevaVenta.setTipo(ventaData.get("tipo").toString());
            nuevaVenta.setUsuarioRegistro(ventaData.get("usuarioRegistro").toString());

            System.out.println("Venta creada en servicio: " +
                    "ProductoID=" + nuevaVenta.getProductoId() +
                    ", Cantidad=" + nuevaVenta.getCantidad() +
                    ", Fecha=" + nuevaVenta.getFecha() +
                    ", Tipo=" + nuevaVenta.getTipo());

            // Verificación de duplicados para debugging
            List<Venta> duplicadas = ventaRepository.findDuplicadas(nuevaVenta);
            if (!duplicadas.isEmpty()) {
                System.out.println("Se encontraron ventas duplicadas existentes:");
                for (Venta dup : duplicadas) {
                    System.out.println("   - ID: " + dup.getId() + ", FechaRegistro: " + dup.getFechaRegistro());
                }
            }

            // Persistir la venta
            boolean creada = ventaRepository.crearVenta(nuevaVenta);

            if (creada) {
                System.out.println(" Venta registrada exitosamente en servicio");
                response.put("success", true);
                response.put("message", "Venta registrada exitosamente");
            } else {
                System.out.println(" No se pudo crear la venta en el repositorio (posible duplicado)");
                response.put("success", false);
                response.put("message", "Error al registrar venta o venta duplicada");
            }

        } catch (Exception e) {
            System.out.println("❌ Error en VentaService.registrarVenta: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al registrar venta: " + e.getMessage());
        }

        return response;
    }

    /**
     * Elimina una venta del sistema.
     *
     * @param id ID de la venta a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     * @see VentaRepository#eliminarVenta(int)
     */
    public boolean eliminarVenta(int id) {
        try {
            return ventaRepository.eliminarVenta(id);
        } catch (Exception e) {
            System.out.println("Error en VentaService.eliminarVenta: " + e.getMessage());
            return false;
        }
    }
}