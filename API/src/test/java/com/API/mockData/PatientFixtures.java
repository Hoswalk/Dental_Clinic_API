package com.API.mockData;

import com.API.dto.dtoRequest.PatientRequestDto;
import com.API.dto.dtoResponse.PatientResponseDto;
import com.API.persistence.entities.Address;
import com.API.persistence.entities.userImpl.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PatientFixtures {

    private static Logger LOGGER = LoggerFactory.getLogger(PatientFixtures.class);

    public static Patient samplePatient(){
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("John");
        patient.setLastName("Doe");
        patient.setBadgeNumber("patient-001");
        patient.setEmail("john.doe@gmail.com");
        patient.setPassword("password123");
        patient.setAddress(null);
        patient.setPhone(null);
        return patient;
    }

    public static PatientRequestDto samplePatientRequestDto(){
        PatientRequestDto patient = new PatientRequestDto();
        patient.setName("John");
        patient.setLastName("Doe");
        patient.setEmail("john.doe@gmail.com");
        patient.setPassword("password123");
        LOGGER.info("PatientRequest: {}", patient);
        return patient;
    }

    public static PatientResponseDto samplePatientResponseDto(){
        PatientResponseDto patient = new PatientResponseDto();
        patient.setId(1L);
        patient.setName("John");
        patient.setLastName("Doe");
        patient.setBadgeNumber("patient-001");
        patient.setEmail("john.doe@gmail.com");
        LOGGER.info("PatientResponse: {}", patient);
        return patient;
    }

    public static List<PatientResponseDto> patientResponseDtoList(){
        System.out.println("Getting patient's list");
        Address address = new Address(1L, "Springfield", 435, "IL", 5362);
        return List.of(
                new PatientResponseDto(2L, "Jane", "Mix", "patient-5b909d6", "jane@gmail.com", address, "3003521516"),
                new PatientResponseDto(3L, "Maria", "Perez", "patient-5b909d4", "maria@gmail.com", address, "3005236125")
        );
    }
}
