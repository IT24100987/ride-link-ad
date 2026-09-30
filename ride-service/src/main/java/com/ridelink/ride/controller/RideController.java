package com.ridelink.ride.controller;

import com.ridelink.ride.model.Ride;
import com.ridelink.ride.service.RideService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @GetMapping("/test")
    public String test() {
        return "Ride Service is working!";
    }

    @PostMapping
    public Ride createRide(@Valid @RequestBody Ride ride) {
        return rideService.createRide(ride);
    }

    @GetMapping
    public List<Ride> getAllRides() {
        return rideService.getAllRides();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ride> getRideById(@PathVariable Long id) {

        Ride ride = rideService.getRideById(id);

        if (ride == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ride);
    }

    // Get an available driver from Driver & Vehicle Service
    // and assign that driver to the requested ride
    @PutMapping("/{id}/assign-driver")
    public ResponseEntity<Ride> assignDriver(@PathVariable Long id) {

        Ride ride = rideService.assignAvailableDriver(id);

        if (ride == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ride);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ride> updateRide(
            @PathVariable Long id,
            @Valid @RequestBody Ride ride) {

        Ride updatedRide = rideService.updateRide(id, ride);

        if (updatedRide == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedRide);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Long id) {

        if (!rideService.deleteRide(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}