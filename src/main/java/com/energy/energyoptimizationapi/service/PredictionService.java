package com.energy.energyoptimizationapi.service;

import com.energy.energyoptimizationapi.model.dto.PredictionRequestDTO;
import com.energy.energyoptimizationapi.model.dto.PredictionResponseDTO;
import com.energy.energyoptimizationapi.model.dto.WeatherDTO;
import com.energy.energyoptimizationapi.model.entity.DemandHistory;
import com.energy.energyoptimizationapi.model.entity.Prediction;
import com.energy.energyoptimizationapi.repository.DemandHistoryRepository;
import com.energy.energyoptimizationapi.repository.PredictionRepository;
import com.energy.energyoptimizationapi.util.PredictionAlgorithm;
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
    private AlertService alertService;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private MobilityService mobilityService;

    @Autowired
    private RedElectricaService redElectricaService; // ¡Para comparar con la realidad!

    public PredictionResponseDTO predictDemand(PredictionRequestDTO request) {
        try {
            // --- FASE 1: RECOLECCIÓN DE DATOS (DATA GATHERING) ---

            // A. Clima (Si el usuario no lo da, lo buscamos fuera)
            Double temp = request.getTemperature();
            Double humidity = request.getHumidity();
            Double wind = request.getWindSpeed();

            if (temp == null || humidity == null) {
                System.out.println("🌍 Consultando Open-Meteo...");
                WeatherDTO weather = weatherService.getCurrentWeather();
                temp = weather.getTemperature();
                humidity = weather.getHumidity();
                wind = weather.getWindSpeed();
            }

            // B. Movilidad (EMT Parkings)
            System.out.println("🚗 Consultando EMT Parkings...");
            Double mobilityIndex = mobilityService.getRealMobilityIndex();

            // C. Demanda REAL de REE (Para validar nuestro modelo)
            System.out.println("⚡ Consultando Red Eléctrica Española...");
            Double realDemandRee = redElectricaService.getRealDemand();

            // --- FASE 2: CÁLCULO ALGORÍTMICO (AI CORE) ---

            PredictionAlgorithm algorithm = new PredictionAlgorithm();

            // Ejecutamos la fórmula matemática calibrada
            Double predictedDemand = algorithm.predictDemand(temp, humidity, mobilityIndex);

            // Calculamos el error porcentual si tenemos el dato real
            Double errorMargin = 0.0;
            if (realDemandRee > 0) {
                errorMargin = algorithm.calculateError(predictedDemand, realDemandRee);
                System.out.println("📊 Precisión del modelo: " + predictedDemand + " vs Real: " + realDemandRee + " (Error: " + String.format("%.2f", errorMargin) + "%)");
            }

            // Confianza: Si el error es bajo, la confianza es alta
            Double confidence = Math.max(0.0, 100.0 - errorMargin) / 100.0;

            // --- FASE 3: GESTIÓN DE ALERTAS Y GUARDADO ---

            Prediction prediction = new Prediction();
            prediction.setPredictedDemand(predictedDemand);
            prediction.setConfidence(confidence);
            prediction.setCreatedAt(LocalDateTime.now());
            prediction.setValidFrom(LocalDateTime.now().plusHours(1));
            prediction.setStatus("PROCESSED");

            Boolean alertTriggered = false;
            String alertMessage = "";
            String recommendation = "Demanda dentro de rangos normales.";

            // Lógica de Alertas (Umbrales de Invierno)
            if (predictedDemand > 50000) {
                // Caso Extremo
                alertService.createAlert("CRITICAL_DEMAND", "Pico extremo: " + String.format("%.0f", predictedDemand) + " MW", "CRITICAL", predictedDemand);
                prediction.setStatus("CRITICAL");
                alertTriggered = true;
                // TODO
                alertMessage = "Trabajando en ello";
                recommendation = "Simulacion simple.";
            } else if (predictedDemand > 45000) {
                // Caso Alto
                alertService.createAlert("HIGH_DEMAND", "Demanda elevada: " + String.format("%.0f", predictedDemand) + " MW", "HIGH", predictedDemand);
                prediction.setStatus("HIGH");
                alertTriggered = true;
                alertMessage = "⚠Aviso: Demanda alta detectada.";
                recommendation = "Monitorizar temperatura de transformadores y preparar reducción de carga.";
            }

            // Guardamos la predicción
            predictionRepository.save(prediction);

            // Guardamos el "momento" en el historial para reentrenar la IA en el futuro
            saveHistory(temp, humidity, wind, mobilityIndex, realDemandRee > 0 ? realDemandRee : predictedDemand);

            // --- FASE 4: RESPUESTA AL CLIENTE ---

            return new PredictionResponseDTO(
                    predictedDemand,
                    confidence,
                    recommendation,
                    alertTriggered,
                    alertMessage
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error en el motor de predicción: " + e.getMessage(), e);
        }
    }

    // Método auxiliar para guardar snapshot histórico
    private void saveHistory(Double temp, Double humidity, Double wind, Double mobility, Double demand) {
        DemandHistory history = new DemandHistory();
        history.setTemperature(temp);
        history.setHumidity(humidity);
        history.setWindSpeed(wind != null ? wind : 0.0);
        history.setMobilityIndex(mobility);
        history.setActualDemand(demand);
        history.setTimestamp(LocalDateTime.now());
        demandHistoryRepository.save(history);
    }

    public List<Prediction> getRecentPredictions() {
        return predictionRepository.findByCreatedAtAfter(LocalDateTime.now().minusHours(24));
    }
}