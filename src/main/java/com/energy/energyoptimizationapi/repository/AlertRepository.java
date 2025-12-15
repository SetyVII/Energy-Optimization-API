package com.energy.energyoptimizationapi.repository;

import com.energy.energyoptimizationapi.model.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    // Busca todas las alertas NO resueltas, ordenadas por fecha
    List<Alert> findByResolvedFalseOrderByTimestampDesc();

    // Busca alertas de un tipo específico recientes
    List<Alert> findByTypeAndTimestampAfter(String type, LocalDateTime time);
}