package org.flightreservation.strategy;

import org.flightreservation.entity.Payment;


public interface PaymentStrategy {
    /**
     * Processes a payment using the specific payment method.
     * 
     * @param payment The payment object containing payment details
     * @return true if payment was processed successfully, false otherwise
     */
    boolean processPayment(Payment payment);
    
    /**
     * Validates payment details specific to this payment method.
     * 
     * @param payment The payment object to validate
     * @return true if payment details are valid, false otherwise
     */
    boolean validatePayment(Payment payment);
    
    /**
     * Gets the display name of this payment method.
     * 
     * @return The name of the payment method
     */
    String getPaymentMethodName();
}
