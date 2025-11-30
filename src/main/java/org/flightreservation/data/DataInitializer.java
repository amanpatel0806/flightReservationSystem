package org.flightreservation.data;

import org.flightreservation.entity.*;

import java.time.LocalDateTime;

public class DataInitializer {
    public static void initializeData() {
        // Create DAO instances
        UserDAO userDAO = new UserDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        AircraftDAO aircraftDAO = new AircraftDAO();
        FlightDAO flightDAO = new FlightDAO();
        
        // Check if data already exists
        if (!userDAO.findAll().isEmpty()) {
            return; // Data already initialized
        }
        
        // Create sample users
        User admin = new User("admin", "admin123", "ADMIN");
        User agent = new User("agent", "agent123", "AGENT");
        User customer = new User("customer", "customer123", "CUSTOMER");
        
        userDAO.save(admin);
        userDAO.save(agent);
        userDAO.save(customer);
        
        // Create sample customers
        Customer cust1 = new Customer("John", "Doe", "john.doe@email.com", "123-456-7890", "123 Main St, City");
        Customer cust2 = new Customer("Jane", "Smith", "jane.smith@email.com", "098-765-4321", "456 Oak Ave, Town");
        
        customerDAO.save(cust1);
        customerDAO.save(cust2);
        
        // Create sample aircraft
        Aircraft aircraft1 = new Aircraft("Boeing 737", 180, "American Airlines");
        Aircraft aircraft2 = new Aircraft("Airbus A320", 150, "Delta Airlines");
        Aircraft aircraft3 = new Aircraft("Boeing 787", 250, "United Airlines");
        
        aircraftDAO.save(aircraft1);
        aircraftDAO.save(aircraft2);
        aircraftDAO.save(aircraft3);
        
        // Create sample flights
        Flight flight1 = new Flight("AA101", "New York", "Los Angeles", "American Airlines",
            LocalDateTime.now().plusDays(1).withHour(8).withMinute(0), 
            LocalDateTime.now().plusDays(1).withHour(11).withMinute(30), 
            299.99, 1, 180);
            
        Flight flight2 = new Flight("DL202", "Los Angeles", "Chicago", "Delta Airlines",
            LocalDateTime.now().plusDays(2).withHour(14).withMinute(0), 
            LocalDateTime.now().plusDays(2).withHour(19).withMinute(45), 
            199.99, 2, 150);
            
        Flight flight3 = new Flight("UA303", "Chicago", "Miami", "United Airlines",
            LocalDateTime.now().plusDays(3).withHour(10).withMinute(30), 
            LocalDateTime.now().plusDays(3).withHour(14).withMinute(15), 
            249.99, 3, 250);
            
        Flight flight4 = new Flight("AA404", "Miami", "New York", "American Airlines",
            LocalDateTime.now().plusDays(4).withHour(16).withMinute(0), 
            LocalDateTime.now().plusDays(4).withHour(18).withMinute(30), 
            179.99, 1, 180);
            
        flightDAO.save(flight1);
        flightDAO.save(flight2);
        flightDAO.save(flight3);
        flightDAO.save(flight4);
    }
}