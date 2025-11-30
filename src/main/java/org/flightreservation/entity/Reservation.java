package org.flightreservation.entity;

import java.time.LocalDateTime;

public class Reservation {
    private int id;
    private int customerId;
    private int flightId;
    private LocalDateTime reservationDate;
    private int numberOfPassengers;
    private String status; // ACTIVE, CANCELLED, CONFIRMED

    public Reservation() {}

    public Reservation(int id, int customerId, int flightId, LocalDateTime reservationDate, 
                       int numberOfPassengers, String status) {
        this.id = id;
        this.customerId = customerId;
        this.flightId = flightId;
        this.reservationDate = reservationDate;
        this.numberOfPassengers = numberOfPassengers;
        this.status = status;
    }

    public Reservation(int customerId, int flightId, LocalDateTime reservationDate, 
                       int numberOfPassengers, String status) {
        this.customerId = customerId;
        this.flightId = flightId;
        this.reservationDate = reservationDate;
        this.numberOfPassengers = numberOfPassengers;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", flightId=" + flightId +
                ", reservationDate=" + reservationDate +
                ", numberOfPassengers=" + numberOfPassengers +
                ", status='" + status + '\'' +
                '}';
    }
}