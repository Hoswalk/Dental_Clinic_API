package com.API.dto.dtoRequest;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDto {

    @NotNull(message = "Street cannot be null.")
    @NotBlank(message = "Must add a street name.")
    private String street;

    @Positive(message = "Number cannot be null or less than 0")
    @Digits(integer = 8, fraction = 0, message = "Number cannot be longer than 8 numbers.")
    @NotBlank
    private int number;

    @NotNull(message = "A state must be added.")
    @NotBlank
    private String state;

    @NotNull(message = "A postal code must be added.")
    @NotBlank
    private String postalCode;
}
