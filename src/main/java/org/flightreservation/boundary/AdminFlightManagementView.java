package org.flightreservation.boundary;

import org.flightreservation.controller.AircraftController;
import org.flightreservation.controller.FlightController;
import org.flightreservation.controller.ReservationController;
import org.flightreservation.entity.Aircraft;
import org.flightreservation.entity.Flight;
import org.flightreservation.entity.Reservation;
import org.flightreservation.entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AdminFlightManagementView extends JFrame {
    private User currentUser;
    private FlightController flightController;
    private AircraftController aircraftController;
    private ReservationController reservationController;
    
    private JTable flightsTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton backButton;
    
    // Form fields
    private JTextField flightNumberField;
    private JTextField originField;
    private JTextField destinationField;
    private JTextField airlineField;
    private JTextField departureTimeField;
    private JTextField arrivalTimeField;
    private JTextField priceField;
    private JComboBox<String> aircraftComboBox;
    private JTextField availableSeatsField;
    private JButton saveButton;
    private JButton cancelButton;
    private int editingFlightId = -1;
    private JPanel formPanel;
    private JPanel topButtonPanel;

    public AdminFlightManagementView(User user) {
        this.currentUser = user;
        this.flightController = new FlightController();
        this.aircraftController = new AircraftController();
        this.reservationController = new ReservationController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupTable();
        populateAircraftComboBox();
        setupWindowProperties();
    }

    private void initializeComponents() {
        // Table setup
        String[] columnNames = {"ID", "Flight Number", "Origin", "Destination", "Airline", "Departure Time", "Arrival Time", "Price", "Aircraft ID", "Available Seats"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        flightsTable = new JTable(tableModel);
        flightsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flightsTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        flightsTable.setRowHeight(25);
        
        // Style the table header
        JTableHeader header = flightsTable.getTableHeader();
        header.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        
        // Style the table
        flightsTable.setSelectionBackground(new Color(173, 216, 230));
        flightsTable.setSelectionForeground(Color.BLACK);
        
        // Create styled buttons
        addButton = createStyledButton("Add New Flight", new Color(60, 179, 113));
        editButton = createStyledButton("Edit Selected", new Color(255, 140, 0));
        deleteButton = createStyledButton("Delete Selected", new Color(220, 20, 60));
        refreshButton = createStyledButton("Refresh", new Color(70, 130, 180));
        backButton = createStyledButton("Back to Main Menu", new Color(105, 105, 105));
        
        // Form fields
        flightNumberField = createStyledTextField(15);
        originField = createStyledTextField(15);
        destinationField = createStyledTextField(15);
        airlineField = createStyledTextField(15);
        departureTimeField = createStyledTextField(20);
        arrivalTimeField = createStyledTextField(20);
        priceField = createStyledTextField(10);
        aircraftComboBox = new JComboBox<>();
        aircraftComboBox.setPreferredSize(new Dimension(200, 30));
        aircraftComboBox.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        availableSeatsField = createStyledTextField(10);
        saveButton = createStyledButton("Save", new Color(60, 179, 113));
        cancelButton = createStyledButton("Cancel", new Color(105, 105, 105));
        
        // Panels
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Flight Details"));
        
        topButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        topButtonPanel.setBackground(Color.WHITE);
    }
    
    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setPreferredSize(new Dimension(150, 30));
        textField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        return textField;
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 35));
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header panel
        // JPanel headerPanel = new JPanel();
        // headerPanel.setBackground(new Color(70, 130, 180));
        // headerPanel.setPreferredSize(new Dimension(800, 60));
        // headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        
        // JLabel headerLabel = new JLabel("Flight Management");
        // headerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        // headerLabel.setForeground(Color.WHITE);
        // headerPanel.add(headerLabel);
        
        // Top button panel
        topButtonPanel.add(addButton);
        topButtonPanel.add(editButton);
        topButtonPanel.add(deleteButton);
        topButtonPanel.add(refreshButton);
        topButtonPanel.add(backButton);
        
        // Form panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Flight Number:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(flightNumberField, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Origin:"), gbc);
        
        gbc.gridx = 3;
        formPanel.add(originField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Destination:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(destinationField, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Airline:"), gbc);
        
        gbc.gridx = 3;
        formPanel.add(airlineField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Departure Time (YYYY-MM-DD HH:MM):"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(departureTimeField, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Arrival Time (YYYY-MM-DD HH:MM):"), gbc);
        
        gbc.gridx = 3;
        formPanel.add(arrivalTimeField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Price:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Aircraft:"), gbc);
        
        gbc.gridx = 3;
        formPanel.add(aircraftComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Available Seats:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(availableSeatsField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        JPanel saveCancelButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        saveCancelButtonPanel.setBackground(Color.WHITE);
        saveCancelButtonPanel.add(saveButton);
        saveCancelButtonPanel.add(cancelButton);
        formPanel.add(saveCancelButtonPanel, gbc);
        
        // Table scroll pane
        JScrollPane tableScrollPane = new JScrollPane(flightsTable);
        tableScrollPane.setPreferredSize(new Dimension(900, 300));
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Main panel layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // add(headerPanel, BorderLayout.NORTH);
        add(topButtonPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFlight();
            }
        });
        
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editFlight();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFlight();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshFlights();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToMainMenu();
            }
        });
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFlight();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelEditing();
            }
        });
        
        flightsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = flightsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                } else {
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            }
        });
    }
    
    private void setupWindowProperties() {
        setTitle("Flight Reservation System - Flight Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void setupTable() {
        refreshFlights();
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
    
    private void populateAircraftComboBox() {
        aircraftComboBox.removeAllItems();
        List<Aircraft> aircrafts = aircraftController.getAllAircrafts();
        for (Aircraft aircraft : aircrafts) {
            aircraftComboBox.addItem(aircraft.getModel() + " (" + aircraft.getId() + ")");
        }
    }
    
    private void refreshFlights() {
        tableModel.setRowCount(0);
        List<Flight> flights = flightController.getAllFlights();
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
                flight.getAircraftId(),
                flight.getAvailableSeats()
            };
            tableModel.addRow(row);
        }
    }
    
    private void addFlight() {
        clearForm();
        editingFlightId = -1;
        flightNumberField.requestFocus();
        JOptionPane.showMessageDialog(this, "Enter flight details and click Save.");
    }
    
    private void editFlight() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int flightId = (int) tableModel.getValueAt(selectedRow, 0);
            Flight flight = flightController.getFlightById(flightId);
            if (flight != null) {
                editingFlightId = flightId;
                flightNumberField.setText(flight.getFlightNumber());
                originField.setText(flight.getOrigin());
                destinationField.setText(flight.getDestination());
                airlineField.setText(flight.getAirline());
                departureTimeField.setText(flight.getDepartureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                arrivalTimeField.setText(flight.getArrivalTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                priceField.setText(String.valueOf(flight.getPrice()));
                availableSeatsField.setText(String.valueOf(flight.getAvailableSeats()));
                
                // Select the aircraft in the combo box
                for (int i = 0; i < aircraftComboBox.getItemCount(); i++) {
                    String item = aircraftComboBox.getItemAt(i);
                    if (item.contains("(" + flight.getAircraftId() + ")")) {
                        aircraftComboBox.setSelectedIndex(i);
                        break;
                    }
                }
                
                JOptionPane.showMessageDialog(this, "Modify flight details and click Save.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a flight to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteFlight() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int flightId = (int) tableModel.getValueAt(selectedRow, 0);
            
            // Check if flight has any reservations
            List<Reservation> reservations = reservationController.getAllReservations();
            int reservationCount = 0;
            for (Reservation reservation : reservations) {
                if (reservation.getFlightId() == flightId) {
                    reservationCount++;
                }
            }
            
            String message = "Are you sure you want to delete this flight?";
            if (reservationCount > 0) {
                message = "This flight has " + reservationCount + " reservation(s). Deleting this flight will also delete all related reservations. " + message;
            }
            
            int option = JOptionPane.showConfirmDialog(this, 
                message, 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // Delete all related reservations first
                for (Reservation reservation : reservations) {
                    if (reservation.getFlightId() == flightId) {
                        reservationController.cancelReservation(reservation.getId());
                    }
                }
                
                if (flightController.deleteFlight(flightId)) {
                    JOptionPane.showMessageDialog(this, "Flight and all related reservations deleted successfully.");
                    refreshFlights();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete flight.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a flight to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void saveFlight() {
        String flightNumber = flightNumberField.getText().trim();
        String origin = originField.getText().trim();
        String destination = destinationField.getText().trim();
        String airline = airlineField.getText().trim();
        String departureTimeStr = departureTimeField.getText().trim();
        String arrivalTimeStr = arrivalTimeField.getText().trim();
        String priceStr = priceField.getText().trim();
        String availableSeatsStr = availableSeatsField.getText().trim();
        
        if (flightNumber.isEmpty() || origin.isEmpty() || destination.isEmpty() || airline.isEmpty() ||
            departureTimeStr.isEmpty() || arrivalTimeStr.isEmpty() || 
            priceStr.isEmpty() || availableSeatsStr.isEmpty() || 
            aircraftComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Parse times
        LocalDateTime departureTime, arrivalTime;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            departureTime = LocalDateTime.parse(departureTimeStr, formatter);
            arrivalTime = LocalDateTime.parse(arrivalTimeStr, formatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date/time format. Use YYYY-MM-DD HH:MM", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Parse price
        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                JOptionPane.showMessageDialog(this, "Price must be greater than 0.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid price format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Parse available seats
        int availableSeats;
        try {
            availableSeats = Integer.parseInt(availableSeatsStr);
            if (availableSeats <= 0) {
                JOptionPane.showMessageDialog(this, "Available seats must be greater than 0.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid available seats format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get aircraft ID
        String selectedAircraft = (String) aircraftComboBox.getSelectedItem();
        int aircraftId = Integer.parseInt(selectedAircraft.substring(selectedAircraft.lastIndexOf("(") + 1, selectedAircraft.lastIndexOf(")")));
        
        if (editingFlightId == -1) {
            // Adding new flight
            Flight flight = new Flight(flightNumber, origin, destination, airline, departureTime, arrivalTime, price, aircraftId, availableSeats);
            if (flightController.addFlight(flight)) {
                JOptionPane.showMessageDialog(this, "Flight added successfully.");
                refreshFlights();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add flight.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Updating existing flight
            Flight flight = new Flight(editingFlightId, flightNumber, origin, destination, airline, departureTime, arrivalTime, price, aircraftId, availableSeats);
            if (flightController.updateFlight(flight)) {
                JOptionPane.showMessageDialog(this, "Flight updated successfully.");
                refreshFlights();
                clearForm();
                editingFlightId = -1;
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update flight.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cancelEditing() {
        clearForm();
        editingFlightId = -1;
    }
    
    private void clearForm() {
        flightNumberField.setText("");
        originField.setText("");
        destinationField.setText("");
        airlineField.setText("");
        departureTimeField.setText("");
        arrivalTimeField.setText("");
        priceField.setText("");
        availableSeatsField.setText("");
        aircraftComboBox.setSelectedIndex(-1);
    }
    
    private void backToMainMenu() {
        dispose();
        new MainMenuView(currentUser);
    }
}