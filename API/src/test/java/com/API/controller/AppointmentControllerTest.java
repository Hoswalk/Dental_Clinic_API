package com.API.controller;

import com.API.dto.dtoRequest.AppointmentRequestDto;
import com.API.dto.dtoResponse.AppointmentResponseDto;
import com.API.dto.dtoResponse.DentistResponseDto;
import com.API.exception.ResourceNotFoundException;
import com.API.mockData.AppointmentFixtures;
import com.API.persistence.dao.impl.AppointmentDaoImpl;
import com.API.persistence.entities.Appointment;
import com.API.service.impl.AppointmentServiceImpl;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class AppointmentControllerTest {

    @Mock
    private AppointmentServiceImpl appointmentService;

    @InjectMocks
    AppointmentController appointmentController;

    private Appointment savedAppointment;
    private AppointmentRequestDto appointmentRequestDto;
    private AppointmentResponseDto appointmentResponseDto;

    @BeforeEach
    public void setUp(){

        // Initializing appointment types mocks
        savedAppointment = AppointmentFixtures.sampleAppointment();
        appointmentRequestDto = AppointmentFixtures.appointmentRequestDto();
        appointmentResponseDto = AppointmentFixtures.appointmentResponseDto();

        // Verify is not null - Appointment types
        assertNotNull(savedAppointment, "savedAppointment is not null");
        assertNotNull(appointmentRequestDto, "appointmentRequestDto is not null");
        assertNotNull(appointmentResponseDto, "appointmentResponseDto is not null");
    }

    @Test
    public void testAppointmentController_SaveAppointment_ReturnsAppointmentResponseDto() throws ResourceNotFoundException {
        // Given: patient types are configured in the setup

        // Config for mock service
        when(appointmentService.saveAppointment(any(AppointmentRequestDto.class))).thenReturn(appointmentResponseDto);

        // Act - Controller method test
        ResponseEntity<AppointmentResponseDto> response = appointmentController.saveAppointment(appointmentRequestDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(appointmentResponseDto.getId(), response.getBody().getId());
    }

    @Test
    public void testAppointmentController_FindAll_ReturnsListAppointmentResponseDto(){
        // Given: patient types are configured in the setup

        // List
        List<AppointmentResponseDto> list = new ArrayList<>();
        list.add(appointmentResponseDto);

        // When - Config for mock service
        when(appointmentService.findAll()).thenReturn(list);

        // Act - Controller method test
        ResponseEntity<List<AppointmentResponseDto>> response = appointmentController.findAllAppointment();

        // Assert
        assertNotNull(response);
        assertFalse(response.getBody().isEmpty());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAppointmentController_FindById_ResturnsAppointmentResponseDto(){
        // Given: patient types are configured in the setup

        // When - Config for mock service
        when(appointmentService.findAppointmentById(appointmentResponseDto.getId())).thenReturn(Optional.ofNullable(appointmentResponseDto));

        // Act - Controller method test
        ResponseEntity<?> response = appointmentController.findAppointmentById(appointmentResponseDto.getId());

        // Aux to see body response
        Optional<AppointmentResponseDto> responseBody = (Optional<AppointmentResponseDto>) response.getBody();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointmentResponseDto.getId(), responseBody.get().getId());
    }

    @Test
    public void testAppointmentController_UpdateAppointmentDateAndHour() throws ResourceNotFoundException {
        // Given: patient types are configured in the setup

        // When - Config for mock service
        when(appointmentService.updateAppointmentDateAndHour(appointmentRequestDto, appointmentResponseDto.getId())).thenReturn(appointmentResponseDto);

        // Act - Controller method test
        ResponseEntity<AppointmentResponseDto> response = appointmentController.updateAppointment(appointmentRequestDto, appointmentResponseDto.getId());

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, appointmentResponseDto.getId());
    }

    @Test
    public void testController_DeleteById_ReturnsNothing() throws ResourceNotFoundException {
        // Given: patient types are configured in the setup

        // When - Config for mock service
        doNothing().when(appointmentService).deleteAppointmentById(appointmentResponseDto.getId());

        // Act - Controller method test
        ResponseEntity<?> response = appointmentController.deleteAppointmentById(appointmentResponseDto.getId());

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        // Verify
        verify(appointmentService, times(1)).deleteAppointmentById(appointmentResponseDto.getId());
    }
}
