package org.flightreservation.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:flight_reservation.db";

    private DatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            // Check if connection is still valid, if not, create a new one
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            // Try to create a new connection
            try {
                connection = DriverManager.getConnection(DB_URL);
            } catch (SQLException ex) {
                System.err.println("Failed to create database connection: " + ex.getMessage());
                ex.printStackTrace();
                return null;
            }
        }
        return connection;
    }

    private void createTables() {
        try (Statement stmt = connection.createStatement()) {
            // Create users table
            String createUserTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL)";
            stmt.execute(createUserTable);
            
            // Create customers table
            String createCustomerTable = "CREATE TABLE IF NOT EXISTS customers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "first_name TEXT NOT NULL," +
                    "last_name TEXT NOT NULL," +
                    "email TEXT UNIQUE NOT NULL," +
                    "phone TEXT," +
                    "address TEXT)";
            stmt.execute(createCustomerTable);
            
            // Create aircraft table
            String createAircraftTable = "CREATE TABLE IF NOT EXISTS aircraft (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "model TEXT NOT NULL," +
                    "capacity INTEGER NOT NULL," +
                    "airline TEXT NOT NULL)";
            stmt.execute(createAircraftTable);
            
            // Create flights table
            String createFlightTable = "CREATE TABLE IF NOT EXISTS flights (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "flight_number TEXT UNIQUE NOT NULL," +
                    "origin TEXT NOT NULL," +
                    "destination TEXT NOT NULL," +
                    "airline TEXT NOT NULL," +
                    "departure_time TEXT NOT NULL," +
                    "arrival_time TEXT NOT NULL," +
                    "price REAL NOT NULL," +
                    "aircraft_id INTEGER NOT NULL," +
                    "available_seats INTEGER NOT NULL," +
                    "FOREIGN KEY (aircraft_id) REFERENCES aircraft(id))";
            stmt.execute(createFlightTable);
            
            // Create reservations table
            String createReservationTable = "CREATE TABLE IF NOT EXISTS reservations (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "customer_id INTEGER NOT NULL," +
                    "flight_id INTEGER NOT NULL," +
                    "reservation_date TEXT NOT NULL," +
                    "number_of_passengers INTEGER NOT NULL," +
                    "status TEXT NOT NULL," +
                    "FOREIGN KEY (customer_id) REFERENCES customers(id)," +
                    "FOREIGN KEY (flight_id) REFERENCES flights(id))";
            stmt.execute(createReservationTable);
            
            // Create payments table
            String createPaymentTable = "CREATE TABLE IF NOT EXISTS payments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "reservation_id INTEGER NOT NULL," +
                    "amount REAL NOT NULL," +
                    "payment_date TEXT NOT NULL," +
                    "payment_method TEXT NOT NULL," +
                    "status TEXT NOT NULL," +
                    "FOREIGN KEY (reservation_id) REFERENCES reservations(id))";
            stmt.execute(createPaymentTable);
            
            // Note: stmt is automatically closed due to try-with-resources
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}