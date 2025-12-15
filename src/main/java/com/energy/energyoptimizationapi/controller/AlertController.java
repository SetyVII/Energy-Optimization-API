package com.energy.energyoptimizationapi.controller;

import com.energy.energyoptimizationapi.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/alerts")
@Tag(name = "Alerts", description = "Sistema de monitoreo y alertas")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @GetMapping
    @Operation(summary = "Alertas Activas", description = "Lista todas las alertas no resueltas")
    public ResponseEntity<?> getActiveAlerts() {
        return ResponseEntity.ok(alertService.getActiveAlerts());
    }

    @PutMapping("/{id}/resolve")
    @Operation(summary = "Resolver Alerta", description = "Marca una alerta como atendida")
    public ResponseEntity<?> resolveAlert(@PathVariable Long id) {
        return ResponseEntity.ok(alertService.resolveAlert(id));
    }
}