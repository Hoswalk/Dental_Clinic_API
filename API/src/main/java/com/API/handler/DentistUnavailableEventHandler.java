package com.API.handler;

import com.API.dto.dtoResponse.AppointmentResponseDto;
import com.API.dto.dtoResponse.DentistResponseDto;
import com.API.dto.dtoResponse.PatientResponseDto;
import com.API.event.DentistUnavailableEvent;
import com.API.persistence.entities.Appointment;
import com.API.persistence.entities.userImpl.Dentist;
import com.API.persistence.repository.AppointmentRepository;
import com.API.persistence.repository.DentistRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DentistUnavailableEventHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(DentistUnavailableEventHandler.class);

    private final DentistRepository dentistRepository;

    private final AppointmentRepository appointmentRepository;

    private final ModelMapper modelMapper;


    @EventListener
    public void handleDenstistUnavailableEvent(DentistUnavailableEvent event){

        Long dentistId = event.getDentistId();

        Dentist newDentist = dentistRepository.findFirstDentistByAvailableTrue();

        if (newDentist != null){
            List<AppointmentResponseDto> reassignedAppointments = reassingAppointmentsToAnotherDentist(dentistId, newDentist.getId());

            // Opcional: registrar o manejar el resultado de las citas reasignadas
            LOGGER.info("Reassigned appointments from dentist {} to dentist {}", dentistId, newDentist.getId());
        } else {
            LOGGER.warn("No available dentist found to reassign appointments for dentist {}, please assign appointment manually.", dentistId);
        }
    }

    public List<AppointmentResponseDto> reassingAppointmentsToAnotherDentist(Long dentistId, Long newDentistId){

        //Get old dentist
        Dentist oldDentist = dentistRepository.findById(dentistId).orElseThrow(() -> new EntityNotFoundException("Old dentist was not found."));

        Dentist newDentist = dentistRepository.findById(newDentistId).orElseThrow(() -> new EntityNotFoundException("New dentist was not found."));

        List<Appointment> appointmentList = appointmentRepository.findAllByDentistId(oldDentist.getId());

        List<AppointmentResponseDto> updateAppointments = new ArrayList<>();


        for (Appointment appointment : appointmentList){
            //Assigning new dentist
            appointment.setDentist(newDentist);

            boolean isAvailable = !appointmentRepository.existsByDentistIdAndAppointmentDateHour(newDentist.getId(), appointment.getAppointmentDateHour());

            //
            if (appointmentRepository.existsByDentistIdAndAppointmentDateHour(newDentist.getId(), appointment.getAppointmentDateHour())){
                // If appointment time is taken, skip this appointment or handle it differently

                if (!isAvailable){
                    findAvailableDentistForAppointmentsAssigned(appointment.getAppointmentDateHour());
                } else LOGGER.error("There is no dentist available at the time of this appointment {}, please schedule it manually.", appointment.getAppointmentDateHour());
            }

            //Save appointments for new dentist
            Appointment updatedAppointment = appointmentRepository.save(appointment);

            //Map appointment to DTO
            AppointmentResponseDto appointmentResponseDto = modelMapper.map(updatedAppointment, AppointmentResponseDto.class);

            //Set dentsit DTO
            appointmentResponseDto.setDentistResponseDto(modelMapper.map(newDentist, DentistResponseDto.class));

            //Set patient DTO
            if (appointment.getPatient() != null){
                appointmentResponseDto.setPatientResponseDto(modelMapper.map(appointment.getPatient(), PatientResponseDto.class));
            }

            //Adding to new list
            updateAppointments.add(appointmentResponseDto);
        }

        return updateAppointments;
    }

    private Optional<Dentist> findAvailableDentistForAppointmentsAssigned(LocalDateTime appointmentDateHour){
        return dentistRepository.findFirstByAvailableTrue()
                .stream()
                .filter(dentist -> !appointmentRepository.existsByDentistIdAndAppointmentDateHour(dentist.getId(), appointmentDateHour))
                .peek(dentist -> LOGGER.info("Appointment assigned to dentist with ID: {}", dentist.getId()))
                .findFirst();
    }
}
