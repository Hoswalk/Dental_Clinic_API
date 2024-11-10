package com.API.service;

import com.API.dto.dtoRequest.PatientRequestDto;
import com.API.dto.dtoResponse.PatientResponseDto;
import com.API.exception.ResourceNotFoundException;
import com.API.mockData.PatientFixtures;
import com.API.persistence.entities.userImpl.Patient;
import com.API.persistence.repository.PatientRepository;
import com.API.service.impl.PatientServiceImpl;
import com.API.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT) // Esta anotacion sirve para que modelmapper no sea tan estricto al momento de mapear datos - esta ya que en el metodo update lo tengo configurado para que pueda aceptar campos vacios y se puedan actualizar cosas especificas - se puede usar esta anotacion aqui o directamenten en el metodo que es la manera en la que esta siendo usada en este caso
public class PatientServiceImplTest {

    @Mock
    PatientRepository patientRepository;

    @Mock
    UserServiceImpl userService;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    PatientServiceImpl patientService;

    private Patient savedPatient;
    private PatientRequestDto patientRequestDto;
    private PatientResponseDto patientResponseDto;

    @BeforeEach
    public void setUp() {

        // Initializing patient types mocks
        savedPatient = PatientFixtures.samplePatient();
        patientRequestDto = PatientFixtures.samplePatientRequestDto();
        patientResponseDto = PatientFixtures.samplePatientResponseDto();

        // Verify is not null
        assertNotNull(savedPatient, "savedPatient is null");
        assertNotNull(patientRequestDto, "patientRequestDto is null");
        assertNotNull(patientResponseDto, "patientResponseDto is null");
    }

    @Test
    public void testPatient_SavePatient_ReturnsPatientResponseDto() {
        //Given: patient types are configured in the setup

        // Configurar el comportamiento de los mocks
        when(userService.saveUser(patientRequestDto, Patient.class, "patient")).thenReturn(savedPatient);
        when(modelMapper.map(savedPatient, PatientResponseDto.class)).thenReturn(patientResponseDto);

        // Llamar al método que se está probando
        PatientResponseDto result = patientService.savePatient(patientRequestDto);

        // Verificar que el resultado no sea nulo
        assertNotNull(result);
        // Verificar que el resultado es el esperado
        verify(userService, times(1)).saveUser(patientRequestDto, Patient.class, "patient");
        verify(modelMapper, times(1)).map(savedPatient, PatientResponseDto.class);
    }

