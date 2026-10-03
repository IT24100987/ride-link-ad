package com.ridelink.driver.dto;

import com.ridelink.driver.entity.Driver;
import com.ridelink.driver.entity.DriverAvailability;
import com.ridelink.driver.entity.VehicleType;

public class AvailableDriverResponse {

    private Long driverId;
    private Long accountId;
    private String licenseNumber;
    private VehicleType vehicleType;
    private String vehicleNumber;
    private String serviceArea;
    private DriverAvailability availability;
    private Double currentLatitude;
    private Double currentLongitude;

    public AvailableDriverResponse() {
    }

    public AvailableDriverResponse(Driver driver) {
        this.driverId = driver.getId();
        this.accountId = driver.getAccountId();
        this.licenseNumber = driver.getLicenseNumber();
        this.serviceArea = driver.getServiceArea();
        this.availability = driver.getAvailability();
        this.currentLatitude = driver.getCurrentLatitude();
        this.currentLongitude = driver.getCurrentLongitude();
        if (driver.getVehicle() != null) {
            this.vehicleType = driver.getVehicle().getVehicleType();
            this.vehicleNumber = driver.getVehicle().getVehicleNumber();
        }
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public DriverAvailability getAvailability() {
        return availability;
    }

    public void setAvailability(DriverAvailability availability) {
        this.availability = availability;
    }

    public Double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(Double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public Double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(Double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }
}
