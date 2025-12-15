package com.energy.energyoptimizationapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EnergyOptimizationApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnergyOptimizationApiApplication.class, args);
    }

}
