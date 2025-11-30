package org.flightreservation.boundary;

import org.flightreservation.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuView extends JFrame {
    private User currentUser;
    private JButton searchFlightButton;
    private JButton bookFlightButton;
    private JButton manageReservationsButton;
    private JButton monthlyPromotionsButton;
    private JButton customerManagementButton;
    private JButton flightManagementButton;
    private JButton promotionManagementButton;
    private JButton logoutButton;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JLabel welcomeLabel;
    private JLabel roleLabel;

    public MainMenuView(User user) {
        this.currentUser = user;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupWindowProperties();
        
        // Set permissions based on role
        setPermissions();
    }

    private void initializeComponents() {
        // Create styled buttons with modern appearance
        searchFlightButton = createStyledButton("Search Flights", new Color(70, 130, 180));
        bookFlightButton = createStyledButton("Book Flight", new Color(70, 130, 180));
        manageReservationsButton = createStyledButton("Manage Reservations", new Color(70, 130, 180));
        monthlyPromotionsButton = createStyledButton("Monthly Promotions", new Color(70, 130, 180));
        customerManagementButton = createStyledButton("Customer Management", new Color(70, 130, 180));
        flightManagementButton = createStyledButton("Flight Management", new Color(70, 130, 180));
        promotionManagementButton = createStyledButton("Promotion Management", new Color(70, 130, 180));
        logoutButton = createStyledButton("Logout", new Color(220, 80, 80));
        
        // Create panels
        sidebarPanel = new JPanel();
        contentPanel = new JPanel();
        
        // Create header labels
        welcomeLabel = new JLabel("Welcome, " + currentUser.getUsername());
        welcomeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        
        roleLabel = new JLabel("Role: " + currentUser.getRole());
        roleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        roleLabel.setForeground(new Color(200, 200, 200));
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 40));
        button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
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
        
        // Create sidebar panel with gradient background
        sidebarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(40, 53, 147), 0, h, new Color(63, 81, 181));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        sidebarPanel.setLayout(new GridBagLayout());
        sidebarPanel.setPreferredSize(new Dimension(250, getHeight()));
        sidebarPanel.setBackground(new Color(40, 53, 147));
        
        // Create header panel for user info
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(0, 0, 0, 0)); // Transparent
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        headerPanel.setOpaque(false);
        
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        headerPanel.add(welcomeLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(roleLabel);
        
        // Setup sidebar layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0;
        sidebarPanel.add(headerPanel, gbc);
        
        // Add buttons to sidebar
        gbc.weighty = 0;
        gbc.gridy = 1;
        sidebarPanel.add(searchFlightButton, gbc);
        
        gbc.gridy = 2;
        sidebarPanel.add(bookFlightButton, gbc);
        
        gbc.gridy = 3;
        sidebarPanel.add(manageReservationsButton, gbc);
        
        gbc.gridy = 4;
        sidebarPanel.add(monthlyPromotionsButton, gbc);
        
        gbc.gridy = 5;
        sidebarPanel.add(customerManagementButton, gbc);
        
        gbc.gridy = 6;
        sidebarPanel.add(flightManagementButton, gbc);
        
        gbc.gridy = 7;
        sidebarPanel.add(promotionManagementButton, gbc);
        
        gbc.gridy = 8;
        gbc.weighty = 1; // Push logout button to bottom
        gbc.anchor = GridBagConstraints.SOUTH;
        sidebarPanel.add(logoutButton, gbc);
        
        // Create content panel
        contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(245, 245, 245), 0, h, new Color(220, 220, 220));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        
        // Add welcome message to content panel
        JLabel titleLabel = new JLabel("Flight Reservation System");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setForeground(new Color(40, 53, 147));
        
        JLabel subtitleLabel = new JLabel("Main Menu");
        subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(96, 125, 139));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(new Color(0, 0, 0, 0)); // Transparent
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(subtitleLabel);
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        contentPanel.add(textPanel, gbc);
        
        // Add panels to frame
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        searchFlightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSearchFlightView();
            }
        });

        bookFlightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSearchFlightView(); // Redirect to search flights page
            }
        });

        manageReservationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openReservationManagementView();
            }
        });

        monthlyPromotionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMonthlyPromotionsView();
            }
        });

        customerManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCustomerManagementView();
            }
        });

        flightManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAdminFlightManagementView();
            }
        });

        promotionManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPromotionManagementView();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
    }
    
    private void setupWindowProperties() {
        setTitle("Flight Reservation System - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);
    }

    private void setPermissions() {
        switch (currentUser.getRole()) {
            case "CUSTOMER":
                // Hide admin-only buttons for customers
                customerManagementButton.setVisible(false);
                flightManagementButton.setVisible(false);
                promotionManagementButton.setVisible(false);
                // Show monthly promotions for customers
                monthlyPromotionsButton.setVisible(true);
                break;
            case "AGENT":
                // Hide admin-only button for agents
                flightManagementButton.setVisible(false);
                // Show promotion management for agents
                promotionManagementButton.setVisible(true);
                monthlyPromotionsButton.setVisible(false);
                break;
            case "ADMIN":
                // All buttons visible for admin
                promotionManagementButton.setVisible(true);
                monthlyPromotionsButton.setVisible(false);
                break;
            default:
                // Hide admin-only buttons for unknown roles
                customerManagementButton.setVisible(false);
                flightManagementButton.setVisible(false);
                manageReservationsButton.setVisible(false);
                promotionManagementButton.setVisible(false);
                monthlyPromotionsButton.setVisible(false);
                break;
        }
        
        // Revalidate and repaint to adjust layout after hiding buttons
        sidebarPanel.revalidate();
        sidebarPanel.repaint();
    }

    private void openSearchFlightView() {
        dispose();
        new SearchFlightView(currentUser);
    }

    private void openReservationManagementView() {
        dispose();
        new ReservationManagementView(currentUser);
    }

    private void openCustomerManagementView() {
        dispose();
        new CustomerManagementView(currentUser);
    }

    private void openAdminFlightManagementView() {
        dispose();
        new AdminFlightManagementView(currentUser);
    }

    private void openMonthlyPromotionsView() {
        dispose();
        new MonthlyPromotionsView(currentUser);
    }

    private void openPromotionManagementView() {
        dispose();
        new PromotionManagementView(currentUser);
    }

    private void logout() {
        dispose();
        new LoginView();
    }
}