package com.energy.energyoptimizationapi.service;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class MobilityService {

    private Random random = new Random();

    public Double getMobilityIndex() {
        // Simulación: Devuelve un valor entre 0.3 y 1.0 dependiendo de la hora
        int hour = java.time.LocalDateTime.now().getHour();

        // Picos de tráfico: 7-9am, 12-14pm, 17-19pm
        if ((hour >= 7 && hour <= 9) || (hour >= 12 && hour <= 14) || (hour >= 17 && hour <= 19)) {
            return 0.7 + (random.nextDouble() * 0.3);
        }
        return 0.3 + (random.nextDouble() * 0.4);
    }

    public String getNextBusInStop(int stopId) {
        return String.format("Bus %d arrives in %d minutes at stop %d",
                random.nextInt(100) + 1, random.nextInt(15) + 1, stopId);
    }

    public Double estimateDemandFromMobility(Double mobilityIndex) {
        // A mayor movilidad, mayor consumo estimado
        return 500 + (mobilityIndex * 300);
    }
}