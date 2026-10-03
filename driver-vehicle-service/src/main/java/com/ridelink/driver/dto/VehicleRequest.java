package com.ridelink.driver.dto;

import com.ridelink.driver.entity.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VehicleRequest {

    @NotNull(message = "Driver ID is required")
    private Long driverId;

    @NotBlank(message = "Vehicle number is required")
    private String vehicleNumber;

    @NotNull(message = "Vehicle type is required (CAR, VAN, BIKE, TUK)")
    private VehicleType vehicleType;

    @NotBlank(message = "Vehicle model is required")
    private String model;

    @NotBlank(message = "Vehicle color is required")
    private String color;

    public VehicleRequest() {
    }

    public VehicleRequest(Long driverId, String vehicleNumber, VehicleType vehicleType, String model, String color) {
        this.driverId = driverId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.model = model;
        this.color = color;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
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
