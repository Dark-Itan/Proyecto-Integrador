package com.inventario.alma_jesus.model;

/**
 * Modelo que representa un producto en el sistema de inventario.
 * <p>
 * Esta clase gestiona productos terminados disponibles para venta directa,
 * incluyendo muebles, accesorios y otros artículos fabricados en el taller.
 * </p>
 *
 * @version 1.0
 * @since 2024
 */
public class Producto {
    private int id;
    private String modelo;
    private String color;
    private int precio;
    private int stock;
    private String tamaño;
    private String imagenUrl;
    private boolean activo;
    private String creadoPor;
    private String fechaCreacion;
    private String tipo;

    /**
     * Constructor por defecto.
     * <p>
     * Crea una instancia vacía de producto.
     * Los valores deben establecerse mediante los setters correspondientes.
     * </p>
     */
    public Producto() {}

    /**
     * Constructor principal para crear un nuevo producto.
     * <p>
     * Inicializa el producto como activo por defecto y establece
     * los campos básicos requeridos.
     * </p>
     *
     * @param modelo Nombre o referencia del modelo del producto
     * @param color Color principal del producto
     * @param precio Precio de venta en centavos (para evitar decimales)
     * @param stock Cantidad disponible en inventario
     * @param tamaño Dimensiones o tamaño del producto
     * @param creadoPor ID del usuario que crea el producto
     * @param tipo Categoría o tipo de producto
     */
    public Producto(String modelo, String color, int precio, int stock,
                    String tamaño, String creadoPor, String tipo) {
        this.modelo = modelo;
        this.color = color;
        this.precio = precio;
        this.stock = stock;
        this.tamaño = tamaño;
        this.creadoPor = creadoPor;
        this.tipo = tipo;
        this.activo = true; // Por defecto, los productos nuevos están activos
    }

    // Getters y Setters

    /**
     * Obtiene el identificador único del producto.
     *
     * @return ID del producto
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del producto.
     *
     * @param id ID único del producto
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre o referencia del modelo.
     * <p>
     * Ejemplos: "Silla Ejecutiva", "Mesa de Centro Moderna", "Estante Librero"
     * </p>
     *
     * @return Nombre del modelo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Establece el nombre o referencia del modelo.
     *
     * @param modelo Nombre del modelo
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Obtiene el color principal del producto.
     * <p>
     * Puede incluir variantes como: "Nogal", "Blanco Mate", "Caoba Oscuro"
     * </p>
     *
     * @return Color del producto
     */
    public String getColor() {
        return color;
    }

    /**
     * Establece el color principal del producto.
     *
     * @param color Color del producto
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Obtiene el precio de venta del producto.
     * <p>
     * Representado en centavos para evitar problemas de precisión
     * con decimales. Ej: $150.50 se almacena como 15050
     * </p>
     *
     * @return Precio en centavos
     */
    public int getPrecio() {
        return precio;
    }

    /**
     * Establece el precio de venta del producto.
     *
     * @param precio Precio en centavos
     */
    public void setPrecio(int precio) {
        this.precio = precio;
    }

    /**
     * Obtiene la cantidad disponible en inventario.
     *
     * @return Stock disponible
     */
    public int getStock() {
        return stock;
    }

    /**
     * Establece la cantidad disponible en inventario.
     *
     * @param stock Cantidad en stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Obtiene las dimensiones o tamaño del producto.
     * <p>
     * Formato común: "Ancho x Alto x Profundidad" en centímetros
     * Ej: "60x40x30"
     * </p>
     *
     * @return Tamaño o dimensiones
     */
    public String getTamaño() {
        return tamaño;
    }

    /**
     * Establece las dimensiones o tamaño del producto.
     *
     * @param tamaño Tamaño o dimensiones
     */
    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
    }

    /**
     * Obtiene la URL de la imagen del producto.
     * <p>
     * Puede ser una ruta relativa en el servidor o URL completa
     * a un servicio de almacenamiento de imágenes.
     * </p>
     *
     * @return URL de la imagen
     */
    public String getImagenUrl() {
        return imagenUrl;
    }

    /**
     * Establece la URL de la imagen del producto.
     *
     * @param imagenUrl URL de la imagen
     */
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    /**
     * Indica si el producto está activo en el catálogo.
     * <p>
     * Los productos inactivos no se muestran en búsquedas
     * ni están disponibles para venta, pero se mantienen
     * en el historial.
     * </p>
     *
     * @return true si está activo, false si está inactivo
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Establece el estado de actividad del producto.
     *
     * @param activo Estado de actividad
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Obtiene el ID del usuario que creó el producto.
     *
     * @return ID del usuario creador
     */
    public String getCreadoPor() {
        return creadoPor;
    }

    /**
     * Establece el ID del usuario que creó el producto.
     *
     * @param creadoPor ID del usuario
     */
    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    /**
     * Obtiene la fecha de creación del registro.
     * <p>
     * Formato común: "YYYY-MM-DD HH:MM:SS"
     * </p>
     *
     * @return Fecha de creación
     */
    public String getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece la fecha de creación del registro.
     *
     * @param fechaCreacion Fecha en formato de cadena
     */
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Obtiene la categoría o tipo de producto.
     * <p>
     * Ejemplos: "SILLA", "MESA", "ESTANTE", "CAMA", "ACCESORIO"
     * </p>
     *
     * @return Tipo de producto
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece la categoría o tipo de producto.
     *
     * @param tipo Tipo de producto
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}