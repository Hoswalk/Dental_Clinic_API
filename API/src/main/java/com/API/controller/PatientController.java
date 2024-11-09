package com.API.controller;


import com.API.dto.dtoRequest.PatientRequestDto;
import com.API.dto.dtoResponse.PatientResponseDto;
import com.API.exception.ResourceNotFoundException;
import com.API.service.impl.PatientServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@AllArgsConstructor
public class PatientController {

    private PatientServiceImpl patientService;

    //POST
    @PostMapping("/save")
    public ResponseEntity<PatientResponseDto> savePatient(@RequestBody @Valid PatientRequestDto patientRequestDto){
        return new ResponseEntity<>(patientService.savePatient(patientRequestDto), HttpStatus.CREATED);
    }

    //GET
    @GetMapping("/findAll")
    public ResponseEntity<List<PatientResponseDto>> findAllPatient(){
        return new ResponseEntity<>(patientService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        return new ResponseEntity<>(patientService.findPatientById(id), HttpStatus.OK);
    }

    @GetMapping("/findByBadgeNumber/{badgeNumber}")
    public ResponseEntity<?> findByBadgeNumber(@PathVariable String badgeNumber){
        return new ResponseEntity<>(patientService.findPatientByBadgeNumebr(badgeNumber), HttpStatus.OK);
    }

    //PUT
    @PutMapping("/update/{id}")
    public ResponseEntity<PatientResponseDto> update(@RequestBody PatientRequestDto patientRequestDto, @PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(patientService.updatePatient(patientRequestDto, id), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById(Long id) throws ResourceNotFoundException {
        patientService.deletePatientById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
