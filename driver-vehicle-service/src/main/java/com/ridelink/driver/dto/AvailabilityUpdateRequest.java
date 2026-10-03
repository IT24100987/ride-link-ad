package com.ridelink.driver.dto;

import com.ridelink.driver.entity.DriverAvailability;
import jakarta.validation.constraints.NotNull;

public class AvailabilityUpdateRequest {

    @NotNull(message = "Availability status is required (AVAILABLE, BUSY, UNAVAILABLE)")
    private DriverAvailability availability;

    public AvailabilityUpdateRequest() {
    }

    public AvailabilityUpdateRequest(DriverAvailability availability) {
        this.availability = availability;
    }

    public DriverAvailability getAvailability() {
        return availability;
    }

    public void setAvailability(DriverAvailability availability) {
        this.availability = availability;
    }
}
