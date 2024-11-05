package com.API.persistence.entities.userImpl;

import com.API.persistence.entities.User;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
@DiscriminatorValue("PATIENTS")
public class Patient extends User {

}
