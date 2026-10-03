package com.ridelink.driver.service;

import com.ridelink.driver.dto.*;
import com.ridelink.driver.entity.Driver;
import com.ridelink.driver.entity.DriverAvailability;
import com.ridelink.driver.entity.Vehicle;
import com.ridelink.driver.exception.ResourceNotFoundException;
import com.ridelink.driver.repository.DriverRepository;
import com.ridelink.driver.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    public DriverService(DriverRepository driverRepository, VehicleRepository vehicleRepository) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public DriverResponse createDriver(DriverRequest request) {
        if (driverRepository.existsByAccountId(request.getAccountId())) {
            throw new IllegalArgumentException(
                    "Driver profile already exists for Account ID: " + request.getAccountId());
        }
        if (driverRepository.existsByLicenseNumber(request.getLicenseNumber().trim())) {
            throw new IllegalArgumentException(
                    "License number '" + request.getLicenseNumber() + "' is already registered.");
        }

        Driver driver = new Driver();
        driver.setAccountId(request.getAccountId());
        driver.setLicenseNumber(request.getLicenseNumber().trim());
        driver.setAvailability(
                request.getAvailability() != null ? request.getAvailability() : DriverAvailability.UNAVAILABLE);
        driver.setServiceArea(request.getServiceArea().trim());
        driver.setCurrentLatitude(request.getCurrentLatitude());
        driver.setCurrentLongitude(request.getCurrentLongitude());

        Driver saved = driverRepository.save(driver);
        return new DriverResponse(saved);
    }

    public DriverResponse getDriverById(Long id) {
        Driver driver = driverRepository.findById(Objects.requireNonNull(id, "Driver ID must not be null"))
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + id));
        return new DriverResponse(driver);
    }

    @Transactional
    public DriverResponse updateDriver(Long id, DriverRequest request) {
        Driver driver = driverRepository.findById(Objects.requireNonNull(id, "Driver ID must not be null"))
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + id));

        driver.setServiceArea(request.getServiceArea().trim());
        if (request.getAvailability() != null) {
            driver.setAvailability(request.getAvailability());
        }
        if (request.getCurrentLatitude() != null) {
            driver.setCurrentLatitude(request.getCurrentLatitude());
        }
        if (request.getCurrentLongitude() != null) {
            driver.setCurrentLongitude(request.getCurrentLongitude());
        }

        Driver updated = driverRepository.save(driver);
        return new DriverResponse(updated);
    }

    @Transactional
    public DriverResponse updateAvailability(Long id, DriverAvailability availability) {
        Driver driver = driverRepository.findById(Objects.requireNonNull(id, "Driver ID must not be null"))
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + id));

        driver.setAvailability(availability);
        Driver updated = driverRepository.save(driver);
        return new DriverResponse(updated);
    }

    @Transactional
    public DriverResponse updateLocation(Long id, Double latitude, Double longitude) {
        Driver driver = driverRepository.findById(Objects.requireNonNull(id, "Driver ID must not be null"))
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + id));

        driver.setCurrentLatitude(latitude);
        driver.setCurrentLongitude(longitude);
        Driver updated = driverRepository.save(driver);
        return new DriverResponse(updated);
    }

    public List<AvailableDriverResponse> getAvailableDrivers(String serviceArea) {
        List<Driver> drivers;
        if (serviceArea != null && !serviceArea.isBlank()) {
            drivers = driverRepository.findByAvailabilityAndServiceAreaIgnoreCase(DriverAvailability.AVAILABLE,
                    serviceArea.trim());
        } else {
            drivers = driverRepository.findByAvailability(DriverAvailability.AVAILABLE);
        }

        return drivers.stream()
                .map(AvailableDriverResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public VehicleResponse registerVehicle(VehicleRequest request) {
        Long driverId = Objects.requireNonNull(request.getDriverId(), "Driver ID must not be null");
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + request.getDriverId()));

        if (vehicleRepository.existsByVehicleNumber(request.getVehicleNumber().trim())) {
            throw new IllegalArgumentException(
                    "Vehicle number '" + request.getVehicleNumber() + "' is already registered.");
        }

        Vehicle vehicle = vehicleRepository.findByDriverId(driver.getId()).orElse(new Vehicle());
        vehicle.setDriver(driver);
        vehicle.setVehicleNumber(request.getVehicleNumber().trim());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setModel(request.getModel().trim());
        vehicle.setColor(request.getColor().trim());

        Vehicle saved = vehicleRepository.save(vehicle);
        driver.setVehicle(saved);
        driverRepository.save(driver);

        return new VehicleResponse(saved);
    }

    public VehicleResponse getVehicleByDriverId(Long driverId) {
        Vehicle vehicle = vehicleRepository.findByDriverId(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("No vehicle found for Driver ID: " + driverId));
        return new VehicleResponse(vehicle);
    }
}
