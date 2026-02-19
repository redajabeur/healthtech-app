package com.healthtech.controller;

import com.healthtech.dto.AppointmentDTO;
import com.healthtech.model.AppointmentStatus;
import com.healthtech.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAll() {
        return ResponseEntity.ok(appointmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.findById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.findByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.findByDoctorId(doctorId));
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> create(@Valid @RequestBody AppointmentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> update(@PathVariable Long id,
                                                  @Valid @RequestBody AppointmentDTO dto) {
        return ResponseEntity.ok(appointmentService.update(id, dto));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AppointmentDTO> updateStatus(@PathVariable Long id,
                                                        @RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/today/count")
    public ResponseEntity<Long> getTodayCount() {
        return ResponseEntity.ok(appointmentService.getTodayCount());
    }
}
