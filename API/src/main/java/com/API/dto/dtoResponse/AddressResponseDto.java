package com.API.dto.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class AddressResponseDto {

    private Long id;
    private String street;
    private int number;
    private String state;
    private int postalCode;
}
