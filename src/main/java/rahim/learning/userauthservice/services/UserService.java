package rahim.learning.userauthservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rahim.learning.userauthservice.models.User;
import rahim.learning.userauthservice.repos.UserRepo;

import java.util.Optional;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepo userRepo;

    @Override
    public User getUserDetails(Long id) {
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent()) {
            return user.get();
        }
        return null;
    }
}
