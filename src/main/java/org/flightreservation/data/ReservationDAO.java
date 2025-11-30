package org.flightreservation.data;

import org.flightreservation.entity.Reservation;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private DatabaseManager dbManager;

    public ReservationDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public Reservation findById(int id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
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
                    Reservation reservation = new Reservation();
                    reservation.setId(rs.getInt("id"));
                    reservation.setCustomerId(rs.getInt("customer_id"));
                    reservation.setFlightId(rs.getInt("flight_id"));
                    reservation.setReservationDate(LocalDateTime.parse(rs.getString("reservation_date")));
                    reservation.setNumberOfPassengers(rs.getInt("number_of_passengers"));
                    reservation.setStatus(rs.getString("status"));
                    return reservation;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Reservation> findByCustomerId(int customerId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE customer_id = ?";
        try (Connection conn = dbManager.getConnection()) {
            // Check if connection is valid
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return reservations;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, customerId);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setId(rs.getInt("id"));
                    reservation.setCustomerId(rs.getInt("customer_id"));
                    reservation.setFlightId(rs.getInt("flight_id"));
                    reservation.setReservationDate(LocalDateTime.parse(rs.getString("reservation_date")));
                    reservation.setNumberOfPassengers(rs.getInt("number_of_passengers"));
                    reservation.setStatus(rs.getString("status"));
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public boolean save(Reservation reservation) {
        String sql = "INSERT INTO reservations (customer_id, flight_id, reservation_date, number_of_passengers, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection()) {
            // Check if connection is valid
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, reservation.getCustomerId());
                pstmt.setInt(2, reservation.getFlightId());
                pstmt.setString(3, reservation.getReservationDate().toString());
                pstmt.setInt(4, reservation.getNumberOfPassengers());
                pstmt.setString(5, reservation.getStatus());
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Reservation> findAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Connection conn = dbManager.getConnection()) {
            // Check if connection is valid
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return reservations;
            }
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setId(rs.getInt("id"));
                    reservation.setCustomerId(rs.getInt("customer_id"));
                    reservation.setFlightId(rs.getInt("flight_id"));
                    reservation.setReservationDate(LocalDateTime.parse(rs.getString("reservation_date")));
                    reservation.setNumberOfPassengers(rs.getInt("number_of_passengers"));
                    reservation.setStatus(rs.getString("status"));
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public boolean update(Reservation reservation) {
        String sql = "UPDATE reservations SET customer_id = ?, flight_id = ?, reservation_date = ?, number_of_passengers = ?, status = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection()) {
            // Check if connection is valid
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, reservation.getCustomerId());
                pstmt.setInt(2, reservation.getFlightId());
                pstmt.setString(3, reservation.getReservationDate().toString());
                pstmt.setInt(4, reservation.getNumberOfPassengers());
                pstmt.setString(5, reservation.getStatus());
                pstmt.setInt(6, reservation.getId());
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM reservations WHERE id = ?";
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