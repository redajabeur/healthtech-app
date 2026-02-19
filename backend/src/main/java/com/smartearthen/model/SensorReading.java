package com.smartearthen.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_readings", indexes = {
    @Index(name = "idx_reading_sensor_time", columnList = "sensor_id, recorded_at"),
    @Index(name = "idx_reading_time", columnList = "recorded_at")
})
@Data
@NoArgsConstructor
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    // Hygrothermiques
    private Double temperature;       // °C
    private Double relativeHumidity;  // %
    private Double absoluteHumidity;  // g/m³
    private Double dewPoint;          // °C

    // Qualité air
    private Double co2Level;          // ppm
    private Double vocIndex;          // COV index
    private Double pressure;          // hPa

    // Matériau (capteurs dans le mur)
    private Double materialMoisture;  // % masse sèche
    private Double thermalFlux;       // W/m²

    // Structurel (accéléromètre)
    private Double vibrationLevel;    // mm/s

    // MQTT metadata
    private String rawPayload;
    private Integer rssi;             // Signal WiFi dBm
}
