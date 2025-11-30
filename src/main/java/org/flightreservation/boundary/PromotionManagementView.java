package org.flightreservation.boundary;

import org.flightreservation.controller.PromotionController;
import org.flightreservation.entity.Promotion;
import org.flightreservation.entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PromotionManagementView extends JFrame {
    private User currentUser;
    private JTable promotionsTable;
    private DefaultTableModel tableModel;
    private JTextField codeField;
    private JTextArea descriptionField;
    private JTextField discountField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JCheckBox activeCheckBox;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton backButton;
    private PromotionController promotionController;
    private int editingPromotionId = -1;

    public PromotionManagementView(User user) {
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
        String[] columnNames = {"ID", "Code", "Description", "Discount %", "Start Date", "End Date", "Active"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        promotionsTable = new JTable(tableModel);
        promotionsTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        promotionsTable.setRowHeight(25);
        promotionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        promotionsTable.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        promotionsTable.getTableHeader().setBackground(new Color(70, 130, 180));
        promotionsTable.getTableHeader().setForeground(Color.WHITE);
        
        // Form fields
        codeField = new JTextField(20);
        codeField.setPreferredSize(new Dimension(250, 30));
        descriptionField = new JTextArea(3, 20);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        discountField = new JTextField(10);
        discountField.setPreferredSize(new Dimension(100, 30));
        startDateField = new JTextField(20);
        startDateField.setPreferredSize(new Dimension(200, 30));
        endDateField = new JTextField(20);
        endDateField.setPreferredSize(new Dimension(200, 30));
        activeCheckBox = new JCheckBox("Active");
        activeCheckBox.setSelected(true);
        
        // Buttons
        saveButton = createStyledButton("Save Promotion", new Color(60, 179, 113));
        deleteButton = createStyledButton("Delete", new Color(220, 80, 80));
        backButton = createStyledButton("Back to Main Menu", new Color(105, 105, 105));
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
        headerPanel.setPreferredSize(new Dimension(900, 60));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        
        JLabel headerLabel = new JLabel("Promotion Management");
        headerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left panel - Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(promotionsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Existing Promotions"));
        scrollPane.setPreferredSize(new Dimension(600, 400));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Right panel - Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Promotion Details"));
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Code
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Promotion Code:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(codeField, gbc);
        
        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(new JScrollPane(descriptionField), gbc);
        
        // Discount
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        formPanel.add(new JLabel("Discount %:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(discountField, gbc);
        
        // Start Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(startDateField, gbc);
        
        // End Date
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(endDateField, gbc);
        
        // Active checkbox
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(activeCheckBox, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        formPanel.add(buttonPanel, gbc);
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePanel, formPanel);
        splitPane.setDividerLocation(600);
        splitPane.setResizeWeight(0.6);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        // Bottom button panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(backButton);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePromotion();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePromotion();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MainMenuView(currentUser);
            }
        });
        
        promotionsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedPromotion();
            }
        });
    }

    private void setupWindowProperties() {
        setTitle("Flight Reservation System - Promotion Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // Load all promotions from database
        List<Promotion> promotions = promotionController.getAllPromotions();
        
        for (Promotion promotion : promotions) {
            Object[] row = {
                promotion.getId(),
                promotion.getCode(),
                promotion.getDescription(),
                promotion.getDiscountPercentage() + "%",
                promotion.getStartDate().format(formatter),
                promotion.getEndDate().format(formatter),
                promotion.isActive() ? "Yes" : "No"
            };
            tableModel.addRow(row);
        }
    }

    private void savePromotion() {
        // Validation
        if (codeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Promotion code is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (descriptionField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Description is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double discount;
        try {
            discount = Double.parseDouble(discountField.getText().trim());
            if (discount <= 0 || discount > 100) {
                JOptionPane.showMessageDialog(this, "Discount must be between 0 and 100.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid discount percentage.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        LocalDateTime startDate;
        LocalDateTime endDate;
        try {
            startDate = LocalDateTime.parse(startDateField.getText().trim() + "T00:00:00");
            endDate = LocalDateTime.parse(endDateField.getText().trim() + "T23:59:59");
            
            if (endDate.isBefore(startDate)) {
                JOptionPane.showMessageDialog(this, "End date must be after start date.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Please enter dates in YYYY-MM-DD format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Save promotion
        if (editingPromotionId > 0) {
            // Update existing
            Promotion existingPromotion = promotionController.getPromotionById(editingPromotionId);
            if (existingPromotion != null) {
                existingPromotion.setCode(codeField.getText().trim());
                existingPromotion.setDescription(descriptionField.getText().trim());
                existingPromotion.setDiscountPercentage(discount);
                existingPromotion.setStartDate(startDate);
                existingPromotion.setEndDate(endDate);
                existingPromotion.setActive(activeCheckBox.isSelected());
                
                if (promotionController.updatePromotion(existingPromotion)) {
                    JOptionPane.showMessageDialog(this, "Promotion updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update promotion.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } else {
            // Create new
            Promotion newPromotion = new Promotion(codeField.getText().trim(), 
                descriptionField.getText().trim(), discount, startDate, endDate, activeCheckBox.isSelected());
            
            if (promotionController.createPromotion(newPromotion)) {
                JOptionPane.showMessageDialog(this, "Promotion created successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create promotion. Code may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        clearForm();
        populateTable();
    }

    private void deletePromotion() {
        int selectedRow = promotionsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a promotion to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this promotion?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            int promotionId = (Integer) tableModel.getValueAt(selectedRow, 0);
            if (promotionController.deletePromotion(promotionId)) {
                clearForm();
                populateTable();
                JOptionPane.showMessageDialog(this, "Promotion deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete promotion.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadSelectedPromotion() {
        int selectedRow = promotionsTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        int promotionId = (Integer) tableModel.getValueAt(selectedRow, 0);
        Promotion promotion = promotionController.getPromotionById(promotionId);
        
        if (promotion != null) {
            editingPromotionId = promotion.getId();
            codeField.setText(promotion.getCode());
            descriptionField.setText(promotion.getDescription());
            discountField.setText(String.valueOf(promotion.getDiscountPercentage()));
            startDateField.setText(promotion.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            endDateField.setText(promotion.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            activeCheckBox.setSelected(promotion.isActive());
        }
    }

    private void clearForm() {
        editingPromotionId = -1;
        codeField.setText("");
        descriptionField.setText("");
        discountField.setText("");
        startDateField.setText("");
        endDateField.setText("");
        activeCheckBox.setSelected(true);
        promotionsTable.clearSelection();
    }
}
