package com.ridelink.ride.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRideRequest(
        @NotNull Long passengerId,
        @NotBlank String pickupLocation,
        @NotBlank String destinationLocation
) {}
