package org.flightreservation.boundary;

import org.flightreservation.controller.CustomerController;
import org.flightreservation.controller.FlightController;
import org.flightreservation.controller.PaymentController;
import org.flightreservation.controller.ReservationController;
import org.flightreservation.entity.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingView extends JFrame {
    private User currentUser;
    private int flightId;
    private int reservationId; // For modification
    private boolean isModification; // Flag to indicate if this is a modification
    
    private FlightController flightController;
    private CustomerController customerController;
    private ReservationController reservationController;
    private PaymentController paymentController;
    
    private Flight flight;
    private Reservation existingReservation; // For modification
    private JComboBox<String> customerComboBox;
    private JTextField passengersField;
    private JTextField cardNumberField;
    private JTextField expiryDateField;
    private JTextField cvvField;
    private JButton confirmButton;
    private JButton cancelButton;
    
    // Payment fields for additional charges
    private JTextField additionalCardNumberField;
    private JTextField additionalExpiryDateField;
    private JTextField additionalCvvField;
    private JPanel additionalPaymentPanel;

    public BookingView(User user, int flightId) {
        this(user, flightId, 0, false);
    }
    
    // Constructor for modifying existing reservation
    public BookingView(User user, int flightId, int reservationId, boolean isModification) {
        this.currentUser = user;
        this.flightId = flightId;
        this.reservationId = reservationId;
        this.isModification = isModification;
        this.flightController = new FlightController();
        this.customerController = new CustomerController();
        this.reservationController = new ReservationController();
        this.paymentController = new PaymentController();
        
        loadFlightData();
        loadExistingReservation(); // Load existing reservation data if modifying
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateCustomerComboBox();
        loadExistingData(); // Load existing data if modifying
        
        setupWindowProperties();
    }

    private void loadFlightData() {
        flight = flightController.getFlightById(flightId);
    }
    
    private void loadExistingReservation() {
        if (isModification && reservationId > 0) {
            existingReservation = reservationController.getReservationById(reservationId);
        }
    }
    
    private void loadExistingData() {
        if (isModification && existingReservation != null) {
            // Select the customer in the combo box
            for (int i = 0; i < customerComboBox.getItemCount(); i++) {
                String item = customerComboBox.getItemAt(i);
                if (item.contains("(" + existingReservation.getCustomerId() + ")")) {
                    customerComboBox.setSelectedIndex(i);
                    break;
                }
            }
            
            // Set the number of passengers
            passengersField.setText(String.valueOf(existingReservation.getNumberOfPassengers()));
            
            // Update button text
            confirmButton.setText("Update Reservation");
        }
    }

    private void initializeComponents() {
        // Create styled combo box
        customerComboBox = new JComboBox<>();
        customerComboBox.setPreferredSize(new Dimension(200, 30));
        customerComboBox.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        
        // Create styled text fields
        passengersField = createStyledTextField(10);
        cardNumberField = createStyledTextField(20);
        expiryDateField = createStyledTextField(10);
        cvvField = createStyledTextField(5);
        
        // Additional payment fields for modification
        additionalCardNumberField = createStyledTextField(20);
        additionalExpiryDateField = createStyledTextField(10);
        additionalCvvField = createStyledTextField(5);
        
        // Create styled buttons
        confirmButton = createStyledButton(isModification ? "Update Reservation" : "Confirm Booking", new Color(60, 179, 113));
        cancelButton = createStyledButton("Cancel", new Color(105, 105, 105));
        
        // Additional payment panel
        additionalPaymentPanel = new JPanel(new GridBagLayout());
        additionalPaymentPanel.setBackground(Color.WHITE);
        additionalPaymentPanel.setBorder(BorderFactory.createTitledBorder("Additional Payment Required"));
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
        
        // Add hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(backgroundColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setPreferredSize(new Dimension(600, 60));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        
        JLabel headerLabel = new JLabel(isModification ? "Modify Reservation" : "Book Flight");
        headerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Flight information
        if (flight != null) {
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            mainPanel.add(new JLabel("Flight: " + flight.getFlightNumber() + " - " + 
                                   flight.getOrigin() + " to " + flight.getDestination() + 
                                   " (" + flight.getAirline() + ")"), gbc);
            
            gbc.gridy = 1;
            mainPanel.add(new JLabel("Price per person: $" + flight.getPrice()), gbc);
            
            gbc.gridy = 2;
            mainPanel.add(new JLabel("Available seats: " + flight.getAvailableSeats()), gbc);
        }
        
        // Customer selection - only show for non-customers or when modifying
        boolean isCustomer = "CUSTOMER".equals(currentUser.getRole());
        if (!isCustomer || isModification) {
            gbc.gridwidth = 1;
            gbc.gridy = 3;
            gbc.gridx = 0;
            mainPanel.add(new JLabel("Select Customer:"), gbc);
            
            gbc.gridx = 1;
            mainPanel.add(customerComboBox, gbc);
        } else {
            // For customers booking (not modifying), hide customer selection
            // We'll pre-select their customer in populateCustomerComboBox
        }
        
        // Passengers
        gbc.gridx = 0;
        gbc.gridy = isCustomer && !isModification ? 3 : 4;
        mainPanel.add(new JLabel("Number of Passengers:"), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(passengersField, gbc);
        
        // Payment information
        gbc.gridwidth = 2;
        gbc.gridy = isCustomer && !isModification ? 4 : 5;
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);
        
        gbc.gridy = isCustomer && !isModification ? 5 : 6;
        mainPanel.add(new JLabel("Payment Information (Required):"), gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = isCustomer && !isModification ? 6 : 7;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Card Number:"), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(cardNumberField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = isCustomer && !isModification ? 7 : 8;
        mainPanel.add(new JLabel("Expiry Date (MM/YY):"), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(expiryDateField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = isCustomer && !isModification ? 8 : 9;
        mainPanel.add(new JLabel("CVV:"), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(cvvField, gbc);
        
        // Additional payment panel (hidden by default)
        setupAdditionalPaymentPanel();
        gbc.gridy = isCustomer && !isModification ? 9 : 10;
        gbc.gridwidth = 2;
        mainPanel.add(additionalPaymentPanel, gbc);
        additionalPaymentPanel.setVisible(false); // Hidden by default
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupAdditionalPaymentPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        additionalPaymentPanel.add(new JLabel("Additional seats require extra payment:"), gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        additionalPaymentPanel.add(new JLabel("Card Number:"), gbc);
        
        gbc.gridx = 1;
        additionalPaymentPanel.add(additionalCardNumberField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        additionalPaymentPanel.add(new JLabel("Expiry Date (MM/YY):"), gbc);
        
        gbc.gridx = 1;
        additionalPaymentPanel.add(additionalExpiryDateField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        additionalPaymentPanel.add(new JLabel("CVV:"), gbc);
        
        gbc.gridx = 1;
        additionalPaymentPanel.add(additionalCvvField, gbc);
    }

    private void setupEventHandlers() {
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isModification) {
                    updateReservation();
                } else {
                    confirmBooking();
                }
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelBooking();
            }
        });
        
        // Listen for passenger count changes in modification mode
        if (isModification) {
            passengersField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    checkAdditionalPaymentRequired();
                }
            });
        }
    }
    
    private void setupWindowProperties() {
        if (isModification) {
            setTitle("Flight Reservation System - Modify Reservation");
        } else {
            setTitle("Flight Reservation System - Book Flight");
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);
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
    
    private void checkAdditionalPaymentRequired() {
        if (!isModification || existingReservation == null) return;
        
        String passengerText = passengersField.getText().trim();
        if (passengerText.isEmpty()) return;
        
        try {
            int newPassengers = Integer.parseInt(passengerText);
            int existingPassengers = existingReservation.getNumberOfPassengers();
            
            if (newPassengers > existingPassengers) {
                additionalPaymentPanel.setVisible(true);
                pack();
            } else {
                additionalPaymentPanel.setVisible(false);
                pack();
            }
        } catch (NumberFormatException e) {
            // Ignore invalid input
        }
    }
    
    private void populateCustomerComboBox() {
        customerComboBox.removeAllItems();
        List<Customer> customers = customerController.getAllCustomers();
        
        // Check if current user is a customer
        boolean isCustomer = "CUSTOMER".equals(currentUser.getRole());
        Customer currentUserCustomer = null;
        
        if (isCustomer) {
            currentUserCustomer = findCustomerByUser(currentUser);
        }
        
        for (Customer customer : customers) {
            String customerItem = customer.getFirstName() + " " + customer.getLastName() + " (" + customer.getId() + ")";
            customerComboBox.addItem(customerItem);
            
            // If this is a customer booking (not modifying), pre-select their customer
            if (isCustomer && !isModification && currentUserCustomer != null && 
                customer.getId() == currentUserCustomer.getId()) {
                customerComboBox.setSelectedItem(customerItem);
                // Disable the combo box for regular customers
                customerComboBox.setEnabled(false);
            }
        }
        
        // If no customers found
        if (customers.isEmpty()) {
            customerComboBox.addItem("No customers available");
            customerComboBox.setEnabled(false);
        }
    }
    
    private void confirmBooking() {
        if (flight == null) {
            JOptionPane.showMessageDialog(this, "Flight information not available.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get selected customer
        int customerId;
        boolean isCustomer = "CUSTOMER".equals(currentUser.getRole());
        
        if (isCustomer && !isModification) {
            // For customers booking, use their own customer ID
            Customer customer = findCustomerByUser(currentUser);
            if (customer == null) {
                JOptionPane.showMessageDialog(this, "Customer account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            customerId = customer.getId();
        } else {
            // For agents/admins or modification, use selected customer from combo box
            if (customerComboBox.getItemCount() == 0 || customerComboBox.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "No customers available.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String selectedCustomer = (String) customerComboBox.getSelectedItem();
            if (selectedCustomer == null || selectedCustomer.contains("No customers available")) {
                JOptionPane.showMessageDialog(this, "No customers available.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                customerId = Integer.parseInt(selectedCustomer.substring(selectedCustomer.lastIndexOf("(") + 1, selectedCustomer.lastIndexOf(")")));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid customer selection.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        String passengerText = passengersField.getText().trim();
        if (passengerText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter number of passengers.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int passengers;
        try {
            passengers = Integer.parseInt(passengerText);
            if (passengers <= 0) {
                JOptionPane.showMessageDialog(this, "Number of passengers must be greater than 0.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (passengers > flight.getAvailableSeats()) {
                JOptionPane.showMessageDialog(this, "Not enough available seats. Only " + flight.getAvailableSeats() + " seats available.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of passengers.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate payment details
        if (!validatePaymentDetails(cardNumberField, expiryDateField, cvvField)) {
            return;
        }
        
        // Create reservation
        Reservation reservation = new Reservation(
            customerId,
            flight.getId(),
            LocalDateTime.now(),
            passengers,
            "CONFIRMED"
        );
        
        if (reservationController.makeReservation(reservation)) {
            // Update flight available seats
            flight.setAvailableSeats(flight.getAvailableSeats() - passengers);
            flightController.updateFlight(flight);
            
            // Process payment
            double totalAmount = flight.getPrice() * passengers;
            Payment payment = new Payment(
                reservation.getId(),
                totalAmount,
                LocalDateTime.now(),
                "Credit Card",
                "PAID"
            );
            
            if (paymentController.processPayment(payment)) {
                showConfirmationPopup(reservation, totalAmount, passengers, 0, 0); // 0 for new booking (no refund, no additional)
            } else {
                JOptionPane.showMessageDialog(this, "Payment failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create reservation.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateReservation() {
        if (flight == null || existingReservation == null) {
            JOptionPane.showMessageDialog(this, "Flight or reservation information not available.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get selected customer
        int customerId;
        String selectedCustomer = (String) customerComboBox.getSelectedItem();
        if (selectedCustomer == null || selectedCustomer.contains("No customers available")) {
            JOptionPane.showMessageDialog(this, "No customers available.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            customerId = Integer.parseInt(selectedCustomer.substring(selectedCustomer.lastIndexOf("(") + 1, selectedCustomer.lastIndexOf(")")));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid customer selection.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String passengerText = passengersField.getText().trim();
        if (passengerText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter number of passengers.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int newPassengers;
        try {
            newPassengers = Integer.parseInt(passengerText);
            if (newPassengers <= 0) {
                JOptionPane.showMessageDialog(this, "Number of passengers must be greater than 0.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int existingPassengers = existingReservation.getNumberOfPassengers();
            int seatDifference = newPassengers - existingPassengers;
            
            // Check if we have enough seats
            if (seatDifference > flight.getAvailableSeats()) {
                JOptionPane.showMessageDialog(this, "Not enough available seats. Only " + flight.getAvailableSeats() + " seats available.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of passengers.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Calculate seat difference and amounts
        int existingPassengers = existingReservation.getNumberOfPassengers();
        int seatDifference = newPassengers - existingPassengers;
        double refundAmount = 0;
        double additionalAmount = 0;
        
        // Handle payment based on seat difference
        if (seatDifference > 0) {
            // Need additional payment
            if (!validatePaymentDetails(additionalCardNumberField, additionalExpiryDateField, additionalCvvField)) {
                return;
            }
            additionalAmount = seatDifference * flight.getPrice();
        } else if (seatDifference < 0) {
            // Process refund
            refundAmount = Math.abs(seatDifference) * flight.getPrice();
        }
        // If seatDifference == 0, no payment changes needed
        
        // Update reservation
        existingReservation.setCustomerId(customerId);
        existingReservation.setNumberOfPassengers(newPassengers);
        existingReservation.setReservationDate(LocalDateTime.now());
        
        if (reservationController.updateReservation(existingReservation)) {
            // Update flight available seats
            flight.setAvailableSeats(flight.getAvailableSeats() - seatDifference);
            flightController.updateFlight(flight);
            
            // Process additional payment if needed
            if (additionalAmount > 0) {
                Payment additionalPayment = new Payment(
                    existingReservation.getId(),
                    additionalAmount,
                    LocalDateTime.now(),
                    "Credit Card",
                    "PAID"
                );
                paymentController.processPayment(additionalPayment);
            }
            
            double totalAmount = flight.getPrice() * newPassengers;
            double previousAmount = flight.getPrice() * existingPassengers;
            
            showConfirmationPopup(existingReservation, totalAmount, newPassengers, refundAmount, additionalAmount, previousAmount);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update reservation.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validatePaymentDetails(JTextField cardField, JTextField expiryField, JTextField cvvField) {
        String cardNumber = cardField.getText().trim();
        String expiryDate = expiryField.getText().trim();
        String cvv = cvvField.getText().trim();
        
        // Card number validation (simple validation)
        if (cardNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Card number is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (cardNumber.length() < 13 || cardNumber.length() > 19 || !cardNumber.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid card number (13-19 digits).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Expiry date validation
        if (expiryDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Expiry date is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!expiryDate.matches("\\d{2}/\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Please enter expiry date in MM/YY format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // CVV validation
        if (cvv.isEmpty()) {
            JOptionPane.showMessageDialog(this, "CVV is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (cvv.length() < 3 || cvv.length() > 4 || !cvv.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid CVV (3-4 digits).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void showConfirmationPopup(Reservation reservation, double totalAmount, int passengers, 
                                     double refundAmount, double additionalAmount) {
        showConfirmationPopup(reservation, totalAmount, passengers, refundAmount, additionalAmount, 0);
    }
    
    private void showConfirmationPopup(Reservation reservation, double totalAmount, int passengers, 
                                     double refundAmount, double additionalAmount, double previousAmount) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("==========================================\n");
        receipt.append("           FLIGHT RESERVATION RECEIPT     \n");
        receipt.append("==========================================\n");
        receipt.append("Reservation ID: ").append(reservation.getId()).append("\n");
        receipt.append("Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
        receipt.append("------------------------------------------\n");
        
        if (flight != null) {
            receipt.append("Flight Number: ").append(flight.getFlightNumber()).append("\n");
            receipt.append("Airline: ").append(flight.getAirline()).append("\n");
            receipt.append("Route: ").append(flight.getOrigin()).append(" to ").append(flight.getDestination()).append("\n");
            receipt.append("Departure: ").append(flight.getDepartureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
            receipt.append("Arrival: ").append(flight.getArrivalTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
        }
        
        // Get customer details
        Customer customer = customerController.getCustomerById(reservation.getCustomerId());
        if (customer != null) {
            receipt.append("Passenger: ").append(customer.getFirstName()).append(" ").append(customer.getLastName()).append("\n");
            receipt.append("Email: ").append(customer.getEmail()).append("\n");
        }
        
        receipt.append("------------------------------------------\n");
        receipt.append("Passengers: ").append(passengers).append("\n");
        receipt.append("Price per person: $").append(flight != null ? flight.getPrice() : 0).append("\n");
        receipt.append("Total Amount: $").append(String.format("%.2f", totalAmount)).append("\n");
        
        if (isModification && previousAmount > 0) {
            receipt.append("Previous Amount: $").append(String.format("%.2f", previousAmount)).append("\n");
        }
        
        if (additionalAmount > 0) {
            receipt.append("Additional Charge: $").append(String.format("%.2f", additionalAmount)).append("\n");
        }
        
        if (refundAmount > 0) {
            receipt.append("Refund Amount: $").append(String.format("%.2f", refundAmount)).append("\n");
            receipt.append("*** REFUND PROCESSED TO ORIGINAL PAYMENT METHOD ***\n");
        }
        
        receipt.append("Payment Method: Credit Card\n");
        receipt.append("Status: CONFIRMED\n");
        receipt.append("==========================================\n");
        receipt.append("         THANK YOU FOR FLYING WITH US     \n");
        receipt.append("==========================================\n");
        
        // Show confirmation in popup with enhanced styling
        JTextArea textArea = new JTextArea(receipt.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create a custom dialog with title bar color
        JOptionPane optionPane = new JOptionPane(scrollPane, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, "Booking Confirmation");
        dialog.setBackground(new Color(70, 130, 180));
        dialog.setVisible(true);
        
        // Close the booking window after confirmation
        backToMainMenu();
    }
    
    private void cancelBooking() {
        backToMainMenu();
    }
    
    private void backToMainMenu() {
        dispose();
        new SearchFlightView(currentUser);
    }
}