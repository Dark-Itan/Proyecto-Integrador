package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.Venta;
import com.inventario.alma_jesus.repository.VentaRepository;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class VentaService {
    private VentaRepository ventaRepository = new VentaRepository();

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

    public Map<String, Object> registrarVenta(Map<String, Object> ventaData) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("Iniciando registro de venta con datos: " + ventaData);

            // Validaciones
            if (ventaData.get("clienteId") == null || ventaData.get("productoId") == null ||
                    ventaData.get("cantidad") == null || ventaData.get("precioUnitario") == null ||
                    ventaData.get("fecha") == null || ventaData.get("tipo") == null ||
                    ventaData.get("usuarioRegistro") == null) {

                System.out.println("Campos faltantes en ventaData");
                response.put("success", false);
                response.put("message", "Todos los campos son requeridos");
                return response;
            }

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

            // Para debugging: buscar duplicados antes de crear
            List<Venta> duplicadas = ventaRepository.findDuplicadas(nuevaVenta);
            if (!duplicadas.isEmpty()) {
                System.out.println("Se encontraron ventas duplicadas existentes:");
                for (Venta dup : duplicadas) {
                    System.out.println("   - ID: " + dup.getId() + ", FechaRegistro: " + dup.getFechaRegistro());
                }
            }

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

    public boolean eliminarVenta(int id) {
        try {
            return ventaRepository.eliminarVenta(id);
        } catch (Exception e) {
            System.out.println("Error en VentaService.eliminarVenta: " + e.getMessage());
            return false;
        }
    }
}