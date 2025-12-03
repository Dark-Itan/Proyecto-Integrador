package com.inventario.alma_jesus.router;

import com.inventario.alma_jesus.controller.UsuarioController;
import io.javalin.Javalin;

/**
 * Router para configurar las rutas relacionadas con la gestión de usuarios en el sistema.
 * Define los endpoints CRUD para operaciones de administración de usuarios, incluyendo
 * creación, consulta, actualización de contraseñas y eliminación de usuarios.
 *
 * @author Alma & Jesús
 * @version 1.0
 * @since 2024
 */
public class UsuarioRouter {
    private final UsuarioController usuarioController;

    /**
     * Constructor que inicializa el router de usuarios.
     * Crea una nueva instancia del controlador de usuarios para manejar las solicitudes.
     */
    public UsuarioRouter() {
        this.usuarioController = new UsuarioController();
        System.out.println("👤 [USUARIO-ROUTER] Router de usuarios inicializado");
    }

    /**
     * Configura todas las rutas relacionadas con usuarios en la aplicación Javalin.
     * Establece los 5 endpoints RESTful para operaciones CRUD sobre usuarios.
     *
     * <p><b>Endpoints configurados:</b></p>
     * <ol start="4">
     *   <li>GET /api/v1/usuarios - Listar todos los usuarios (Endpoint 4)</li>
     *   <li>GET /api/v1/usuarios/{id} - Obtener usuario específico (Endpoint 5)</li>
     *   <li>POST /api/v1/usuarios - Crear nuevo usuario (Endpoint 6)</li>
     *   <li>PUT /api/v1/usuarios/{id}/password - Cambiar contraseña (Endpoint 7)</li>
     *   <li>DELETE /api/v1/usuarios/{id} - Eliminar usuario (Endpoint 8)</li>
     * </ol>
     *
     * @param app Instancia de la aplicación Javalin donde se registrarán las rutas.
     * @throws IllegalArgumentException Si la app proporcionada es null.
     *
     * @example
     * <pre>{@code
     * Javalin app = Javalin.create();
     * UsuarioRouter router = new UsuarioRouter();
     * router.configureRoutes(app);
     * app.start(8080);
     * }</pre>
     */
    public void configureRoutes(Javalin app) {
        if (app == null) {
            throw new IllegalArgumentException("La instancia de Javalin no puede ser null");
        }

        System.out.println("🛣️ [USUARIO-ROUTER] Configurando 5 endpoints de usuarios");

        // Endpoint 4: Listar todos los usuarios (operación READ - colección)
        app.get("/api/v1/usuarios", usuarioController::listarUsuarios);

        // Endpoint 5: Obtener usuario específico por ID (operación READ - recurso)
        app.get("/api/v1/usuarios/{id}", usuarioController::obtenerUsuarioPorId);

        // Endpoint 6: Crear nuevo usuario (operación CREATE)
        app.post("/api/v1/usuarios", usuarioController::crearUsuario);

        // Endpoint 7: Cambiar contraseña de usuario (operación UPDATE - parcial para contraseña)
        app.put("/api/v1/usuarios/{id}/password", usuarioController::cambiarPassword);

        // Endpoint 8: Eliminar usuario (operación DELETE)
        app.delete("/api/v1/usuarios/{id}", usuarioController::eliminarUsuario);

        System.out.println("✅ [USUARIO-ROUTER] 5 endpoints configurados exitosamente");
    }

    /**
     * Obtiene información sobre todas las rutas configuradas para usuarios.
     *
     * @return Array de strings con las rutas configuradas en formato "MÉTODO RUTA - Descripción".
     */
    public String[] getRutasDetalladas() {
        return new String[] {
                "GET    /api/v1/usuarios - Listar todos los usuarios (Endpoint 4)",
                "GET    /api/v1/usuarios/{id} - Obtener usuario específico (Endpoint 5)",
                "POST   /api/v1/usuarios - Crear nuevo usuario (Endpoint 6)",
                "PUT    /api/v1/usuarios/{id}/password - Cambiar contraseña (Endpoint 7)",
                "DELETE /api/v1/usuarios/{id} - Eliminar usuario (Endpoint 8)"
        };
    }

    /**
     * Verifica si una ruta dada corresponde a un endpoint de usuarios.
     *
     * @param ruta Ruta a verificar (ej: "/api/v1/usuarios").
     * @return true si la ruta pertenece a este router, false en caso contrario.
     */
    public boolean esRutaUsuario(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            return false;
        }

        return ruta.startsWith("/api/v1/usuarios");
    }

    /**
     * Obtiene el número total de endpoints configurados por este router.
     *
     * @return Cantidad de endpoints configurados.
     */
    public int getTotalEndpoints() {
        return 5;
    }

    /**
     * Obtiene el controlador asociado a este router.
     *
     * @return Instancia de UsuarioController utilizada por este router.
     */
    public UsuarioController getController() {
        return usuarioController;
    }

    /**
     * Proporciona información sobre los tipos de operaciones disponibles.
     *
     * @return Array con los tipos de operaciones soportadas.
     */
    public String[] getOperacionesSoportadas() {
        return new String[] {
                "LISTAR_USUARIOS",
                "OBTENER_USUARIO",
                "CREAR_USUARIO",
                "CAMBIAR_PASSWORD",
                "ELIMINAR_USUARIO"
        };
    }
}