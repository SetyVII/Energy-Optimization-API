package com.energy.energyoptimizationapi.service;

import com.energy.energyoptimizationapi.model.dto.EmtLoginDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class LoginDebugTest {

    private static final String EMAIL_REAL = "sebastianvh111@gmail.com";
    private static final String PASS_REAL = "02Dramaturgy";

    // Asegúrate de que la URL termina en / (barra)
    private static final String URL_LOGIN = "https://openapi.emtmadrid.es/v1/mobilitylabs/user/login/";

    @Test
    public void testLoginReal() {
        System.out.println("🧪 INICIANDO DIAGNÓSTICO DE LOGIN...");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        // Configuración exacta del HTML que funcionó
        headers.set("email", EMAIL_REAL);
        headers.set("password", PASS_REAL);
        // headers.set("X-ClientId", ...); // Si usaras la otra forma
        // headers.set("passKey", ...);

        System.out.println("📡 Enviando petición a: " + URL_LOGIN);
        System.out.println("🔑 Headers: email=" + EMAIL_REAL);

        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // 1. Intentamos mapear directamente al DTO para ver si Jackson funciona bien
            ResponseEntity<EmtLoginDTO> response = restTemplate.exchange(
                    URL_LOGIN, HttpMethod.GET, entity, EmtLoginDTO.class
            );

            System.out.println("✅ ¡CONEXIÓN ESTABLECIDA!");

            if (response.getBody() != null) {
                System.out.println("📦 Respuesta Recibida: " + response.getBody());

                if (response.getBody().getData() != null && !response.getBody().getData().isEmpty()) {
                    String token = response.getBody().getData().get(0).getAccessToken();
                    System.out.println("\n🎉 ¡ÉXITO! TOKEN OBTENIDO:");
                    System.out.println(token);
                    System.out.println("\n➡️ Copia este token si quieres probar los parkings manualmente.");
                } else {
                    System.err.println("❌ El campo 'data' está vacío o nulo.");
                }
            } else {
                System.err.println("❌ El cuerpo de la respuesta es nulo.");
            }

        } catch (Exception e) {
            System.err.println("💥 ERROR GRAVE DURANTE EL LOGIN:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}