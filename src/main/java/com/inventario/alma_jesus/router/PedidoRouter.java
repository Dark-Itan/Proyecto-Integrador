package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.PedidoController;
import com.inventario.alma_jesus.service.PedidoService;
import com.inventario.alma_jesus.repository.PedidoRepository;
import io.javalin.Javalin;

public class PedidoRouter {
    public void configureRoutes(Javalin app) {
        PedidoRepository repository = new PedidoRepository();
        PedidoService service = new PedidoService(repository);

        PedidoController controller = new PedidoController(service);

        app.get("/api/v1/pedidos/fecha", controller.listarPedidosPorFecha);
        app.get("/api/v1/pedidos", controller.listarPedidos);
        app.get("/api/v1/pedidos/{id}", controller.obtenerPedido);
        app.post("/api/v1/pedidos", controller.crearPedido);
        app.put("/api/v1/pedidos/{id}/etapa", controller.actualizarEtapa);
        app.delete("/api/v1/pedidos/{id}", controller.eliminarPedido);

    }
}