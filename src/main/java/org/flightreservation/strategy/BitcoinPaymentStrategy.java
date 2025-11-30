package org.flightreservation.strategy;

import org.flightreservation.entity.Payment;

public class BitcoinPaymentStrategy implements PaymentStrategy {
    
    @Override
    public boolean processPayment(Payment payment) {
        if (!validatePayment(payment)) {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean validatePayment(Payment payment) {
        if (payment == null || payment.getAmount() <= 0) {
            return false;
        }
        
        // Basic Bitcoin wallet address validation
        // Bitcoin addresses are typically 26-35 characters, alphanumeric
        // and start with 1, 3, or bc1
        String walletAddress = payment.getPaymentMethod();
        if (walletAddress == null || walletAddress.trim().isEmpty()) {
            return false;
        }

        String trimmed = walletAddress.trim();
        if (trimmed.length() < 26 || trimmed.length() > 35) {
            return false;
        }
        
        // Check if it starts with common Bitcoin address prefixes
        if (!trimmed.startsWith("1") && !trimmed.startsWith("3") && !trimmed.startsWith("bc1")) {
            return false;
        }
        return true;
    }
    
    @Override
    public String getPaymentMethodName() {
        return "Bitcoin";
    }
}
