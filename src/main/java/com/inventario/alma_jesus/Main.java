package com.inventario.alma_jesus;

import com.inventario.alma_jesus.router.AuthRouter;
import com.inventario.alma_jesus.router.UsuarioRouter;
import com.inventario.alma_jesus.router.ProductoRouter;
import com.inventario.alma_jesus.router.VentaRouter;
import com.inventario.alma_jesus.router.HerramientaRouter;
import com.inventario.alma_jesus.router.MateriaPrimaRouter;
import com.inventario.alma_jesus.router.ReparacionRouter;
import com.inventario.alma_jesus.router.RecetarioRouter;
import com.inventario.alma_jesus.router.TareaRouter;
import com.inventario.alma_jesus.router.PedidoRouter;
import com.inventario.alma_jesus.router.EstadisticasRouter;
import io.javalin.Javalin;

/**
 * Clase principal de la aplicación de gestión de inventario "Alma Jesús".
 * <p>
 * Esta clase inicia el servidor Javalin y configura todas las rutas de la API REST.
 * El sistema gestiona inventario, ventas, reparaciones, tareas, pedidos y estadísticas
 * para un taller o negocio de reparaciones.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */

public class Main {

    /**
     * Método principal que inicia la aplicación.
     * <p>
     * Configura el servidor Javalin en el puerto 7000, habilita CORS para todas las rutas
     * y registra todos los routers de los diferentes módulos del sistema.
     * </p>
     *
     * @param args Argumentos de línea de comandos (no utilizados)
     *
     * @see AuthRouter
     * @see UsuarioRouter
     * @see ProductoRouter
     * @see VentaRouter
     * @see HerramientaRouter
     * @see MateriaPrimaRouter
     * @see ReparacionRouter
     * @see RecetarioRouter
     * @see TareaRouter
     * @see PedidoRouter
     * @see EstadisticasRouter
     *
     * @example
     * Para ejecutar la aplicación:
     * <pre>
     * java -jar inventario-alma-jesus.jar
     * </pre>
     *
     * La aplicación estará disponible en: http://localhost:7000
     */

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> {
                    it.anyHost();
                });
            });
        }).start(7000);

        // Registrar todas las rutas de los diferentes módulos

        AuthRouter authRouter = new AuthRouter();
        authRouter.configureRoutes(app);

        UsuarioRouter usuarioRouter = new UsuarioRouter();
        usuarioRouter.configureRoutes(app);

        ProductoRouter productoRouter = new ProductoRouter();
        productoRouter.configureRoutes(app);

        VentaRouter ventaRouter = new VentaRouter();
        ventaRouter.configureRoutes(app);

        HerramientaRouter herramientaRouter = new HerramientaRouter();
        herramientaRouter.configureRoutes(app);

        MateriaPrimaRouter.configureRoutes(app);

        ReparacionRouter reparacionRouter = new ReparacionRouter();
        reparacionRouter.configureRoutes(app);

        RecetarioRouter recetarioRouter = new RecetarioRouter();
        recetarioRouter.configureRoutes(app);

        TareaRouter.configureRoutes(app);

        PedidoRouter pedidoRouter = new PedidoRouter();
        pedidoRouter.configureRoutes(app);

        EstadisticasRouter estadisticasRouter = new EstadisticasRouter();
        estadisticasRouter.configureRoutes(app);

        // Imprimir información del servidor y endpoints disponibles
        System.out.println("Servidor corriendo en: http://localhost:7000");
        System.out.println("ENDPOINTS AUTENTICACION");
        System.out.println("POST http://localhost:7000/api/v1/auth/login");
        System.out.println("POST http://localhost:7000/api/v1/auth/logout");
        System.out.println("GET http://localhost:7000/api/v1/auth/verify");
        System.out.println("ENDPOINTS USUARIOS");
        System.out.println("GET http://localhost:7000/api/v1/usuarios");
        System.out.println("GET http://localhost:7000/api/v1/usuarios/{id}");
        System.out.println("POST http://localhost:7000/api/v1/usuarios");
        System.out.println("PUT http://localhost:7000/api/v1/usuarios/{id}/password");
        System.out.println("DELETE http://localhost:7000/api/v1/usuarios/{id}");
        System.out.println("ENDPOINTS PRODUCTOS");
        System.out.println("GET http://localhost:7000/api/v1/productos");
        System.out.println("GET http://localhost:7000/api/v1/productos/{id}");
        System.out.println("POST http://localhost:7000/api/v1/productos");
        System.out.println("PUT http://localhost:7000/api/v1/productos/{id}");
        System.out.println("DELETE http://localhost:7000/api/v1/productos/{id}");
        System.out.println("POST http://localhost:7000/api/v1/productos/publish");
        System.out.println("ENDPOINTS VENTAS");
        System.out.println("GET http://localhost:7000/api/v1/ventas");
        System.out.println("GET http://localhost:7000/api/v1/ventas/{id}");
        System.out.println("POST http://localhost:7000/api/v1/ventas");
        System.out.println("ENDPOINTS HERRAMIENTAS");
        System.out.println("GET http://localhost:7000/api/v1/herramientas");
        System.out.println("GET http://localhost:7000/api/v1/herramientas/{id o nombre}");
        System.out.println("POST http://localhost:7000/api/v1/herramientas");
        System.out.println("PUT http://localhost:7000/api/v1/herramientas/{id o nombre}/stock");
        System.out.println("PUT http://localhost:7000/api/v1/herramientas/{id o nombre}/tomar");
        System.out.println("PUT http://localhost:7000/api/v1/herramientas/{id o nombre}/devolver");
        System.out.println("DELETE http://localhost:7000/api/v1/herramientas/{id o nombre}");
        System.out.println("ENDPOINTS REPARACIONES");
        System.out.println("GET http://localhost:7000/api/v1/reparaciones");
        System.out.println("GET http://localhost:7000/api/v1/reparaciones/{id}");
        System.out.println("POST http://localhost:7000/api/v1/reparaciones");
        System.out.println("PUT http://localhost:7000/api/v1/reparaciones/{id}/estado");
        System.out.println("GET http://localhost:7000/api/v1/reparaciones/{id}/historial");
        System.out.println("GET http://localhost:7000/api/v1/reparaciones/{id}/recibo");
        System.out.println("DELETE http://localhost:7000/api/v1/reparaciones/{id}");
        System.out.println("ENDPOINTS RECETARIO");
        System.out.println("GET http://localhost:7000/api/v1/recetas");
        System.out.println("GET http://localhost:7000/api/v1/recetas/{id}");
        System.out.println("POST http://localhost:7000/api/v1/recetas");
        System.out.println("PUT http://localhost:7000/api/v1/recetas/{id}");
        System.out.println("DELETE http://localhost:7000/api/v1/recetas/{id}");
        System.out.println("ENDPOINTS TAREAS");
        System.out.println("GET http://localhost:7000/api/v1/tareas");
        System.out.println("GET http://localhost:7000/api/v1/tareas/{id}");
        System.out.println("POST http://localhost:7000/api/v1/tareas");
        System.out.println("PUT http://localhost:7000/api/v1/tareas/{id}/estado");
        System.out.println("DELETE http://localhost:7000/api/v1/tareas/{id}");
        System.out.println("PUT http://localhost:7000/api/v1/tareas/{id}");
        System.out.println("ENDPOINTS PEDIDOS");
        System.out.println("GET http://localhost:7000/api/v1/pedidos");
        System.out.println("GET http://localhost:7000/api/v1/pedidos/{id}");
        System.out.println("POST http://localhost:7000/api/v1/pedidos");
        System.out.println("PUT http://localhost:7000/api/v1/pedidos/{id}/etapa");
        System.out.println("DELETE http://localhost:7000/api/v1/pedidos/{id}");
        System.out.println("ENDPOINTS ESTADÍSTICAS DASHBOARD");
        System.out.println("GET http://localhost:7000/api/v1/dashboard/stats");
        System.out.println("GET http://localhost:7000/api/v1/analytics/venta?periodo=semanal|mensual|anual");
        System.out.println("GET http://localhost:7000/api/v1/analytics/reparaciones?periodo=semanal|mensual|anual");
        System.out.println("GET http://localhost:7000/api/v1/analytics/materiales?periodo=semanal|mensual|anual");
        System.out.println("CREDENCIALES DE PRUEBA");
        System.out.println("Body ejemplo login: {\"username\": \"admin001\", \"password\": \"AdminAlm@2024\"}");
    }
}