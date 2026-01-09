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
    private RedElectricaService redElectricaService;

    public PredictionResponseDTO predictDemand(PredictionRequestDTO request) {
        try {



            Double temp = request.getTemperature();
            Double humidity = request.getHumidity();
            Double wind = request.getWindSpeed();

            if (temp == null || humidity == null) {

                WeatherDTO weather = weatherService.getCurrentWeather();
                temp = weather.getTemperature();
                humidity = weather.getHumidity();
                wind = weather.getWindSpeed();
            }


            Double mobilityIndex = mobilityService.getRealMobilityIndex();


            Double realDemandRee = redElectricaService.getRealDemand();



            PredictionAlgorithm algorithm = new PredictionAlgorithm();


            Double predictedDemand = algorithm.predictDemand(temp, humidity, mobilityIndex);


            Double errorMargin = 0.0;
            if (realDemandRee > 0) {
                errorMargin = algorithm.calculateError(predictedDemand, realDemandRee);
            }


            Double confidence = Math.max(0.0, 100.0 - errorMargin) / 100.0;



            Prediction prediction = new Prediction();
            prediction.setPredictedDemand(predictedDemand);
            prediction.setConfidence(confidence);
            prediction.setCreatedAt(LocalDateTime.now());
            prediction.setValidFrom(LocalDateTime.now().plusHours(1));
            prediction.setStatus("PROCESSED");

            Boolean alertTriggered = false;
            String alertMessage = "";
            String recommendation = "Demanda dentro de rangos normales.";


            if (predictedDemand > 50000) {

                alertService.createAlert("CRITICAL_DEMAND", "Pico extremo: " + String.format("%.0f", predictedDemand) + " MW", "CRITICAL", predictedDemand);
                prediction.setStatus("CRITICAL");
                alertTriggered = true;
                // TODO
                alertMessage = "Trabajando en ello";
                // TODO: Integration with SendGrid or Twilio for SMS alerts
                recommendation = "Simulacion simple.";
            } else if (predictedDemand > 45000) {

                alertService.createAlert("HIGH_DEMAND", "Demanda elevada: " + String.format("%.0f", predictedDemand) + " MW", "HIGH", predictedDemand);
                prediction.setStatus("HIGH");
                alertTriggered = true;
                alertMessage = "⚠Aviso: Demanda alta detectada.";
                recommendation = "Monitorizar temperatura de transformadores y preparar reducción de carga.";
            }


            predictionRepository.save(prediction);


            saveHistory(temp, humidity, wind, mobilityIndex, realDemandRee > 0 ? realDemandRee : predictedDemand);



            return new PredictionResponseDTO(
                    predictedDemand,
                    confidence,
                    recommendation,
                    alertTriggered,
                    alertMessage
            );

        } catch (Exception e) {
            // TODO: Implement structured logging (SLF4J) and error reporting
            e.printStackTrace();
            throw new RuntimeException("Error en el motor de predicción: " + e.getMessage(), e);
        }
    }


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