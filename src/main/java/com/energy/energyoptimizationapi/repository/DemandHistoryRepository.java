package com.energy.energyoptimizationapi.repository;

import com.energy.energyoptimizationapi.model.entity.DemandHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DemandHistoryRepository extends JpaRepository<DemandHistory, Long> {

    List<DemandHistory> findByTimestampBetween(LocalDateTime start, LocalDateTime end);


    List<DemandHistory> findTop100ByOrderByTimestampDesc();
}