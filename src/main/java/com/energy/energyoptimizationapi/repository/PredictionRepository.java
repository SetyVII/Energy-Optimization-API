package com.energy.energyoptimizationapi.repository;

import com.energy.energyoptimizationapi.model.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    List<Prediction> findByStatus(String status);


    List<Prediction> findByCreatedAtAfter(LocalDateTime time);
}