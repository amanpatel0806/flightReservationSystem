package org.flightreservation.boundary;

import org.flightreservation.controller.CustomerController;
import org.flightreservation.controller.FlightController;
import org.flightreservation.controller.PaymentController;
import org.flightreservation.controller.ReservationController;
import org.flightreservation.entity.*;
import org.flightreservation.strategy.*;
import org.flightreservation.decorator.*;
import org.flightreservation.data.BookingAddOnDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class BookingView extends JFrame {
    private User currentUser;
    private int flightId;
    private int reservationId; // For modification
    private boolean isModification; // Flag to indicate if this is a modification
    
    private FlightController flightController;
    private CustomerController customerController;
    private ReservationController reservationController;
    private PaymentController paymentController;
    private BookingAddOnDAO bookingAddOnDAO;
    
    private Flight flight;
    private Reservation existingReservation; // For modification
    private JComboBox<String> customerComboBox;
    private JTextField passengersField;
    private JButton confirmButton;
    private JButton cancelButton;
    
    // Add-on checkboxes
    private JCheckBox seatUpgradeCheckbox;
    private JCheckBox mealUpgradeCheckbox;
    private JCheckBox travelInsuranceCheckbox;
    
    // Payment method selector
    private JComboBox<String> paymentMethodComboBox;
    
    // Credit Card payment fields
    private JTextField cardNumberField;
    private JTextField expiryDateField;
    private JTextField cvvField;
    private JPanel creditCardPanel;
    
    // PayPal payment fields
    private JButton paypalLoginButton;
    private JPanel paypalPanel;
    
    // Bitcoin payment fields
    private JTextField bitcoinWalletField;
    private JPanel bitcoinPanel;
    
    // Payment panels container
    private JPanel paymentDetailsPanel;
    
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
        this.bookingAddOnDAO = new BookingAddOnDAO();
        
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
            
            // Load existing add-ons and check the boxes
            List<BookingAddOn> existingAddOns = bookingAddOnDAO.findByReservationId(existingReservation.getId());
            for (BookingAddOn addOn : existingAddOns) {
                switch (addOn.getAddOnType()) {
                    case "SEAT_UPGRADE":
                        seatUpgradeCheckbox.setSelected(true);
                        break;
                    case "MEAL_UPGRADE":
                        mealUpgradeCheckbox.setSelected(true);
                        break;
                    case "TRAVEL_INSURANCE":
                        travelInsuranceCheckbox.setSelected(true);
                        break;
                }
            }
            
            // Update button text
            confirmButton.setText("Update Reservation");
        }
    }

    private void initializeComponents() {
        // Create styled combo box
        customerComboBox = new JComboBox<>();
        customerComboBox.setPreferredSize(new Dimension(200, 30));
        customerComboBox.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        
        // Payment method selector
        paymentMethodComboBox = new JComboBox<>(new String[]{"Credit Card", "PayPal", "Bitcoin"});
        paymentMethodComboBox.setPreferredSize(new Dimension(200, 30));
        paymentMethodComboBox.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        
        // Create styled text fields
        passengersField = createStyledTextField(10);
        cardNumberField = createStyledTextField(20);
        expiryDateField = createStyledTextField(10);
        cvvField = createStyledTextField(5);
        bitcoinWalletField = createStyledTextField(35);
        
        // Additional payment fields for modification
        additionalCardNumberField = createStyledTextField(20);
        additionalExpiryDateField = createStyledTextField(10);
        additionalCvvField = createStyledTextField(5);
        
        // Create styled buttons
        confirmButton = createStyledButton(isModification ? "Update Reservation" : "Confirm Booking", new Color(60, 179, 113));
        cancelButton = createStyledButton("Cancel", new Color(105, 105, 105));
        
        // PayPal login button
        paypalLoginButton = createStyledButton("Login and Pay", new Color(0, 112, 210));
        
        // Create add-on checkboxes
        seatUpgradeCheckbox = new JCheckBox("Seat Upgrade (+$50 per passenger)");
        mealUpgradeCheckbox = new JCheckBox("Meal Upgrade (+$25 per passenger)");
        travelInsuranceCheckbox = new JCheckBox("Travel Insurance (+$30 per passenger)");
        
        // Style checkboxes
        Font checkboxFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        seatUpgradeCheckbox.setFont(checkboxFont);
        mealUpgradeCheckbox.setFont(checkboxFont);
        travelInsuranceCheckbox.setFont(checkboxFont);
        seatUpgradeCheckbox.setBackground(Color.WHITE);
        mealUpgradeCheckbox.setBackground(Color.WHITE);
        travelInsuranceCheckbox.setBackground(Color.WHITE);
        
        // Create payment method panels
        createPaymentPanels();
        
        // Payment details container panel
        paymentDetailsPanel = new JPanel(new CardLayout());
        paymentDetailsPanel.add(creditCardPanel, "Credit Card");
        paymentDetailsPanel.add(paypalPanel, "PayPal");
        paymentDetailsPanel.add(bitcoinPanel, "Bitcoin");
        
        // Additional payment panel
        additionalPaymentPanel = new JPanel(new GridBagLayout());
        additionalPaymentPanel.setBackground(Color.WHITE);
        additionalPaymentPanel.setBorder(BorderFactory.createTitledBorder("Additional Payment Required"));
    }
    
    private void createPaymentPanels() {
        // Credit Card Panel
        creditCardPanel = new JPanel(new GridBagLayout());
        creditCardPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        creditCardPanel.add(new JLabel("Card Number:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        creditCardPanel.add(cardNumberField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        creditCardPanel.add(new JLabel("Expiry Date (MM/YY):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        creditCardPanel.add(expiryDateField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        creditCardPanel.add(new JLabel("CVV:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        creditCardPanel.add(cvvField, gbc);
        
        // PayPal Panel
        paypalPanel = new JPanel(new GridBagLayout());
        paypalPanel.setBackground(Color.WHITE);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        paypalPanel.add(new JLabel("Click below to login and pay with PayPal:"), gbc);
        gbc.gridy = 1;
        paypalPanel.add(paypalLoginButton, gbc);
        
        // Bitcoin Panel
        bitcoinPanel = new JPanel(new GridBagLayout());
        bitcoinPanel.setBackground(Color.WHITE);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        bitcoinPanel.add(new JLabel("Bitcoin Wallet Address:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        bitcoinPanel.add(bitcoinWalletField, gbc);
    }
    
    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        // Make fields wider - calculate width based on columns
        int width = Math.max(200, columns * 10);
        textField.setPreferredSize(new Dimension(width, 30));
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
            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.NONE;
            mainPanel.add(new JLabel("Select Customer:"), gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            mainPanel.add(customerComboBox, gbc);
        } else {
            // For customers booking (not modifying), hide customer selection
            // We'll pre-select their customer in populateCustomerComboBox
        }
        
        // Passengers
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = isCustomer && !isModification ? 3 : 4;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Number of Passengers:"), gbc);
        
        gbc.gridwidth = 2;
        gbc.gridy = isCustomer && !isModification ? 4 : 5;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(passengersField, gbc);
        
        // Add-ons section
        gbc.gridwidth = 2;
        gbc.gridy = isCustomer && !isModification ? 5 : 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);
        
        gbc.gridy = isCustomer && !isModification ? 6 : 7;
        gbc.fill = GridBagConstraints.NONE;
        JLabel addOnsLabel = new JLabel("Optional Add-ons:");
        addOnsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        mainPanel.add(addOnsLabel, gbc);
        
        gbc.gridy = isCustomer && !isModification ? 7 : 8;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(seatUpgradeCheckbox, gbc);
        
        gbc.gridy = isCustomer && !isModification ? 8 : 9;
        mainPanel.add(mealUpgradeCheckbox, gbc);
        
        gbc.gridy = isCustomer && !isModification ? 9 : 10;
        mainPanel.add(travelInsuranceCheckbox, gbc);
        
        // Payment information
        gbc.gridwidth = 2;
        gbc.gridy = isCustomer && !isModification ? 10 : 11;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);
        
        gbc.gridy = isCustomer && !isModification ? 11 : 12;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Payment Information (Required):"), gbc);
        
        // Payment method selector
        gbc.gridwidth = 1;
        gbc.gridy = isCustomer && !isModification ? 12 : 13;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Payment Method:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(paymentMethodComboBox, gbc);
        
        // Payment details panel (shows different fields based on selection)
        gbc.gridwidth = 2;
        gbc.gridy = isCustomer && !isModification ? 13 : 14;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        mainPanel.add(paymentDetailsPanel, gbc);
        
        // Additional payment panel (hidden by default)
        setupAdditionalPaymentPanel();
        gbc.gridy = isCustomer && !isModification ? 14 : 15;
        gbc.gridwidth = 2;
        mainPanel.add(additionalPaymentPanel, gbc);
        additionalPaymentPanel.setVisible(false); // Hidden by default
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        // Wrap main panel in scroll pane to handle overflow
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
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
        
        // Payment method selector change handler
        paymentMethodComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMethod = (String) paymentMethodComboBox.getSelectedItem();
                CardLayout cardLayout = (CardLayout) paymentDetailsPanel.getLayout();
                cardLayout.show(paymentDetailsPanel, selectedMethod);
            }
        });
        
        // PayPal login button handler
        paypalLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePayPalLogin();
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
    
    private void handlePayPalLogin() {
        // This will be handled by the PayPal strategy when payment is processed
        // For now, just show a message that user needs to click confirm
        JOptionPane.showMessageDialog(this, 
            "Please click 'Confirm Booking' to process PayPal payment.", 
            "PayPal", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void setupWindowProperties() {
        if (isModification) {
            setTitle("Flight Reservation System - Modify Reservation");
        } else {
            setTitle("Flight Reservation System - Book Flight");
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 900);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
    private Customer findCustomerByUser(User user) {
        // Simply find customer by username - direct link
        return customerController.getCustomerByUsername(user.getUsername());
    }
    
    /**
     * Creates a Customer record for a User if it doesn't exist.
     * Prompts user for required information.
     */
    private Customer createCustomerFromUser(User user) {
        // Prompt user for their personal information
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        JTextField firstNameField = createStyledTextField(20);
        JTextField lastNameField = createStyledTextField(20);
        JTextField emailField = createStyledTextField(20);
        JTextField phoneField = createStyledTextField(20);
        JTextField addressField = createStyledTextField(20);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        panel.add(firstNameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        panel.add(lastNameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        panel.add(addressField, gbc);
        
        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Complete Your Profile",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }
        
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        
        // Validate required fields
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "First name, last name, and email are required.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        // Validate email format
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid email address.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        // Check if email already exists
        Customer existingCustomer = customerController.getCustomerByEmail(email);
        if (existingCustomer != null) {
            JOptionPane.showMessageDialog(this, 
                "This email is already registered to another account.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        // Create customer record
        Customer customer = new Customer(firstName, lastName, email, phone, address, user.getUsername());
        if (customerController.addCustomer(customer)) {
            JOptionPane.showMessageDialog(this, 
                "Profile created successfully! You can now proceed with booking.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            return customer;
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to create customer profile. Please try again.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
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
                // Customer record doesn't exist - create it automatically
                customer = createCustomerFromUser(currentUser);
                if (customer == null) {
                    JOptionPane.showMessageDialog(this, 
                        "Unable to create customer profile. Please contact support.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
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
        
        // Get selected payment method and set strategy
        String selectedPaymentMethod = (String) paymentMethodComboBox.getSelectedItem();
        PaymentStrategy strategy = getPaymentStrategy(selectedPaymentMethod);
        if (strategy == null) {
            JOptionPane.showMessageDialog(this, "Please select a valid payment method.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate payment details based on selected method
        if (!validatePaymentByMethod(selectedPaymentMethod)) {
            return;
        }
        
        // Set the payment strategy in controller
        paymentController.setPaymentStrategy(strategy);

        int resId = (int) (UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        if (resId < 0) resId = -resId;
        // Create reservation
        Reservation reservation = new Reservation(
            resId,
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
            
            // Build booking decorator chain
            BookingComponent booking = buildBookingDecorator(flight, passengers);
            double totalAmount = booking.getCost();
            String bookingDescription = booking.getDescription();
            
            // Save add-ons to database
            saveBookingAddOns(reservation.getId(), booking, passengers);
            
            // Process payment
            Payment payment = new Payment(
                reservation.getId(),
                totalAmount,
                LocalDateTime.now(),
                "", // Will be set by strategy
                "PENDING"
            );
            
            // For Bitcoin, set wallet address in payment method field
            if ("Bitcoin".equals(selectedPaymentMethod)) {
                payment.setPaymentMethod(bitcoinWalletField.getText().trim());
            }
            
            if (paymentController.processPayment(payment)) {
                showConfirmationPopup(reservation, totalAmount, passengers, 0, 0, 0, selectedPaymentMethod, bookingDescription);
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
            
            // Delete old add-ons and save new ones
            bookingAddOnDAO.deleteByReservationId(existingReservation.getId());
            BookingComponent booking = buildBookingDecorator(flight, newPassengers);
            saveBookingAddOns(existingReservation.getId(), booking, newPassengers);
            
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
            
            // Calculate total with add-ons
            double totalAmount = booking.getCost();
            double previousAmount = flight.getPrice() * existingPassengers;
            String bookingDescription = booking.getDescription();
            
            showConfirmationPopup(existingReservation, totalAmount, newPassengers, refundAmount, additionalAmount, previousAmount, "Credit Card", bookingDescription);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update reservation.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private PaymentStrategy getPaymentStrategy(String paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }
        
        switch (paymentMethod) {
            case "Credit Card":
                return new CreditCardPaymentStrategy();
            case "PayPal":
                return new PayPalPaymentStrategy();
            case "Bitcoin":
                return new BitcoinPaymentStrategy();
            default:
                return null;
        }
    }
    
    private boolean validatePaymentByMethod(String paymentMethod) {
        if (paymentMethod == null) {
            return false;
        }
        
        switch (paymentMethod) {
            case "Credit Card":
                return validatePaymentDetails(cardNumberField, expiryDateField, cvvField);
            case "PayPal":
                // PayPal validation is handled by the strategy (login dialog)
                return true;
            case "Bitcoin":
                return validateBitcoinWallet();
            default:
                return false;
        }
    }
    
    private boolean validateBitcoinWallet() {
        String walletAddress = bitcoinWalletField.getText().trim();
        
        if (walletAddress.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitcoin wallet address is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Basic Bitcoin address validation
        if (walletAddress.length() < 26 || walletAddress.length() > 35) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Bitcoin wallet address (26-35 characters).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Check if it starts with common Bitcoin address prefixes
        if (!walletAddress.startsWith("1") && !walletAddress.startsWith("3") && !walletAddress.startsWith("bc1")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Bitcoin wallet address (must start with 1, 3, or bc1).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
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
            JOptionPane.showMessageDialog(this, "Please enter expiry date in MM/YY format slash included.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validate that expiry date is not in the past
        try {
            String[] parts = expiryDate.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]);
            
            // Validate month is between 1-12
            if (month < 1 || month > 12) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid month. Please enter a month between 01 and 12.", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            // Get current date
            java.time.LocalDate currentDate = java.time.LocalDate.now();
            int currentYear = currentDate.getYear();
            int currentMonth = currentDate.getMonthValue();
            
            // Convert YY to full year (always use 2000s for credit cards)
            int fullYear = 2000 + year;
            
            // Validate year is reasonable (not more than 20 years in the future)
            int maxFutureYear = currentYear + 20;
            if (fullYear > maxFutureYear) {
                JOptionPane.showMessageDialog(this, 
                    "Expiry date is too far in the future. Please enter a valid expiry date (within 20 years).", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            java.time.YearMonth expiryYearMonth = java.time.YearMonth.of(fullYear, month);
            java.time.YearMonth currentYearMonth = java.time.YearMonth.of(currentYear, currentMonth);
            
            if (expiryYearMonth.isBefore(currentYearMonth)) {
                JOptionPane.showMessageDialog(this, 
                    "Credit card has expired. Please use a card with a valid expiry date.", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid expiry date format. Please enter in MM/YY format.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
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
    
    /**
     * Builds a decorator chain for the booking based on selected add-ons.
     * @param flight The flight being booked
     * @param passengers Number of passengers
     * @return BookingComponent with decorators applied
     */
    private BookingComponent buildBookingDecorator(Flight flight, int passengers) {
        // Start with base booking
        BookingComponent booking = new BaseBooking(flight, passengers);
        
        // Apply decorators based on checkbox selections
        if (seatUpgradeCheckbox.isSelected()) {
            booking = new SeatUpgradeDecorator(booking, passengers);
        }
        if (mealUpgradeCheckbox.isSelected()) {
            booking = new MealUpgradeDecorator(booking, passengers);
        }
        if (travelInsuranceCheckbox.isSelected()) {
            booking = new TravelInsuranceDecorator(booking, passengers);
        }
        
        return booking;
    }
    
    /**
     * Saves selected add-ons to the database.
     * @param reservationId The reservation ID
     * @param booking The booking component (may contain decorators)
     * @param passengers Number of passengers
     */
    private void saveBookingAddOns(int reservationId, BookingComponent booking, int passengers) {
        // Save each selected add-on to database
        if (seatUpgradeCheckbox.isSelected()) {
            BookingAddOn addOn = new BookingAddOn(reservationId, "SEAT_UPGRADE", 
                SeatUpgradeDecorator.getPricePerPassenger() * passengers, passengers);
            bookingAddOnDAO.save(addOn);
        }
        if (mealUpgradeCheckbox.isSelected()) {
            BookingAddOn addOn = new BookingAddOn(reservationId, "MEAL_UPGRADE", 
                MealUpgradeDecorator.getPricePerPassenger() * passengers, passengers);
            bookingAddOnDAO.save(addOn);
        }
        if (travelInsuranceCheckbox.isSelected()) {
            BookingAddOn addOn = new BookingAddOn(reservationId, "TRAVEL_INSURANCE", 
                TravelInsuranceDecorator.getPricePerPassenger() * passengers, passengers);
            bookingAddOnDAO.save(addOn);
        }
    }
    
    private void showConfirmationPopup(Reservation reservation, double totalAmount, int passengers, 
                                     double refundAmount, double additionalAmount) {
        showConfirmationPopup(reservation, totalAmount, passengers, refundAmount, additionalAmount, 0, "Credit Card");
    }
    
    private void showConfirmationPopup(Reservation reservation, double totalAmount, int passengers, 
                                     double refundAmount, double additionalAmount, double previousAmount) {
        showConfirmationPopup(reservation, totalAmount, passengers, refundAmount, additionalAmount, previousAmount, "Credit Card");
    }
    
    private void showConfirmationPopup(Reservation reservation, double totalAmount, int passengers, 
                                     double refundAmount, double additionalAmount, double previousAmount, String paymentMethod) {
        showConfirmationPopup(reservation, totalAmount, passengers, refundAmount, additionalAmount, previousAmount, paymentMethod, null);
    }
    
    private void showConfirmationPopup(Reservation reservation, double totalAmount, int passengers, 
                                     double refundAmount, double additionalAmount, double previousAmount, String paymentMethod, String bookingDescription) {
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
        
        // Show booking breakdown with add-ons if available
        if (bookingDescription != null && !bookingDescription.isEmpty()) {
            receipt.append("------------------------------------------\n");
            receipt.append("Booking Breakdown:\n");
            String[] lines = bookingDescription.split("\n");
            for (String line : lines) {
                receipt.append("  ").append(line).append("\n");
            }
        } else {
            receipt.append("Price per person: $").append(flight != null ? flight.getPrice() : 0).append("\n");
        }
        
        receipt.append("------------------------------------------\n");
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
        
        receipt.append("Payment Method: ").append(paymentMethod != null ? paymentMethod : "Credit Card").append("\n");
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