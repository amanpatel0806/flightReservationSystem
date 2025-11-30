package org.flightreservation.strategy;

import org.flightreservation.entity.Payment;

import javax.swing.*;


public class PayPalPaymentStrategy implements PaymentStrategy {
    
    @Override
    public boolean processPayment(Payment payment) {
        // Simulate PayPal login and payment
        if (!validatePayment(payment)) {
            return false;
        }
        
        // Show PayPal login dialog (simulated)
        int option = JOptionPane.showConfirmDialog(
            null,
            "Redirecting to PayPal...\n\nClick OK to simulate successful PayPal payment.",
            "PayPal Payment",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );
        
        if (option == JOptionPane.OK_OPTION) {
            // Simulate successful PayPal payment
            JOptionPane.showMessageDialog(
                null,
                "PayPal payment successful!\nAmount: $" + String.format("%.2f", payment.getAmount()),
                "Payment Confirmed",
                JOptionPane.INFORMATION_MESSAGE
            );
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean validatePayment(Payment payment) {
        if (payment == null || payment.getAmount() <= 0) {
            return false;
        }
        return true;
    }
    
    @Override
    public String getPaymentMethodName() {
        return "PayPal";
    }
}
