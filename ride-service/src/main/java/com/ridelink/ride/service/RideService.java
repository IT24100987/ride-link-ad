package com.ridelink.ride.service;

import com.ridelink.ride.client.DriverClient;
import com.ridelink.ride.dto.CreateRideRequest;
import com.ridelink.ride.exception.InvalidRideStatusTransitionException;
import com.ridelink.ride.exception.NoAvailableDriverException;
import com.ridelink.ride.exception.RideNotFoundException;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import com.ridelink.ride.repository.RideRepository;

import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final DriverClient driverClient;

    public RideService(RideRepository rideRepository, DriverClient driverClient) {
        this.rideRepository = rideRepository;
        this.driverClient = driverClient;
    }

    public Ride requestRide(CreateRideRequest request) {
        Ride ride = new Ride();
        ride.setPassengerId(request.passengerId());
        ride.setPickupLocation(request.pickupLocation());
        ride.setDestinationLocation(request.destinationLocation());
        ride.setStatus(RideStatus.REQUESTED);
        ride.setCreatedAt(Instant.now());
        ride.setUpdatedAt(Instant.now());
        return rideRepository.save(ride);
    }

    public Ride getRide(Long id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(id));
    }

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    // REQUESTED -> ASSIGNED : find a driver and attach them to the ride
    public Ride assignDriver(Long id) {
        Ride ride = getRide(id);
        requireStatus(ride, RideStatus.REQUESTED, RideStatus.ASSIGNED);

        Long driverId = driverClient.findAvailableDriver()
                .orElseThrow(() -> new NoAvailableDriverException(id));

        ride.setDriverId(driverId);
        return updateStatus(ride, RideStatus.ASSIGNED);
    }

    // ASSIGNED -> ACCEPTED : the assigned driver accepts the ride
    public Ride acceptRide(Long id) {
        Ride ride = getRide(id);
        requireStatus(ride, RideStatus.ASSIGNED, RideStatus.ACCEPTED);
        return updateStatus(ride, RideStatus.ACCEPTED);
    }

    // ACCEPTED -> IN_PROGRESS : the ride starts
    public Ride startRide(Long id) {
        Ride ride = getRide(id);
        requireStatus(ride, RideStatus.ACCEPTED, RideStatus.IN_PROGRESS);
        return updateStatus(ride, RideStatus.IN_PROGRESS);
    }

    // IN_PROGRESS -> COMPLETED : the ride finishes
    public Ride completeRide(Long id) {
        Ride ride = getRide(id);
        requireStatus(ride, RideStatus.IN_PROGRESS, RideStatus.COMPLETED);
        return updateStatus(ride, RideStatus.COMPLETED);
    }

    // REQUESTED / ASSIGNED / ACCEPTED -> CANCELLED : cancel before the ride starts
    public Ride cancelRide(Long id) {
        Ride ride = getRide(id);
        RideStatus current = ride.getStatus();
        boolean cancellable = current == RideStatus.REQUESTED
                || current == RideStatus.ASSIGNED
                || current == RideStatus.ACCEPTED;
        if (!cancellable) {
            throw new InvalidRideStatusTransitionException(current, RideStatus.CANCELLED);
        }
        return updateStatus(ride, RideStatus.CANCELLED);
    }

    // --- helpers ---

    // Guard: the ride must be in `expected` before it can move to `target`.
    private void requireStatus(Ride ride, RideStatus expected, RideStatus target) {
        if (ride.getStatus() != expected) {
            throw new InvalidRideStatusTransitionException(ride.getStatus(), target);
        }
    }

    private Ride updateStatus(Ride ride, RideStatus newStatus) {
        ride.setStatus(newStatus);
        ride.setUpdatedAt(Instant.now());
        return rideRepository.save(ride);
    }
}
