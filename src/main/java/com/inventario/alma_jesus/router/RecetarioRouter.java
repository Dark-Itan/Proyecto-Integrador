package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.RecetarioController;
import com.inventario.alma_jesus.service.RecetarioService;
import com.inventario.alma_jesus.repository.RecetarioRepository;
import io.javalin.Javalin;

/**
 * Router para la configuración de rutas de gestión de recetarios.
 * <p>
 * Esta clase define y configura todas las rutas relacionadas con la gestión
 * de recetas de fabricación en la API REST. Las recetas contienen las
 * instrucciones técnicas, materiales y tiempos necesarios para fabricar
 * productos de manera estandarizada.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see RecetarioController
 * @see RecetarioService
 * @see RecetarioRepository
 */
public class RecetarioRouter {

    /**
     * Configura todas las rutas de gestión de recetarios en la aplicación Javalin.
     * <p>
     * Inicializa las dependencias necesarias (repositorio, servicio, controlador)
     * siguiendo el patrón de diseño Repository-Service-Controller y define los
     * siguientes endpoints bajo el path base /api/v1/recetas:
     * <ul>
     *   <li>GET /api/v1/recetas - Listar todas las recetas disponibles</li>
     *   <li>GET /api/v1/recetas/trabajador - Listar recetas para visualización de trabajadores</li>
     *   <li>GET /api/v1/recetas/{id} - Obtener receta específica por ID con todos sus detalles</li>
     *   <li>POST /api/v1/recetas - Crear nueva receta de fabricación</li>
     *   <li>PUT /api/v1/recetas/{id} - Actualizar receta existente</li>
     *   <li>DELETE /api/v1/recetas/{id} - Eliminar receta del sistema</li>
     * </ul>
     * Cada receta incluye información sobre materiales requeridos, tiempo de fabricación,
     * instrucciones técnicas y herramientas necesarias.
     * </p>
     *
     * @param app Instancia de {@link Javalin} donde se configurarán las rutas
     * @throws IllegalArgumentException Si la aplicación Javalin es nula
     */
    public void configureRoutes(Javalin app) {
        // Inicialización de dependencias siguiendo el patrón Repository-Service-Controller
        RecetarioRepository repository = new RecetarioRepository();
        RecetarioService service = new RecetarioService(repository);
        RecetarioController controller = new RecetarioController(service);

        // Endpoint: Listar todas las recetas (para administradores)
        // GET /api/v1/recetas
        app.get("/api/v1/recetas", controller.listarRecetas);

        // Endpoint: Listar recetas para trabajadores
        // GET /api/v1/recetas/trabajador
        // Específico para trabajadores que necesitan visualizar recetas de fabricación
        app.get("/api/v1/recetas/trabajador", controller.listarRecetasTrabajador);

        // Endpoint: Obtener receta específica por ID
        // GET /api/v1/recetas/{id}
        // Incluye materiales, instrucciones y herramientas asociadas
        app.get("/api/v1/recetas/{id}", controller.obtenerReceta);

        // Endpoint: Crear nueva receta
        // POST /api/v1/recetas
        // Body debe incluir productoId, tiempoFabricacion, instrucciones, materiales, etc.
        app.post("/api/v1/recetas", controller.crearReceta);

        // Endpoint: Actualizar receta existente
        // PUT /api/v1/recetas/{id}
        // Permite modificar todos los campos excepto el ID
        app.put("/api/v1/recetas/{id}", controller.actualizarReceta);

        // Endpoint: Eliminar receta
        // DELETE /api/v1/recetas/{id}
        // Elimina la receta y sus relaciones con materiales
        app.delete("/api/v1/recetas/{id}", controller.eliminarReceta);
    }
}