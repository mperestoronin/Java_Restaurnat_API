package com.thelobsterpot.userauthorization.user.security;

import com.thelobsterpot.userauthorization.user.User;
import com.thelobsterpot.userauthorization.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find the user by username in the repository
        User user = userRepository.findByUsername(username);
        // Check if the user exists
        if (user == null) {
            // Throw an exception if the user is not found
            throw new UsernameNotFoundException("User not found");
        }
        // Create and return a User object from Spring Security's UserDetails interface
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                new ArrayList<>()
        );
    }
}