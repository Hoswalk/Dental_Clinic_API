package com.API.controller;

import com.API.dto.dtoRequest.DentistRequestDto;
import com.API.dto.dtoResponse.DentistResponseDto;
import com.API.exception.ResourceNotFoundException;
import com.API.mockData.DentistFixtures;
import com.API.persistence.entities.userImpl.Dentist;
import com.API.service.impl.DentistServiceImpl;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DentistControllerTest {

    @Mock
    DentistServiceImpl dentistService;

    @InjectMocks
    DentistController dentistController;

    private Dentist savedDentist;
    private DentistRequestDto dentistRequestDto;
    private DentistResponseDto dentistResponseDto;

    @BeforeEach
    public void setUp(){

        // Initializing dentist types mocks
        savedDentist = DentistFixtures.sampleDentist();
        dentistRequestDto = DentistFixtures.sampleDentistRequestDto();
        dentistResponseDto = DentistFixtures.sampleDentistResponseDto();

        // Verify is not null
        assertNotNull(savedDentist, "savedDentist is not null");
        assertNotNull(dentistRequestDto, "dentistRequestDto is not null");
        assertNotNull(dentistResponseDto, "dentistResponseDto is not null");
    }

    @Test
    public void testDentistController_SaveDentist_ReturnsDentistResponseDto(){
        // Given: patient types are configured in the setup

        // Config for mock service
        when(dentistService.saveDentist(any(DentistRequestDto.class))).thenReturn(dentistResponseDto);

        // Act - Controller method test
        ResponseEntity<DentistResponseDto> response = dentistController.saveDentist(dentistRequestDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dentistResponseDto.getId(), response.getBody().getId());
    }

    @Test
    public void testDentistController_FindAll_ReturnsListDentistResponseDto(){
        // Given: patient types are configured in the setup

        // List
        List<DentistResponseDto> dentistResponseDtoList = new ArrayList<>();
        dentistResponseDtoList.add(dentistResponseDto);

        // When - Config mock service
        when(dentistService.findAll()).thenReturn(dentistResponseDtoList);

        // Act - Controller method test
        ResponseEntity<List<DentistResponseDto>> response = dentistController.findAllDentist();

        // Assert
        assertNotNull(response);
        assertFalse(response.getBody().isEmpty());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDentistController_FindById_ReturnsDentistResponseDto(){
        // Given: patient types are configured in the setup

        // When - Config mock service
        when(dentistService.findDentistById(dentistResponseDto.getId())).thenReturn(Optional.ofNullable(dentistResponseDto));

        // Act - Controller method test
        ResponseEntity<?> response = dentistController.findById(dentistResponseDto.getId());

        // Aux to see the body response
        Optional<DentistResponseDto> responseBody = (Optional<DentistResponseDto>) response.getBody();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(responseBody.isPresent());
        assertEquals(dentistResponseDto.getId(), responseBody.get().getId());
    }

    @Test
    public void testDentistController_FindByBadgeNumber_ReturnsDentistResponseDto(){
        // Given: patient types are configured in the setup

        // When - Config mock service
        when(dentistService.findDentistByBadgeNumber(dentistResponseDto.getBadgeNumber())).thenReturn(Optional.ofNullable(dentistResponseDto));

        // Act - Controller method test
        ResponseEntity<?> response = dentistController.findByBadgeNumber(dentistResponseDto.getBadgeNumber());

        // Aux to see the body response
        Optional<DentistResponseDto> responseBody = (Optional<DentistResponseDto>) response.getBody();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(responseBody.isPresent());
        assertEquals(dentistResponseDto.getBadgeNumber(), responseBody.get().getBadgeNumber());
    }

    @Test
    public void testDentistController_UpdateDentist_ReturnsDentistResponseDto() throws ResourceNotFoundException {
        // Given: patient types are configured in the setup

        // When - Config mock service
        when(dentistService.updateDentist(dentistRequestDto, dentistResponseDto.getId())).thenReturn(dentistResponseDto);

        // Act - Controller method test
        ResponseEntity<DentistResponseDto> response = dentistController.updateDentist(dentistRequestDto, dentistResponseDto.getId());

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John", response.getBody().getName());
    }

    @Test
    public void testDentistController_DeleteById_ReturnsNothing() throws ResourceNotFoundException {
        // Given: patient types are configured in the setup

        // When - Config mock service
        doNothing().when(dentistService).deleteDentistById(savedDentist.getId());

        // Act - Controller method test
        ResponseEntity<?> response = dentistController.deleteById(savedDentist.getId());

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verify
        verify(dentistService).deleteDentistById(savedDentist.getId());
    }


}
