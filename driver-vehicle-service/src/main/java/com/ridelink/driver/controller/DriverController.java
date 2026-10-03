package com.ridelink.driver.controller;

import com.ridelink.driver.dto.*;
import com.ridelink.driver.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@Tag(name = "Driver Management", description = "Endpoints for driver operational details, availability, and location")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    @Operation(summary = "Create a driver operational profile")
    public ResponseEntity<DriverResponse> createDriver(@Valid @RequestBody DriverRequest request) {
        DriverResponse response = driverService.createDriver(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get driver profile by driver ID")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id) {
        DriverResponse response = driverService.getDriverById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update driver operational profile")
    public ResponseEntity<DriverResponse> updateDriver(@PathVariable Long id,
                                                       @Valid @RequestBody DriverRequest request) {
        DriverResponse response = driverService.updateDriver(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/availability")
    @Operation(summary = "Update driver availability status (AVAILABLE, BUSY, UNAVAILABLE)")
    public ResponseEntity<DriverResponse> updateAvailability(@PathVariable Long id,
                                                             @Valid @RequestBody AvailabilityUpdateRequest request) {
        DriverResponse response = driverService.updateAvailability(id, request.getAvailability());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/location")
    @Operation(summary = "Update driver simulated latitude and longitude")
    public ResponseEntity<DriverResponse> updateLocation(@PathVariable Long id,
                                                         @Valid @RequestBody LocationUpdateRequest request) {
        DriverResponse response = driverService.updateLocation(id, request.getLatitude(), request.getLongitude());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    @Operation(summary = "Get all eligible available drivers (optional filter by serviceArea)")
    public ResponseEntity<List<AvailableDriverResponse>> getAvailableDrivers(
            @RequestParam(required = false) String serviceArea) {
        List<AvailableDriverResponse> availableDrivers = driverService.getAvailableDrivers(serviceArea);
        return ResponseEntity.ok(availableDrivers);
    }
}
