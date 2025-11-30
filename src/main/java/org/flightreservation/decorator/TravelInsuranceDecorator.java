package org.flightreservation.decorator;

/**
 * Decorator for travel insurance add-on.
 * Adds $30 per passenger for travel insurance coverage.
 */
public class TravelInsuranceDecorator extends BookingDecorator {
    private static final double PRICE_PER_PASSENGER = 30.00;
    
    public TravelInsuranceDecorator(BookingComponent booking, int numberOfPassengers) {
        super(booking, numberOfPassengers);
    }
    
    @Override
    protected double getAddOnCost() {
        return PRICE_PER_PASSENGER * numberOfPassengers;
    }
    
    @Override
    protected String getAddOnDescription() {
        return String.format("Travel Insurance (%d passengers @ $%.2f each): $%.2f", 
            numberOfPassengers, PRICE_PER_PASSENGER, getAddOnCost());
    }
    
    @Override
    public String getAddOnName() {
        return "TRAVEL_INSURANCE";
    }
    
    public static double getPricePerPassenger() {
        return PRICE_PER_PASSENGER;
    }
}
