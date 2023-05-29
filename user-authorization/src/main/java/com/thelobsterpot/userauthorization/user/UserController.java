package com.thelobsterpot.userauthorization.user;

import com.thelobsterpot.userauthorization.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    /***
     * Registration
     * @param user user that needs to be registered (accepted as JSON)
     * @return creates a response with an HTTP status code of 200 (OK) and the registered user as the response body.
     */
    // User registration
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }


    /***
     * Login
     * @param username users name
     * @param password users password
     * @return creates a response with an HTTP status code of 200 (OK) and the token as the response body.
     */
    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username, @RequestParam String password) {
        String token = userService.loginUser(username, password);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return ResponseEntity.ok(tokenMap);
    }


    /***
     * Viewing information about the user
     * @param request token is required to view the information. Token can be gotten after logging in
     * @return creates a response with an HTTP status code of 200 (OK) and the user as a JSON (info about the user) as the response body.
     */
    // Get user information
    @GetMapping("/me")
    public ResponseEntity<User> getUserInfo(HttpServletRequest request) {
        // Check for authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is missing or invalid.");
        }
        // Extract the token
        String token = authHeader.substring(7);
        // Get username from the token
        String username = tokenProvider.getUsernameFromJWT(token);
        // Find the user by username
        User user = userService.findUser(username);
        return ResponseEntity.ok(user);
    }
}

