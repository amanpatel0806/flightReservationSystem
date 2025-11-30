package org.flightreservation.data;

import org.flightreservation.entity.Payment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private DatabaseManager dbManager;

    public PaymentDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public Payment findById(int id) {
        String sql = "SELECT * FROM payments WHERE id = ?";
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
                    Payment payment = new Payment();
                    payment.setId(rs.getInt("id"));
                    payment.setReservationId(rs.getInt("reservation_id"));
                    payment.setAmount(rs.getDouble("amount"));
                    payment.setPaymentDate(LocalDateTime.parse(rs.getString("payment_date")));
                    payment.setPaymentMethod(rs.getString("payment_method"));
                    payment.setStatus(rs.getString("status"));
                    return payment;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Payment findByReservationId(int reservationId) {
        String sql = "SELECT * FROM payments WHERE reservation_id = ?";
        try (Connection conn = dbManager.getConnection()) {
            // Check if connection is valid
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return null;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, reservationId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    Payment payment = new Payment();
                    payment.setId(rs.getInt("id"));
                    payment.setReservationId(rs.getInt("reservation_id"));
                    payment.setAmount(rs.getDouble("amount"));
                    payment.setPaymentDate(LocalDateTime.parse(rs.getString("payment_date")));
                    payment.setPaymentMethod(rs.getString("payment_method"));
                    payment.setStatus(rs.getString("status"));
                    return payment;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean save(Payment payment) {
        String sql = "INSERT INTO payments (reservation_id, amount, payment_date, payment_method, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection()) {
            // Check if connection is valid
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, payment.getReservationId());
                pstmt.setDouble(2, payment.getAmount());
                pstmt.setString(3, payment.getPaymentDate().toString());
                pstmt.setString(4, payment.getPaymentMethod());
                pstmt.setString(5, payment.getStatus());
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Payment> findAll() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments";
        try (Connection conn = dbManager.getConnection()) {
            // Check if connection is valid
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return payments;
            }
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Payment payment = new Payment();
                    payment.setId(rs.getInt("id"));
                    payment.setReservationId(rs.getInt("reservation_id"));
                    payment.setAmount(rs.getDouble("amount"));
                    payment.setPaymentDate(LocalDateTime.parse(rs.getString("payment_date")));
                    payment.setPaymentMethod(rs.getString("payment_method"));
                    payment.setStatus(rs.getString("status"));
                    payments.add(payment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    public boolean update(Payment payment) {
        String sql = "UPDATE payments SET reservation_id = ?, amount = ?, payment_date = ?, payment_method = ?, status = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection()) {
            // Check if connection is valid
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, payment.getReservationId());
                pstmt.setDouble(2, payment.getAmount());
                pstmt.setString(3, payment.getPaymentDate().toString());
                pstmt.setString(4, payment.getPaymentMethod());
                pstmt.setString(5, payment.getStatus());
                pstmt.setInt(6, payment.getId());
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM payments WHERE id = ?";
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