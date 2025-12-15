package com.energy.energyoptimizationapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // Ej: "HIGH_DEMAND", "HIGH_POLLUTION"

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String severity; // "LOW", "MEDIUM", "HIGH", "CRITICAL"

    @Column(nullable = false)
    private Double triggerValue; // El valor que disparó la alerta

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Boolean resolved; // Si ya se atendió la alerta
}