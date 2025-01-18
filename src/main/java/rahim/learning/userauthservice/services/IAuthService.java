package rahim.learning.userauthservice.services;

import rahim.learning.userauthservice.exceptions.PasswordMismatchException;
import rahim.learning.userauthservice.exceptions.UserAlreadyExistsException;
import rahim.learning.userauthservice.exceptions.UserNotRegisteredException;
import rahim.learning.userauthservice.models.User;

public interface IAuthService {
    User signup(String username, String password) throws UserAlreadyExistsException;
    User login(String username, String password) throws UserNotRegisteredException, PasswordMismatchException;
}
