package com.API.service.impl;

import com.API.dto.dtoRequest.PatientRequestDto;
import com.API.dto.dtoResponse.PatientResponseDto;
import com.API.exception.ResourceNotFoundException;
import com.API.persistence.entities.userImpl.Patient;
import com.API.persistence.repository.PatientRepository;
import com.API.service.IPatientService;
import com.API.utils.JsonPrinter;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements IPatientService {

    private final Logger LOGGER = LoggerFactory.getLogger(PatientServiceImpl.class);

    private final UserServiceImpl userService;

    private PatientRepository patientRepository;

    private final ModelMapper modelMapper;

    // Configuración inicial del ModelMapper
    /*@PostConstruct
    public void init() {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }*/

    @Override
    public PatientResponseDto savePatient(PatientRequestDto patientRequestDto) {

        //Reutilizacion
        Patient savedPatient = userService.saveUser(patientRequestDto, Patient.class, "patient");

        //Log
        LOGGER.info("Patient saved: {}", JsonPrinter.toString(savedPatient));

        //Mapping
        return modelMapper.map(savedPatient, PatientResponseDto.class);
    }

    @Override
    public List<PatientResponseDto> findAll() {

        List<PatientResponseDto> patientResponseDtoList = patientRepository.findAll()
                .stream()
                .map(patient -> modelMapper.map(patient, PatientResponseDto.class))
                .toList();

        return patientResponseDtoList;
    }

    @Override
    public Optional<PatientResponseDto> findPatientById(Long id) {

        Patient patientSearched = patientRepository.findById(id).orElse(null);
        PatientResponseDto patientFound = null;

        if (patientSearched != null){
            patientFound = modelMapper.map(patientSearched, PatientResponseDto.class);
            LOGGER.info("Patient found: {}", JsonPrinter.toString(patientFound));
        } else LOGGER.error("Patient not found: {}", JsonPrinter.toString(patientSearched));

        return Optional.ofNullable(patientFound);
    }

    @Override
    public Optional<PatientResponseDto> findPatientByBadgeNumebr(String badgeNumber){

        Patient patientSearched = patientRepository.findPatientByBadgeNumber(badgeNumber).orElse(null);
        PatientResponseDto patientFound = null;

        if (patientSearched != null){
            patientFound = modelMapper.map(patientSearched, PatientResponseDto.class);
            LOGGER.info("Patient found.");
        } else LOGGER.error("Patient was not found.");

        return Optional.of(patientFound);
    }

    @Override
    public PatientResponseDto updatePatient(PatientRequestDto patientRequestDto, Long id) throws ResourceNotFoundException {

        Patient patientUpdate = patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient not found."));

        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(patientRequestDto, patientUpdate);

        patientRepository.save(patientUpdate);

        PatientResponseDto patientUpdated = modelMapper.map(patientUpdate, PatientResponseDto.class);

        return patientUpdated;
    }

    @Override
    public void deletePatientById(Long id) throws ResourceNotFoundException {

        if (findPatientById(id).isPresent()){
            patientRepository.deleteById(id);
            LOGGER.warn("Patient with id: " + id + " has been deleted");
        } else {
            throw new ResourceNotFoundException("Patient with id: " + id + " was not found");
        }
    }
}
