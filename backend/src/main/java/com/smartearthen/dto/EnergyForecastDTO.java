package com.smartearthen.dto;

import lombok.Data;

@Data
public class EnergyForecastDTO {
    private Long buildingId;
    private Double predictedConsumptionKwh;
    private Double baselineConsumptionKwh;
    private Double savingsKwh;
    private Double savingsPercent;
    private Double savingsCostMAD;
    private Double co2ReductionKg;
    private String period;
    private String recommendation;
}
