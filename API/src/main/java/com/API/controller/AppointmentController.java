package com.API.controller;

import com.API.dto.dtoRequest.AppointmentRequestDto;
import com.API.dto.dtoResponse.AppointmentResponseDto;
import com.API.exception.ResourceNotFoundException;
import com.API.handler.DentistUnavailableEventHandler;
import com.API.service.impl.AppointmentServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
@AllArgsConstructor
public class AppointmentController {

    private final Logger LOGGER = LoggerFactory.getLogger(AppointmentController.class);

    private AppointmentServiceImpl appointmentService;

    private final DentistUnavailableEventHandler dentistUnavailableEventHandler;

    //POST
    @PostMapping("/save")
    public ResponseEntity<AppointmentResponseDto> saveAppointment(@RequestBody AppointmentRequestDto appointmentRequestDto) throws ResourceNotFoundException {
        LOGGER.info("Badge Number del Paciente en DTO: {}", appointmentRequestDto.getPatientBadgeNumber());
        return new ResponseEntity<>(appointmentService.saveAppointment(appointmentRequestDto), HttpStatus.CREATED);
    }

    //GET
    @GetMapping("/findAll")
    public ResponseEntity<List<AppointmentResponseDto>> findAllAppointment(){
        return new ResponseEntity<>(appointmentService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findAppointmentById(@PathVariable Long id){
        return new ResponseEntity<>(appointmentService.findAppointmentById(id), HttpStatus.OK);
    }

    //PUT
    @PutMapping("/update/{id}")
    public ResponseEntity<AppointmentResponseDto> updateAppointment(@RequestBody AppointmentRequestDto appointmentRequestDto, @PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(appointmentService.updateAppointmentDateAndHour(appointmentRequestDto, id), HttpStatus.OK);
    }

    @PutMapping("/updateAppointmentsToNewDentist/{oldDentistId}/{newDentistId}")
    public ResponseEntity<List<AppointmentResponseDto>> updateAppointmentToNewDentist(@PathVariable Long oldDentistId, @PathVariable Long newDentistId){
        return new ResponseEntity<>(dentistUnavailableEventHandler.reassingAppointmentsToAnotherDentist(oldDentistId, newDentistId), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("deleteById/{id}")
    public ResponseEntity<?> deleteAppointmentById(@PathVariable Long id) throws ResourceNotFoundException {
        appointmentService.deleteAppointmentById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
