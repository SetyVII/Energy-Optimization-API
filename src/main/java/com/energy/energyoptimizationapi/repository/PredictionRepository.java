package com.energy.energyoptimizationapi.repository;

import com.energy.energyoptimizationapi.model.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    // Encuentra predicciones por estado (ej: "ALERT")
    List<Prediction> findByStatus(String status);

    // Encuentra predicciones hechas después de cierta hora
    List<Prediction> findByCreatedAtAfter(LocalDateTime time);
}