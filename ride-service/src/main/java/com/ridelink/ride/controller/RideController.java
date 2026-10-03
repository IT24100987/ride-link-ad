package com.ridelink.ride.controller;

import com.ridelink.ride.dto.CreateRideRequest;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.service.RideService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<Ride> create(@Valid @RequestBody CreateRideRequest request) {
        Ride ride = rideService.requestRide(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ride);
    }

    @GetMapping("/{id}")
    public Ride getById(@PathVariable Long id) {
        return rideService.getRide(id);
    }

    @GetMapping
    public List<Ride> getAll() {
        return rideService.getAllRides();
    }

    @PostMapping("/{id}/assign")
    public Ride assignDriver(@PathVariable Long id) {
        return rideService.assignDriver(id);
    }

    @PostMapping("/{id}/accept")
    public Ride accept(@PathVariable Long id) {
        return rideService.acceptRide(id);
    }

    @PostMapping("/{id}/start")
    public Ride start(@PathVariable Long id) {
        return rideService.startRide(id);
    }

    @PostMapping("/{id}/complete")
    public Ride complete(@PathVariable Long id) {
        return rideService.completeRide(id);
    }

    @PostMapping("/{id}/cancel")
    public Ride cancel(@PathVariable Long id) {
        return rideService.cancelRide(id);
    }
}
