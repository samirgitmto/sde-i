package com.actuator.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalServiceHealthIndicator implements HealthIndicator {

    private final RestTemplate restTemplate = new RestTemplate();
    
    @Value("${external.service.url}")
    private String externalServiceUrl;

    @Override
    public Health health() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(externalServiceUrl, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                return Health.up()
                    .withDetail("ExternalService", "Available")
                    .withDetail("Status", response.getStatusCode())
                    .withDetail("ResponseTime", "OK")
                    .build();
            } else {
                return Health.down()
                    .withDetail("ExternalService", "Unavailable")
                    .withDetail("Status", response.getStatusCode())
                    .withDetail("Error", "Non-2xx response")
                    .build();
            }
        } catch (Exception ex) {
            return Health.down()
                .withDetail("ExternalService", "Unavailable")
                .withDetail("Error", ex.getMessage())
                .withException(ex)
                .build();
        }
    }
} 