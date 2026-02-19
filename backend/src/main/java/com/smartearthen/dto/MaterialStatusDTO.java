package com.smartearthen.dto;

import lombok.Data;

@Data
public class MaterialStatusDTO {
    private Long sensorId;
    private String materialType;
    private Double moistureContent;
    private String status;           // NOMINAL, ELEVATED, CRITICAL
    private Double degradationRisk;  // 0-1
    private String intervention;
}
