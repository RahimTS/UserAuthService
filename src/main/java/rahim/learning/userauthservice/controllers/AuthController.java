package rahim.learning.userauthservice.controllers;

import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import rahim.learning.userauthservice.dtos.LoginRequest;
import rahim.learning.userauthservice.dtos.SignupRequest;
import rahim.learning.userauthservice.dtos.UserDto;
import rahim.learning.userauthservice.dtos.ValidateTokenDto;
import rahim.learning.userauthservice.exceptions.PasswordMismatchException;
import rahim.learning.userauthservice.exceptions.UnauthorizedException;
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
            Pair<User,String> response = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.SET_COOKIE, response.b);
            return new ResponseEntity<>(from(response.a),headers, HttpStatus.OK);
        } catch (UserNotRegisteredException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (PasswordMismatchException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/validateToken")
    public boolean validateToken(@RequestBody ValidateTokenDto validateTokenDto) throws UnauthorizedException {
        Boolean result = authService.validateToken(validateTokenDto.getToken(), validateTokenDto.getUserId());

        if(!result) {
            throw new UnauthorizedException("Please Login again");
        }
        return true;
    }

    UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        //userDto.setRoles(user.getRoles());
        return userDto;
    }
}
