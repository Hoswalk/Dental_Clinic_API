package com.API.persistence.entities.userImpl;

import com.API.persistence.entities.User;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
@DiscriminatorValue("DENTISTS")
public class Dentist extends User {

    private boolean available = true;
    private String unavailableReason;

}
