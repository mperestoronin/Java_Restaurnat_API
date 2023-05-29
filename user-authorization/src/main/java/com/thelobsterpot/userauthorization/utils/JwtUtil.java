package com.thelobsterpot.userauthorization.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long JWT_TOKEN_VALIDITY;

    /***
     * Extracts the username from the JWT token
     * @param token The JWT token
     * @return The username extracted from the token
     ***/
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /***
     * Extracts the expiration date from the JWT token
     * @param token The JWT token
     * @return The expiration date extracted from the token
     ***/
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /***
     * Extracts a claim from the JWT token using the provided claims resolver function
     * @param token The JWT token
     * @param claimsResolver The claims resolver function
     * @param <T> The type of the claim value
     * @return The extracted claim value
     ***/
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /***
     * Extracts all claims from the JWT token
     * @param token The JWT token
     * @return The Claims object containing all the claims from the token
     ***/
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    /***
     * Checks if the JWT token is expired
     * @param token The JWT token
     * @return True if the token is expired, false otherwise
     ***/
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /***
     * Generates a JWT token for the given UserDetails object
     * @param userDetails The UserDetails object representing the user
     * @return The generated JWT token
     ***/
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /***
     * Creates a JWT token with the specified claims and subject
     * @param claims The claims to include in the token
     * @param subject The subject (username) of the token
     * @return The created JWT token
     ***/
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /***
     * Validates the JWT token
     * @param token The JWT token to validate
     * @param userDetails The UserDetails object representing the user
     * @return True if the token is valid, false otherwise
     ***/
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
