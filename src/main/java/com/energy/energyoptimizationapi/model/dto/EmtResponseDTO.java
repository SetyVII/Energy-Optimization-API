package com.energy.energyoptimizationapi.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class EmtResponseDTO {
    private String code;
    private String description;
    private DataContainer data;

    @Data
    public static class DataContainer {
        // Para la respuesta de Login
        private String accessToken;

        // Para la respuesta de Autobuses (llega una lista)
        private List<Arrive> arrive;
    }

    @Data
    public static class Arrive {
        private String line;        // Número de línea (ej: "27")
        private String destination; // Destino
        private Integer estimateArrive; // Tiempo en segundos
        private Double DistanceBus;     // Distancia en metros
    }
}