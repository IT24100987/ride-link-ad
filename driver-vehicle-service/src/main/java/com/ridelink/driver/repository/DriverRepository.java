package com.ridelink.driver.repository;

import com.ridelink.driver.entity.Driver;
import com.ridelink.driver.entity.DriverAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByAccountId(Long accountId);
    boolean existsByAccountId(Long accountId);
    boolean existsByLicenseNumber(String licenseNumber);
    List<Driver> findByAvailability(DriverAvailability availability);
    List<Driver> findByAvailabilityAndServiceAreaIgnoreCase(DriverAvailability availability, String serviceArea);
}
