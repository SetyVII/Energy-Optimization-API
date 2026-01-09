package com.energy.energyoptimizationapi.service;

import com.energy.energyoptimizationapi.model.dto.WeatherDTO;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Locale;

@Service
public class WeatherService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String OPENMETEO_API = "https://api.open-meteo.com/v1/forecast";
    private static final Double MADRID_LAT = 40.4168;
    private static final Double MADRID_LON = -3.7038;

    public WeatherDTO getCurrentWeather() {
        try {
            String url = String.format(Locale.US,
                    "%s?latitude=%f&longitude=%f&current=temperature_2m,relative_humidity_2m,wind_speed_10m&timezone=auto&timeformat=unixtime",
                    OPENMETEO_API, MADRID_LAT, MADRID_LON
            );

            String response = restTemplate.getForObject(url, String.class);

            JsonObject json = JsonParser.parseString(response).getAsJsonObject();
            JsonObject current = json.getAsJsonObject("current");

            return new WeatherDTO(
                    current.get("temperature_2m").getAsDouble(),
                    current.get("relative_humidity_2m").getAsDouble(),
                    current.get("wind_speed_10m").getAsDouble(),
                    "Current weather in Madrid",
                    "Madrid",
                    current.get("time").getAsLong()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error fetching weather data: " + e.getMessage(), e);
        }
    }

    public WeatherDTO getWeatherForecast() {
        try {

            String url = String.format(Locale.US,
                    "%s?latitude=%f&longitude=%f&hourly=temperature_2m,relative_humidity_2m,wind_speed_10m&forecast_days=1&timeformat=unixtime",
                    OPENMETEO_API, MADRID_LAT, MADRID_LON
            );

            String response = restTemplate.getForObject(url, String.class);
            JsonObject json = JsonParser.parseString(response).getAsJsonObject();
            JsonObject hourly = json.getAsJsonObject("hourly");

            Double avgTemp = hourly.getAsJsonArray("temperature_2m").get(0).getAsDouble();
            Double avgHumidity = hourly.getAsJsonArray("relative_humidity_2m").get(0).getAsDouble();
            Double avgWind = hourly.getAsJsonArray("wind_speed_10m").get(0).getAsDouble();

            return new WeatherDTO(
                    avgTemp,
                    avgHumidity,
                    avgWind,
                    "24-hour forecast for Madrid",
                    "Madrid",
                    System.currentTimeMillis() / 1000
            );
        } catch (Exception e) {
            throw new RuntimeException("Error fetching forecast: " + e.getMessage(), e);
        }
    }
}