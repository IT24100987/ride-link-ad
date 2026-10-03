package com.ridelink.driver.dto;

import com.ridelink.driver.entity.Driver;
import com.ridelink.driver.entity.DriverAvailability;

public class DriverResponse {

    private Long id;
    private Long accountId;
    private String licenseNumber;
    private DriverAvailability availability;
    private String serviceArea;
    private Double currentLatitude;
    private Double currentLongitude;
    private VehicleResponse vehicle;

    public DriverResponse() {
    }

    public DriverResponse(Driver driver) {
        this.id = driver.getId();
        this.accountId = driver.getAccountId();
        this.licenseNumber = driver.getLicenseNumber();
        this.availability = driver.getAvailability();
        this.serviceArea = driver.getServiceArea();
        this.currentLatitude = driver.getCurrentLatitude();
        this.currentLongitude = driver.getCurrentLongitude();
        if (driver.getVehicle() != null) {
            this.vehicle = new VehicleResponse(driver.getVehicle());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public DriverAvailability getAvailability() {
        return availability;
    }

    public void setAvailability(DriverAvailability availability) {
        this.availability = availability;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
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

    public VehicleResponse getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleResponse vehicle) {
        this.vehicle = vehicle;
    }
}
