package org.flightreservation.decorator;

/**
 * Decorator for meal upgrade add-on.
 * Adds $25 per passenger for premium meal selection.
 */
public class MealUpgradeDecorator extends BookingDecorator {
    private static final double PRICE_PER_PASSENGER = 25.00;
    
    public MealUpgradeDecorator(BookingComponent booking, int numberOfPassengers) {
        super(booking, numberOfPassengers);
    }
    
    @Override
    protected double getAddOnCost() {
        return PRICE_PER_PASSENGER * numberOfPassengers;
    }
    
    @Override
    protected String getAddOnDescription() {
        return String.format("Meal Upgrade (%d passengers @ $%.2f each): $%.2f", 
            numberOfPassengers, PRICE_PER_PASSENGER, getAddOnCost());
    }
    
    @Override
    public String getAddOnName() {
        return "MEAL_UPGRADE";
    }
    
    public static double getPricePerPassenger() {
        return PRICE_PER_PASSENGER;
    }
}
