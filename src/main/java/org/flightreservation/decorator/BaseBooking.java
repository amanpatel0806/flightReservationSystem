package org.flightreservation.decorator;

import org.flightreservation.entity.Flight;

/**
 * Concrete component representing the base booking (flight price * passengers).
 */
public class BaseBooking implements BookingComponent {
    private Flight flight;
    private int numberOfPassengers;
    
    public BaseBooking(Flight flight, int numberOfPassengers) {
        this.flight = flight;
        this.numberOfPassengers = numberOfPassengers;
    }
    
    @Override
    public double getCost() {
        if (flight == null) {
            return 0.0;
        }
        return flight.getPrice() * numberOfPassengers;
    }
    
    @Override
    public String getDescription() {
        if (flight == null) {
            return "Base Booking: $0.00";
        }
        return String.format("Base Flight (%d passengers @ $%.2f each): $%.2f", 
            numberOfPassengers, flight.getPrice(), getCost());
    }
    
    @Override
    public double getBaseCost() {
        return getCost();
    }
    
    public Flight getFlight() {
        return flight;
    }
    
    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }
}
