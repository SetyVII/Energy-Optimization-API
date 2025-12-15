package com.energy.energyoptimizationapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDTO {
    private Double temperature;
    private Double humidity;
    private Double windSpeed;
    private String description;
    private String location;
    private Long timestamp;
}