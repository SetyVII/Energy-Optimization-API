package com.energy.energyoptimizationapi.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class EmtLoginDTO {
    private String code;
    private String description;
    private List<LoginData> data;

    @Data
    public static class LoginData {
        private String accessToken;
        private String email;
        private Integer tokenSecExpiration;
    }
}