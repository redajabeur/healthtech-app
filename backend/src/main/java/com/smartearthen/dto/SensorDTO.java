package com.smartearthen.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SensorDTO {
    private Long id;
    private String sensorId;
    private String type;
    private String location;
    private String zone;
    private String mqttTopic;
    private String status;
    private Double batteryLevel;
    private LocalDateTime lastSeen;
    private Long buildingId;
    private String buildingName;
    private SensorReadingDTO latestReading;
}

