package com.energy.energyoptimizationapi.service;

import com.energy.energyoptimizationapi.model.dto.RedElectricaResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RedElectricaService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${external.api.ree.url}")
    private String apiUrl;

    @Value("${external.api.ree.token}")
    private String apiToken;

    public Double getRealDemand() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            headers.set("Content-Type", "application/json");
            headers.set("x-api-key", apiToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<RedElectricaResponseDTO> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    entity,
                    RedElectricaResponseDTO.class
            );

            if (response.getBody() != null &&
                    response.getBody().getIndicator() != null &&
                    !response.getBody().getIndicator().getValues().isEmpty()) {

                var values = response.getBody().getIndicator().getValues();
                return values.get(values.size() - 1).getValue();
            }

            return 0.0; // Si no hay datos

        } catch (Exception e) {
            System.err.println("Error conectando a REE: " + e.getMessage());
            return 25000.0; // Valor fallback
        }
    }
}