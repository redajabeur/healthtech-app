package com.smartearthen.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class PredictionDTO {
    private Long buildingId;
    private int horizonHours;
    private String modelType;           // "LSTM"
    private Double modelAccuracy;       // %
    private List<Map<String, Object>> temperatureForecast;
    private List<Map<String, Object>> humidityForecast;
    private String generatedAt;
}
