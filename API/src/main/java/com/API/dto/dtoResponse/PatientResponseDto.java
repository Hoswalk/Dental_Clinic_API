package com.API.dto.dtoResponse;

import com.API.persistence.entities.Address;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class PatientResponseDto extends UserResponseDto{

    public PatientResponseDto(Long id, String name, String lastName, String badgeNumber, String email, Address address, String phone) {
        super(id, name, lastName, badgeNumber, email, address, phone);
    }
}
