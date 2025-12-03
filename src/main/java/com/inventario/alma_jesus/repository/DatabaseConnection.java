package com.inventario.alma_jesus.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase para la gestión de conexiones a la base de datos utilizando HikariCP.
 * <p>
 * Esta clase implementa un patrón Singleton para proporcionar un pool de conexiones
 * eficiente y configurado a la base de datos MySQL del sistema.
 * </p>
 *
 * @version 1.0
 * @since 2024
 * @see HikariDataSource
 * @see HikariConfig
 */
public class DatabaseConnection {
    /**
     * Pool de conexiones HikariCP configurado estáticamente.
     */
    private static HikariDataSource dataSource;

    /**
     * Bloque de inicialización estática.
     * Configura y inicializa el pool de conexiones HikariCP al cargar la clase.
     *
     * @throws RuntimeException Si ocurre un error durante la configuración del pool
     */
    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://35.170.167.235:3306/inventario_alma_jesus");
            config.setUsername("root");
            config.setPassword("albafica");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            // **AQUÍ AGREGAS LA DESACTIVACIÓN DE ONLY_FULL_GROUP_BY**
            config.addDataSourceProperty("sessionVariables", "sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION'");

            dataSource = new HikariDataSource(config);
            System.out.println("[DATABASE] Pool HikariCP inicializado correctamente");
            System.out.println("[DATABASE] URL: jdbc:mysql://localhost:3306/inventario_alma_jesus");
            System.out.println("[DATABASE] Modo SQL: ONLY_FULL_GROUP_BY DESACTIVADO");

        } catch (Exception e) {
            System.err.println("[DATABASE] Error inicializando pool: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error crítico: No se pudo inicializar el pool de conexiones", e);
        }
    }

    /**
     * Obtiene una conexión del pool de conexiones.
     * <p>
     * Este método proporciona una conexión activa a la base de datos desde el pool.
     * Las conexiones deben ser cerradas después de su uso para retornarlas al pool.
     * </p>
     *
     * @return Una conexión activa a la base de datos
     * @throws SQLException Si ocurre un error al obtener la conexión del pool
     *
     * @example
     * <pre>
     * try (Connection conn = DatabaseConnection.getConnection()) {
     *     // Usar la conexión
     *     PreparedStatement stmt = conn.prepareStatement("SELECT * FROM usuarios");
     *     ResultSet rs = stmt.executeQuery();
     *     // Procesar resultados
     * } catch (SQLException e) {
     *     // Manejar error
     * }
     * </pre>
     */
    public static Connection getConnection() throws SQLException {
        System.out.println("[DATABASE] Solicitando conexion del pool...");
        Connection conn = dataSource.getConnection();
        System.out.println("[DATABASE] Conexion obtenida: " + conn.toString());
        return conn;
    }
}