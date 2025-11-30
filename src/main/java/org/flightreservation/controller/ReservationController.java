package org.flightreservation.controller;

import org.flightreservation.data.ReservationDAO;
import org.flightreservation.entity.Reservation;

import java.util.List;

public class ReservationController {
    private ReservationDAO reservationDAO;

    public ReservationController() {
        this.reservationDAO = new ReservationDAO();
    }

    public boolean makeReservation(Reservation reservation) {
        return reservationDAO.save(reservation);
    }

    public boolean updateReservation(Reservation reservation) {
        return reservationDAO.update(reservation);
    }

    public Reservation getReservationById(int id) {
        return reservationDAO.findById(id);
    }

    public List<Reservation> getReservationsByCustomerId(int customerId) {
        return reservationDAO.findByCustomerId(customerId);
    }

    public List<Reservation> getAllReservations() {
        return reservationDAO.findAll();
    }

    public boolean cancelReservation(int id) {
        Reservation reservation = reservationDAO.findById(id);
        if (reservation != null) {
            reservation.setStatus("CANCELLED");
            return reservationDAO.update(reservation);
        }
        return false;
    }
}