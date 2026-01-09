package com.energy.energyoptimizationapi.util;

public class PredictionAlgorithm {


    public Double predictDemand(Double temperature, Double humidity, Double mobilityIndex) {



        // TODO: Externalize this configuration to a database or properties file
        double baseDemand = 24500.0;


        double tempDifference = 21.0 - temperature;

        double weatherImpact = 0;


        if (temperature < 18) {
            // TODO: Refine this heuristic with historical regression data
            weatherImpact = tempDifference * 450;


            if (humidity > 70) {
                weatherImpact += 500;
            }
        else if (temperature > 26) {
            weatherImpact = Math.abs(temperature - 26) * 600;
        }


        double mobilityImpact = mobilityIndex * 5000;


        double predictedMw = baseDemand + weatherImpact + mobilityImpact;


        predictedMw += (Math.random() * 400) - 200;

        return predictedMw;
    }


    public Double calculateError(Double predicted, Double realFromRee) {
        if (realFromRee == null || realFromRee == 0) return 0.0;
        return Math.abs(predicted - realFromRee) / realFromRee * 100;
    }
}