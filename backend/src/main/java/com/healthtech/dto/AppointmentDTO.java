package com.healthtech.dto;

import com.healthtech.model.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDTO {
    private Long id;

    @NotNull(message = "L'ID du patient est requis")
    private Long patientId;

    @NotNull(message = "L'ID du médecin est requis")
    private Long doctorId;

    // Champs enrichis pour l'affichage
    private String patientName;
    private String doctorName;
    private String doctorSpeciality;

    @NotNull(message = "La date du rendez-vous est requise")
    @Future(message = "La date doit être dans le futur")
    private LocalDateTime appointmentDate;

    private String reason;
    private AppointmentStatus status;
    private String notes;
    private LocalDateTime createdAt;
}
