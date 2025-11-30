package org.flightreservation.decorator;


public abstract class BookingDecorator implements BookingComponent {
    protected BookingComponent booking;
    protected int numberOfPassengers;
    
    public BookingDecorator(BookingComponent booking, int numberOfPassengers) {
        this.booking = booking;
        this.numberOfPassengers = numberOfPassengers;
    }
    
    @Override
    public double getCost() {
        return booking.getCost() + getAddOnCost();
    }
    
    @Override
    public String getDescription() {
        return booking.getDescription() + "\n" + getAddOnDescription();
    }
    
    @Override
    public double getBaseCost() {
        return booking.getBaseCost();
    }
    
    /**
     * Returns the cost of this specific add-on.
     * @return add-on cost
     */
    protected abstract double getAddOnCost();
    
    /**
     * Returns the description of this specific add-on.
     * @return add-on description
     */
    protected abstract String getAddOnDescription();
    
    /**
     * Returns the name/type of this add-on.
     * @return add-on name
     */
    public abstract String getAddOnName();
}
