package org.flightreservation.boundary;

import org.flightreservation.controller.CustomerController;
import org.flightreservation.controller.AuthenticationController;
import org.flightreservation.entity.Customer;
import org.flightreservation.entity.User;
import org.flightreservation.data.UserDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CustomerManagementView extends JFrame {
    private User currentUser;
    private CustomerController customerController;
    private AuthenticationController authController;
    private UserDAO userDAO;
    
    private JTable customersTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton backButton;
    
    // Form fields
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private JButton saveButton;
    private JButton cancelButton;
    private int editingCustomerId = -1;
    private JPanel formPanel;
    private JPanel buttonPanel;
    private JPanel topButtonPanel;

    public CustomerManagementView(User user) {
        this.currentUser = user;
        this.customerController = new CustomerController();
        this.authController = new AuthenticationController();
        this.userDAO = new UserDAO();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupTable();
        setupWindowProperties();
    }

    private void initializeComponents() {
        // Table setup
        String[] columnNames = {"ID", "First Name", "Last Name", "Email", "Phone", "Address"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        customersTable = new JTable(tableModel);
        customersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customersTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        customersTable.setRowHeight(25);
        
        // Style the table header
        JTableHeader header = customersTable.getTableHeader();
        header.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        
        // Style the table
        customersTable.setSelectionBackground(new Color(173, 216, 230));
        customersTable.setSelectionForeground(Color.BLACK);
        
        // Create styled buttons
        addButton = createStyledButton("Add New Customer", new Color(60, 179, 113));
        editButton = createStyledButton("Edit Selected", new Color(255, 140, 0));
        deleteButton = createStyledButton("Delete Selected", new Color(220, 20, 60));
        refreshButton = createStyledButton("Refresh", new Color(70, 130, 180));
        backButton = createStyledButton("Back to Main Menu", new Color(105, 105, 105));
        
        // Form fields
        firstNameField = createStyledTextField(20);
        lastNameField = createStyledTextField(20);
        emailField = createStyledTextField(20);
        phoneField = createStyledTextField(20);
        addressField = createStyledTextField(20);
        saveButton = createStyledButton("Save", new Color(60, 179, 113));
        cancelButton = createStyledButton("Cancel", new Color(105, 105, 105));
        
        // Panels
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        topButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topButtonPanel.setBackground(Color.WHITE);
    }
    
    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setPreferredSize(new Dimension(200, 30));
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
        
        // // Header panel
        // JPanel headerPanel = new JPanel();
        // headerPanel.setBackground(new Color(70, 130, 180));
        // headerPanel.setPreferredSize(new Dimension(800, 60));
        // headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        
        // JLabel headerLabel = new JLabel("Customer Management");
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
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("First Name:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(firstNameField, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Last Name:"), gbc);
        
        gbc.gridx = 3;
        formPanel.add(lastNameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Phone:"), gbc);
        
        gbc.gridx = 3;
        formPanel.add(phoneField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Address:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        formPanel.add(addressField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JPanel saveCancelButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        saveCancelButtonPanel.setBackground(Color.WHITE);
        saveCancelButtonPanel.add(saveButton);
        saveCancelButtonPanel.add(cancelButton);
        formPanel.add(saveCancelButtonPanel, gbc);
        
        // // Button panel
        // buttonPanel.add(editButton);
        // buttonPanel.add(deleteButton);
        
        // Table scroll pane
        JScrollPane tableScrollPane = new JScrollPane(customersTable);
        tableScrollPane.setPreferredSize(new Dimension(800, 300));
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Main panel layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // add(headerPanel, BorderLayout.NORTH);
        add(topButtonPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });
        
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editCustomer();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCustomer();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshCustomers();
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
                saveCustomer();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelEditing();
            }
        });
        
        customersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = customersTable.getSelectedRow();
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
        setTitle("Flight Reservation System - Customer Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void setupTable() {
        refreshCustomers();
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
    
    private void refreshCustomers() {
        tableModel.setRowCount(0);
        List<Customer> customers = customerController.getAllCustomers();
        for (Customer customer : customers) {
            Object[] row = {
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress()
            };
            tableModel.addRow(row);
        }
    }
    
    private void addCustomer() {
        clearForm();
        editingCustomerId = -1;
        firstNameField.requestFocus();
        JOptionPane.showMessageDialog(this, "Enter customer details and click Save.");
    }
    
    private void editCustomer() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow >= 0) {
            int customerId = (int) tableModel.getValueAt(selectedRow, 0);
            Customer customer = customerController.getCustomerById(customerId);
            if (customer != null) {
                editingCustomerId = customerId;
                firstNameField.setText(customer.getFirstName());
                lastNameField.setText(customer.getLastName());
                emailField.setText(customer.getEmail());
                phoneField.setText(customer.getPhone());
                addressField.setText(customer.getAddress());
                JOptionPane.showMessageDialog(this, "Modify customer details and click Save.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteCustomer() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow >= 0) {
            int customerId = (int) tableModel.getValueAt(selectedRow, 0);
            
            // Check if customer has any reservations
            // In a real implementation, we would check for related reservations
            int option = JOptionPane.showConfirmDialog(this, 
                "Deleting this customer will also delete all related reservations. Are you sure you want to proceed?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                if (customerController.deleteCustomer(customerId)) {
                    // Also delete the associated user account
                    // In a real implementation, we would find and delete the user associated with this customer
                    JOptionPane.showMessageDialog(this, "Customer and associated user account deleted successfully.");
                    refreshCustomers();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete customer.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void saveCustomer() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name, last name, and email are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate email format
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.\nExample: user@example.com", "Validation Error", JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return;
        }
        
        // Validate phone number format (if provided)
        if (!phone.isEmpty() && !isValidPhoneNumber(phone)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid phone number.\nFormat: (123) 456-7890 or 123-456-7890 or 1234567890", "Validation Error", JOptionPane.ERROR_MESSAGE);
            phoneField.requestFocus();
            return;
        }
        
        if (editingCustomerId == -1) {
            // Adding new customer
            // Automatically generate username from firstname.lastname
            String username = firstName.toLowerCase() + "." + lastName.toLowerCase();
            
            // Check if user already exists and create with unique username if needed
            int counter = 1;
            String originalUsername = username;
            while (userDAO.findByUsername(username) != null) {
                username = originalUsername + counter;
                counter++;
            }
            
            // Create customer with username link
            Customer customer = new Customer(firstName, lastName, email, phone, address, username);
            if (customerController.addCustomer(customer)) {
                // Automatically create a user account for the customer
                User user = new User(username, "123", "CUSTOMER"); // Default password is "123"
                
                if (authController.registerUser(user)) {
                    JOptionPane.showMessageDialog(this, "Customer added successfully. User account created with username: " + username + " and default password: 123");
                } else {
                    JOptionPane.showMessageDialog(this, "Customer added but failed to create user account.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
                
                refreshCustomers();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add customer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Updating existing customer - preserve username
            Customer existingCustomer = customerController.getCustomerById(editingCustomerId);
            if (existingCustomer == null) {
                JOptionPane.showMessageDialog(this, "Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Preserve the username when updating
            Customer customer = new Customer(editingCustomerId, firstName, lastName, email, phone, address, existingCustomer.getUsername());
            if (customerController.updateCustomer(customer)) {
                JOptionPane.showMessageDialog(this, "Customer updated successfully.");
                refreshCustomers();
                clearForm();
                editingCustomerId = -1;
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update customer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cancelEditing() {
        clearForm();
        editingCustomerId = -1;
    }
    
    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        
        // Basic email validation pattern
        // Format: local@domain
        // Local part: alphanumeric, dots, hyphens, underscores
        // Domain: alphanumeric, dots, hyphens
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }
    
    private boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) {
            return true; // Phone is optional, so empty is valid
        }
        
        // Remove common phone number formatting characters
        String cleanedPhone = phone.replaceAll("[\\s()\\-\\.]", "");
        
        // Check if it's all digits
        if (!cleanedPhone.matches("\\d+")) {
            return false;
        }
        
        // Check length (should be 10 digits for US format, or 7-15 for international)
        int length = cleanedPhone.length();
        return length >= 7 && length <= 15;
    }
    
    private void clearForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }
    
    private void backToMainMenu() {
        dispose();
        new MainMenuView(currentUser);
    }
}