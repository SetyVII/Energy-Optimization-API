package com.energy.energyoptimizationapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ParkingDebugTests {


    private static final String MI_TOKEN_TEMPORAL = "21d76fd1-456b-4a89-b69e-ad1cf6bb7f8f";

    @Test
    public void testVerRespuestaCrudaParkings() {
        System.out.println("🧪 INICIANDO TEST DE DIAGNÓSTICO DE PARKINGS...");


        RestTemplate restTemplate = new RestTemplate();
        String url = "https://openapi.emtmadrid.es/v1/citymad/places/parkings/availability/";

        HttpHeaders headers = new HttpHeaders();
        headers.set("accessToken", MI_TOKEN_TEMPORAL);
        headers.set("Accept", "application/json");

        try {

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class
            );

            String jsonCrudo = response.getBody();


            System.out.println("\n🔍 --- RESPUESTA REAL RECIBIDA DE LA EMT ---");
            System.out.println(jsonCrudo);
            System.out.println("-------------------------------------------\n");


            JsonObject jsonObject = JsonParser.parseString(jsonCrudo).getAsJsonObject();

            if (!jsonObject.has("data")) {
                System.err.println("❌ ERROR CRÍTICO: El JSON no tiene campo 'data'.");
                return;
            }

            JsonElement dataElement = jsonObject.get("data");
            if (dataElement.isJsonArray()) {
                JsonArray lista = dataElement.getAsJsonArray();
                System.out.println("✅ El campo 'data' es una lista con " + lista.size() + " elementos.");

                int contados = 0;
                for (JsonElement e : lista) {
                    JsonObject p = e.getAsJsonObject();
                    String nombre = p.has("name") ? p.get("name").getAsString() : "Sin Nombre";


                    System.out.print("   🅿️ " + nombre + " -> ");
                    if (p.has("freeParking") && !p.get("freeParking").isJsonNull()) {
                        System.out.println("LIBRES: " + p.get("freeParking").getAsInt());
                        contados++;
                    } else {
                        System.out.println("Datos nulos (Ignorado)");
                    }
                }
                System.out.println("\n📊 Total parkings válidos encontrados: " + contados);

            } else {
                System.err.println("❌ El campo 'data' NO es una lista.");
            }

        } catch (Exception e) {
            System.err.println("❌ EXCEPCIÓN AL CONECTAR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}