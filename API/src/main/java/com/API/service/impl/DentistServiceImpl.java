package com.API.service.impl;

import com.API.dto.dtoRequest.DentistRequestDto;
import com.API.dto.dtoResponse.DentistResponseDto;
import com.API.event.DentistUnavailableEvent;
import com.API.exception.ResourceNotFoundException;
import com.API.persistence.entities.userImpl.Dentist;
import com.API.persistence.repository.DentistRepository;
import com.API.service.IDentistService;
import com.API.utils.JsonPrinter;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DentistServiceImpl implements IDentistService {

    private final Logger LOGGER = LoggerFactory.getLogger(DentistServiceImpl.class);

    private final UserServiceImpl userService;

    private final ApplicationEventPublisher eventPublisher;

    private final DentistRepository dentistRepository;

    private final ModelMapper modelMapper;

    public void markDentistAsUnavailable(Long dentistId, String reason){

        //Get dentist id
        Dentist dentist = dentistRepository.findById(dentistId).orElseThrow(() -> new EntityNotFoundException("Dentist was not found."));

        //Setting unavailability
        dentist.setAvailable(false);
        dentist.setUnavailableReason(reason);

        //Save changes
        dentistRepository.save(dentist);

        //Publishing the event
        DentistUnavailableEvent event = new DentistUnavailableEvent(this, dentistId, reason);
        eventPublisher.publishEvent(event);
    }

    @Override
    public DentistResponseDto saveDentist(DentistRequestDto dentistRequestDto) {

        //Reutilizacion de userService logica
        Dentist savedDentist = userService.saveUser(dentistRequestDto, Dentist.class, "dentist");

        //Log
        LOGGER.info("Dentist saved: {}", JsonPrinter.toString(savedDentist));

        //Mapping el resultado
        return modelMapper.map(savedDentist, DentistResponseDto.class);
    }

    @Override
    public List<DentistResponseDto> findAll() {

        List<DentistResponseDto> dentistResponseDtoList = dentistRepository.findAll()
                .stream()
                .map(dentist -> modelMapper.map(dentist, DentistResponseDto.class))
                .toList();

        LOGGER.info("Dentist's list: {}", JsonPrinter.toString(dentistResponseDtoList));

        return dentistResponseDtoList;
    }

    @Override
    public Optional<DentistResponseDto> findDentistById(Long id) {

        Dentist dentistSearched = dentistRepository.findById(id).orElse(null);
        DentistResponseDto dentistFound = null;

        if (dentistSearched != null){
            dentistFound = modelMapper.map(dentistSearched, DentistResponseDto.class);
            LOGGER.info("Dentist found: {}", JsonPrinter.toString(dentistFound));
        } else LOGGER.error("Dentist not found: {}", JsonPrinter.toString(dentistSearched));

        return Optional.of(dentistFound);
    }

    @Override
    public Optional<DentistResponseDto> findDentistByBadgeNumber(String badgeNumber){

        Dentist dentistSearched = dentistRepository.findDentistByBadgeNumber(badgeNumber).orElse(null);
        DentistResponseDto dentistFound = null;

        if (dentistSearched != null){
            dentistFound = modelMapper.map(dentistSearched, DentistResponseDto.class);
            LOGGER.info("Dentist found.");
        } else LOGGER.error("Dentist was not found");

        return Optional.of(dentistFound);
    }

    @Override
    public DentistResponseDto updateDentist(DentistRequestDto dentistRequestDto, Long id) throws ResourceNotFoundException {

        Dentist dentistUpdate = dentistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dentist not found."));

        // Mapear las propiedades no nulas del DTO a la entidad existente
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(dentistRequestDto, dentistUpdate);

        // Si el dentista está disponible, eliminamos la razón de indisponibilidad // la actualizacion se realiza luego del mapeo de modelmapper para que no se sobreescriva la informacion
        if (dentistUpdate.isAvailable()){
            dentistUpdate.setUnavailableReason(null);
        }

        // Guardar los cambios en la base de datos
        dentistRepository.save(dentistUpdate);

        // Convertir la entidad actualizada a un DTO de respuesta
        DentistResponseDto dentistUpdated = modelMapper.map(dentistUpdate, DentistResponseDto.class);

        return dentistUpdated;
    }

    @Override
    public void deleteDentistById(Long id) throws ResourceNotFoundException {

        if (findDentistById(id).isPresent()){
            dentistRepository.deleteById(id);
            LOGGER.warn("Dentist with id: " + id + " has been deleted.");
        } else {
            throw new ResourceNotFoundException("Dentist with id: " + id + " was not found.");
        }
    }
}
