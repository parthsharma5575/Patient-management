package com.pm.patientservice.dto;

import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PatientRequestDTO(
        @NotBlank(message = "Name cannot be blank")
        @Size(max = 100,message = "Name cannot exceed 100 characters")
        String name,
        @NotBlank(message = "Email cannot be blank")
        String email,
        @NotBlank(message = "Address cannot be blank")
        String address,
        @NotBlank(message = "Date of Birth cannot be blank")
        String dateOfBirth,

        @NotBlank(groups = CreatePatientValidationGroup.class,message = "Registered Date cannot be blank")
        String registeredDate
) {
}
