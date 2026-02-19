package com.smartearthen.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class SensorReadingDTO {
    private Long id;
    private String sensorId;
    private LocalDateTime recordedAt;
    private Double temperature;
    private Double relativeHumidity;
    private Double absoluteHumidity;
    private Double dewPoint;
    private Double co2Level;
    private Double pressure;
    private Double materialMoisture;
    private Integer rssi;
}

