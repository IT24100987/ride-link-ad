package com.ridelink.driver.dto;

import com.ridelink.driver.entity.DriverAvailability;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DriverRequest {

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    private DriverAvailability availability = DriverAvailability.UNAVAILABLE;

    @NotBlank(message = "Service area is required")
    private String serviceArea;

    private Double currentLatitude;
    private Double currentLongitude;

    public DriverRequest() {
    }

    public DriverRequest(Long accountId, String licenseNumber, DriverAvailability availability,
                         String serviceArea, Double currentLatitude, Double currentLongitude) {
        this.accountId = accountId;
        this.licenseNumber = licenseNumber;
        this.availability = availability;
        this.serviceArea = serviceArea;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
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
}
