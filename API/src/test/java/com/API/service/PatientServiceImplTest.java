package com.API.service;

import com.API.dto.dtoRequest.PatientRequestDto;
import com.API.dto.dtoResponse.PatientResponseDto;
import com.API.exception.ResourceNotFoundException;
import com.API.mockData.PatientFixtures;
import com.API.persistence.entities.userImpl.Patient;
import com.API.persistence.repository.PatientRepository;
import com.API.service.impl.PatientServiceImpl;
import com.API.service.impl.UserServiceImpl;
import jakarta.annotation.PostConstruct;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PatientServiceImplTest {

    @InjectMocks
    PatientServiceImpl patientService;

    @Mock
    PatientRepository patientRepository;

    @Mock
    UserServiceImpl userService;

    @Mock
    ModelMapper modelMapper;

    private PatientRequestDto patientRequestDto;
    private Patient savedPatient;
    private PatientResponseDto patientResponseDto;

    @BeforeEach
    public void setUp() {
        // Inicializamos los objetos de prueba
        patientRequestDto = new PatientRequestDto();
        savedPatient = new Patient();
        patientResponseDto = new PatientResponseDto();
    }

    @PostConstruct
    public void init() {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    @Test
    public void testSavePatient() {
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

        // Creamos una lista de pacientes para el mock del repositorio
        List<Patient> patientList = new ArrayList<>();
        patientList.add(savedPatient);

        // Creamos una lista de PatientResponseDto esperada
        List<PatientResponseDto> expectedResponse = new ArrayList<>();
        expectedResponse.add(patientResponseDto);

        // Configuramos el comportamiento de los mocks
        when(patientRepository.findAll()).thenReturn(patientList);
        when(modelMapper.map(savedPatient, PatientResponseDto.class)).thenReturn(patientResponseDto);

        // Llamamos al método a probar
        List<PatientResponseDto> result = patientService.findAll();

        // Verificamos que el resultado es el esperado
        assertEquals(expectedResponse, result);
        // Verificamos que el repositorio y el mapper fueron llamados una vez
        verify(patientRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(savedPatient, PatientResponseDto.class);
    }

    @Test
    public void testPatient_FindById_ReturnsPatientResponseDto(){

        // Given: Creamos un objeto Patient y su correspondiente PatientResponseDto
        Patient patient = new Patient(1L, "John", "Doe", "12345678", "patient-001", "john.doe@gmail.com", "password123", null, "555-1234");

        PatientResponseDto expectedResponse = PatientFixtures.samplePatientResponseDto();

        //config comportamiento repo
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        //config modelmapper
        when(modelMapper.map(patient, PatientResponseDto.class)).thenReturn(expectedResponse);

        //metodo servicio
        Optional<PatientResponseDto> result = patientService.findPatientById(1L);

        //assert
        assertTrue(result.isPresent());
        assertEquals(1L, expectedResponse.getId());
        assertEquals(1L, result.get().getId());

        //verify que se use el repositorio con el id especificado y que se utilice modelmapper
        verify(patientRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(patient, PatientResponseDto.class);
    }

    @Test
    public void testPatient_FindByBadgeNumber_ReturnsPatientResponseDto(){

        //Given: objeto patient y correspondiente responseDTO
        Patient patient = PatientFixtures.samplePatient();

        PatientResponseDto expectedResponse = PatientFixtures.samplePatientResponseDto();

        //config comportamiento del repo
        when(patientRepository.findPatientByBadgeNumber(patient.getBadgeNumber())).thenReturn(Optional.of(patient));
        //config modelmapper
        when(modelMapper.map(patient, PatientResponseDto.class)).thenReturn(expectedResponse);

        //metodo servicio
        Optional<PatientResponseDto> result = patientService.findPatientByBadgeNumebr("patient-001");

        //assert
        assertTrue(result.isPresent());
        assertNotNull(result);
        assertEquals("patient-001", result.get().getBadgeNumber());

        //verify
        verify(patientRepository, times(1)).findPatientByBadgeNumber("patient-001");
        verify(modelMapper, times(1)).map(patient, PatientResponseDto.class);
    }

    @Test
    public void testPatient_UpdatePatient_ReturnsPatientResponseDto() throws ResourceNotFoundException {
        //Given: patient, patientRequestDto, and PatientResponseDto
        Patient patient = PatientFixtures.samplePatient();

        PatientRequestDto patientUpdate = PatientFixtures.samplePatientRequestDto();

        PatientResponseDto patientUpdated = PatientFixtures.samplePatientResponseDto();

        //Config
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(modelMapper.map(patientUpdate, Patient.class)).thenReturn(patient);
        when(modelMapper.map(patient, PatientResponseDto.class)).thenReturn(patientUpdated);

        PatientResponseDto result = patientService.updatePatient(patientUpdate, patient.getId());

        //assert
        assertNotNull(result);
        assertEquals(patientUpdated, result);

        //verify invocations
        verify(patientRepository, times(1)).findById(patient.getId());
        verify(patientRepository, times(1)).save(patient);
        verify(modelMapper).map(patientUpdate, Patient.class);
        verify(modelMapper).map(patient, PatientResponseDto.class);
    }

    @Test
    public void testPatient_DeleteById_ReturnsNothing() throws ResourceNotFoundException {

        //Given: obejeto patient y ID
        Patient patient = new Patient(1L, "John", "Doe", "12345678", "patient-001", "john.doe@gmail.com", "password123", null, "555-1234");

        PatientResponseDto expectedResponse = PatientFixtures.samplePatientResponseDto();

        // Configuramos el comportamiento del repositorio:
        // 1. `findById` devuelve el `Patient` antes de la eliminación.
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));

        //2. `findById` requiere un mapeo de patient a patientResponseDto
        when(modelMapper.map(patient, PatientResponseDto.class)).thenReturn(expectedResponse);

        // 3. `deleteById` no hace nada cuando se le llama (ya que es `void`)
        doNothing().when(patientRepository).deleteById(patient.getId());

        //llamar al servicio
        patientService.deletePatientById(patient.getId());

        // Assert:
        // 1. Verificamos que `deleteById` fue llamado exactamente una vez con el ID especificado.
        verify(patientRepository, times(1)).deleteById(patient.getId());
        // 2. Verificamos que `findById` fue llamado para comprobar si el paciente existía antes de eliminarlo.
        verify(patientRepository, times(1)).findById(patient.getId());
        // 3. Verificamos que `modelMapper` fue llamado para mapear de patient a patientResponseDto
        verify(modelMapper, times(1)).map(patient, PatientResponseDto.class);
    }
}
