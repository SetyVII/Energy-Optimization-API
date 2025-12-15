package com.energy.energyoptimizationapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResponseDTO {
    private Double predictedDemand;
    private Double confidence;
    private String recommendation;
    private Boolean alertTriggered;
    private String alertMessage;
}