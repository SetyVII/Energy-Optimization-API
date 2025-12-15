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
@Tag(name = "Mobility", description = "Datos de movilidad urbana")
public class MobilityController {

    @Autowired
    private MobilityService mobilityService;

    @GetMapping("/index")
    @Operation(summary = "Índice de Movilidad", description = "Nivel de tráfico actual (0.0 a 1.0)")
    public ResponseEntity<Map<String, Object>> getMobilityIndex() {
        Map<String, Object> response = new HashMap<>();
        response.put("mobilityIndex", mobilityService.getMobilityIndex());
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}