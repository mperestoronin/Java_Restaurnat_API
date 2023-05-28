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

    // User registration
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

//    // User registration
//    @GetMapping("/register")
//    public ResponseEntity<User> register() {
//        User user = new User(1,"Oleg", "oleg@gmail.com", "Azjheprt1733has5kcxA!Azjheprt1733has5kcxA!Azjheprt1733has5kcxA!Azjheprt1733has5kcxA!", "admin");
//        User registeredUser = userService.registerUser(user);
//        return ResponseEntity.ok(registeredUser);
//    }

    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }


    // User login
    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username, @RequestParam String password) {
        String token = userService.loginUser(username, password);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return ResponseEntity.ok(tokenMap);
    }

//    @GetMapping("/login")
//    public ResponseEntity<Map<String, String>> login() {
//        String username = "Oleg";
//        String password = "Azjheprt1733has5kcxA!Azjheprt1733has5kcxA!Azjheprt1733has5kcxA!Azjheprt1733has5kcxA!!";
//        String token = userService.loginUser(username, password);
//        Map<String, String> tokenMap = new HashMap<>();
//        tokenMap.put("token", token);
//        return ResponseEntity.ok(tokenMap);
//    }

    // Get user information
    @GetMapping("/me")
    public ResponseEntity<User> getUserInfo(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is missing or invalid.");
        }
        String token = authHeader.substring(7);
        String username = tokenProvider.getUsernameFromJWT(token);
        User user = userService.findUser(username);
        return ResponseEntity.ok(user);
    }
}

