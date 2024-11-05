package com.API.service.impl;

import com.API.dto.dtoRequest.AppointmentRequestDto;
import com.API.dto.dtoResponse.AppointmentResponseDto;
import com.API.dto.dtoResponse.DentistResponseDto;
import com.API.dto.dtoResponse.PatientResponseDto;
import com.API.event.DentistUnavailableEvent;
import com.API.exception.ResourceNotFoundException;
import com.API.persistence.entities.Appointment;
import com.API.persistence.entities.userImpl.Dentist;
import com.API.persistence.entities.userImpl.Patient;
import com.API.persistence.repository.AppointmentRepository;
import com.API.persistence.repository.DentistRepository;
import com.API.persistence.repository.PatientRepository;
import com.API.service.IAppointmentService;
import com.API.utils.JsonPrinter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements IAppointmentService {

    private final Logger LOGGER = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private final AppointmentRepository appointmentRepository;

    private final DentistRepository dentistRepository;

    private final PatientRepository patientRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public AppointmentResponseDto saveAppointment(AppointmentRequestDto appointmentRequestDto){
        //Check if is getting the badge numbers
        LOGGER.info("Patient Badge Number: {}", appointmentRequestDto.getPatientBadgeNumber());
        LOGGER.info("Dentist Badge Number: {}", appointmentRequestDto.getDentistBadgeNumber());

        //Getting patient and dentist
        Patient patient = patientRepository.findPatientByBadgeNumber(appointmentRequestDto.getPatientBadgeNumber()).orElseThrow(() -> new EntityNotFoundException("Patient not found."));

        Dentist dentist = dentistRepository.findDentistByBadgeNumber(appointmentRequestDto.getDentistBadgeNumber()).orElseThrow(() -> new EntityNotFoundException("Patient not found."));

        //Creating appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDentist(dentist);
        appointment.setAppointmentDateHour(appointmentRequestDto.getAppointmentDateHour()); //

        //Saving appointment
        Appointment savedAppointment = appointmentRepository.save(appointment);

        //Set dentist availability after appointment assigned and save in DB
        dentist.setAvailable(false);
        dentistRepository.save(dentist);

        //Schedule task to set availability back to true in 1 hour
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            dentist.setAvailable(true);
            dentistRepository.save(dentist);
            LOGGER.info("Dentist {} availability set back to true.", dentist.getBadgeNumber());
        }, 1, TimeUnit.HOURS);

        LOGGER.info("New appointment created: {}", JsonPrinter.toString(appointment));

       return mapToResponseDto(savedAppointment, patient, dentist);
    }

    @Override
    public List<AppointmentResponseDto> findAll() {

        List<AppointmentResponseDto> appointments = appointmentRepository.findAll()
                .stream()
                .map(appointment -> mapToResponseDto(appointment, appointment.getPatient(), appointment.getDentist()))
                .toList();
        LOGGER.info("Appointment's list: {}", JsonPrinter.toString(appointments));

        return appointments;
    }

    @Override
    public Optional<AppointmentResponseDto> findAppointmentById(Long id) {

        return appointmentRepository.findById(id)
                .map(appointment -> {
                    AppointmentResponseDto appointmentResponseDto = mapToResponseDto(appointment, appointment.getPatient(), appointment.getDentist());
                    LOGGER.info("Appointment found: {}", JsonPrinter.toString(appointmentResponseDto));
                    return appointmentResponseDto;
                });
    }

    @Override
    public AppointmentResponseDto updateAppointmentDateAndHour(AppointmentRequestDto appointmentRequestDto, Long id) throws ResourceNotFoundException {

        //Get appointment ID
        Appointment existingAppointment = appointmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Appointment doesn't exists."));

        AppointmentResponseDto appointmentResponseDto;

        if (existingAppointment != null){
            //Seting new fields for update //
             existingAppointment.setAppointmentDateHour(appointmentRequestDto.getAppointmentDateHour()); //

            //Save appointment
            Appointment updatedAppointment = appointmentRepository.save(existingAppointment);

            //Mapping to DTO
            appointmentResponseDto = mapToResponseDto(updatedAppointment, existingAppointment.getPatient(), existingAppointment.getDentist());

             /*if (existingAppointment.getPatient() != null){
                 appointmentResponseDto.setPatientResponseDto(
                         modelMapper.map(existingAppointment.getPatient(), PatientResponseDto.class)
                 );
             }

             if (existingAppointment.getDentist() != null){
                 appointmentResponseDto.setDentistResponseDto(
                         modelMapper.map(existingAppointment.getDentist(), DentistResponseDto.class)
                 );
             }*/

            return appointmentResponseDto;
        } else LOGGER.error("There was an error updating the appointment");
        throw new ResourceNotFoundException("Was not possible to update appointment with id: " + id + " because it is not in the database");
    }

    @Override
    public void deleteAppointmentById(Long id) throws ResourceNotFoundException {

        if (findAppointmentById(id).isPresent()){
            appointmentRepository.deleteById(id);
            LOGGER.warn("Appointment with ID: {}", id + " has been deleted successfully.");
        } else throw new ResourceNotFoundException("Appointment with ID: " + id + " was not found.");
    }

    //Handy methods
    private AppointmentResponseDto mapToResponseDto(Appointment appointment, Patient patient, Dentist dentist){
        AppointmentResponseDto responseDto = modelMapper.map(appointment, AppointmentResponseDto.class);
        responseDto.setPatientResponseDto(modelMapper.map(patient, PatientResponseDto.class));
        responseDto.setDentistResponseDto(modelMapper.map(dentist, DentistResponseDto.class));

        return responseDto;
    }
}
