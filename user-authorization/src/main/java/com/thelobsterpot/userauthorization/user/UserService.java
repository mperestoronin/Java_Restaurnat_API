package com.thelobsterpot.userauthorization.user;

import com.thelobsterpot.userauthorization.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpStatus;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    /** Register new user */
    public User registerUser(User user) {
        // Check if the username or email is already taken

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use.");
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken.");
        }
        // Hash the password
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        // Save the user in the repository
        return userRepository.save(user);
    }

    /***
     * Log in a user
     * @param username user's username
     * @param password user's password
     * @return The JWT token for the user
     ***/
    public String loginUser(String username, String password) {
        // Find the user by username
        User user = userRepository.findByUsername(username);
        // Check if the user exists and the password is correct
        if (user != null && passwordEncoder.matches(password, user.getPasswordHash())) {
            // Generate and return a JWT token for the user
            return tokenProvider.generateToken(username, user.getRole());
        } else {
            // Throw an exception if the username or password is invalid
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username/password.");
        }
    }

    /***
     * Get user information by username
     * @param username user's
     * @return user's information
     ***/
    public User findUser(String username) {
        // Find the user by username
        User user = userRepository.findByUsername(username);
        if(user != null) {
            // Return the user if found
            return user;
        } else {
            // Throw an exception if the user is not found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
    }

}
