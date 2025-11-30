package org.flightreservation.data;

import org.flightreservation.entity.Flight;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightDAO {
    private DatabaseManager dbManager;

    public FlightDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public Flight findById(int id) {
        String sql = "SELECT * FROM flights WHERE id = ?";
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
                    Flight flight = new Flight();
                    flight.setId(rs.getInt("id"));
                    flight.setFlightNumber(rs.getString("flight_number"));
                    flight.setOrigin(rs.getString("origin"));
                    flight.setDestination(rs.getString("destination"));
                    flight.setAirline(rs.getString("airline"));
                    flight.setDepartureTime(LocalDateTime.parse(rs.getString("departure_time")));
                    flight.setArrivalTime(LocalDateTime.parse(rs.getString("arrival_time")));
                    flight.setPrice(rs.getDouble("price"));
                    flight.setAircraftId(rs.getInt("aircraft_id"));
                    flight.setAvailableSeats(rs.getInt("available_seats"));
                    return flight;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Flight> searchFlights(String origin, String destination, String date, String airline) {
        List<Flight> flights = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM flights WHERE 1=1");
        
        if (origin != null && !origin.isEmpty()) {
            sql.append(" AND origin LIKE ?");
        }
        if (destination != null && !destination.isEmpty()) {
            sql.append(" AND destination LIKE ?");
        }
        if (date != null && !date.isEmpty()) {
            sql.append(" AND departure_time LIKE ?");
        }
        if (airline != null && !airline.isEmpty()) {
            sql.append(" AND airline LIKE ?");
        }
        
        try (Connection conn = dbManager.getConnection()) {
            // Check if connection is valid
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return flights;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                
                int paramIndex = 1;
                if (origin != null && !origin.isEmpty()) {
                    pstmt.setString(paramIndex++, "%" + origin + "%");
                }
                if (destination != null && !destination.isEmpty()) {
                    pstmt.setString(paramIndex++, "%" + destination + "%");
                }
                if (date != null && !date.isEmpty()) {
                    pstmt.setString(paramIndex++, "%" + date + "%");
                }
                if (airline != null && !airline.isEmpty()) {
                    pstmt.setString(paramIndex, "%" + airline + "%");
                }
                
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    Flight flight = new Flight();
                    flight.setId(rs.getInt("id"));
                    flight.setFlightNumber(rs.getString("flight_number"));
                    flight.setOrigin(rs.getString("origin"));
                    flight.setDestination(rs.getString("destination"));
                    flight.setAirline(rs.getString("airline"));
                    flight.setDepartureTime(LocalDateTime.parse(rs.getString("departure_time")));
                    flight.setArrivalTime(LocalDateTime.parse(rs.getString("arrival_time")));
                    flight.setPrice(rs.getDouble("price"));
                    flight.setAircraftId(rs.getInt("aircraft_id"));
                    flight.setAvailableSeats(rs.getInt("available_seats"));
                    flights.add(flight);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    public boolean save(Flight flight) {
        String sql = "INSERT INTO flights (flight_number, origin, destination, airline, departure_time, arrival_time, price, aircraft_id, available_seats) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection()) {
            // Check if connection is valid
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, flight.getFlightNumber());
                pstmt.setString(2, flight.getOrigin());
                pstmt.setString(3, flight.getDestination());
                pstmt.setString(4, flight.getAirline());
                pstmt.setString(5, flight.getDepartureTime().toString());
                pstmt.setString(6, flight.getArrivalTime().toString());
                pstmt.setDouble(7, flight.getPrice());
                pstmt.setInt(8, flight.getAircraftId());
                pstmt.setInt(9, flight.getAvailableSeats());
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Flight> findAll() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights";
        try (Connection conn = dbManager.getConnection()) {
            // Check if connection is valid
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return flights;
            }
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Flight flight = new Flight();
                    flight.setId(rs.getInt("id"));
                    flight.setFlightNumber(rs.getString("flight_number"));
                    flight.setOrigin(rs.getString("origin"));
                    flight.setDestination(rs.getString("destination"));
                    flight.setAirline(rs.getString("airline"));
                    flight.setDepartureTime(LocalDateTime.parse(rs.getString("departure_time")));
                    flight.setArrivalTime(LocalDateTime.parse(rs.getString("arrival_time")));
                    flight.setPrice(rs.getDouble("price"));
                    flight.setAircraftId(rs.getInt("aircraft_id"));
                    flight.setAvailableSeats(rs.getInt("available_seats"));
                    flights.add(flight);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    public boolean update(Flight flight) {
        String sql = "UPDATE flights SET flight_number = ?, origin = ?, destination = ?, airline = ?, departure_time = ?, arrival_time = ?, price = ?, aircraft_id = ?, available_seats = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection()) {
            // Check if connection is valid
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, flight.getFlightNumber());
                pstmt.setString(2, flight.getOrigin());
                pstmt.setString(3, flight.getDestination());
                pstmt.setString(4, flight.getAirline());
                pstmt.setString(5, flight.getDepartureTime().toString());
                pstmt.setString(6, flight.getArrivalTime().toString());
                pstmt.setDouble(7, flight.getPrice());
                pstmt.setInt(8, flight.getAircraftId());
                pstmt.setInt(9, flight.getAvailableSeats());
                pstmt.setInt(10, flight.getId());
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM flights WHERE id = ?";
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