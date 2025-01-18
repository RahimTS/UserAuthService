package rahim.learning.userauthservice.controllers;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rahim.learning.userauthservice.dtos.LoginRequest;
import rahim.learning.userauthservice.dtos.SignupRequest;
import rahim.learning.userauthservice.dtos.UserDto;
import rahim.learning.userauthservice.exceptions.PasswordMismatchException;
import rahim.learning.userauthservice.exceptions.UserAlreadyExistsException;
import rahim.learning.userauthservice.exceptions.UserNotRegisteredException;
import rahim.learning.userauthservice.models.User;
import rahim.learning.userauthservice.services.IAuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequest signupRequest) {
        try {
            User user = authService.signup(signupRequest.getEmail(), signupRequest.getPassword());
            return new ResponseEntity<>(from(user), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return new ResponseEntity<>(from(user), HttpStatus.OK);
        } catch (UserNotRegisteredException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (PasswordMismatchException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}
