package com.smartearthen.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sensors")
@Data
@NoArgsConstructor
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sensorId; // ex: "SEB-01"

    @Enumerated(EnumType.STRING)
    private SensorType type; // DHT22, BME280, SHT31, BME680, DS18B20

    private String location;     // "Salon", "Cuisine", "Mur Nord"
    private String zone;         // "INT_LIVING", "INT_KITCHEN", "EXT", "WALL_N"
    private String mqttTopic;    // "earthen/batA/sensors/SEB-01"

    private Double wallDepthCm;   // profondeur dans le mur si capteur mural
    private String floor;         // "RDC", "R+1"

    @Enumerated(EnumType.STRING)
    private SensorStatus status; // ONLINE, OFFLINE, LOW_BATTERY, FAULTY

    private Double batteryLevel;  // 0-100%
    private LocalDateTime lastSeen;
    private LocalDateTime installedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL)
    private List<SensorReading> readings = new ArrayList<>();
}
