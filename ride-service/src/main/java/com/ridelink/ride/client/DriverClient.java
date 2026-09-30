package com.ridelink.ride.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class DriverClient {

    private final RestClient restClient;

    public DriverClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
    }

    public Optional<Long> findAvailableDriver() {

        List<Map> drivers = restClient.get()
                .uri("/api/drivers/available")
                .retrieve()
                .body(List.class);

        if (drivers == null || drivers.isEmpty()) {
            return Optional.empty();
        }

        Object id = drivers.get(0).get("id");

        if (id instanceof Number number) {
            return Optional.of(number.longValue());
        }

        return Optional.empty();
    }
}