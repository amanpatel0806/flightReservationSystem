package org.reservation;

import org.flightreservation.boundary.LoginView;
import org.flightreservation.data.DataInitializer;
import org.flightreservation.data.DatabaseManager;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Delete existing database to reinitialize with new schema
        File dbFile = new File("flight_reservation.db");
        if (dbFile.exists()) {
            dbFile.delete();
        }
        
        // Initialize sample data
        DataInitializer.initializeData();
        
        // Launch the login view
        SwingUtilities.invokeLater(() -> new LoginView());
    }
}