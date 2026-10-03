package com.ridelink.driver;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "RideLink Driver & Vehicle Service API",
        version = "1.0.0",
        description = "Manages driver operational profiles, vehicles, availability, and simulated live locations.",
        contact = @Contact(name = "RideLink Development Team", email = "support@ridelink.com")
    )
)
public class DriverVehicleApplication {
    public static void main(String[] args) {
        SpringApplication.run(DriverVehicleApplication.class, args);
    }
}
