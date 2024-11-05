package com.API.dto.dtoResponse;


import lombok.*;

@Getter @Setter
public class DentistResponseDto extends UserResponseDto{

    private boolean available;
    private String unavailableReason;
}
