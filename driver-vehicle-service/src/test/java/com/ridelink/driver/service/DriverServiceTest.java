package com.ridelink.driver.service;

import com.ridelink.driver.dto.AvailableDriverResponse;
import com.ridelink.driver.dto.DriverRequest;
import com.ridelink.driver.dto.DriverResponse;
import com.ridelink.driver.dto.VehicleRequest;
import com.ridelink.driver.dto.VehicleResponse;
import com.ridelink.driver.entity.Driver;
import com.ridelink.driver.entity.DriverAvailability;
import com.ridelink.driver.entity.Vehicle;
import com.ridelink.driver.entity.VehicleType;
import com.ridelink.driver.repository.DriverRepository;
import com.ridelink.driver.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private DriverService driverService;

    private Driver sampleDriver;
    private DriverRequest driverRequest;

    @BeforeEach
    void setUp() {
        sampleDriver = new Driver(
                1L,
                2L,
                "B1234567",
                DriverAvailability.AVAILABLE,
                "Colombo",
                6.9271,
                79.8612);

        driverRequest = new DriverRequest(
                2L,
                "B1234567",
                DriverAvailability.AVAILABLE,
                "Colombo",
                6.9271,
                79.8612);
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Should successfully create a driver profile")
    void createDriver_Success() {
        when(driverRepository.existsByAccountId(2L)).thenReturn(false);
        when(driverRepository.existsByLicenseNumber("B1234567")).thenReturn(false);
        when(driverRepository.save(any(Driver.class))).thenReturn(sampleDriver);

        DriverResponse response = driverService.createDriver(driverRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("B1234567", response.getLicenseNumber());
        assertEquals(DriverAvailability.AVAILABLE, response.getAvailability());
        assertEquals("Colombo", response.getServiceArea());
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Should throw IllegalArgumentException when creating duplicate driver account")
    void createDriver_DuplicateAccountId_ThrowsException() {
        when(driverRepository.existsByAccountId(2L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> driverService.createDriver(driverRequest));
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Should update driver availability successfully")
    void updateAvailability_Success() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(sampleDriver));
        when(driverRepository.save(any(Driver.class))).thenReturn(sampleDriver);

        DriverResponse response = driverService.updateAvailability(1L, DriverAvailability.BUSY);

        assertNotNull(response);
        verify(driverRepository, times(1)).save(sampleDriver);
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Should update driver simulated location successfully")
    void updateLocation_Success() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(sampleDriver));
        when(driverRepository.save(any(Driver.class))).thenReturn(sampleDriver);

        DriverResponse response = driverService.updateLocation(1L, 6.9300, 79.8700);

        assertNotNull(response);
        assertEquals(6.9300, sampleDriver.getCurrentLatitude());
        assertEquals(79.8700, sampleDriver.getCurrentLongitude());
    }

    @Test
    @DisplayName("Should retrieve available drivers list")
    void getAvailableDrivers_Success() {
        when(driverRepository.findByAvailabilityAndServiceAreaIgnoreCase(DriverAvailability.AVAILABLE, "Colombo"))
                .thenReturn(Collections.singletonList(sampleDriver));

        List<AvailableDriverResponse> available = driverService.getAvailableDrivers("Colombo");

        assertNotNull(available);
        assertEquals(1, available.size());
        assertEquals(1L, available.get(0).getDriverId());
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Should register vehicle and link to driver")
    void registerVehicle_Success() {
        VehicleRequest vehicleRequest = new VehicleRequest(1L, "CAB-1234", VehicleType.CAR, "Toyota Prius", "White");
        Vehicle sampleVehicle = new Vehicle(1L, sampleDriver, "CAB-1234", VehicleType.CAR, "Toyota Prius", "White");

        when(driverRepository.findById(1L)).thenReturn(Optional.of(sampleDriver));
        when(vehicleRepository.existsByVehicleNumber("CAB-1234")).thenReturn(false);
        when(vehicleRepository.findByDriverId(1L)).thenReturn(Optional.empty());
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(sampleVehicle);

        VehicleResponse response = driverService.registerVehicle(vehicleRequest);

        assertNotNull(response);
        assertEquals("CAB-1234", response.getVehicleNumber());
        assertEquals(VehicleType.CAR, response.getVehicleType());
    }
}
