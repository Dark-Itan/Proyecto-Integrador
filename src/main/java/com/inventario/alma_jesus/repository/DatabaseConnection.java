package com.inventario.alma_jesus.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static HikariDataSource dataSource;

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

            dataSource = new HikariDataSource(config);
            System.out.println("[DATABASE] Pool HikariCP inicializado correctamente");
            System.out.println("[DATABASE] URL: jdbc:mysql://localhost:3306/inventario_alma_jesus");

        } catch (Exception e) {
            System.err.println("[DATABASE] Error inicializando pool: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        System.out.println("[DATABASE] Solicitando conexion del pool...");
        Connection conn = dataSource.getConnection();
        System.out.println("[DATABASE] Conexion obtenida: " + conn.toString());
        return conn;
    }
}