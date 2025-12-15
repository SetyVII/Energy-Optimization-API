package com.energy.energyoptimizationapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "demand_history")
@Data // Lombok genera getters, setters y toString automáticamente
@NoArgsConstructor
@AllArgsConstructor
public class DemandHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private Double humidity;

    @Column(nullable = false)
    private Double windSpeed;

    @Column(nullable = false)
    private Double mobilityIndex;

    @Column(nullable = false)
    private Double actualDemand; // El consumo real en MW

    @Column(nullable = false)
    private LocalDateTime timestamp;
}