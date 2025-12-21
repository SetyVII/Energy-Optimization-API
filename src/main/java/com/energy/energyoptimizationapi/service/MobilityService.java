package com.energy.energyoptimizationapi.service;

import com.energy.energyoptimizationapi.model.dto.EmtResponseDTO;
import com.google.gson.JsonArray;
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

    @Value("${external.api.emt.email}")
    private String emtEmail;

    @Value("${external.api.emt.password}")
    private String emtPassword;

    private String currentAccessToken = "";
    private String lastError = "";

    /**
     * Login MANUAL usando GSON (JsonParser) para evitar el problema de Array vs Objeto
     */
    private boolean loginToEmt() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-ClientId", emtEmail);
            headers.set("passKey", emtPassword);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // 1. Pedimos la respuesta como String puro para analizarla nosotros
            ResponseEntity<String> response = restTemplate.exchange(
                    loginUrl, HttpMethod.GET, entity, String.class
            );

            if (response.getBody() != null) {
                // 2. Analizamos el JSON
                JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();

                if (jsonResponse.has("data") && jsonResponse.get("data").isJsonArray()) {
                    JsonArray dataArray = jsonResponse.getAsJsonArray("data");
                    if (dataArray.size() > 0) {
                        // Tomamos el primer elemento y sacamos el token
                        this.currentAccessToken = dataArray.get(0).getAsJsonObject().get("accessToken").getAsString();
                        System.out.println(" Login EMT Exitoso. Token actualizado.");
                        return true;
                    }
                }
            }
            this.lastError = "Respuesta de Login vacía o formato inesperado";
            return false;
        } catch (Exception e) {
            this.lastError = "Excepción en Login: " + e.getMessage();
            System.err.println("X " + lastError);
            return false;
        }
    }

    public String getNextBusInStop(int stopId) {
        // Si no tenemos token, intentamos loguearnos
        if (currentAccessToken.isEmpty()) {
            boolean loginSuccess = loginToEmt();
            if (!loginSuccess) {
                return " Error Autenticación EMT: " + lastError;
            }
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("accessToken", currentAccessToken);

            // Body requerido por la API v2
            String jsonBody = "{\"cultureInfo\":\"ES\", \"Text_StopRequired_YN\":\"Y\", \"Text_EstimationsRequired_YN\":\"Y\", \"Text_IncidencesRequired_YN\":\"N\", \"DateTime_Referenced_Incidencies_YYYYMMDD\":\"20250101\"}";

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            String finalUrl = arrivalsUrl.replace("{stopId}", String.valueOf(stopId));

            // Para "Arrives", la API sí devuelve "data" como Objeto, así que el DTO funciona bien aquí
            ResponseEntity<EmtResponseDTO> response = restTemplate.exchange(
                    finalUrl, HttpMethod.POST, entity, EmtResponseDTO.class
            );

            if (response.getBody() != null &&
                    response.getBody().getData() != null &&
                    response.getBody().getData().getArrive() != null &&
                    !response.getBody().getData().getArrive().isEmpty()) {

                EmtResponseDTO.Arrive firstBus = response.getBody().getData().getArrive().get(0);

                // Conversión de segundos a minutos
                int segundos = firstBus.getEstimateArrive();
                if (segundos > 1200) { // Código 999999 suele ser >20min
                    return String.format(" Línea %s a %s: +20 min", firstBus.getLine(), firstBus.getDestination());
                }

                int minutos = segundos / 60;
                return String.format(" Línea %s a %s llega en %d min (%d m)",
                        firstBus.getLine(), firstBus.getDestination(), minutos, firstBus.getDistanceBus().intValue());
            }

            return "⚠️ No hay autobuses próximos para la parada " + stopId;

        } catch (Exception e) {
            // Si el token caducó (Error 401), limpiamos para forzar login la próxima vez
            if (e.getMessage().contains("401")) {
                this.currentAccessToken = "";
                return " Token expirado. Por favor reintenta la petición.";
            }
            return "Error consultando parada: " + e.getMessage();
        }
    }

    // Métodos auxiliares mantenidos
    public Double getMobilityIndex() { return 0.85; }
    public Double estimateDemandFromMobility(Double val) { return 500 + (val * 300); }
}