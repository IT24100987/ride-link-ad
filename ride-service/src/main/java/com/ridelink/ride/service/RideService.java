package com.ridelink.ride.service;

import com.ridelink.ride.client.DriverClient;
import com.ridelink.ride.enums.RideStatus;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final DriverClient driverClient;

    public RideService(
            RideRepository rideRepository,
            DriverClient driverClient) {

        this.rideRepository = rideRepository;
        this.driverClient = driverClient;
    }

    public Ride createRide(Ride ride) {
        // Every new ride starts as REQUESTED
        ride.setStatus(RideStatus.REQUESTED);
        return rideRepository.save(ride);
    }

    public Ride assignAvailableDriver(Long rideId) {

        Ride ride = rideRepository.findById(rideId).orElse(null);

        if (ride == null) {
            return null;
        }

        // A driver can only be assigned to a requested ride
        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new IllegalStateException(
                    "Driver can only be assigned to a REQUESTED ride"
            );
        }

        // Get an available driver from Driver & Vehicle Service
        Long driverId = driverClient.findAvailableDriver()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No available driver found"
                        )
                );

        ride.setDriverId(String.valueOf(driverId));
        ride.setStatus(RideStatus.ASSIGNED);

        return rideRepository.save(ride);
    }

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    public Ride getRideById(Long id) {
        return rideRepository.findById(id).orElse(null);
    }

    public Ride updateRide(Long id, Ride updatedRide) {

        Ride existingRide = rideRepository.findById(id).orElse(null);

        if (existingRide == null) {
            return null;
        }

        // Check status transition only if the status is changing
        if (updatedRide.getStatus() != null &&
                updatedRide.getStatus() != existingRide.getStatus()) {

            if (!isValidStatusTransition(
                    existingRide.getStatus(),
                    updatedRide.getStatus())) {

                throw new IllegalStateException(
                        "Invalid ride status transition from "
                                + existingRide.getStatus()
                                + " to "
                                + updatedRide.getStatus()
                );
            }
        }

        updatedRide.setId(id);

        return rideRepository.save(updatedRide);
    }

    private boolean isValidStatusTransition(
            RideStatus currentStatus,
            RideStatus newStatus) {

        if (currentStatus == null || newStatus == null) {
            return false;
        }

        return switch (currentStatus) {

            case REQUESTED ->
                    newStatus == RideStatus.ASSIGNED ||
                    newStatus == RideStatus.CANCELLED;

            case ASSIGNED ->
                    newStatus == RideStatus.ACCEPTED ||
                    newStatus == RideStatus.CANCELLED;

            case ACCEPTED ->
                    newStatus == RideStatus.IN_PROGRESS ||
                    newStatus == RideStatus.CANCELLED;

            case IN_PROGRESS ->
                    newStatus == RideStatus.COMPLETED ||
                    newStatus == RideStatus.CANCELLED;

            case COMPLETED, CANCELLED -> false;
        };
    }

    public boolean deleteRide(Long id) {

        if (!rideRepository.existsById(id)) {
            return false;
        }

        rideRepository.deleteById(id);
        return true;
    }
}