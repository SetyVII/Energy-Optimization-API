package com.energy.energyoptimizationapi.controller;

import com.energy.energyoptimizationapi.model.dto.WeatherDTO;
import com.energy.energyoptimizationapi.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/weather")
@Tag(name = "Weather", description = "Operaciones de datos meteorológicos")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/current")
    @Operation(summary = "Obtener clima actual", description = "Obtiene datos en tiempo real de Open-Meteo")
    public ResponseEntity<WeatherDTO> getCurrentWeather() {
        return ResponseEntity.ok(weatherService.getCurrentWeather());
    }

    @GetMapping("/forecast")
    @Operation(summary = "Pronóstico 24h", description = "Obtiene la previsión para el día siguiente")
    public ResponseEntity<WeatherDTO> getForecast() {
        return ResponseEntity.ok(weatherService.getWeatherForecast());
    }
}