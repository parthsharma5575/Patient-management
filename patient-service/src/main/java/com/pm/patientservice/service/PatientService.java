package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.kafka.kafkaProducer;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repo.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final kafkaProducer kafkaProducer;

    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient, kafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer=kafkaProducer;
    }

    public List<PatientResponseDTO> getAllPatients(){
        List<Patient> patients= patientRepository.findAll();
        return patients.stream().
                map(PatientMapper::mapToPatientResponseDTO).toList();
    }

    public PatientResponseDTO savePatient(PatientRequestDTO patientRequestDTO){
        if(patientRepository.existsByEmail(patientRequestDTO.email())){
            throw new EmailAlreadyExistsException("A patient with this email already exists");
        }
        Patient newPatient=patientRepository.save(PatientMapper.mapToPatient(patientRequestDTO));
        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(),
                newPatient.getName(),
                newPatient.getEmail()
        );

        kafkaProducer.sendEvent(newPatient);

        return PatientMapper.mapToPatientResponseDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO){
        Patient patient=patientRepository.findById(id).orElseThrow(()->new PatientNotFoundException("Patient not found with id "+id));
        if(patientRepository.existsByEmail(patientRequestDTO.email())){
            if(patient.getEmail().equals(patientRequestDTO.email())){}
            else{
                throw new EmailAlreadyExistsException("A patient with this email already exists");
            }

        }
        patient.setName(patientRequestDTO.name());
        patient.setEmail(patientRequestDTO.email());
        patient.setAddress(patientRequestDTO.address());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.dateOfBirth()));
        patientRepository.save(patient);
        return PatientMapper.mapToPatientResponseDTO(patient);
    }
    public void deletePatient(UUID id){
        if(!patientRepository.existsById(id)){
            throw new PatientNotFoundException("Patient not found with id "+id);
        }
        patientRepository.deleteById(id);
    }
}
