package org.flightreservation.strategy;

import org.flightreservation.entity.Payment;

public class CreditCardPaymentStrategy implements PaymentStrategy {
    
    @Override
    public boolean processPayment(Payment payment) {
        // Credit card payment is processed immediately after validation
        return validatePayment(payment);
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
        return "Credit Card";
    }
}
