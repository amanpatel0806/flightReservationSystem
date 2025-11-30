package org.flightreservation.boundary;

import org.flightreservation.controller.AuthenticationController;
import org.flightreservation.controller.CustomerController;
import org.flightreservation.entity.Customer;
import org.flightreservation.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SignUpView extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton signUpButton;
    private JButton backButton;
    private AuthenticationController authController;
    private CustomerController customerController;

    public SignUpView() {
        authController = new AuthenticationController();
        customerController = new CustomerController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupWindowProperties();
    }

    private void initializeComponents() {
        firstNameField = createStyledTextField(20);
        lastNameField = createStyledTextField(20);
        emailField = createStyledTextField(20);
        phoneField = createStyledTextField(20);
        addressField = createStyledTextField(20);
        usernameField = createStyledTextField(20);
        passwordField = createStyledPasswordField(20);
        confirmPasswordField = createStyledPasswordField(20);
        
        signUpButton = createStyledButton("Sign Up", new Color(60, 179, 113));
        backButton = createStyledButton("Back to Login", new Color(105, 105, 105));
    }
    
    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setPreferredSize(new Dimension(200, 35));
        textField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180)), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return textField;
    }
    
    private JPasswordField createStyledPasswordField(int columns) {
        JPasswordField passwordField = new JPasswordField(columns);
        passwordField.setPreferredSize(new Dimension(200, 35));
        passwordField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180)), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return passwordField;
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 35));
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(240, 248, 255);
                Color color2 = new Color(230, 230, 250);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        // Sign up panel with border
        JPanel signUpPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(5, 5, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                
                g.setColor(getBackground());
                g.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 10, 10);
            }
        };
        signUpPanel.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        signUpPanel.setBackground(Color.WHITE);
        signUpPanel.setPreferredSize(new Dimension(500, 600));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        
        // Title label
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 20, 10);
        signUpPanel.add(titleLabel, gbc);
        
        // Personal Information Section
        gbc.gridwidth = 2;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 5, 10);
        JLabel personalInfoLabel = new JLabel("Personal Information");
        personalInfoLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        personalInfoLabel.setForeground(new Color(70, 130, 180));
        signUpPanel.add(personalInfoLabel, gbc);
        
        // First Name
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.anchor = GridBagConstraints.EAST;
        signUpPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        signUpPanel.add(firstNameField, gbc);
        
        // Last Name
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        signUpPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        signUpPanel.add(lastNameField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        signUpPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        signUpPanel.add(emailField, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        signUpPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        signUpPanel.add(phoneField, gbc);
        
        // Address
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        signUpPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        signUpPanel.add(addressField, gbc);
        
        // Account Information Section
        gbc.gridwidth = 2;
        gbc.gridy = 7;
        gbc.insets = new Insets(15, 10, 5, 10);
        JLabel accountInfoLabel = new JLabel("Account Information");
        accountInfoLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        accountInfoLabel.setForeground(new Color(70, 130, 180));
        signUpPanel.add(accountInfoLabel, gbc);
        
        // Username
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.anchor = GridBagConstraints.EAST;
        signUpPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        signUpPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.EAST;
        signUpPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        signUpPanel.add(passwordField, gbc);
        
        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.EAST;
        signUpPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        signUpPanel.add(confirmPasswordField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(signUpButton);
        buttonPanel.add(backButton);
        
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 10, 10, 10);
        signUpPanel.add(buttonPanel, gbc);
        
        mainPanel.add(signUpPanel);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginView();
            }
        });
        
        // Allow Enter key to trigger sign up
        confirmPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    signUp();
                }
            }
        });
    }
    
    private void setupWindowProperties() {
        setTitle("Flight Reservation System - Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 650);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        getRootPane().setDefaultButton(signUpButton);
    }

    private void signUp() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Validation
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || 
            username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all required fields (First Name, Last Name, Email, Username, Password).", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate email format
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid email address.\nExample: user@example.com", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return;
        }
        
        // Validate phone number format (if provided)
        if (!phone.isEmpty() && !isValidPhoneNumber(phone)) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid phone number.\nFormat: (123) 456-7890 or 123-456-7890 or 1234567890", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            phoneField.requestFocus();
            return;
        }
        
        // Check password match
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Passwords do not match. Please try again.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            confirmPasswordField.setText("");
            confirmPasswordField.requestFocus();
            return;
        }
        
        // Check password length
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "Password must be at least 6 characters long.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            confirmPasswordField.setText("");
            passwordField.requestFocus();
            return;
        }
        
        // Check if username already exists
        User existingUser = authController.getUserByUsername(username);
        if (existingUser != null) {
            JOptionPane.showMessageDialog(this, 
                "Username already exists. Please choose a different username.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            usernameField.requestFocus();
            return;
        }
        
        // Check if email already exists
        Customer existingCustomer = customerController.getCustomerByEmail(email);
        if (existingCustomer != null) {
            JOptionPane.showMessageDialog(this, 
                "Email already registered. Please use a different email or login.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return;
        }
        
        // Create customer with username link
        Customer customer = new Customer(firstName, lastName, email, phone, address, username);
        if (!customerController.addCustomer(customer)) {
            JOptionPane.showMessageDialog(this, 
                "Failed to create customer account. Please try again.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create user account (only as CUSTOMER)
        User user = new User(username, password, "CUSTOMER");
        if (authController.registerUser(user)) {
            JOptionPane.showMessageDialog(this, 
                "Account created successfully! You can now login.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginView();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Customer created but failed to create user account. Please contact support.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }
    
    private boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) {
            return true; // Phone is optional
        }
        String cleanedPhone = phone.replaceAll("[\\s()\\-\\.]", "");
        if (!cleanedPhone.matches("\\d+")) {
            return false;
        }
        int length = cleanedPhone.length();
        return length >= 7 && length <= 15;
    }
}
