package com.inventario.alma_jesus.model;

public class Usuario {
    private String id;
    private String username;
    private String password;
    private String rol;
    private String email;
    private boolean activo;
    private String fechaCreacion;
    public Usuario() {
    }

    public Usuario(String id, String username, String password, String rol, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.email = email;
        this.activo = true;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}