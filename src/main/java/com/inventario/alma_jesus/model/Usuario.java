package com.inventario.alma_jesus.model;

/**
 * Modelo que representa un usuario del sistema.
 * <p>
 * Esta clase gestiona la información de autenticación y autorización de usuarios,
 * incluyendo credenciales, roles de acceso y estado de la cuenta.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class Usuario {
    /**
     * Identificador único del usuario (generalmente username o ID numérico).
     */
    private String id;

    /**
     * Nombre de usuario para inicio de sesión (debe ser único).
     */
    private String username;

    /**
     * Contraseña encriptada del usuario.
     */
    private String password;

    /**
     * Rol o nivel de acceso del usuario (ADMIN, TRABAJADOR, CLIENTE, etc.).
     */
    private String rol;

    /**
     * Correo electrónico del usuario para comunicación.
     */
    private String email;

    /**
     * Indica si la cuenta del usuario está activa y puede acceder al sistema.
     */
    private boolean activo;

    /**
     * Fecha en que se creó la cuenta del usuario.
     */
    private String fechaCreacion;

    /**
     * Constructor por defecto.
     * Crea una instancia de Usuario sin inicializar atributos.
     */
    public Usuario() {
    }

    /**
     * Constructor para creación de nuevos usuarios.
     *
     * @param id Identificador único
     * @param username Nombre de usuario
     * @param password Contraseña (debe encriptarse después)
     * @param rol Rol de acceso
     * @param email Correo electrónico
     */
    public Usuario(String id, String username, String password, String rol, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.email = email;
        this.activo = true;
    }

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return El ID del usuario
     */
    public String getId() { return id; }

    /**
     * Establece el identificador único del usuario.
     *
     * @param id El ID a asignar
     */
    public void setId(String id) { this.id = id; }

    /**
     * Obtiene el nombre de usuario para inicio de sesión.
     *
     * @return El nombre de usuario
     */
    public String getUsername() { return username; }

    /**
     * Establece el nombre de usuario para inicio de sesión.
     *
     * @param username El nombre de usuario a asignar
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Obtiene la contraseña encriptada del usuario.
     *
     * @return La contraseña encriptada
     */
    public String getPassword() { return password; }

    /**
     * Establece la contraseña del usuario (debe encriptarse antes de almacenar).
     *
     * @param password La contraseña a asignar
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Obtiene el rol o nivel de acceso del usuario.
     *
     * @return El rol del usuario
     */
    public String getRol() { return rol; }

    /**
     * Establece el rol o nivel de acceso del usuario.
     *
     * @param rol El rol a asignar
     */
    public void setRol(String rol) { this.rol = rol; }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return El email del usuario
     */
    public String getEmail() { return email; }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email El email a asignar
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Verifica si la cuenta del usuario está activa.
     *
     * @return true si la cuenta está activa, false en caso contrario
     */
    public boolean isActivo() { return activo; }

    /**
     * Establece el estado de actividad de la cuenta.
     *
     * @param activo El estado de actividad a asignar
     */
    public void setActivo(boolean activo) { this.activo = activo; }

    /**
     * Obtiene la fecha de creación de la cuenta.
     *
     * @return La fecha de creación (formato: YYYY-MM-DD HH:MM:SS)
     */
    public String getFechaCreacion() { return fechaCreacion; }

    /**
     * Establece la fecha de creación de la cuenta.
     *
     * @param fechaCreacion La fecha de creación a asignar
     */
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}