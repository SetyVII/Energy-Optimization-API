package com.energy.energyoptimizationapi.service;

import com.energy.energyoptimizationapi.model.entity.Alert;
import com.energy.energyoptimizationapi.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public Alert createAlert(String type, String message, String severity, Double triggerValue) {
        Alert alert = new Alert();
        alert.setType(type);
        alert.setMessage(message);
        alert.setSeverity(severity);
        alert.setTriggerValue(triggerValue);
        alert.setTimestamp(LocalDateTime.now());
        alert.setResolved(false);
        return alertRepository.save(alert);
    }

    public List<Alert> getActiveAlerts() {
        return alertRepository.findByResolvedFalseOrderByTimestampDesc();
    }

    public List<Alert> getAlertsByType(String type) {
        return alertRepository.findByTypeAndTimestampAfter(type, LocalDateTime.now().minusHours(24));
    }

    public Alert resolveAlert(Long alertId) {
        Alert alert = alertRepository.findById(alertId).orElseThrow();
        alert.setResolved(true);
        return alertRepository.save(alert);
    }
}