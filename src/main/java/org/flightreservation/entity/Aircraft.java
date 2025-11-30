package org.flightreservation.entity;

public class Aircraft {
    private int id;
    private String model;
    private int capacity;
    private String airline;

    public Aircraft() {}

    public Aircraft(int id, String model, int capacity, String airline) {
        this.id = id;
        this.model = model;
        this.capacity = capacity;
        this.airline = airline;
    }

    public Aircraft(String model, int capacity, String airline) {
        this.model = model;
        this.capacity = capacity;
        this.airline = airline;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    @Override
    public String toString() {
        return "Aircraft{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", capacity=" + capacity +
                ", airline='" + airline + '\'' +
                '}';
    }
}