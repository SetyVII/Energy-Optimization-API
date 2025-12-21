package com.energy.energyoptimizationapi.util;

public class PredictionAlgorithm {

    /**
     * Calcula la demanda basándose en Factores Ambientales y Sociales.
     * Objetivo: Acercarse a la realidad (aprox 30k-35k MW en noches de invierno)
     */
    public Double predictDemand(Double temperature, Double humidity, Double mobilityIndex) {

        // 1. DEMANDA BASE ESTRUCTURAL
        // El consumo mínimo de España "si nadie hiciera nada" (neveras, servidores, alumbrado, hospitales)
        // En invierno sube por la ineficiencia térmica de los edificios.
        double baseDemand = 24500.0;

        // 2. FACTOR CLIMÁTICO (El más importante hoy)
        // Temperatura de confort: 21°C.
        double tempDifference = 21.0 - temperature; // Si hace 6°C, la diferencia es 15.

        double weatherImpact = 0;

        // Si hace frío (menos de 18°C), la calefacción dispara el consumo exponencialmente
        if (temperature < 18) {
            // Por cada grado que baja, sumamos 450 MW de calefacción nacional
            // Ejemplo hoy (6°C): 15 * 450 = +6,750 MW extra
            weatherImpact = tempDifference * 450;

            // Bonus por humedad: El frío húmedo se siente más -> la gente sube más la calefacción
            if (humidity > 70) {
                weatherImpact += 500;
            }
        }
        // Si hace calor extremo (verano)
        else if (temperature > 26) {
            weatherImpact = Math.abs(temperature - 26) * 600; // El aire acondicionado gasta mucho
        }

        // 3. FACTOR MOVILIDAD (Actividad Económica)
        // mobilityIndex va de 0.0 (Noche/Fiesta) a 1.0 (Hora Punta Laboral)
        // La industria y oficinas pueden sumar hasta 5000 MW extra en horario laboral.
        // Hoy (Reyes, noche) el índice será bajo (~0.15), sumando poco.
        double mobilityImpact = mobilityIndex * 5000;

        // 4. CÁLCULO FINAL
        double predictedMw = baseDemand + weatherImpact + mobilityImpact;

        // Simulamos una pequeña fluctuación aleatoria natural de la red (+- 200 MW)
        predictedMw += (Math.random() * 400) - 200;

        return predictedMw;
    }

    // Método para medir qué tan buenos somos comparados con REE
    public Double calculateError(Double predicted, Double realFromRee) {
        if (realFromRee == null || realFromRee == 0) return 0.0;
        return Math.abs(predicted - realFromRee) / realFromRee * 100; // % de error
    }
}