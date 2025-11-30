package org.flightreservation.controller;

import org.flightreservation.data.PaymentDAO;
import org.flightreservation.entity.Payment;
import org.flightreservation.strategy.PaymentStrategy;
import org.flightreservation.strategy.CreditCardPaymentStrategy;

import java.util.List;

public class PaymentController {
    private PaymentDAO paymentDAO;
    private PaymentStrategy paymentStrategy;

    public PaymentController() {
        this.paymentDAO = new PaymentDAO();
        // Default to credit card strategy
        this.paymentStrategy = new CreditCardPaymentStrategy();
    }

    /**
     * Sets the payment strategy to use for processing payments.
     * 
     * @param strategy The payment strategy to use
     */
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    /**
     * Gets the current payment strategy.
     * 
     * @return The current payment strategy
     */
    public PaymentStrategy getPaymentStrategy() {
        return paymentStrategy;
    }

    public boolean processPayment(Payment payment) {
        // Use the selected payment strategy
        if (paymentStrategy == null) {
            paymentStrategy = new CreditCardPaymentStrategy();
        }
        
        // For Bitcoin, the wallet address is already in paymentMethod field
        // For other methods, set the payment method name from strategy
        if (!"Bitcoin".equals(paymentStrategy.getPaymentMethodName())) {
            payment.setPaymentMethod(paymentStrategy.getPaymentMethodName());
        }
        
        // Process payment using the strategy
        boolean success = paymentStrategy.processPayment(payment);
        
        if (success) {
            payment.setStatus("PAID");
            return paymentDAO.save(payment);
        }
        
        return false;
    }

    public boolean updatePayment(Payment payment) {
        return paymentDAO.update(payment);
    }

    public Payment getPaymentById(int id) {
        return paymentDAO.findById(id);
    }

    public Payment getPaymentByReservationId(int reservationId) {
        return paymentDAO.findByReservationId(reservationId);
    }

    public List<Payment> getAllPayments() {
        return paymentDAO.findAll();
    }

    public boolean cancelPayment(int id) {
        Payment payment = paymentDAO.findById(id);
        if (payment != null) {
            payment.setStatus("CANCELLED");
            return paymentDAO.update(payment);
        }
        return false;
    }
}