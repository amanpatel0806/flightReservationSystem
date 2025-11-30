package org.flightreservation.entity;

public class BookingAddOn {
    private int id;
    private int reservationId;
    private String addOnType; // SEAT_UPGRADE, MEAL_UPGRADE, TRAVEL_INSURANCE
    private double price;
    private int quantity; // Number of passengers this add-on applies to
    
    public BookingAddOn() {}
    
    public BookingAddOn(int id, int reservationId, String addOnType, double price, int quantity) {
        this.id = id;
        this.reservationId = reservationId;
        this.addOnType = addOnType;
        this.price = price;
        this.quantity = quantity;
    }
    
    public BookingAddOn(int reservationId, String addOnType, double price, int quantity) {
        this.reservationId = reservationId;
        this.addOnType = addOnType;
        this.price = price;
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getReservationId() {
        return reservationId;
    }
    
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    
    public String getAddOnType() {
        return addOnType;
    }
    
    public void setAddOnType(String addOnType) {
        this.addOnType = addOnType;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    @Override
    public String toString() {
        return "BookingAddOn{" +
                "id=" + id +
                ", reservationId=" + reservationId +
                ", addOnType='" + addOnType + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
