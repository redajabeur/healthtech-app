package com.healthtech.controller;

import com.healthtech.dto.PatientDTO;
import com.healthtech.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAll() {
        return ResponseEntity.ok(patientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PatientDTO> create(@Valid @RequestBody PatientDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> update(@PathVariable Long id,
                                              @Valid @RequestBody PatientDTO dto) {
        return ResponseEntity.ok(patientService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        return ResponseEntity.ok(patientService.getCount());
    }

    @GetMapping("/search")
    public ResponseEntity<List<PatientDTO>> search(@RequestParam String query) {
        return ResponseEntity.ok(patientService.search(query));
    }
}
