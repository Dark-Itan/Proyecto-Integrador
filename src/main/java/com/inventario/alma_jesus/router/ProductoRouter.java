package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.ProductoController;
import io.javalin.Javalin;

/**
 * Router para la configuración de rutas de gestión de productos.
 * <p>
 * Esta clase define y configura todas las rutas relacionadas con la gestión
 * de productos en el catálogo del sistema. Incluye operaciones CRUD completas,
 * filtrado por tipo, publicación de precios y carga de imágenes a Cloudinary.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see ProductoController
 */
public class ProductoRouter {
    private ProductoController productoController = new ProductoController();

    /**
     * Configura todas las rutas de gestión de productos en la aplicación Javalin.
     * <p>
     * Define los siguientes endpoints bajo el path base /api/v1/productos:
     * <ul>
     *   <li>GET /api/v1/productos - Listar todos los productos</li>
     *   <li>GET /api/v1/productos/filtrar - Filtrar productos por tipo (query param: tipo)</li>
     *   <li>GET /api/v1/productos/{id} - Obtener producto específico por ID</li>
     *   <li>POST /api/v1/productos - Crear nuevo producto</li>
     *   <li>PUT /api/v1/productos/{id} - Actualizar producto existente</li>
     *   <li>DELETE /api/v1/productos/{id} - Eliminar producto (eliminación lógica)</li>
     *   <li>POST /api/v1/productos/publish - Publicar precios (para sincronización con sistemas externos)</li>
     *   <li>POST /api/v1/productos/upload - Subir imagen a Cloudinary y asociarla a producto</li>
     * </ul>
     * También imprime en consola un resumen de todas las rutas configuradas.
     * </p>
     *
     * @param app Instancia de {@link Javalin} donde se configurarán las rutas
     * @throws IllegalArgumentException Si la aplicación Javalin es nula
     */
    public void configureRoutes(Javalin app) {

        // ENDPOINT 9: LISTAR PRODUCTOS
        // GET http://localhost:7000/api/v1/productos
        app.get("/api/v1/productos", productoController::listarProductos);

        // ENDPOINT NUEVO: FILTRAR PRODUCTOS POR TIPO
        // GET http://localhost:7000/api/v1/productos/filtrar?tipo=religiosas
        app.get("/api/v1/productos/filtrar", productoController::listarProductosPorTipo);

        // ENDPOINT 10: OBTENER PRODUCTO ESPECÍFICO
        // GET http://localhost:7000/api/v1/productos/{id}
        // Ejemplo: GET http://localhost:7000/api/v1/productos/1
        app.get("/api/v1/productos/{id}", productoController::obtenerProductoPorId);

        // ENDPOINT 11: CREAR PRODUCTO
        // POST http://localhost:7000/api/v1/productos
        app.post("/api/v1/productos", productoController::crearProducto);

        // ENDPOINT 12: ACTUALIZAR PRODUCTO
        // PUT http://localhost:7000/api/v1/productos/{id}
        // Ejemplo: PUT http://localhost:7000/api/v1/productos/1
        app.put("/api/v1/productos/{id}", productoController::actualizarProducto);

        // ENDPOINT 13: ELIMINAR PRODUCTO (eliminación lógica)
        // DELETE http://localhost:7000/api/v1/productos/{id}
        // Ejemplo: DELETE http://localhost:7000/api/v1/productos/1
        app.delete("/api/v1/productos/{id}", productoController::eliminarProducto);

        // ENDPOINT 14: PUBLICAR PRECIOS
        // POST http://localhost:7000/api/v1/productos/publish
        app.post("/api/v1/productos/publish", productoController::publicarPrecios);

        // ENDPOINT 15: SUBIR IMAGEN A CLOUDINARY (NUEVO)
        // POST http://localhost:7000/api/v1/productos/upload
        app.post("/api/v1/productos/upload", productoController::subirImagen);

        // Log de configuración para depuración
        System.out.println("ProductoRouter: Rutas de productos configuradas");
        System.out.println("    GET    /api/v1/productos              - Listar productos");
        System.out.println("    GET    /api/v1/productos/filtrar      - Filtrar productos por tipo");
        System.out.println("    GET    /api/v1/productos/{id}         - Obtener producto por ID");
        System.out.println("    POST   /api/v1/productos              - Crear producto");
        System.out.println("   ️ PUT    /api/v1/productos/{id}         - Actualizar producto");
        System.out.println("   DELETE /api/v1/productos/{id}         - Eliminar producto");
        System.out.println("    POST   /api/v1/productos/publish      - Publicar precios");
        System.out.println("    POST   /api/v1/productos/upload       - Subir imagen a Cloudinary");
    }
}