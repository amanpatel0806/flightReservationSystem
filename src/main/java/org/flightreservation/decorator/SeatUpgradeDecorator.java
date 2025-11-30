package org.flightreservation.decorator;

/**
 * Decorator for seat upgrade add-on.
 * Adds $50 per passenger for premium seat selection.
 */
public class SeatUpgradeDecorator extends BookingDecorator {
    private static final double PRICE_PER_PASSENGER = 50.00;
    
    public SeatUpgradeDecorator(BookingComponent booking, int numberOfPassengers) {
        super(booking, numberOfPassengers);
    }
    
    @Override
    protected double getAddOnCost() {
        return PRICE_PER_PASSENGER * numberOfPassengers;
    }
    
    @Override
    protected String getAddOnDescription() {
        return String.format("Seat Upgrade (%d passengers @ $%.2f each): $%.2f", 
            numberOfPassengers, PRICE_PER_PASSENGER, getAddOnCost());
    }
    
    @Override
    public String getAddOnName() {
        return "SEAT_UPGRADE";
    }
    
    public static double getPricePerPassenger() {
        return PRICE_PER_PASSENGER;
    }
}
