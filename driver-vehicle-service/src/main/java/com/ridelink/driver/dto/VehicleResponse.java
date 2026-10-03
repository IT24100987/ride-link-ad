package com.ridelink.driver.dto;

import com.ridelink.driver.entity.Vehicle;
import com.ridelink.driver.entity.VehicleType;

public class VehicleResponse {

    private Long id;
    private String vehicleNumber;
    private VehicleType vehicleType;
    private String model;
    private String color;

    public VehicleResponse() {
    }

    public VehicleResponse(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.vehicleNumber = vehicle.getVehicleNumber();
        this.vehicleType = vehicle.getVehicleType();
        this.model = vehicle.getModel();
        this.color = vehicle.getColor();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
