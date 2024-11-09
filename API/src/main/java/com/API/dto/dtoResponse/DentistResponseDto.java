package com.API.dto.dtoResponse;


import com.API.persistence.entities.Address;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class DentistResponseDto extends UserResponseDto{

    private boolean available;
    private String unavailableReason;

    public DentistResponseDto(Long id, String name, String lastName, String badgeNumber, String email, Address address, String phone) {
        super(id, name, lastName, badgeNumber, email, address, phone);
    }

    public DentistResponseDto() {
    }
}
