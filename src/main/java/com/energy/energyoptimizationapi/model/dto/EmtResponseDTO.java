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

        private String accessToken;


        private List<Arrive> arrive;
    }

    @Data
    public static class Arrive {
        private String line;
        private String destination;
        private Integer estimateArrive;
        private Double DistanceBus;
    }
}