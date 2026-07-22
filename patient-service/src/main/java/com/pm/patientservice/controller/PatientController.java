package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import com.pm.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patient", description = "Patient API")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(summary = "Get all patients")
    @RequestMapping
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients(){
        List<PatientResponseDTO>patients=patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @Operation(summary = "Create a new patient")
    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO){
        PatientResponseDTO patientResponseDTO=patientService.savePatient(patientRequestDTO);
        return ResponseEntity.ok(patientResponseDTO);
    }

    @Operation(summary = "Update an existing patient")
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable @Valid UUID id, @RequestBody @Validated({Default.class}) PatientRequestDTO patientRequestDTO){
        PatientResponseDTO patientResponseDTO=patientService.updatePatient(id,patientRequestDTO);
        return ResponseEntity.ok(patientResponseDTO);
    }

    @Operation(summary = "Delete a patient")
    @DeleteMapping("/{id}")
    public ResponseEntity<String>detelePatient(@PathVariable @Valid UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok("Patient deleted successfully");
    }

}
