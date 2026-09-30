package com.ridelink.ride.service;

import com.ridelink.ride.client.DriverClient;
import com.ridelink.ride.enums.RideStatus;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.repository.RideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverClient driverClient;

    private RideService rideService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rideService = new RideService(rideRepository, driverClient);
    }

    @Test
    void shouldCreateRideWithRequestedStatus() {

        Ride ride = new Ride();
        ride.setPassengerId("P001");
        ride.setPickupLocation("Colombo");
        ride.setDropoffLocation("Kandy");
        ride.setDistance(115);
        ride.setFare(8500);

        when(rideRepository.save(ride))
                .thenReturn(ride);

        Ride result = rideService.createRide(ride);

        assertEquals(
                RideStatus.REQUESTED,
                result.getStatus()
        );

        verify(rideRepository).save(ride);
    }

    @Test
    void shouldAllowRequestedToAssigned() {

        Ride existingRide = new Ride();
        existingRide.setId(1L);
        existingRide.setStatus(RideStatus.REQUESTED);

        Ride updatedRide = new Ride();
        updatedRide.setStatus(RideStatus.ASSIGNED);

        when(rideRepository.findById(1L))
                .thenReturn(Optional.of(existingRide));

        when(rideRepository.save(updatedRide))
                .thenReturn(updatedRide);

        Ride result =
                rideService.updateRide(1L, updatedRide);

        assertEquals(
                RideStatus.ASSIGNED,
                result.getStatus()
        );

        verify(rideRepository).save(updatedRide);
    }

    @Test
    void shouldAllowInProgressToCompleted() {

        Ride existingRide = new Ride();
        existingRide.setId(1L);
        existingRide.setStatus(RideStatus.IN_PROGRESS);

        Ride updatedRide = new Ride();
        updatedRide.setStatus(RideStatus.COMPLETED);

        when(rideRepository.findById(1L))
                .thenReturn(Optional.of(existingRide));

        when(rideRepository.save(updatedRide))
                .thenReturn(updatedRide);

        Ride result =
                rideService.updateRide(1L, updatedRide);

        assertEquals(
                RideStatus.COMPLETED,
                result.getStatus()
        );
    }

    @Test
    void shouldRejectRequestedToCompleted() {

        Ride existingRide = new Ride();
        existingRide.setId(1L);
        existingRide.setStatus(RideStatus.REQUESTED);

        Ride updatedRide = new Ride();
        updatedRide.setStatus(RideStatus.COMPLETED);

        when(rideRepository.findById(1L))
                .thenReturn(Optional.of(existingRide));

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> rideService.updateRide(
                                1L,
                                updatedRide
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains(
                                "Invalid ride status transition"
                        )
        );

        verify(
                rideRepository,
                never()
        ).save(any());
    }

    @Test
    void shouldRejectChangesAfterCompleted() {

        Ride existingRide = new Ride();
        existingRide.setId(1L);
        existingRide.setStatus(RideStatus.COMPLETED);

        Ride updatedRide = new Ride();
        updatedRide.setStatus(RideStatus.CANCELLED);

        when(rideRepository.findById(1L))
                .thenReturn(Optional.of(existingRide));

        assertThrows(
                IllegalStateException.class,
                () -> rideService.updateRide(
                        1L,
                        updatedRide
                )
        );

        verify(
                rideRepository,
                never()
        ).save(any());
    }

    @Test
    void shouldAssignAvailableDriver() {

        Ride ride = new Ride();
        ride.setId(5L);
        ride.setStatus(RideStatus.REQUESTED);

        when(rideRepository.findById(5L))
                .thenReturn(Optional.of(ride));

        when(driverClient.findAvailableDriver())
                .thenReturn(Optional.of(1L));

        when(rideRepository.save(ride))
                .thenReturn(ride);

        Ride result =
                rideService.assignAvailableDriver(5L);

        assertEquals(
                "1",
                result.getDriverId()
        );

        assertEquals(
                RideStatus.ASSIGNED,
                result.getStatus()
        );

        verify(driverClient)
                .findAvailableDriver();

        verify(rideRepository)
                .save(ride);
    }

    @Test
    void shouldRejectWhenNoAvailableDriver() {

        Ride ride = new Ride();
        ride.setId(5L);
        ride.setStatus(RideStatus.REQUESTED);

        when(rideRepository.findById(5L))
                .thenReturn(Optional.of(ride));

        when(driverClient.findAvailableDriver())
                .thenReturn(Optional.empty());

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> rideService
                                .assignAvailableDriver(5L)
                );

        assertEquals(
                "No available driver found",
                exception.getMessage()
        );

        verify(
                rideRepository,
                never()
        ).save(any());
    }
}