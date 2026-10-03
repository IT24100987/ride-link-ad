package com.ridelink.ride.exception;

import com.ridelink.ride.model.RideStatus;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409: request conflicts with the ride's current state
public class InvalidRideStatusTransitionException extends RuntimeException {
    public InvalidRideStatusTransitionException(RideStatus current, RideStatus target) {
        super("Cannot change ride status from " + current + " to " + target);
    }
}
