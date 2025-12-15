package com.energy.energyoptimizationapi.service;

import com.energy.energyoptimizationapi.model.dto.PredictionRequestDTO;
import com.energy.energyoptimizationapi.model.dto.PredictionResponseDTO;
import com.energy.energyoptimizationapi.model.entity.DemandHistory;
import com.energy.energyoptimizationapi.model.entity.Prediction;
import com.energy.energyoptimizationapi.repository.DemandHistoryRepository;
import com.energy.energyoptimizationapi.repository.PredictionRepository;
import com.energy.energyoptimizationapi.util.PredictionAlghorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PredictionService {

    @Autowired
    private DemandHistoryRepository demandHistoryRepository;

    @Autowired
    private PredictionRepository predictionRepository;

    @Autowired
    private AlertService alertService; // Usamos el servicio de alertas que creaste antes

    public PredictionResponseDTO predictDemand(PredictionRequestDTO request) {
        try {
            // 1. Obtener datos históricos para "entrenar" (simulado)
            List<DemandHistory> historicalData = demandHistoryRepository.findTop100ByOrderByTimestampDesc();

            // 2. Usar el algoritmo
            PredictionAlghorithm algorithm = new PredictionAlghorithm();
            Double predictedDemand = algorithm.predictLinearRegression(
                    request.getTemperature(),
                    request.getHumidity(),
                    request.getWindSpeed(),
                    request.getMobilityIndex(),
                    historicalData
            );

            // 3. Calcular confianza (simulada)
            Double confidence = 0.85; // 85% de confianza

            // 4. Guardar la predicción en base de datos
            Prediction prediction = new Prediction();
            prediction.setPredictedDemand(predictedDemand);
            prediction.setConfidence(confidence);
            prediction.setCreatedAt(LocalDateTime.now());
            prediction.setValidFrom(LocalDateTime.now().plusHours(1));
            prediction.setStatus("PENDING");

            // 5. Lógica de Alertas y Recomendaciones
            Boolean alertTriggered = false;
            String alertMessage = "";
            String recommendation = "Mantener operación normal.";

            if (predictedDemand > 1200) {
                // CASO CRÍTICO
                alertService.createAlert("HIGH_DEMAND", "Pico crítico previsto: " + predictedDemand + " MW", "CRITICAL", predictedDemand);
                prediction.setStatus("ALERT");
                alertTriggered = true;
                alertMessage = "⚠️ ALERTA CRÍTICA: Se espera un consumo muy elevado.";
                recommendation = "🚨 URGENTE: Activar generadores de respaldo y reducir carga no esencial.";
            } else if (predictedDemand > 1000) {
                // CASO ALTO
                alertService.createAlert("HIGH_DEMAND", "Demanda elevada: " + predictedDemand + " MW", "HIGH", predictedDemand);
                alertTriggered = true;
                alertMessage = "⚠️ ALERTA: Consumo elevado previsto.";
                recommendation = "⚠️ Sugerencia: Desplazar procesos industriales a horario valle.";
            }

            // Guardamos finalmente
            predictionRepository.save(prediction);

            // 6. Devolver respuesta al usuario
            return new PredictionResponseDTO(
                    predictedDemand,
                    confidence,
                    recommendation,
                    alertTriggered,
                    alertMessage
            );
        } catch (Exception e) {
            throw new RuntimeException("Error during prediction: " + e.getMessage(), e);
        }
    }

    public List<Prediction> getRecentPredictions() {
        return predictionRepository.findByCreatedAtAfter(LocalDateTime.now().minusHours(24));
    }
}