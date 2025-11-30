package org.flightreservation.controller;

import org.flightreservation.data.PaymentDAO;
import org.flightreservation.entity.Payment;

import java.util.List;

public class PaymentController {
    private PaymentDAO paymentDAO;

    public PaymentController() {
        this.paymentDAO = new PaymentDAO();
    }

    public boolean processPayment(Payment payment) {
        // Basic validation
        if (payment.getAmount() <= 0) {
            return false;
        }
        payment.setStatus("PAID");
        return paymentDAO.save(payment);
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