package com.energy.energyoptimizationapi.service;

import com.energy.energyoptimizationapi.model.dto.CarbonFootprintDTO;
import org.springframework.stereotype.Service;

@Service
public class CarbonFootprintService {

    public CarbonFootprintDTO calculateEnergyEmissions(Double energyMWh) {
        Double emissionsFactor = 0.23; // Factor de emisión en España (kg CO2/kWh) aprox.
        Double totalTons = (energyMWh * 1000 * emissionsFactor) / 1000;

        String recommendation = totalTons > 100
                ? "Consider renewable energy sources"
                : "Good energy efficiency level";

        return new CarbonFootprintDTO(totalTons, "tons", "Energy consumption", recommendation);
    }

    public CarbonFootprintDTO calculateTransportEmissions(Double passengerKm) {
        Double emissionsFactor = 0.089; // Factor autobús
        Double totalTons = (passengerKm * emissionsFactor) / 1000;

        return new CarbonFootprintDTO(totalTons, "tons", "Public transport", "Low emissions");
    }

    public CarbonFootprintDTO getOverallFootprint(Double energyMWh, Double mobilityIndex) {
        Double energyEmissions = calculateEnergyEmissions(energyMWh).getCo2Equivalent();
        Double mobilityEmissions = calculateTransportEmissions(mobilityIndex * 500).getCo2Equivalent();

        return new CarbonFootprintDTO(
                energyEmissions + mobilityEmissions,
                "tons",
                "Combined",
                "Focus on renewable energy adoption"
        );
    }
}