package rahim.learning.userauthservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rahim.learning.userauthservice.dtos.UserDto;
import rahim.learning.userauthservice.models.User;
import rahim.learning.userauthservice.services.UserService;

@RestController
@RequestMapping("/api/v1/auth/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserDetails(id);
        if (user == null) return null;
        return from(user);
    }

     UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        // userDto.setRoles(user.getRoles());
        return userDto;
     }
}
