package com.API.persistence.entities.userImpl;

import com.API.persistence.entities.Address;
import com.API.persistence.entities.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
@DiscriminatorValue("PATIENTS")
public class Patient extends User {

    public Patient(Long id, String name, String lastName, String dni, String badgeNumber, String email, String password, @Nullable Address address, @Nullable String phone) {
        super(id, name, lastName, dni, badgeNumber, email, password, address, phone);
    }

    public Patient() {
    }
}
