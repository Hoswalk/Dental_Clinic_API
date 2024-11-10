package com.API.service;

import com.API.dto.dtoRequest.AppointmentRequestDto;
import com.API.dto.dtoRequest.DentistRequestDto;
import com.API.dto.dtoRequest.PatientRequestDto;
import com.API.dto.dtoResponse.AppointmentResponseDto;
import com.API.dto.dtoResponse.DentistResponseDto;
import com.API.dto.dtoResponse.PatientResponseDto;
import com.API.exception.ResourceNotFoundException;
import com.API.mockData.AppointmentFixtures;
import com.API.mockData.DentistFixtures;
import com.API.mockData.PatientFixtures;
import com.API.persistence.entities.Appointment;
import com.API.persistence.entities.userImpl.Dentist;
import com.API.persistence.entities.userImpl.Patient;
import com.API.persistence.repository.AppointmentRepository;
import com.API.persistence.repository.DentistRepository;
import com.API.persistence.repository.PatientRepository;
import com.API.service.impl.AppointmentServiceImpl;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DentistRepository dentistRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private Appointment savedAppointment;
    private AppointmentRequestDto appointmentRequestDto;
    private AppointmentResponseDto appointmentResponseDto;

    private Patient savedPatient;
    private PatientRequestDto patientRequestDto;
    private PatientResponseDto patientResponseDto;

    private Dentist savedDentist;
    private DentistRequestDto dentistRequestDto;
    private DentistResponseDto dentistResponseDto;
    @BeforeEach
    public void setUp(){

        // Initializing appointments types mocks
        savedAppointment = AppointmentFixtures.sampleAppointment();
        appointmentRequestDto = AppointmentFixtures.appointmentRequestDto();
        appointmentResponseDto = AppointmentFixtures.appointmentResponseDto();

        // Initializing patient types mocks
        savedPatient = PatientFixtures.samplePatient();
        patientRequestDto = PatientFixtures.samplePatientRequestDto();
        patientResponseDto = PatientFixtures.samplePatientResponseDto();

        // Initializing dentist types mocks
        savedDentist = DentistFixtures.sampleDentist();
        dentistRequestDto = DentistFixtures.sampleDentistRequestDto();
        dentistResponseDto = DentistFixtures.sampleDentistResponseDto();

        // Verify is not null - Appointment types
        assertNotNull(savedAppointment, "savedAppointment is not null");
        assertNotNull(appointmentRequestDto, "appointmentRequestDto is not null");
        assertNotNull(appointmentResponseDto, "appointmentResponseDto is not null");

        // Verify is not null - Dentist types
        assertNotNull(savedDentist, "savedDentist is not null");
        assertNotNull(dentistRequestDto, "dentistRequestDto is not null");
        assertNotNull(dentistResponseDto, "dentistResponseDto is not null");

        // Verify is not null - Patient types
        assertNotNull(savedPatient, "savedPatient is null");
        assertNotNull(patientRequestDto, "patientRequestDto is null");
        assertNotNull(patientResponseDto, "patientResponseDto is null");
    }

    @Test
    public void testAppointment_SaveAppointment_ResturnsAppointmentResponseDto(){
        // Given: patient types are configured in the setup

        // Config mocks repo
        when(patientRepository.findPatientByBadgeNumber(savedPatient.getBadgeNumber())).thenReturn(Optional.ofNullable(savedPatient));
        when(dentistRepository.findDentistByBadgeNumber(savedDentist.getBadgeNumber())).thenReturn(Optional.ofNullable(savedDentist));
        // Config mock behavior
        when(appointmentRepository.save(savedAppointment)).thenReturn(savedAppointment);
        when(modelMapper.map(savedAppointment, AppointmentResponseDto.class)).thenReturn(appointmentResponseDto);
        when(modelMapper.map(savedPatient, PatientResponseDto.class)).thenReturn(patientResponseDto);
        when(modelMapper.map(savedDentist, DentistResponseDto.class)).thenReturn(dentistResponseDto);

        // Service method test
        AppointmentResponseDto result = appointmentService.saveAppointment(appointmentRequestDto);

        // Assert
        assertNotNull(result);

        // Verify
        verify(patientRepository, times(1)).findPatientByBadgeNumber(savedPatient.getBadgeNumber());
        verify(dentistRepository, times(1)).findDentistByBadgeNumber(savedDentist.getBadgeNumber());
        verify(appointmentRepository, times(1)).save(savedAppointment);
        verify(modelMapper, times(1)).map(savedPatient, PatientResponseDto.class);
        verify(modelMapper, times(1)).map(savedDentist, DentistResponseDto.class);
    }

    @Test
    public void testAppointment_FindAll_ReturnsListAppointmentResponseDto(){
        // Given: patient types are configured in the setup

        // List of mock appointments
        List<Appointment> appointmentList = new ArrayList<>();
        appointmentList.add(savedAppointment);

        // List of mock appointmentResponseDto expected
        List<AppointmentResponseDto> appointmentResponseDtoList = new ArrayList<>();
        appointmentResponseDtoList.add(appointmentResponseDto);

        // Config mock repo
        when(appointmentRepository.findAll()).thenReturn(appointmentList);
        when(modelMapper.map(savedAppointment, AppointmentResponseDto.class)).thenReturn(appointmentResponseDto);

        // Service method test
        List<AppointmentResponseDto> result = appointmentService.findAll();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Verify
        verify(appointmentRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(savedAppointment, AppointmentResponseDto.class);
    }

    @Test
    public void testAppointment_FindById_ReturnsAppointmentResponseDto(){
        // Given: patient types are configured in the setup

        Long appointmentId = 1L;

        // Config mock repo
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.ofNullable(savedAppointment));

        // Config mapper for entities
        when(modelMapper.map(savedAppointment, AppointmentResponseDto.class)).thenReturn(appointmentResponseDto);
        when(modelMapper.map(savedPatient, PatientResponseDto.class)).thenReturn(patientResponseDto);
        when(modelMapper.map(savedDentist, DentistResponseDto.class)).thenReturn(dentistResponseDto);

        // Service method test
        Optional<AppointmentResponseDto> result = appointmentService.findAppointmentById(appointmentId);

        // Assert
        assertNotNull(result);
        assertEquals(appointmentId, appointmentResponseDto.getId());

        // Verify
        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(modelMapper, times(1)).map(savedAppointment, AppointmentResponseDto.class);
        verify(modelMapper, times(1)).map(savedPatient, PatientResponseDto.class);
        verify(modelMapper, times(1)).map(savedDentist, DentistResponseDto.class);
    }

    @Test
    public void testAppointment_UpdateAppointmentDateAndHour_ReturnsAppointmentResponseDto() throws ResourceNotFoundException {
        // Given: patient types are configured in the setup

        // Config mock repo
        when(appointmentRepository.findById(1L)).thenReturn(Optional.ofNullable(savedAppointment));
        when(appointmentRepository.save(savedAppointment)).thenReturn(savedAppointment);

        // Config mappers
        when(modelMapper.map(savedAppointment, AppointmentResponseDto.class)).thenReturn(appointmentResponseDto);
        when(modelMapper.map(savedPatient, PatientResponseDto.class)).thenReturn(patientResponseDto);
        when(modelMapper.map(savedDentist, DentistResponseDto.class)).thenReturn(dentistResponseDto);

        // Service method test
        AppointmentResponseDto result = appointmentService.updateAppointmentDateAndHour(appointmentRequestDto, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(appointmentRequestDto.getAppointmentDateHour(), appointmentResponseDto.getAppointmentDateHour());

        // Verify
        verify(appointmentRepository, times(1)).findById(1L);
        verify(appointmentRepository, times(1)).save(savedAppointment);
        verify(modelMapper, times(1)).map(savedAppointment, AppointmentResponseDto.class);
        verify(modelMapper, times(1)).map(savedPatient, PatientResponseDto.class);
        verify(modelMapper, times(1)).map(savedDentist, DentistResponseDto.class);
    }

    @Test
    public void testAppintment_DeleteById_ReturnsNothing() throws ResourceNotFoundException {
        // Given: patient types are configured in the setup

        // Config mock repo
        // 1 `findById`
        when(appointmentRepository.findById(1L)).thenReturn(Optional.ofNullable(savedAppointment));

        // 2 `findById` requires mapping from appointment to appointmentResponseDto
        when(modelMapper.map(savedAppointment, AppointmentResponseDto.class)).thenReturn(appointmentResponseDto);

        // 3 `deleteById` does nothing because is void
        doNothing().when(appointmentRepository).deleteById(1L);

        // Service method test
        appointmentService.deleteAppointmentById(1L);

        // Assert
        // Verify
        verify(appointmentRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(savedAppointment, AppointmentResponseDto.class);
        verify(appointmentRepository, times(1)).deleteById(1L);
    }
}
