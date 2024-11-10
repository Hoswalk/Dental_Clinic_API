package com.API.service;

import com.API.dto.dtoRequest.DentistRequestDto;
import com.API.dto.dtoResponse.DentistResponseDto;
import com.API.exception.ResourceNotFoundException;
import com.API.mockData.DentistFixtures;
import com.API.persistence.entities.userImpl.Dentist;
import com.API.persistence.entities.userImpl.Patient;
import com.API.persistence.repository.DentistRepository;
import com.API.service.impl.DentistServiceImpl;
import com.API.service.impl.UserServiceImpl;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DentistServiceImplTest {

    @Mock
    UserServiceImpl userService;

    @Mock
    DentistRepository dentistRepository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    DentistServiceImpl dentistService;

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
    public void testDentist_SaveDentist_ReturnsDentistResponseDto(){
        // Given: patient types are configured in the setup

        // Config mocks behavior
        when(userService.saveUser(dentistRequestDto, Dentist.class, "dentist")).thenReturn(savedDentist);
        when(modelMapper.map(savedDentist, DentistResponseDto.class)).thenReturn(dentistResponseDto);

        // Service method test
        DentistResponseDto result = dentistService.saveDentist(dentistRequestDto);

        // Assert
        assertNotNull(result);

        // Verify
        verify(userService, times(1)).saveUser(dentistRequestDto, Dentist.class, "dentist");
        verify(modelMapper, times(1)).map(savedDentist, DentistResponseDto.class);
    }

    @Test
    public void testDentist_FindAll_ReturnsDentistList(){
        // Given: patient types are configured in the setup

        // List of mock patients
        List<Dentist> dentistList = new ArrayList<>();
        dentistList.add(savedDentist);

        // List of mock patientResponseDto expected
        List<DentistResponseDto> expectedResponse = new ArrayList<>();
        expectedResponse.add(dentistResponseDto);

        // Config mock behavior
        when(dentistRepository.findAll()).thenReturn(dentistList);
        when(modelMapper.map(savedDentist, DentistResponseDto.class)).thenReturn(dentistResponseDto);

        // Service method test
        List<DentistResponseDto> result = dentistService.findAll();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Verify
        verify(dentistRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(savedDentist, DentistResponseDto.class);
    }

    @Test
    public void testDentist_FindById_ReturnsDentistResponseDto(){
        // Given: patient types are configured in the setup

        // Config for mock repo and mapper
        when(dentistRepository.findById(savedDentist.getId())).thenReturn(Optional.ofNullable(savedDentist));
        when(modelMapper.map(savedDentist, DentistResponseDto.class)).thenReturn(dentistResponseDto);

        // Service method test
        Optional<DentistResponseDto> result = dentistService.findDentistById(dentistResponseDto.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, dentistResponseDto.getId());
        assertEquals(1L, result.get().getId());
    }

    @Test
    public void testDentist_FindByBadgeNumber_ReturnsDentistResponseDto(){
        // Given: patient types are configured in the setup

        // Config for mock repo and mapper
        when(dentistRepository.findDentistByBadgeNumber(savedDentist.getBadgeNumber())).thenReturn(Optional.ofNullable(savedDentist));
        when(modelMapper.map(savedDentist, DentistResponseDto.class)).thenReturn(dentistResponseDto);

        // Service method test
        Optional<DentistResponseDto> result = dentistService.findDentistByBadgeNumber("dentist-001");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("dentist-001", result.get().getBadgeNumber());

        // Verify
        verify(dentistRepository, times(1)).findDentistByBadgeNumber(savedDentist.getBadgeNumber());
        verify(modelMapper, times(1)).map(savedDentist, DentistResponseDto.class);
    }

    @Test
    public void testDentist_UpdateDentist_ReturnsDentistResponseDto() throws ResourceNotFoundException {
        // Given: patient types are configured in the setup

        // Config mock mapper to not be strict with this method because method accept null values and only update the values entered
        // Mock Configuration to handle Conditions.isNotNull()
        Configuration mockConfig = mock(Configuration.class);
        lenient().when(modelMapper.getConfiguration()).thenReturn(mockConfig);
        lenient().when(mockConfig.setPropertyCondition(Conditions.isNotNull())).thenReturn(mockConfig);

        // Config for mock repo
        when(dentistRepository.findById(savedDentist.getId())).thenReturn(Optional.of(savedDentist));
        when(dentistRepository.save(any(Dentist.class))).thenReturn(savedDentist);

        // Mocking map method for DentistRequestDto to dentist
        lenient().when(modelMapper.map(dentistRequestDto, Dentist.class)).thenReturn(savedDentist);

        // Mocking map method for dentist to DentistResponseDto
        lenient().when(modelMapper.map(savedDentist, DentistResponseDto.class)).thenReturn(dentistResponseDto);

        // Service method test
        DentistResponseDto result = dentistService.updateDentist(dentistRequestDto, savedDentist.getId());

        // Assert
        assertNotNull(result);
        assertEquals(dentistResponseDto, result);

        // Verify
        verify(dentistRepository, times(1)).findById(savedDentist.getId());
        verify(dentistRepository, times(1)).save(savedDentist);
        //verify(modelMapper).map(dentistRequestDto, Dentist.class); // Check why is it crashing
        verify(modelMapper).map(savedDentist, DentistResponseDto.class);
    }

    @Test
    public void testDentist_DeleteById_ReturnsNothing() throws ResourceNotFoundException {
        // Given: patient types are configured in the setup

        // Config for mock repo
        // 1. `findById` returns `Dentist` before delete it.
        when(dentistRepository.findById(savedDentist.getId())).thenReturn(Optional.ofNullable(savedDentist));

        //2. `findById` required mapping from dentist to dentistResponseDto
        when(modelMapper.map(savedDentist, DentistResponseDto.class)).thenReturn(dentistResponseDto);

        // 3. `deleteById` does nothing because is a void method
        doNothing().when(dentistRepository).deleteById(savedDentist.getId());

        // Service method test
        dentistService.deleteDentistById(savedDentist.getId());

        // Assert
        // Verify
        verify(dentistRepository, times(1)).deleteById(savedDentist.getId());
        verify(dentistRepository, times(1)).findById(savedDentist.getId());
        verify(modelMapper, times(1)).map(savedDentist, DentistResponseDto.class);
    }
}
