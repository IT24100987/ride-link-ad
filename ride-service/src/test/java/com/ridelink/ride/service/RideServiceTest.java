package com.ridelink.ride.service;

import com.ridelink.ride.client.DriverClient;
import com.ridelink.ride.dto.CreateRideRequest;
import com.ridelink.ride.exception.InvalidRideStatusTransitionException;
import com.ridelink.ride.exception.NoAvailableDriverException;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import com.ridelink.ride.repository.RideRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverClient driverClient;

    private RideService rideService;

    @BeforeEach
    void setUp() {
        rideService = new RideService(rideRepository, driverClient);
    }

    private Ride createRideWithStatus(RideStatus status) {
        Ride ride = new Ride();
        ride.setId(1L);
        ride.setPassengerId(100L);
        ride.setPickupLocation("Colombo");
        ride.setDestinationLocation("Kandy");
        ride.setStatus(status);
        return ride;
    }

    @Test
    void shouldCreateRequestedRide() {
        CreateRideRequest request =
                new CreateRideRequest(100L, "Colombo", "Kandy");

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Ride result = rideService.requestRide(request);

        assertEquals(100L, result.getPassengerId());
        assertEquals("Colombo", result.getPickupLocation());
        assertEquals("Kandy", result.getDestinationLocation());
        assertEquals(RideStatus.REQUESTED, result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        verify(rideRepository).save(any(Ride.class));
    }

    @Test
    void shouldAssignAvailableDriver() {
        Ride ride = createRideWithStatus(RideStatus.REQUESTED);

        when(rideRepository.findById(1L))
                .thenReturn(Optional.of(ride));

        when(driverClient.findAvailableDriver())
                .thenReturn(Optional.of(10L));

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Ride result = rideService.assignDriver(1L);

        assertEquals(10L, result.getDriverId());
        assertEquals(RideStatus.ASSIGNED, result.getStatus());

        verify(driverClient).findAvailableDriver();
        verify(rideRepository).save(ride);
    }

    @Test
    void shouldAcceptAssignedRide() {
        Ride ride = createRideWithStatus(RideStatus.ASSIGNED);

        when(rideRepository.findById(1L))
                .thenReturn(Optional.of(ride));

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Ride result = rideService.acceptRide(1L);

        assertEquals(RideStatus.ACCEPTED, result.getStatus());
    }

    @Test
    void shouldStartAcceptedRide() {
        Ride ride = createRideWithStatus(RideStatus.ACCEPTED);

        when(rideRepository.findById(1L))
                .thenReturn(Optional.of(ride));

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Ride result = rideService.startRide(1L);

        assertEquals(RideStatus.IN_PROGRESS, result.getStatus());
    }

    @Test
    void shouldCompleteRideInProgress() {
        Ride ride = createRideWithStatus(RideStatus.IN_PROGRESS);

        when(rideRepository.findById(1L))
                .thenReturn(Optional.of(ride));

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Ride result = rideService.completeRide(1L);

        assertEquals(RideStatus.COMPLETED, result.getStatus());
    }

    @Test
    void shouldCancelRequestedRide() {
        Ride ride = createRideWithStatus(RideStatus.REQUESTED);

        when(rideRepository.findById(1L))
                .thenReturn(Optional.of(ride));

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Ride result = rideService.cancelRide(1L);

        assertEquals(RideStatus.CANCELLED, result.getStatus());
    }

    @Test
    void shouldRejectInvalidStatusTransition() {
        Ride ride = createRideWithStatus(RideStatus.REQUESTED);

        when(rideRepository.findById(1L))
                .thenReturn(Optional.of(ride));

        assertThrows(
                InvalidRideStatusTransitionException.class,
                () -> rideService.completeRide(1L)
        );

        verify(rideRepository, never()).save(any(Ride.class));
    }

    @Test
    void shouldFailWhenNoDriverIsAvailable() {
        Ride ride = createRideWithStatus(RideStatus.REQUESTED);

        when(rideRepository.findById(1L))
                .thenReturn(Optional.of(ride));

        when(driverClient.findAvailableDriver())
                .thenReturn(Optional.empty());

        assertThrows(
                NoAvailableDriverException.class,
                () -> rideService.assignDriver(1L)
        );

        verify(rideRepository, never()).save(any(Ride.class));
    }
}
