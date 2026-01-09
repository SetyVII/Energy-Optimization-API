-- data.sql
-- Este script inserta datos simulados si la tabla está vacía

INSERT INTO demand_history (temperature, humidity, wind_speed, mobility_index, actual_demand, timestamp)
SELECT
    (random() * 16) + 2,   -- Temp 2-18
    (random() * 50) + 40,  -- Humedad 40-90
    (random() * 40),       -- Viento
    (random() * 0.9) + 0.1,-- Movilidad
    22000 + (random() * 13000), -- Demanda
    NOW() - (i || ' hours')::interval
FROM generate_series(1, 240) AS i
WHERE NOT EXISTS (SELECT 1 FROM demand_history LIMIT 1);