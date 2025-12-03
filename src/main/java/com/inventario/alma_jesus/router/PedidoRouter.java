package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.PedidoController;
import com.inventario.alma_jesus.service.PedidoService;
import com.inventario.alma_jesus.repository.PedidoRepository;
import io.javalin.Javalin;

/**
 * Router para la configuración de rutas de gestión de pedidos.
 * <p>
 * Esta clase define y configura todas las rutas relacionadas con la gestión
 * de pedidos de fabricación en la API REST. Incluye operaciones para crear,
 * consultar, actualizar etapas y eliminar pedidos, siguiendo el flujo de
 * trabajo del taller de producción.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see PedidoController
 * @see PedidoService
 * @see PedidoRepository
 */
public class PedidoRouter {

    /**
     * Configura todas las rutas de gestión de pedidos en la aplicación Javalin.
     * <p>
     * Inicializa las dependencias necesarias (repositorio, servicio, controlador)
     * y define los siguientes endpoints bajo el path base /api/v1/pedidos:
     * <ul>
     *   <li>GET /api/v1/pedidos/fecha - Listar pedidos filtrados por fecha específica (query param: fecha)</li>
     *   <li>GET /api/v1/pedidos - Listar todos los pedidos</li>
     *   <li>GET /api/v1/pedidos/{id} - Obtener pedido específico por ID</li>
     *   <li>POST /api/v1/pedidos - Crear nuevo pedido</li>
     *   <li>PUT /api/v1/pedidos/{id}/etapa - Actualizar etapa del pedido</li>
     *   <li>DELETE /api/v1/pedidos/{id} - Eliminar pedido (eliminación en cascada)</li>
     * </ul>
     * El sistema sigue el patrón de diseño Repository-Service-Controller
     * para separación de responsabilidades.
     * </p>
     *
     * @param app Instancia de {@link Javalin} donde se configurarán las rutas
     * @throws IllegalArgumentException Si la aplicación Javalin es nula
     */
    public void configureRoutes(Javalin app) {
        // Inicialización de dependencias siguiendo el patrón Repository-Service-Controller
        PedidoRepository repository = new PedidoRepository();
        PedidoService service = new PedidoService(repository);
        PedidoController controller = new PedidoController(service);

        // Endpoint: Listar pedidos por fecha específica
        // GET /api/v1/pedidos/fecha?fecha=YYYY-MM-DD
        app.get("/api/v1/pedidos/fecha", controller.listarPedidosPorFecha);

        // Endpoint: Listar todos los pedidos
        // GET /api/v1/pedidos
        app.get("/api/v1/pedidos", controller.listarPedidos);

        // Endpoint: Obtener pedido específico por ID
        // GET /api/v1/pedidos/{id}
        app.get("/api/v1/pedidos/{id}", controller.obtenerPedido);

        // Endpoint: Crear nuevo pedido
        // POST /api/v1/pedidos
        app.post("/api/v1/pedidos", controller.crearPedido);

        // Endpoint: Actualizar etapa del pedido
        // PUT /api/v1/pedidos/{id}/etapa
        app.put("/api/v1/pedidos/{id}/etapa", controller.actualizarEtapa);

        // Endpoint: Eliminar pedido
        // DELETE /api/v1/pedidos/{id}
        app.delete("/api/v1/pedidos/{id}", controller.eliminarPedido);
    }
}