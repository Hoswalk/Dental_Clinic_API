package com.API.service;

import com.API.dto.dtoRequest.PatientRequestDto;
import com.API.dto.dtoResponse.PatientResponseDto;
import com.API.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IPatientService {

    PatientResponseDto savePatient(PatientRequestDto patientRequestDto);

    List<PatientResponseDto> findAll();

    Optional<PatientResponseDto> findPatientById(Long id);

    Optional<PatientResponseDto> findPatientByBadgeNumebr(String badgeNumber);

    PatientResponseDto updatePatient(PatientRequestDto patientRequestDto, Long id) throws ResourceNotFoundException;

    void deletePatientById(Long id) throws ResourceNotFoundException;
}
