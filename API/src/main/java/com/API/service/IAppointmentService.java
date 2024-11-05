package com.API.service;

import com.API.dto.dtoRequest.AppointmentRequestDto;
import com.API.dto.dtoResponse.AppointmentResponseDto;
import com.API.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IAppointmentService {

    AppointmentResponseDto saveAppointment(AppointmentRequestDto appointmentRequestDto) throws ResourceNotFoundException;

    List<AppointmentResponseDto> findAll();

    Optional<AppointmentResponseDto> findAppointmentById(Long id);

    //Optional<AppointmentResponseDto> findAppointmentByBadgeNumber(String badgeNumebr);

    AppointmentResponseDto updateAppointmentDateAndHour(AppointmentRequestDto appointmentRequestDto, Long id) throws ResourceNotFoundException;

    //List<AppointmentResponseDto> reassingAppointmentsToAnotherDentist(Long dentistId, Long newDentistId);

    void deleteAppointmentById(Long id) throws ResourceNotFoundException;
}
