package org.flightreservation.boundary;

import org.flightreservation.controller.AuthenticationController;
import org.flightreservation.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;
    private ButtonGroup roleButtonGroup;
    private JRadioButton customerRadio;
    private JRadioButton agentRadio;
    private JRadioButton adminRadio;
    private AuthenticationController authController;

    public LoginView() {
        authController = new AuthenticationController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupWindowProperties();
    }

    private void initializeComponents() {
        usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(200, 35));
        usernameField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180)), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        ((JTextField) usernameField).setOpaque(true);
        
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 35));
        passwordField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180)), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        ((JPasswordField) passwordField).setOpaque(true);
        
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 35));
        loginButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(100, 149, 237));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(70, 130, 180));
            }
        });
        
        // Sign Up button
        signUpButton = new JButton("Sign Up");
        signUpButton.setPreferredSize(new Dimension(100, 35));
        signUpButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        signUpButton.setBackground(new Color(60, 179, 113));
        signUpButton.setForeground(new Color(70, 130, 180));
        signUpButton.setFocusPainted(false);
        signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        signUpButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                signUpButton.setBackground(new Color(80, 200, 120));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                signUpButton.setBackground(new Color(60, 179, 113));
            }
        });
        
        // Role radio buttons
        roleButtonGroup = new ButtonGroup();
        customerRadio = new JRadioButton("Customer", true);
        agentRadio = new JRadioButton("Agent");
        adminRadio = new JRadioButton("Admin");
        
        customerRadio.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        agentRadio.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        adminRadio.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        
        roleButtonGroup.add(customerRadio);
        roleButtonGroup.add(agentRadio);
        roleButtonGroup.add(adminRadio);
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
        
        // Login panel with border
        JPanel loginPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw subtle shadow
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(5, 5, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                
                // Draw panel background
                g.setColor(getBackground());
                g.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 10, 10);
            }
        };
        loginPanel.setBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setPreferredSize(new Dimension(450, 350));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        
        // Title label
        JLabel titleLabel = new JLabel("Welcome");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 20, 5); // More space below title
        loginPanel.add(titleLabel, gbc);
        
        // Username label and field
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        loginPanel.add(usernameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(usernameField, gbc);
        
        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        loginPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(passwordField, gbc);
        
        // Role selection
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel roleLabel = new JLabel("Login As:");
        roleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        loginPanel.add(roleLabel, gbc);
        
        // Radio buttons panel
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        rolePanel.setBackground(Color.WHITE);
        rolePanel.add(customerRadio);
        rolePanel.add(agentRadio);
        rolePanel.add(adminRadio);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(rolePanel, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(buttonPanel, gbc);
        
        mainPanel.add(loginPanel);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignUpView();
            }
        });

        // Allow Enter key to trigger login
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });
        
        // Allow Enter key to trigger login from username field
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });
    }
    
    private void openSignUpView() {
        dispose();
        new SignUpView();
    }
    
    private void setupWindowProperties() {
        setTitle("Flight Reservation System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 450);
        setResizable(false);
        // Center the window
        setLocationRelativeTo(null);
        setVisible(true);
        getRootPane().setDefaultButton(loginButton); // Make login button default
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Get selected role
        String selectedRole = "CUSTOMER";
        if (agentRadio.isSelected()) {
            selectedRole = "AGENT";
        } else if (adminRadio.isSelected()) {
            selectedRole = "ADMIN";
        }

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = authController.authenticate(username, password);
        if (user != null) {
            // Check if user's role matches selected role
            if (!user.getRole().equals(selectedRole)) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid role. Please select the correct role for this account.", 
                    "Role Mismatch", 
                    JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                usernameField.requestFocus();
                return;
            }
            
            // Successful login
            dispose(); // Close login window
            // Open main menu
            new MainMenuView(user);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            // Clear password field on failed login
            passwordField.setText("");
            usernameField.requestFocus();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new LoginView();
            }
        });
    }
}