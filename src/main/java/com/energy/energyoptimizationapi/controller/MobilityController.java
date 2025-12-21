package com.energy.energyoptimizationapi.controller;

import com.energy.energyoptimizationapi.service.MobilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mobility")
@Tag(name = "Mobility (EMT)", description = "Datos de movilidad y transporte público (EMT Madrid)")
public class MobilityController {

    @Autowired
    private MobilityService mobilityService;

    @GetMapping("/bus/{stopId}")
    @Operation(summary = "📅 Tiempo de llegada Autobús", description = "Consulta a la API real de la EMT cuánto falta para el bus")
    public ResponseEntity<Map<String, String>> getNextBus(@PathVariable int stopId) {
        // Llamamos al servicio que acabamos de configurar con tus claves
        String resultado = mobilityService.getNextBusInStop(stopId);

        Map<String, String> response = new HashMap<>();
        response.put("stopId", String.valueOf(stopId));
        response.put("info", resultado);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/index")
    @Operation(summary = "🚦 Índice de Movilidad", description = "Simulación del nivel de tráfico actual (0.0 a 1.0)")
    public ResponseEntity<Map<String, Object>> getMobilityIndex() {
        Map<String, Object> response = new HashMap<>();
        response.put("mobilityIndex", mobilityService.getMobilityIndex());
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}