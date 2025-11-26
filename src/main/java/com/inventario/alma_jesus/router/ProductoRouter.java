package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.ProductoController;
import io.javalin.Javalin;

public class ProductoRouter {
    private ProductoController productoController = new ProductoController();

    public void configureRoutes(Javalin app) {

        //  ENDPOINT 9: LISTAR PRODUCTOS
        // GET http://localhost:7000/api/v1/productos
        app.get("/api/v1/productos", productoController::listarProductos);

        //  ENDPOINT NUEVO: FILTRAR PRODUCTOS POR TIPO
        // GET http://localhost:7000/api/v1/productos/filtrar?tipo=religiosas
        app.get("/api/v1/productos/filtrar", productoController::listarProductosPorTipo);

        //  ENDPOINT 10: OBTENER PRODUCTO ESPECÍFICO
        // GET http://localhost:7000/api/v1/productos/{id}
        // Ejemplo: GET http://localhost:7000/api/v1/productos/1
        app.get("/api/v1/productos/{id}", productoController::obtenerProductoPorId);

        //  ENDPOINT 11: CREAR PRODUCTO
        // POST http://localhost:7000/api/v1/productos
        app.post("/api/v1/productos", productoController::crearProducto);

        //  ENDPOINT 12: ACTUALIZAR PRODUCTO
        // PUT http://localhost:7000/api/v1/productos/{id}
        // Ejemplo: PUT http://localhost:7000/api/v1/productos/1
        app.put("/api/v1/productos/{id}", productoController::actualizarProducto);

        //  ENDPOINT 13: ELIMINAR PRODUCTO (eliminación lógica)
        // DELETE http://localhost:7000/api/v1/productos/{id}
        // Ejemplo: DELETE http://localhost:7000/api/v1/productos/1
        app.delete("/api/v1/productos/{id}", productoController::eliminarProducto);

        // ENDPOINT 14: PUBLICAR PRECIOS
        // POST http://localhost:7000/api/v1/productos/publish
        app.post("/api/v1/productos/publish", productoController::publicarPrecios);

        // ENDPOINT 15: SUBIR IMAGEN A CLOUDINARY (NUEVO)
        // POST http://localhost:7000/api/v1/productos/upload
        app.post("/api/v1/productos/upload", productoController::subirImagen);

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