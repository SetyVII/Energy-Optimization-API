package com.energy.energyoptimizationapi.service;

import com.energy.energyoptimizationapi.model.dto.WeatherDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // Carga todo el contexto de Spring (como si arrancaras la app)
public class WeatherServiceIntegrationTest {

    @Autowired
    private WeatherService weatherService;

    @Test
    public void testConexionRealOpenMeteo() {
        System.out.println("⏳ Conectando con Open-Meteo...");

        WeatherDTO clima = weatherService.getCurrentWeather();

        // VibeCodeado numero 1
        System.out.println("------------------------------------------------");
        System.out.println("✅ ¡CONEXIÓN EXITOSA!");
        System.out.println("📍 Ubicación: " + clima.getLocation());
        System.out.println("🌡️ Temperatura: " + clima.getTemperature() + "°C");
        System.out.println("💧 Humedad: " + clima.getHumidity() + "%");
        System.out.println("💨 Viento: " + clima.getWindSpeed() + " km/h");
        System.out.println("------------------------------------------------");

        assertNotNull(clima);
        assertNotNull(clima.getTemperature());
    }
}