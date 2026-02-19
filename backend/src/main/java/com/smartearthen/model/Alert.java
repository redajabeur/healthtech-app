package com.smartearthen.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Data
@NoArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @Enumerated(EnumType.STRING)
    private AlertType type; // TEMPERATURE_HIGH, HUMIDITY_CRITICAL, BATTERY_LOW, ANOMALY_AI, STRUCTURAL

    @Enumerated(EnumType.STRING)
    private AlertSeverity severity; // LOW, MEDIUM, HIGH, CRITICAL

    private String title;
    private String description;
    private String recommendation;

    private Double triggerValue;
    private Double thresholdValue;

    private LocalDateTime triggeredAt;
    private LocalDateTime acknowledgedAt;
    private String acknowledgedBy;

    private boolean active = true;
    private boolean aiGenerated = false; // true si générée par IA

    // Score d'anomalie IA (0-1)
    private Double anomalyScore;
}
