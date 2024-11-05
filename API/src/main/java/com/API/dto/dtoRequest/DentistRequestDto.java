package com.API.dto.dtoRequest;
import lombok.*;

@Getter @Setter
public class DentistRequestDto extends UserRequestDto{

    private boolean available = true;
    private String unavailableReason;
}
