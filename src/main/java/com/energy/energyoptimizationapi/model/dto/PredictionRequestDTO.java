package com.energy.energyoptimizationapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionRequestDTO {
    private Double temperature;
    private Double humidity;
    private Double windSpeed;
    private Double mobilityIndex;
}