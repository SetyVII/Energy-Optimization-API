-- data.sql
-- Este script se ejecuta automáticamente al iniciar la aplicación

-- 1. Insertamos datos SOLO si la tabla está vacía
INSERT INTO demand_history (temperature, humidity, wind_speed, mobility_index, actual_demand, timestamp)
SELECT
    -- Generamos temperatura aleatoria entre 2°C y 18°C (Invierno)
    (random() * 16) + 2,

    -- Humedad entre 40% y 90%
    (random() * 50) + 40,

    -- Viento entre 0 y 40 km/h
    (random() * 40),

    -- Movilidad entre 0.1 y 1.0
    (random() * 0.9) + 0.1,

    -- Demanda simulada entre 22.000 y 35.000 MW
    22000 + (random() * 13000),

    -- Generamos fechas hacia atrás (últimos 10 días, hora a hora)
    NOW() - (i || ' hours')::interval
FROM generate_series(1, 240) AS i -- Genera 240 registros (10 días x 24 horas)
WHERE NOT EXISTS (SELECT 1 FROM demand_history LIMIT 1);

-- 2. Mensaje de confirmación en logs de la DB (Opcional)
DO $$
BEGIN
    RAISE NOTICE '✅ Seed Data: Se han generado registros históricos simulados.';
END $$;