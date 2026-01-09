package com.energy.energyoptimizationapi.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class RedElectricaResponseDTO {
    private Indicator indicator;

    @Data
    public static class Indicator {
        private String name;
        private String short_name;
        private List<Value> values;
    }

    @Data
    public static class Value {
        private Double value;
        private String datetime;
        private String tz_time;
    }
}