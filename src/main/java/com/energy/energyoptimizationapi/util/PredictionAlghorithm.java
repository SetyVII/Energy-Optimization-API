package com.energy.energyoptimizationapi.util;

import com.energy.energyoptimizationapi.model.entity.DemandHistory;
import java.util.List;

public class PredictionAlghorithm {

    /**
     * Regresión lineal simple: Demand = a + b1*Temp + b2*Humidity + b3*Wind + b4*Mobility
     */
    public Double predictLinearRegression(
            Double temperature,
            Double humidity,
            Double windSpeed,
            Double mobilityIndex,
            List<DemandHistory> historicalData) {

        // Si no tenemos datos históricos, devolvemos una base segura
        if (historicalData.isEmpty()) {
            return 700.0;
        }

        // Coeficientes (en un sistema real, estos se calcularían analizando los datos históricos)
        Double intercept = 500.0; // Consumo base
        Double tempCoeff = -8.0;  // Si hace más calor, baja la calefacción (invierno)
        Double humidityCoeff = 0.5;
        Double windCoeff = -5.0;  // El viento puede enfriar edificios
        Double mobilityCoeff = 300.0; // Más gente moviéndose = más consumo

        Double prediction = intercept
                + (tempCoeff * temperature)
                + (humidityCoeff * humidity)
                + (windCoeff * windSpeed)
                + (mobilityCoeff * mobilityIndex);

        // Aseguramos que el resultado esté en un rango realista (ej: entre 300 y 1500 MW)
        return Math.max(300.0, Math.min(1500.0, prediction));
    }
}