package com.smartearthen.controller;

import com.smartearthen.dto.*;
import com.smartearthen.service.AIPredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AIPredictionController {

    private final AIPredictionService aiService;

    /** Score de confort hygrothermique 0-100 (modèle CNN) */
    @GetMapping("/comfort-score/{buildingId}")
    public ResponseEntity<ComfortScoreDTO> getComfortScore(@PathVariable Long buildingId) {
        return ResponseEntity.ok(aiService.computeComfortScore(buildingId));
    }

    /** Prédictions température et humidité sur 48h (modèle LSTM) */
    @GetMapping("/predictions/{buildingId}")
    public ResponseEntity<PredictionDTO> getPredictions(
            @PathVariable Long buildingId,
            @RequestParam(defaultValue = "48") int horizonHours) {
        return ResponseEntity.ok(aiService.predict(buildingId, horizonHours));
    }

    /** Détection d'anomalies en temps réel (Isolation Forest) */
    @GetMapping("/anomalies")
    public ResponseEntity<?> getAnomalies(
            @RequestParam(required = false) Long buildingId) {
        return ResponseEntity.ok(aiService.getActiveAnomalies(buildingId));
    }

    /** Prévision énergétique et économies estimées */
    @GetMapping("/energy-forecast/{buildingId}")
    public ResponseEntity<EnergyForecastDTO> getEnergyForecast(@PathVariable Long buildingId) {
        return ResponseEntity.ok(aiService.forecastEnergy(buildingId));
    }

    /** Classification état matériau (Random Forest) */
    @GetMapping("/material-status/{sensorId}")
    public ResponseEntity<MaterialStatusDTO> getMaterialStatus(@PathVariable Long sensorId) {
        return ResponseEntity.ok(aiService.classifyMaterialStatus(sensorId));
    }
}
