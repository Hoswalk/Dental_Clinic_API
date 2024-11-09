package com.API.dto.dtoRequest;
import com.API.persistence.entities.Address;
import jakarta.annotation.Nullable;
import lombok.*;

@Getter @Setter
//@AllArgsConstructor
public class PatientRequestDto extends UserRequestDto{

    public PatientRequestDto(String name, String lastName, String email, String password, @Nullable Address address, @Nullable String phone) {
        super(name, lastName, email, password, address, phone);
    }

    public PatientRequestDto() {
    }
}
