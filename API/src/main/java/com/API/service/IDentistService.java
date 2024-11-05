package com.API.service;

import com.API.dto.dtoRequest.DentistRequestDto;
import com.API.dto.dtoResponse.DentistResponseDto;
import com.API.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IDentistService {

    DentistResponseDto saveDentist(DentistRequestDto dentistRequestDto);

    List<DentistResponseDto> findAll();

    Optional<DentistResponseDto> findDentistById(Long id);

    Optional<DentistResponseDto> findDentistByBadgeNumber(String badgeNumber);

    DentistResponseDto updateDentist(DentistRequestDto dentistRequestDto, Long id) throws ResourceNotFoundException;

    void deleteDentistById(Long id) throws ResourceNotFoundException;
}
