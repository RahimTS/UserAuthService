package rahim.learning.userauthservice.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rahim.learning.userauthservice.exceptions.PasswordMismatchException;
import rahim.learning.userauthservice.exceptions.UserAlreadyExistsException;
import rahim.learning.userauthservice.exceptions.UserNotRegisteredException;
import rahim.learning.userauthservice.models.Role;
import rahim.learning.userauthservice.models.User;
import rahim.learning.userauthservice.repos.UserRepo;

import javax.crypto.SecretKey;
import java.util.*;

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
    public Pair<User, String> login(String username, String password) throws UserNotRegisteredException, PasswordMismatchException {
        Optional<User> userOptional = userRepo.findByEmail(username);
        if (userOptional.isEmpty()) {
            throw new UserNotRegisteredException("User not found");
        }
        String storedPassword = userOptional.get().getPassword();
        if (!passwordEncoder.matches(password, storedPassword)) {
            throw new PasswordMismatchException("Password mismatch");
        }

        Map<String, Object> payload = new HashMap<>();
        Long nowInMillis = System.currentTimeMillis();
        payload.put("iat", nowInMillis);
        payload.put("exp", nowInMillis + (nowInMillis / 1000));
        payload.put("userId", userOptional.get().getId());
        payload.put("iss", "scaler");
        payload.put("scope", userOptional.get().getRoles());

        MacAlgorithm algorithm = Jwts.SIG.HS256;
        SecretKey secretKey = algorithm.key().build();
        String token = Jwts.builder().claims(payload).signWith(secretKey).compact();
        return new Pair<>(userOptional.get(), token);
    }
}
