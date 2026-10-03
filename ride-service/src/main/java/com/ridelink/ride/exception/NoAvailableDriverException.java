package com.ridelink.ride.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409: no driver could be assigned right now
public class NoAvailableDriverException extends RuntimeException {
    public NoAvailableDriverException(Long rideId) {
        super("No available driver could be assigned to ride " + rideId);
    }
}
