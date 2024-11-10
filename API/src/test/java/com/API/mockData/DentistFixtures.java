package com.API.mockData;

import com.API.dto.dtoRequest.DentistRequestDto;
import com.API.dto.dtoResponse.DentistResponseDto;
import com.API.persistence.entities.userImpl.Dentist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DentistFixtures {

    private static Logger LOGGER = LoggerFactory.getLogger(DentistFixtures.class);

    public static Dentist sampleDentist(){

        return new Dentist(1L, "John", "Doe", null, "dentist-001", "john.doe@gmail.com", "password123", null, null);
    }

    public static DentistRequestDto sampleDentistRequestDto(){
        return new DentistRequestDto("John", "Doe", "john.doe@gmail.com", "password123", null, null);
    }

    public static DentistResponseDto sampleDentistResponseDto(){
        return new DentistResponseDto(1L, "John", "Doe", "dentist-001", "john.doe@gmail.com", null, null);
    }
}
