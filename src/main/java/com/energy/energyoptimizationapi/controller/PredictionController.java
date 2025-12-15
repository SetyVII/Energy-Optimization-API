package com.energy.energyoptimizationapi.controller;

import com.energy.energyoptimizationapi.model.dto.PredictionRequestDTO;
import com.energy.energyoptimizationapi.model.dto.PredictionResponseDTO;
import com.energy.energyoptimizationapi.service.PredictionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prediction")
@Tag(name = "Prediction", description = "Motor de IA para predicción de demanda")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @PostMapping("/demand")
    @Operation(summary = "Predecir Demanda", description = "Calcula la demanda energética futura basada en clima y movilidad")
    public ResponseEntity<PredictionResponseDTO> predictDemand(@RequestBody PredictionRequestDTO request) {
        return ResponseEntity.ok(predictionService.predictDemand(request));
    }

    @GetMapping("/recent")
    @Operation(summary = "Historial reciente", description = "Ver las últimas predicciones realizadas")
    public ResponseEntity<?> getRecentPredictions() {
        return ResponseEntity.ok(predictionService.getRecentPredictions());
    }
}