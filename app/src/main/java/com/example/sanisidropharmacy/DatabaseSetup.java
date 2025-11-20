package com.example.sanisidropharmacy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {

    public static void initializeDatabases(Context context) {
        createMySQLDatabase();
        createLocalSQLite(context);
    }

    private static void createMySQLDatabase() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://10.0.2.2:3306/",
                    "root", "SanIsidroPharmacy2025"
            );
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS SanIsidroPharmacy");
            stmt.executeUpdate("USE SanIsidroPharmacy");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS UserAccount (" +
                    "USER_ID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(45), " +
                    "email VARCHAR(45) UNIQUE, " +
                    "password VARCHAR(45), " +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "phone_no INT)");
            System.out.println("✅ MySQL database and table ready.");
            connection.close();
        } catch (SQLException e) {
            System.out.println("❌ MySQL setup error: " + e.getMessage());
        }
    }

    private static void createLocalSQLite(Context context) {
        DatabaseConnection.LocalSQLiteHelper helper =
                new DatabaseConnection.LocalSQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.close();
    }
}
