package org.flightreservation.boundary;

import org.flightreservation.controller.CustomerController;
import org.flightreservation.controller.FlightController;
import org.flightreservation.controller.ReservationController;
import org.flightreservation.entity.Customer;
import org.flightreservation.entity.Flight;
import org.flightreservation.entity.Reservation;
import org.flightreservation.entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationManagementView extends JFrame {
    private User currentUser;
    private ReservationController reservationController;
    private CustomerController customerController;
    private FlightController flightController;
    
    private JTable reservationsTable;
    private DefaultTableModel tableModel;
    private JButton modifyButton;
    private JButton cancelButton;
    private JButton refreshButton;
    private JButton backButton;
    
    // Filter fields
    private JTextField customerIdFilterField;
    private JTextField dateFilterField;
    private JButton filterButton;

    public ReservationManagementView(User user) {
        this.currentUser = user;
        this.reservationController = new ReservationController();
        this.customerController = new CustomerController();
        this.flightController = new FlightController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupTable();
        setTitle("Flight Reservation System - Reservation Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeComponents() {
        // Table setup
        String[] columnNames = {"ID", "Customer Name", "Flight Number", "Airline", "Reservation Date", "Passengers", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reservationsTable = new JTable(tableModel);
        reservationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Buttons
        modifyButton = new JButton("Modify Selected Reservation");
        cancelButton = new JButton("Cancel Selected Reservation");
        refreshButton = new JButton("Refresh");
        backButton = new JButton("Back to Main Menu");
        
        // Filter fields
        customerIdFilterField = new JTextField(10);
        dateFilterField = new JTextField(15);
        filterButton = new JButton("Filter");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel with filter controls
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel("Customer ID:"));
        filterPanel.add(customerIdFilterField);
        filterPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        filterPanel.add(dateFilterField);
        filterPanel.add(filterButton);
        filterPanel.add(refreshButton);
        filterPanel.add(backButton);
        
        // Middle panel with buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(modifyButton);
        buttonPanel.add(cancelButton);
        
        // Table scroll pane
        JScrollPane tableScrollPane = new JScrollPane(reservationsTable);
        tableScrollPane.setPreferredSize(new Dimension(900, 400));
        
        add(filterPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tableScrollPane, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyReservation();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelReservation();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshReservations();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToMainMenu();
            }
        });
        
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterReservations();
            }
        });
        
        reservationsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = reservationsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    modifyButton.setEnabled(true);
                    cancelButton.setEnabled(true);
                } else {
                    modifyButton.setEnabled(false);
                    cancelButton.setEnabled(false);
                }
            }
        });
    }
    
    private void setupTable() {
        refreshReservations();
        modifyButton.setEnabled(false);
        cancelButton.setEnabled(false);
    }
    
    private void refreshReservations() {
        tableModel.setRowCount(0);
        
        // For customers, only show their own reservations
        if ("CUSTOMER".equals(currentUser.getRole())) {
            // Find customer ID for current user
            Customer currentCustomer = findCustomerByUser(currentUser);
            if (currentCustomer != null) {
                List<Reservation> reservations = reservationController.getReservationsByCustomerId(currentCustomer.getId());
                populateTable(reservations);
            }
        } else {
            // For agents and admins, show all reservations
            List<Reservation> reservations = reservationController.getAllReservations();
            populateTable(reservations);
        }
    }
    
    private Customer findCustomerByUser(User user) {
        // For customers, find the customer that matches their username pattern
        // Username is created as firstname.lastname
        String username = user.getUsername();
        List<Customer> customers = customerController.getAllCustomers();
        
        // Try to find customer by matching username pattern
        for (Customer customer : customers) {
            String expectedUsername = (customer.getFirstName().toLowerCase() + "." + customer.getLastName().toLowerCase());
            if (expectedUsername.equals(username)) {
                return customer;
            }
        }
        
        // If not found by exact match, try with counter suffixes
        for (Customer customer : customers) {
            String baseUsername = (customer.getFirstName().toLowerCase() + "." + customer.getLastName().toLowerCase());
            if (username.startsWith(baseUsername)) {
                return customer;
            }
        }
        
        return null;
    }
    
    private void filterReservations() {
        String customerIdStr = customerIdFilterField.getText().trim();
        String dateStr = dateFilterField.getText().trim();
        
        List<Reservation> reservations;
        
        // For customers, only show their own reservations
        if ("CUSTOMER".equals(currentUser.getRole())) {
            Customer currentCustomer = findCustomerByUser(currentUser);
            if (currentCustomer != null) {
                reservations = reservationController.getReservationsByCustomerId(currentCustomer.getId());
            } else {
                reservations = reservationController.getAllReservations();
            }
        } else {
            if (!customerIdStr.isEmpty()) {
                try {
                    int customerId = Integer.parseInt(customerIdStr);
                    reservations = reservationController.getReservationsByCustomerId(customerId);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid customer ID format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                reservations = reservationController.getAllReservations();
            }
        }
        
        // Further filter by date if provided
        if (!dateStr.isEmpty()) {
            // In a real implementation, we would filter by date here
            // For simplicity, we'll just show all reservations
        }
        
        tableModel.setRowCount(0);
        populateTable(reservations);
    }
    
    private void populateTable(List<Reservation> reservations) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        for (Reservation reservation : reservations) {
            // Get customer name
            Customer customer = customerController.getCustomerById(reservation.getCustomerId());
            String customerName = customer != null ? 
                customer.getFirstName() + " " + customer.getLastName() : 
                "Unknown Customer";
            
            // Get flight details
            Flight flight = flightController.getFlightById(reservation.getFlightId());
            String flightNumber = flight != null ? flight.getFlightNumber() : "Unknown Flight";
            String airline = flight != null ? flight.getAirline() : "Unknown Airline";
            
            Object[] row = {
                reservation.getId(),
                customerName,
                flightNumber,
                airline,
                reservation.getReservationDate().format(formatter),
                reservation.getNumberOfPassengers(),
                reservation.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    private void modifyReservation() {
        int selectedRow = reservationsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int reservationId = (int) tableModel.getValueAt(selectedRow, 0);
            int flightId = getFlightIdFromReservation(reservationId);
            
            if (flightId > 0) {
                dispose();
                new BookingView(currentUser, flightId, reservationId, true); // true indicates modification
            } else {
                JOptionPane.showMessageDialog(this, "Failed to find flight for reservation.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a reservation to modify.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private int getFlightIdFromReservation(int reservationId) {
        Reservation reservation = reservationController.getReservationById(reservationId);
        return reservation != null ? reservation.getFlightId() : -1;
    }
    
    private void cancelReservation() {
        int selectedRow = reservationsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int reservationId = (int) tableModel.getValueAt(selectedRow, 0);
            
            // Get reservation to update flight seats
            Reservation reservation = reservationController.getReservationById(reservationId);
            if (reservation != null) {
                int option = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to cancel this reservation?", 
                    "Confirm Cancel", 
                    JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    if (reservationController.cancelReservation(reservationId)) {
                        // Update flight available seats
                        Flight flight = flightController.getFlightById(reservation.getFlightId());
                        if (flight != null) {
                            flight.setAvailableSeats(flight.getAvailableSeats() + reservation.getNumberOfPassengers());
                            flightController.updateFlight(flight);
                        }
                        
                        JOptionPane.showMessageDialog(this, "Reservation cancelled successfully.");
                        refreshReservations();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to cancel reservation.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a reservation to cancel.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void backToMainMenu() {
        dispose();
        new MainMenuView(currentUser);
    }
}