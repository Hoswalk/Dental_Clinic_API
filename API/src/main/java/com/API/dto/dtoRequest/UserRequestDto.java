package com.API.dto.dtoRequest;

import com.API.persistence.entities.Address;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @Size(min = 2, max = 20, message = "Dentist's name can be from 2 to 20 characteres")
    @NotBlank(message = "Every dentist must have a name")
    @NotNull
    private String name;

    @Size(min = 2, max = 50, message = "Dentist's last name can be from 2 to 50 characteres")
    @NotBlank(message = "Every dentist must have a last name")
    @Pattern(regexp = "^[A-Za-z]{2,20}(\\s[A-Za-z]{2,40})?$", message = "Last name must be between 2 and 40 alphabetic characteres.")
    @Valid
    private String lastName;

    @NotNull(message = "Email cannot be null.")
    @NotBlank(message = "Must add an email.")
    @Email(message = "Email must be valid.")
    private String email;

    @NotNull
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,25}$",
            message = "The password must be between 8 and 25 characters, including at least one uppercase letter, one lowercase letter, one number, and one special character."
    )
    private String password;

    @Nullable
    @Valid
    private Address address;

    @Nullable
    private String phone;
}
