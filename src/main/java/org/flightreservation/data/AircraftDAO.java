package org.flightreservation.data;

import org.flightreservation.entity.Aircraft;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AircraftDAO {
    private DatabaseManager dbManager;

    public AircraftDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public Aircraft findById(int id) {
        String sql = "SELECT * FROM aircraft WHERE id = ?";
        try (Connection conn = dbManager.getConnection()) {
            // Check if connection is valid
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return null;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    Aircraft aircraft = new Aircraft();
                    aircraft.setId(rs.getInt("id"));
                    aircraft.setModel(rs.getString("model"));
                    aircraft.setCapacity(rs.getInt("capacity"));
                    aircraft.setAirline(rs.getString("airline"));
                    return aircraft;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean save(Aircraft aircraft) {
        String sql = "INSERT INTO aircraft (model, capacity, airline) VALUES (?, ?, ?)";
        try (Connection conn = dbManager.getConnection()) {
            // Check if connection is valid
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, aircraft.getModel());
                pstmt.setInt(2, aircraft.getCapacity());
                pstmt.setString(3, aircraft.getAirline());
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Aircraft> findAll() {
        List<Aircraft> aircrafts = new ArrayList<>();
        String sql = "SELECT * FROM aircraft";
        try (Connection conn = dbManager.getConnection()) {
            // Check if connection is valid
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return aircrafts;
            }
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Aircraft aircraft = new Aircraft();
                    aircraft.setId(rs.getInt("id"));
                    aircraft.setModel(rs.getString("model"));
                    aircraft.setCapacity(rs.getInt("capacity"));
                    aircraft.setAirline(rs.getString("airline"));
                    aircrafts.add(aircraft);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aircrafts;
    }

    public boolean update(Aircraft aircraft) {
        String sql = "UPDATE aircraft SET model = ?, capacity = ?, airline = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection()) {
            // Check if connection is valid
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, aircraft.getModel());
                pstmt.setInt(2, aircraft.getCapacity());
                pstmt.setString(3, aircraft.getAirline());
                pstmt.setInt(4, aircraft.getId());
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM aircraft WHERE id = ?";
        try (Connection conn = dbManager.getConnection()) {
            // Check if connection is valid
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}