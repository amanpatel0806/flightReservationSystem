package org.flightreservation.boundary;

import org.flightreservation.controller.PromotionController;
import org.flightreservation.entity.Promotion;
import org.flightreservation.entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MonthlyPromotionsView extends JFrame {
    private User currentUser;
    private JTable promotionsTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    private PromotionController promotionController;

    public MonthlyPromotionsView(User user) {
        this.currentUser = user;
        this.promotionController = new PromotionController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupWindowProperties();
        populateTable();
    }

    private void initializeComponents() {
        // Create table model
        String[] columnNames = {"Code", "Description", "Discount %", "Valid Until"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        promotionsTable = new JTable(tableModel);
        promotionsTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        promotionsTable.setRowHeight(25);
        promotionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        promotionsTable.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        promotionsTable.getTableHeader().setBackground(new Color(70, 130, 180));
        promotionsTable.getTableHeader().setForeground(Color.WHITE);
        
        backButton = createStyledButton("Back to Main Menu", new Color(105, 105, 105));
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(180, 35));
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
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
        
        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setPreferredSize(new Dimension(700, 60));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        
        JLabel headerLabel = new JLabel("Monthly Promotions");
        headerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Table scroll pane
        JScrollPane scrollPane = new JScrollPane(promotionsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Available Coupons"));
        scrollPane.setPreferredSize(new Dimension(650, 400));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(backButton);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MainMenuView(currentUser);
            }
        });
        
        // Add window focus listener to refresh promotions when view is opened
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowActivated(java.awt.event.WindowEvent e) {
                populateTable();
            }
        });
    }

    private void setupWindowProperties() {
        setTitle("Flight Reservation System - Monthly Promotions");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // Load active promotions from database
        List<Promotion> promotions = promotionController.getActivePromotions();
        
        for (Promotion promotion : promotions) {
            Object[] row = {
                promotion.getCode(),
                promotion.getDescription(),
                promotion.getDiscountPercentage() + "%",
                promotion.getEndDate().format(formatter)
            };
            tableModel.addRow(row);
        }
        
        // Refresh the table
        tableModel.fireTableDataChanged();
    }
}
