package org.flightreservation.controller;

import org.flightreservation.data.AircraftDAO;
import org.flightreservation.entity.Aircraft;

import java.util.List;

public class AircraftController {
    private AircraftDAO aircraftDAO;

    public AircraftController() {
        this.aircraftDAO = new AircraftDAO();
    }

    public boolean addAircraft(Aircraft aircraft) {
        return aircraftDAO.save(aircraft);
    }

    public boolean updateAircraft(Aircraft aircraft) {
        return aircraftDAO.update(aircraft);
    }

    public Aircraft getAircraftById(int id) {
        return aircraftDAO.findById(id);
    }

    public List<Aircraft> getAllAircrafts() {
        return aircraftDAO.findAll();
    }

    public boolean deleteAircraft(int id) {
        return aircraftDAO.delete(id);
    }
}