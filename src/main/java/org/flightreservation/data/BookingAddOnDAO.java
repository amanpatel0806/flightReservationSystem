package org.flightreservation.data;

import org.flightreservation.entity.BookingAddOn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingAddOnDAO {
    private DatabaseManager dbManager;
    
    public BookingAddOnDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    public boolean save(BookingAddOn addOn) {
        String sql = "INSERT INTO booking_add_ons (reservation_id, add_on_type, price, quantity) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection()) {
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, addOn.getReservationId());
                pstmt.setString(2, addOn.getAddOnType());
                pstmt.setDouble(3, addOn.getPrice());
                pstmt.setInt(4, addOn.getQuantity());
                
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            addOn.setId(generatedKeys.getInt(1));
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    
    public List<BookingAddOn> findByReservationId(int reservationId) {
        String sql = "SELECT * FROM booking_add_ons WHERE reservation_id = ?";
        List<BookingAddOn> addOns = new ArrayList<>();
        
        try (Connection conn = dbManager.getConnection()) {
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return addOns;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, reservationId);
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    addOns.add(mapResultSetToBookingAddOn(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return addOns;
    }
    
    public boolean deleteByReservationId(int reservationId) {
        String sql = "DELETE FROM booking_add_ons WHERE reservation_id = ?";
        try (Connection conn = dbManager.getConnection()) {
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, reservationId);
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean delete(int id) {
        String sql = "DELETE FROM booking_add_ons WHERE id = ?";
        try (Connection conn = dbManager.getConnection()) {
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
    
    private BookingAddOn mapResultSetToBookingAddOn(ResultSet rs) throws SQLException {
        BookingAddOn addOn = new BookingAddOn();
        addOn.setId(rs.getInt("id"));
        addOn.setReservationId(rs.getInt("reservation_id"));
        addOn.setAddOnType(rs.getString("add_on_type"));
        addOn.setPrice(rs.getDouble("price"));
        addOn.setQuantity(rs.getInt("quantity"));
        return addOn;
    }
}
