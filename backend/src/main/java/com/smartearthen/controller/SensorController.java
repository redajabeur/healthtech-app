package com.smartearthen.controller;

import com.smartearthen.dto.SensorDTO;
import com.smartearthen.dto.SensorReadingDTO;
import com.smartearthen.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SensorController {

    private final SensorService sensorService;

    @GetMapping
    public ResponseEntity<List<SensorDTO>> getAll() {
        return ResponseEntity.ok(sensorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sensorService.findById(id));
    }

    @GetMapping("/building/{buildingId}")
    public ResponseEntity<List<SensorDTO>> getByBuilding(@PathVariable Long buildingId) {
        return ResponseEntity.ok(sensorService.findByBuilding(buildingId));
    }

    @PostMapping
    public ResponseEntity<SensorDTO> create(@RequestBody SensorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sensorService.create(dto));
    }

    @GetMapping("/{id}/readings")
    public ResponseEntity<List<SensorReadingDTO>> getReadings(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(sensorService.getReadings(id, from, to));
    }

    @GetMapping("/{id}/latest")
    public ResponseEntity<SensorReadingDTO> getLatestReading(@PathVariable Long id) {
        return ResponseEntity.ok(sensorService.getLatestReading(id));
    }

    @GetMapping("/status/summary")
    public ResponseEntity<?> getStatusSummary() {
        return ResponseEntity.ok(sensorService.getStatusSummary());
    }
}
