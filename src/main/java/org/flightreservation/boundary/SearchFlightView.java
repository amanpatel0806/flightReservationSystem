package org.flightreservation.boundary;

import org.flightreservation.controller.FlightController;
import org.flightreservation.entity.Flight;
import org.flightreservation.entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SearchFlightView extends JFrame {
    private User currentUser;
    private FlightController flightController;
    
    private JTextField originField;
    private JTextField destinationField;
    private JTextField dateField;
    private JTextField airlineField;
    private JButton searchButton;
    private JTable flightsTable;
    private DefaultTableModel tableModel;
    private JButton bookButton;
    private JButton backButton;

    public SearchFlightView(User user) {
        this.currentUser = user;
        this.flightController = new FlightController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupTable();
        setTitle("Flight Reservation System - Search Flights");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeComponents() {
        originField = new JTextField(15);
        destinationField = new JTextField(15);
        dateField = new JTextField(15);
        airlineField = new JTextField(15);
        searchButton = new JButton("Search");
        bookButton = new JButton("Book Selected Flight");
        backButton = new JButton("Back to Main Menu");
        
        // Table setup
        String[] columnNames = {"ID", "Flight Number", "Origin", "Destination", "Airline", "Departure", "Arrival", "Price", "Available Seats"};
        tableModel = new DefaultTableModel(columnNames, 0);
        flightsTable = new JTable(tableModel);
        flightsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel for search controls
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(new JLabel("Origin:"), gbc);
        
        gbc.gridx = 1;
        searchPanel.add(originField, gbc);
        
        gbc.gridx = 2;
        searchPanel.add(new JLabel("Destination:"), gbc);
        
        gbc.gridx = 3;
        searchPanel.add(destinationField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        searchPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 1;
        searchPanel.add(dateField, gbc);
        
        gbc.gridx = 2;
        searchPanel.add(new JLabel("Airline:"), gbc);
        
        gbc.gridx = 3;
        searchPanel.add(airlineField, gbc);
        
        gbc.gridx = 4;
        searchPanel.add(searchButton, gbc);
        
        // Bottom panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(bookButton);
        buttonPanel.add(backButton);
        
        // Add components to frame
        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(flightsTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFlights();
            }
        });
        
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookFlight();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToMainMenu();
            }
        });
    }
    
    private void setupTable() {
        // Load all flights initially
        searchFlights();
    }

    private void searchFlights() {
        String origin = originField.getText().trim();
        String destination = destinationField.getText().trim();
        String date = dateField.getText().trim();
        String airline = airlineField.getText().trim();
        
        // Clear existing data
        tableModel.setRowCount(0);
        
        List<Flight> flights = flightController.searchFlights(
            origin.isEmpty() ? null : origin,
            destination.isEmpty() ? null : destination,
            date.isEmpty() ? null : date,
            airline.isEmpty() ? null : airline
        );
        
        for (Flight flight : flights) {
            Object[] row = {
                flight.getId(),
                flight.getFlightNumber(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getAirline(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getPrice(),
                flight.getAvailableSeats()
            };
            tableModel.addRow(row);
        }
    }
    
    private void bookFlight() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int flightId = (int) tableModel.getValueAt(selectedRow, 0);
            dispose();
            new BookingView(currentUser, flightId);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a flight to book.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void backToMainMenu() {
        dispose();
        new MainMenuView(currentUser);
    }
}