package com.smartearthen.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ComfortScoreDTO {
    private Long buildingId;
    private Double overallScore;        // 0-100
    private Double temperatureScore;
    private Double humidityScore;
    private Double co2Score;
    private String comfortLevel;        // EXCELLENT, GOOD, ACCEPTABLE, POOR, CRITICAL
    private String recommendation;
    private Map<String, Double> zoneScores;
}

@Data
class PredictionPoint {
    private String timestamp;
    private Double temperature;
    private Double humidity;
    private Double confidence;
}

