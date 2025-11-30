package org.flightreservation.entity;

import java.time.LocalDateTime;

public class Payment {
    private int id;
    private int reservationId;
    private double amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String status; // PAID, PENDING, FAILED

    public Payment() {}

    public Payment(int id, int reservationId, double amount, LocalDateTime paymentDate, 
                   String paymentMethod, String status) {
        this.id = id;
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public Payment(int reservationId, double amount, LocalDateTime paymentDate, 
                   String paymentMethod, String status) {
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
public String toString() {
        return "Payment{" +
                "id=" + id +
                ", reservationId=" + reservationId +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}