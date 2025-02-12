package rahim.learning.userauthservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Session extends BaseModel {
    private String token;
    @ManyToOne // One user can have multiple sessions
    private User user;

}
