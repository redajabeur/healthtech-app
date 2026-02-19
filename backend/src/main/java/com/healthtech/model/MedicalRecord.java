package com.healthtech.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medical_records")
@Data
@NoArgsConstructor
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    private LocalDate recordDate = LocalDate.now();
    private String diagnosis;
    private String prescription;
    private String symptoms;
    private String notes;

    @ElementCollection
    @CollectionTable(name = "record_attachments", joinColumns = @JoinColumn(name = "record_id"))
    @Column(name = "attachment_url")
    private List<String> attachments = new ArrayList<>();
}
