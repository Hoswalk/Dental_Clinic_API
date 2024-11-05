package com.API.dto.dtoRequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequestDto {

    @NotNull(message = "Patient cannot be null.")
    @Valid
    private String patientBadgeNumber;

    @NotNull(message = "Dentist cannot be null.")
    @Valid
    private String dentistBadgeNumber;

    @FutureOrPresent(message = "Appointment date cannot be before today.")
    @NotNull(message = "Must specify date and appointment hour.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime appointmentDateHour;
}
