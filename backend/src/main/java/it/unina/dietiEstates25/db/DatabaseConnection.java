package it.unina.dietiEstates25.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private DatabaseConnection(){}

    public static Connection getConnection() throws SQLException {
        try {
            String url = DatabaseConfiguration.getDbUrl();
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            return null;
        }
    }
}
