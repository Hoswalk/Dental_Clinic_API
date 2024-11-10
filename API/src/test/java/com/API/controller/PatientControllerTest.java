package com.API.controller;

import com.API.dto.dtoRequest.PatientRequestDto;
import com.API.dto.dtoResponse.PatientResponseDto;
import com.API.exception.ResourceNotFoundException;
import com.API.mockData.PatientFixtures;
import com.API.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {

    @Mock
    private PatientServiceImpl patientService;

    @InjectMocks
    private PatientController patientController;

    @Test
    public void test_Controller_SaveUser_Return_SavedPatientResponseDto(){
        //Given
        PatientRequestDto patientRequestDto = new PatientRequestDto("Jonh", "Doe", "john@gmail.com", "1234523", null, "");

        PatientResponseDto patientResponseDto = new PatientResponseDto(1L, "John", "Doe", "patient-02523", "john@gmail.com", null, "");

        //When
        when(patientService.savePatient(any(PatientRequestDto.class))).thenReturn(patientResponseDto);

        //Act
        ResponseEntity<PatientResponseDto> response = patientController.savePatient(patientRequestDto);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(patientResponseDto.getId(), response.getBody().getId());
    }

    @Test
    public void testFindAll(){
        //Given
        List<PatientResponseDto> patientResponseDtoList = PatientFixtures.patientResponseDtoList();

        //When
        when(patientService.findAll()).thenReturn(patientResponseDtoList);

        //Act
        ResponseEntity<List<PatientResponseDto>> response = patientController.findAllPatient();

        //Assert
        assertNotNull(response);
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void testFindById(){
        //Given
        PatientResponseDto patientResponseDto = new PatientResponseDto(1L, "John", "Doe", "patient-02523", "john@gmail.com", null, "");

        //When
        when(patientService.findPatientById(patientResponseDto.getId())).thenReturn(Optional.of(patientResponseDto));

        //Act
        ResponseEntity<?> response = patientController.findById(patientResponseDto.getId());

        //Se crea para poder desenvolver el body de la respuesta y asi poder verificar el id
        Optional<PatientResponseDto> responseBody = (Optional<PatientResponseDto>) response.getBody();

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(responseBody.isPresent());
        assertEquals(1L, responseBody.get().getId());
    }

    @Test
    public void testFindByBadgeNumber(){
        //Given
        PatientResponseDto patientResponseDto = new PatientResponseDto(1L, "John", "Doe", "patient-02523", "john@gmail.com", null, "");

        //When
        when(patientService.findPatientByBadgeNumebr(patientResponseDto.getBadgeNumber())).thenReturn(Optional.of(patientResponseDto));

        //Act
        ResponseEntity<?> response = patientController.findByBadgeNumber(patientResponseDto.getBadgeNumber());

        Optional<PatientResponseDto> responseBody = (Optional<PatientResponseDto>) response.getBody();

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("patient-02523", responseBody.get().getBadgeNumber());
    }

    @Test
    public void testUpdate() throws ResourceNotFoundException {
        //Given
        PatientRequestDto patientRequestDto = new PatientRequestDto("Jonh", "Doe", "john@gmail.com", "1234523", null, "");

        PatientResponseDto patientResponseDto = new PatientResponseDto(1L, "Jonh", "Doe", "patient-02523", "john@gmail.com", null, "");

        //When
        when(patientService.updatePatient(patientRequestDto, patientResponseDto.getId())).thenReturn(patientResponseDto);

        //Act
        ResponseEntity<PatientResponseDto> response = patientController.update(patientRequestDto, patientResponseDto.getId());

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Jonh", response.getBody().getName());
    }

    @Test
    public void testDeleteById() throws ResourceNotFoundException {
        //Given
        Long patientId = 1L;

        //When
        doNothing().when(patientService).deletePatientById(patientId);

        //Act
        ResponseEntity<?> response = patientController.deleteById(patientId);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        //Verify
        verify(patientService).deletePatientById(patientId);
    }

}
