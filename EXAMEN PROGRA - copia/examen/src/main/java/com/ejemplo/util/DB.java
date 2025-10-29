package com.ejemplo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private static final String URL  = "jdbc:mysql://localhost:3306/webapp?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";  // ← cámbialo si usas otro usuario
    private static final String PASS = "root";  // ← cámbialo si tu clave es diferente

    public static Connection getConnection() throws SQLException {
        System.out.println("[DB] Conectado a: " + URL);
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
