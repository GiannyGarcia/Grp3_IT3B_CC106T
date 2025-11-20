package com.example.sanisidropharmacy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // 🔗 MySQL Connection Info
    private static final String MYSQL_URL = "jdbc:mysql://10.0.2.2:3306/SanIsidroPharmacy";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASS = "SanIsidroPharmacy2025";

    /**
     * Connects to the MySQL Database via JDBC
     * Make sure your MySQL server allows connections from your Android emulator (10.0.2.2)
     */
    public static Connection getMySQLConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
            System.out.println("✅ Connected to MySQL Server!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("❌ MySQL Connection failed: " + e.getMessage());
        }
        return connection;
    }

    /**
     * Optional: Local SQLite Helper
     * This is only for App Inspection visibility.
     * If you don't want SQLite, you can safely ignore or comment this inner class.
     */
    public static class LocalSQLiteHelper extends SQLiteOpenHelper {

        private static final String DB_NAME = "SanIsidroPharmacyLocal.db";
        private static final int DB_VERSION = 1;

        public LocalSQLiteHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS UserAccount (" +
                    "USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT, " +   // ✅ Fixed missing column
                    "email TEXT, " +
                    "password TEXT, " +
                    "created_at DATETIME, " +
                    "phone_no TEXT)");
            System.out.println("✅ Local SQLite table created for inspection.");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS UserAccount");
            onCreate(db);
        }
    }
}
