package com.ridelink.driver.controller;

import com.ridelink.driver.dto.VehicleRequest;
import com.ridelink.driver.dto.VehicleResponse;
import com.ridelink.driver.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehicle Management", description = "Endpoints for vehicle registration and driver vehicle retrieval")
public class VehicleController {

    private final DriverService driverService;

    public VehicleController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    @Operation(summary = "Register vehicle details and associate with a driver")
    public ResponseEntity<VehicleResponse> registerVehicle(@Valid @RequestBody VehicleRequest request) {
        VehicleResponse response = driverService.registerVehicle(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Retrieve vehicle details associated with a driver ID")
    public ResponseEntity<VehicleResponse> getVehicleByDriverId(@PathVariable Long driverId) {
        VehicleResponse response = driverService.getVehicleByDriverId(driverId);
        return ResponseEntity.ok(response);
    }
}