    @Test
    public void testPatient_FindAll_ReturnsPatientDtoList(){

        //Given: patient types are configured in the setup

        // List of patients for mock respository
        List<Patient> patientList = new ArrayList<>();
        patientList.add(savedPatient);

        // Creamos una lista de PatientResponseDto esperada
        List<PatientResponseDto> expectedResponse = new ArrayList<>();
        expectedResponse.add(patientResponseDto);

        // Config mock behavior
        when(patientRepository.findAll()).thenReturn(patientList);
        when(modelMapper.map(savedPatient, PatientResponseDto.class)).thenReturn(patientResponseDto);

        // Service method test
        List<PatientResponseDto> result = patientService.findAll();

        //Assert
        assertEquals(expectedResponse, result);

        // Verify repository method and mapper
        verify(patientRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(savedPatient, PatientResponseDto.class);
    }

    @Test
    public void testPatient_FindById_ReturnsPatientResponseDto(){

        // Given: patient types are configured in the setup

        // Config for mock repo
        when(patientRepository.findById(1L)).thenReturn(Optional.of(savedPatient));

        // Config modelmapper
        when(modelMapper.map(savedPatient, PatientResponseDto.class)).thenReturn(patientResponseDto);

        // Service method test
        Optional<PatientResponseDto> result = patientService.findPatientById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, patientResponseDto.getId());
        assertEquals(1L, result.get().getId());

        // Verify repository method and mapper
        verify(patientRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(savedPatient, PatientResponseDto.class);
    }

    @Test
    public void testPatient_FindByBadgeNumber_ReturnsPatientResponseDto(){

        // Given: patient types are configured in the setup


        // Config for mock repo
        when(patientRepository.findPatientByBadgeNumber(savedPatient.getBadgeNumber())).thenReturn(Optional.of(savedPatient));
        //config modelmapper
        when(modelMapper.map(savedPatient, PatientResponseDto.class)).thenReturn(patientResponseDto);

        // Service method test
        Optional<PatientResponseDto> result = patientService.findPatientByBadgeNumebr("patient-001");

        // Assert
        assertTrue(result.isPresent());
        assertNotNull(result);
        assertEquals("patient-001", result.get().getBadgeNumber());

        // Verify repository method and mapper
        verify(patientRepository, times(1)).findPatientByBadgeNumber("patient-001");
        verify(modelMapper, times(1)).map(savedPatient, PatientResponseDto.class);
    }

    @Test
    public void testPatient_UpdatePatient_ReturnsPatientResponseDto() throws ResourceNotFoundException {

        //lenient() esta siendo utlizada en este metodo para que modelmapper no sea estricto al momento de mapear ya qeu este metodo acepta valores individuales al momento de actualizar informacion sin importar solo un campo es null - al inicio de la clase esta la otra forma de utilizarlo que ya seria a nivel de clase, pero este caso solo es necesario para este metodo

        // Given: patient types are configured in the setup

        // Mock Configuration to handle Conditions.isNotNull()
        Configuration mockConfig = mock(Configuration.class);
        lenient().when(modelMapper.getConfiguration()).thenReturn(mockConfig);
        lenient().when(mockConfig.setPropertyCondition(Conditions.isNotNull())).thenReturn(mockConfig);

        // Config for mock repo
        when(patientRepository.findById(savedPatient.getId())).thenReturn(Optional.of(savedPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);

        // Mocking the map method for PatientRequestDto to Patient
        lenient().when(modelMapper.map(patientRequestDto, Patient.class)).thenReturn(savedPatient);

        // Mocking the map method for Patient to PatientResponseDto
        lenient().when(modelMapper.map(savedPatient, PatientResponseDto.class)).thenReturn(patientResponseDto);

        // Service method test
        PatientResponseDto result = patientService.updatePatient(patientRequestDto, savedPatient.getId());

        // Assert
        assertNotNull(result);
        assertEquals(patientResponseDto, result);

        // Verify repository methods and mappers
        verify(patientRepository, times(1)).findById(savedPatient.getId());
        verify(patientRepository, times(1)).save(any(Patient.class));
        verify(modelMapper).map(patientRequestDto, savedPatient);  // Mock for PatientRequestDto to Patient
        verify(modelMapper).map(savedPatient, PatientResponseDto.class);  // Mock for Patient to PatientResponseDto
    }

    @Test
    public void testPatient_DeleteById_ReturnsNothing() throws ResourceNotFoundException {

        // Given: patient types are configured in the setup


        // Config for mock repo
        // 1. `findById` returns `Patient` before delete it.
        when(patientRepository.findById(savedPatient.getId())).thenReturn(Optional.of(savedPatient));

        //2. `findById` required mapping from patient to patientResponseDto
        when(modelMapper.map(savedPatient, PatientResponseDto.class)).thenReturn(patientResponseDto);

        // 3. `deleteById` does nothing because is a void method
        doNothing().when(patientRepository).deleteById(savedPatient.getId());

        // Service method test
        patientService.deletePatientById(savedPatient.getId());

        // Assert:
        // 1. Verificamos que `deleteById` fue llamado exactamente una vez con el ID especificado.
        verify(patientRepository, times(1)).deleteById(savedPatient.getId());
        // 2. Verificamos que `findById` fue llamado para comprobar si el paciente existía antes de eliminarlo.
        verify(patientRepository, times(1)).findById(savedPatient.getId());
        // 3. Verificamos que `modelMapper` fue llamado para mapear de patient a patientResponseDto
        verify(modelMapper, times(1)).map(savedPatient, PatientResponseDto.class);
    }
}
