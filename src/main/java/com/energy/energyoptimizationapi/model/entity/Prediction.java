package com.energy.energyoptimizationapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "predictions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double predictedDemand; // La demanda calculada

    @Column(nullable = false)
    private Double confidence; // Qué tan seguro está el algoritmo (0.0 a 1.0)

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime validFrom; // Para cuándo es esta predicción

    @Column(nullable = false)
    private String status; // Ej: "PENDING", "CONFIRMED", "ALERT"
}