package org.flightreservation.controller;

import org.flightreservation.data.FlightDAO;
import org.flightreservation.entity.Flight;

import java.util.List;

public class FlightController {
    private FlightDAO flightDAO;

    public FlightController() {
        this.flightDAO = new FlightDAO();
    }

    public boolean addFlight(Flight flight) {
        return flightDAO.save(flight);
    }

    public boolean updateFlight(Flight flight) {
        return flightDAO.update(flight);
    }

    public Flight getFlightById(int id) {
        return flightDAO.findById(id);
    }

    public List<Flight> searchFlights(String origin, String destination, String date, String airline) {
        return flightDAO.searchFlights(origin, destination, date, airline);
    }

    public List<Flight> getAllFlights() {
        return flightDAO.findAll();
    }

    public boolean deleteFlight(int id) {
        return flightDAO.delete(id);
    }
}