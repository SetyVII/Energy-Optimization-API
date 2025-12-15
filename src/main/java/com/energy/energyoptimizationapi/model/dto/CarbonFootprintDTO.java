package com.energy.energyoptimizationapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarbonFootprintDTO {
    private Double co2Equivalent;
    private String unit; // "kg", "tons"
    private String activity;
    private String recommendation;
}