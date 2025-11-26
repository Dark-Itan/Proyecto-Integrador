package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.Producto;
import com.inventario.alma_jesus.repository.ProductoRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ProductoService {
    private ProductoRepository productoRepository = new ProductoRepository();

    public Map<String, Object> listarProductos() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Producto> productos = productoRepository.findAll();

            List<Map<String, Object>> productosSeguros = productos.stream()
                    .map(producto -> {
                        Map<String, Object> productoMap = new HashMap<>();
                        productoMap.put("id", producto.getId());
                        productoMap.put("modelo", producto.getModelo());
                        productoMap.put("color", producto.getColor());
                        productoMap.put("precio", producto.getPrecio());
                        productoMap.put("stock", producto.getStock());
                        productoMap.put("tamaño", producto.getTamaño());
                        productoMap.put("imagenUrl", producto.getImagenUrl());
                        productoMap.put("activo", producto.isActivo());
                        productoMap.put("creadoPor", producto.getCreadoPor());
                        productoMap.put("fechaCreacion", producto.getFechaCreacion());
                        productoMap.put("tipo", producto.getTipo());
                        return productoMap;
                    })
                    .collect(Collectors.toList());

            response.put("success", true);
            response.put("message", "Productos listados exitosamente");
            response.put("productos", productosSeguros);
            response.put("total", productosSeguros.size());

        } catch (Exception e) {
            System.out.println("Error en ProductoService.listarProductos: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al listar productos: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> listarProductosPorTipo(String tipo) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Producto> productos = productoRepository.findByTipo(tipo);

            List<Map<String, Object>> productosSeguros = productos.stream()
                    .map(producto -> {
                        Map<String, Object> productoMap = new HashMap<>();
                        productoMap.put("id", producto.getId());
                        productoMap.put("modelo", producto.getModelo());
                        productoMap.put("color", producto.getColor());
                        productoMap.put("precio", producto.getPrecio());
                        productoMap.put("stock", producto.getStock());
                        productoMap.put("tamaño", producto.getTamaño());
                        productoMap.put("imagenUrl", producto.getImagenUrl());
                        productoMap.put("tipo", producto.getTipo());
                        return productoMap;
                    })
                    .collect(Collectors.toList());

            response.put("success", true);
            response.put("productos", productosSeguros);
            response.put("total", productosSeguros.size());

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al filtrar productos: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> obtenerProductoPorId(int id) {
        Map<String, Object> response = new HashMap<>();

        try {
            var productoOpt = productoRepository.findById(id);

            if (productoOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Producto no encontrado");
                return response;
            }

            Producto producto = productoOpt.get();

            Map<String, Object> productoSeguro = new HashMap<>();
            productoSeguro.put("id", producto.getId());
            productoSeguro.put("modelo", producto.getModelo());
            productoSeguro.put("color", producto.getColor());
            productoSeguro.put("precio", producto.getPrecio());
            productoSeguro.put("stock", producto.getStock());
            productoSeguro.put("tamaño", producto.getTamaño());
            productoSeguro.put("imagenUrl", producto.getImagenUrl());
            productoSeguro.put("activo", producto.isActivo());
            productoSeguro.put("creadoPor", producto.getCreadoPor());
            productoSeguro.put("fechaCreacion", producto.getFechaCreacion());
            productoSeguro.put("tipo", producto.getTipo());

            response.put("success", true);
            response.put("message", "Producto encontrado exitosamente");
            response.put("producto", productoSeguro);

        } catch (Exception e) {
            System.out.println("Error en ProductoService.obtenerProductoPorId: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al obtener producto: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> crearProducto(Map<String, Object> productoData) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (productoData.get("modelo") == null || productoData.get("color") == null ||
                    productoData.get("precio") == null || productoData.get("stock") == null ||
                    productoData.get("creadoPor") == null) {

                response.put("success", false);
                response.put("message", "Modelo, color, precio, stock y creadoPor son requeridos");
                return response;
            }

            Producto nuevoProducto = new Producto();
            nuevoProducto.setModelo(productoData.get("modelo").toString());
            nuevoProducto.setColor(productoData.get("color").toString());
            nuevoProducto.setPrecio(Integer.parseInt(productoData.get("precio").toString()));
            nuevoProducto.setStock(Integer.parseInt(productoData.get("stock").toString()));
            nuevoProducto.setTamaño(productoData.get("tamaño") != null ? productoData.get("tamaño").toString() : "200x300");
            nuevoProducto.setImagenUrl(productoData.get("imagenUrl") != null ? productoData.get("imagenUrl").toString() : "");
            nuevoProducto.setCreadoPor(productoData.get("creadoPor").toString());
            nuevoProducto.setTipo(productoData.get("tipo") != null ? productoData.get("tipo").toString() : "religiosas");

            boolean creado = productoRepository.crearProducto(nuevoProducto);

            if (creado) {
                response.put("success", true);
                response.put("message", "Producto creado exitosamente");
                response.put("producto", Map.of(
                        "modelo", nuevoProducto.getModelo(),
                        "color", nuevoProducto.getColor(),
                        "precio", nuevoProducto.getPrecio(),
                        "stock", nuevoProducto.getStock(),
                        "tamaño", nuevoProducto.getTamaño(),
                        "tipo", nuevoProducto.getTipo()
                ));
            } else {
                response.put("success", false);
                response.put("message", "Error al crear producto en la base de datos");
            }

        } catch (Exception e) {
            System.out.println("Error en ProductoService.crearProducto: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al crear producto: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> actualizarProducto(int id, Map<String, Object> productoData) {
        Map<String, Object> response = new HashMap<>();

        try {
            var productoOpt = productoRepository.findById(id);

            if (productoOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Producto no encontrado");
                return response;
            }

            Producto productoExistente = productoOpt.get();

            if (productoData.get("modelo") != null) {
                productoExistente.setModelo(productoData.get("modelo").toString());
            }
            if (productoData.get("color") != null) {
                productoExistente.setColor(productoData.get("color").toString());
            }
            if (productoData.get("precio") != null) {
                productoExistente.setPrecio(Integer.parseInt(productoData.get("precio").toString()));
            }
            if (productoData.get("stock") != null) {
                productoExistente.setStock(Integer.parseInt(productoData.get("stock").toString()));
            }
            if (productoData.get("tamaño") != null) {
                productoExistente.setTamaño(productoData.get("tamaño").toString());
            }
            if (productoData.get("imagenUrl") != null) {
                productoExistente.setImagenUrl(productoData.get("imagenUrl").toString());
            }
            if (productoData.get("tipo") != null) {
                productoExistente.setTipo(productoData.get("tipo").toString());
            }

            boolean actualizado = productoRepository.actualizarProducto(productoExistente);

            if (actualizado) {
                response.put("success", true);
                response.put("message", "Producto actualizado exitosamente");
                response.put("producto", Map.of(
                        "id", productoExistente.getId(),
                        "modelo", productoExistente.getModelo(),
                        "color", productoExistente.getColor(),
                        "precio", productoExistente.getPrecio(),
                        "stock", productoExistente.getStock(),
                        "tipo", productoExistente.getTipo()
                ));
            } else {
                response.put("success", false);
                response.put("message", "Error al actualizar producto");
            }

        } catch (Exception e) {
            System.out.println("Error en ProductoService.actualizarProducto: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al actualizar producto: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> eliminarProducto(int id) {
        Map<String, Object> response = new HashMap<>();

        try {
            var productoOpt = productoRepository.findById(id);

            if (productoOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Producto no encontrado");
                return response;
            }

            Producto producto = productoOpt.get();

            boolean eliminado = productoRepository.eliminarProducto(id);

            if (eliminado) {
                response.put("success", true);
                response.put("message", "Producto eliminado exitosamente");
                response.put("producto", producto.getModelo());
            } else {
                response.put("success", false);
                response.put("message", "Error al eliminar producto");
            }

        } catch (Exception e) {
            System.out.println("Error en ProductoService.eliminarProducto: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al eliminar producto: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> publicarPrecios() {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean publicados = productoRepository.publicarPrecios();

            if (publicados) {
                response.put("success", true);
                response.put("message", "Precios publicados exitosamente en el catálogo");
            } else {
                response.put("success", false);
                response.put("message", "Error al publicar precios");
            }

        } catch (Exception e) {
            System.out.println("Error en ProductoService.publicarPrecios: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al publicar precios: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> subirImagen(byte[] fileBytes, String fileName) {
        Map<String, Object> response = new HashMap<>();

        try {
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "djnss3at2",
                    "api_key", "193776531776879",
                    "api_secret", "7L8Wp1cVYgJ6quc2b8EByZ-hrpA"
            ));

            Map<?, ?> uploadResult = cloudinary.uploader()
                    .upload(fileBytes, ObjectUtils.asMap(
                            "public_id", "alma_jesus/" + System.currentTimeMillis() + "_" + fileName,
                            "folder", "alma_jesus/productos"
                    ));

            String imageUrl = (String) uploadResult.get("secure_url");

            response.put("success", true);
            response.put("message", "Imagen subida exitosamente");
            response.put("imageUrl", imageUrl);

        } catch (Exception e) {
            System.out.println("Error subiendo imagen: " + e.getMessage());
            response.put("success", false);
            response.put("message", "Error al subir imagen: " + e.getMessage());
        }

        return response;
    }
}