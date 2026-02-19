package com.healthtech.dto;

import com.healthtech.model.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientDTO {
    private Long id;

    @NotBlank(message = "Le prénom est requis")
    private String firstName;

    @NotBlank(message = "Le nom est requis")
    private String lastName;

    private String cin;
    private LocalDate dateOfBirth;
    private String bloodType;
    private String phoneNumber;
    private String address;

    @NotNull(message = "Le genre est requis")
    private Gender gender;
}
