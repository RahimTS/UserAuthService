package rahim.learning.userauthservice.dtos;

import lombok.Getter;
import lombok.Setter;
import rahim.learning.userauthservice.models.Role;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class UserDto {
    private Long id;
    private String email;
    private List<Role> roles = new ArrayList<>();
}
