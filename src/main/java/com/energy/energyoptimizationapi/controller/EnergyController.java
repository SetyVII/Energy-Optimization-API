package com.energy.energyoptimizationapi.controller;

import com.energy.energyoptimizationapi.service.RedElectricaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/grid")
@Tag(name = "National Grid (REE)", description = "Datos de la Red Eléctrica Española")
public class EnergyController {

    @Autowired
    private RedElectricaService redElectricaService;

    @GetMapping("/demand/current")
    @Operation(summary = "Demanda Real España", description = "Obtiene la demanda eléctrica actual desde ESIOS/REE")
    public ResponseEntity<Map<String, Object>> getRealDemand() {
        Double demand = redElectricaService.getRealDemand();

        Map<String, Object> response = new HashMap<>();
        response.put("source", "Red Eléctrica de España (ESIOS)");
        response.put("actualDemandMW", demand);
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }
}