package com.energy.energyoptimizationapi.service;

import com.energy.energyoptimizationapi.model.dto.EmtLoginDTO;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MobilityService {

    @Autowired
    private RestTemplate restTemplate;


    @Value("${external.api.emt.url.login}")
    private String loginUrl;

    @Value("${external.api.emt.url.arrivals}")
    private String arrivalsUrl;

    @Value("${external.api.emt.url.parkings}")
    private String parkingsUrl;


    @Value("${external.api.emt.email}")
    private String emtEmail;

    @Value("${external.api.emt.password}")
    private String emtPassword;

    private String currentAccessToken = "";
    private String lastError = "";


    private boolean loginToEmt() {
        System.out.println("🔄 Intentando Login en EMT con: " + emtEmail);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("email", emtEmail);
            headers.set("password", emtPassword);
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<EmtLoginDTO> response = restTemplate.exchange(
                    loginUrl, HttpMethod.GET, entity, EmtLoginDTO.class
            );

            if (response.getBody() != null && response.getBody().getData() != null) {
                if (!response.getBody().getData().isEmpty()) {
                    this.currentAccessToken = response.getBody().getData().get(0).getAccessToken();
                    return true;
                }
            }
            System.err.println("Fallo Login: Respuesta vacía.");
            return false;
        } catch (Exception e) {
            this.lastError = e.getMessage();
            System.err.println("Excepción Login: " + e.getMessage());
            return false;
        }
    }


    public Double getRealMobilityIndex() {
        if (currentAccessToken.isEmpty()) {
            boolean success = loginToEmt();
            if (!success) {
                System.out.println("No se puede calcular movilidad: Fallo el login.");
                return 0.5;
            }
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("accessToken", currentAccessToken);
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);


            ResponseEntity<String> response = restTemplate.exchange(
                    parkingsUrl, HttpMethod.GET, entity, String.class
            );

            String jsonRaw = response.getBody();


            if (jsonRaw != null) {
                JsonObject jsonObject = JsonParser.parseString(jsonRaw).getAsJsonObject();

                if (jsonObject.has("data") && jsonObject.get("data").isJsonArray()) {
                    JsonArray dataArray = jsonObject.getAsJsonArray("data");

                    double totalFreeSpots = 0;
                    int validParkingsCount = 0;

                    for (JsonElement element : dataArray) {
                        JsonObject p = element.getAsJsonObject();
                        if (p.has("freeParking") && !p.get("freeParking").isJsonNull()) {
                            totalFreeSpots += p.get("freeParking").getAsInt();
                            validParkingsCount++;
                        }
                    }




                    double maxCapacityReference = 9000.0;
                    double index = 1.0 - (totalFreeSpots / maxCapacityReference);

                    return Math.max(0.1, Math.min(1.0, index));
                }
            }

        } catch (Exception e) {
            System.err.println("Error en Parkings: " + e.getMessage());
            if (e.getMessage().contains("401")) {
                System.out.println("Token caducado. Borrando para renovar.");
                this.currentAccessToken = "";
            }
        }

        return 0.5;
    }



    public String getNextBusInStop(int stopId) {
        if (currentAccessToken.isEmpty()) loginToEmt();

        return "Función bus simplificada para este ejemplo";
    }

    public Double getMobilityIndex() {
        return getRealMobilityIndex();
    }

    public Double estimateDemandFromMobility(Double mobilityIndex) {
        return 500 + (mobilityIndex * 3000);
    }
}