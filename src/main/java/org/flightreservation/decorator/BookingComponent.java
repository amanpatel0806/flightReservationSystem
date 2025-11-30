package org.flightreservation.decorator;

public interface BookingComponent {
    /**
     * Calculates the total cost of the booking including all decorators.
     * @return the total cost
     */
    double getCost();
    
    /**
     * Returns a description of the booking including all add-ons.
     * @return description string
     */
    String getDescription();
    
    /**
     * Returns the base cost (flight price * passengers) without add-ons.
     * @return base cost
     */
    double getBaseCost();
}
