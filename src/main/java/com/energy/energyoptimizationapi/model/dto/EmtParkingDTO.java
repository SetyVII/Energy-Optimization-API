package com.energy.energyoptimizationapi.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class EmtParkingDTO {
    private String code;
    private String description;
    private List<ParkingItem> data;

    @Data
    public static class ParkingItem {
        private Integer id;
        private String name;
        private Integer freeParking; // Este es el dato clave (puede ser null)
        private Boolean isEmtPark;
    }
}