package com.energy.energyoptimizationapi.controller;

import com.energy.energyoptimizationapi.model.dto.CarbonFootprintDTO;
import com.energy.energyoptimizationapi.service.CarbonFootprintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carbon")
@Tag(name = "Carbon Footprint", description = "Cálculo de huella de carbono")
public class CarbonFootprintController {

    @Autowired
    private CarbonFootprintService carbonService;

    @PostMapping("/energy")
    @Operation(summary = "Huella Energética", description = "Calcula CO2 basado en consumo MWh")
    public ResponseEntity<CarbonFootprintDTO> calculateEnergyEmissions(@RequestParam Double energyMWh) {
        return ResponseEntity.ok(carbonService.calculateEnergyEmissions(energyMWh));
    }
}