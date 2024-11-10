package com.API.mockData;

import com.API.dto.dtoRequest.AppointmentRequestDto;
import com.API.dto.dtoRequest.DentistRequestDto;
import com.API.dto.dtoResponse.AppointmentResponseDto;
import com.API.persistence.entities.Appointment;
import com.API.persistence.entities.userImpl.Patient;

import java.time.LocalDateTime;

public class AppointmentFixtures {

    private static LocalDateTime appointmentDateHour = LocalDateTime.of(2024, 11, 12, 14, 30);
    public static Appointment sampleAppointment(){
        return new Appointment(null, DentistFixtures.sampleDentist(), PatientFixtures.samplePatient(), appointmentDateHour);
    }

    public static AppointmentRequestDto appointmentRequestDto(){
        return new AppointmentRequestDto(DentistFixtures.sampleDentistResponseDto().getBadgeNumber(), PatientFixtures.samplePatientResponseDto().getBadgeNumber(), appointmentDateHour);
    }

    public static AppointmentResponseDto appointmentResponseDto(){
        return new AppointmentResponseDto(1L, appointmentDateHour, DentistFixtures.sampleDentistResponseDto(), PatientFixtures.samplePatientResponseDto());
    }
}
