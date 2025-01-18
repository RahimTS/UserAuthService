package rahim.learning.userauthservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rahim.learning.userauthservice.exceptions.PasswordMismatchException;
import rahim.learning.userauthservice.exceptions.UserAlreadyExistsException;
import rahim.learning.userauthservice.exceptions.UserNotRegisteredException;
import rahim.learning.userauthservice.models.Role;
import rahim.learning.userauthservice.models.User;
import rahim.learning.userauthservice.repos.UserRepo;

import java.util.Date;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User signup(String email, String password) throws UserAlreadyExistsException {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setCreatedAt(new Date());
        user.setLastUpdatedAt(new Date());
        Role role = new Role();
        role.setValue("ROLE_USER");

        userRepo.save(user);
        return user;
    }

    @Override
    public User login(String username, String password) throws UserNotRegisteredException, PasswordMismatchException {
        Optional<User> userOptional = userRepo.findByEmail(username);
        if (userOptional.isEmpty()) {
            throw new UserNotRegisteredException("User not found");
        }
        String storedPassword = userOptional.get().getPassword();
        if (!passwordEncoder.matches(password, storedPassword)) {
            throw new PasswordMismatchException("Password mismatch");
        }
        return userOptional.get();
    }
}
