package org.flightreservation.data;

import org.flightreservation.entity.Promotion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PromotionDAO {
    private DatabaseManager dbManager;

    public PromotionDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public Promotion findById(int id) {
        String sql = "SELECT * FROM promotions WHERE id = ?";
        try (Connection conn = dbManager.getConnection()) {
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return null;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return mapResultSetToPromotion(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Promotion findByCode(String code) {
        String sql = "SELECT * FROM promotions WHERE code = ?";
        try (Connection conn = dbManager.getConnection()) {
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return null;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, code);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return mapResultSetToPromotion(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Promotion> findAll() {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT * FROM promotions";
        try (Connection conn = dbManager.getConnection()) {
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return promotions;
            }
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    promotions.add(mapResultSetToPromotion(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promotions;
    }

    public List<Promotion> findActive() {
        List<Promotion> promotions = new ArrayList<>();
        // Get all active promotions and filter by date in Java
        String sql = "SELECT * FROM promotions WHERE is_active = 1";
        try (Connection conn = dbManager.getConnection()) {
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return promotions;
            }
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                
                while (rs.next()) {
                    try {
                        Promotion promotion = mapResultSetToPromotion(rs);
                        
                        // Filter by checking if promotion is currently valid (between start and end date)
                        if (promotion.getStartDate() != null && promotion.getEndDate() != null) {
                            boolean isBeforeEnd = promotion.getEndDate().isAfter(now) || 
                                                promotion.getEndDate().isEqual(now);
                            
                            // Show promotion if it hasn't ended yet (regardless of start date)
                            if (isBeforeEnd) {
                                promotions.add(promotion);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return promotions;
    }

    public boolean save(Promotion promotion) {
        String sql = "INSERT INTO promotions (code, description, discount_percentage, start_date, end_date, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection()) {
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, promotion.getCode());
                pstmt.setString(2, promotion.getDescription());
                pstmt.setDouble(3, promotion.getDiscountPercentage());
                pstmt.setString(4, promotion.getStartDate().toString());
                pstmt.setString(5, promotion.getEndDate().toString());
                pstmt.setInt(6, promotion.isActive() ? 1 : 0);
                pstmt.executeUpdate();
                
                // Get the generated ID
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        promotion.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Promotion promotion) {
        String sql = "UPDATE promotions SET code = ?, description = ?, discount_percentage = ?, start_date = ?, end_date = ?, is_active = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection()) {
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, promotion.getCode());
                pstmt.setString(2, promotion.getDescription());
                pstmt.setDouble(3, promotion.getDiscountPercentage());
                pstmt.setString(4, promotion.getStartDate().toString());
                pstmt.setString(5, promotion.getEndDate().toString());
                pstmt.setInt(6, promotion.isActive() ? 1 : 0);
                pstmt.setInt(7, promotion.getId());
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM promotions WHERE id = ?";
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

    private Promotion mapResultSetToPromotion(ResultSet rs) throws SQLException {
        Promotion promotion = new Promotion();
        promotion.setId(rs.getInt("id"));
        promotion.setCode(rs.getString("code"));
        promotion.setDescription(rs.getString("description"));
        promotion.setDiscountPercentage(rs.getDouble("discount_percentage"));
        
        // Parse dates - handle both with and without time
        String startDateStr = rs.getString("start_date");
        String endDateStr = rs.getString("end_date");
        
        try {
            // Try parsing with time first (format: "2024-12-31T23:59:59")
            if (startDateStr.contains("T")) {
                promotion.setStartDate(LocalDateTime.parse(startDateStr));
            } else {
                // If no time, add default time
                promotion.setStartDate(LocalDateTime.parse(startDateStr + "T00:00:00"));
            }
            
            if (endDateStr.contains("T")) {
                promotion.setEndDate(LocalDateTime.parse(endDateStr));
            } else {
                // If no time, add end of day
                promotion.setEndDate(LocalDateTime.parse(endDateStr + "T23:59:59"));
            }
        } catch (Exception e) {
            throw new SQLException("Failed to parse promotion dates", e);
        }
        
        promotion.setActive(rs.getInt("is_active") == 1);
        return promotion;
    }
}
