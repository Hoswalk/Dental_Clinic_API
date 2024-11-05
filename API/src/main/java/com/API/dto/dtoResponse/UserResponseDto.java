package com.API.dto.dtoResponse;

import com.API.persistence.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String name;
    private String lastName;
    private String badgeNumber;
    private String email;
    private Address address;
    private String phone;
}
