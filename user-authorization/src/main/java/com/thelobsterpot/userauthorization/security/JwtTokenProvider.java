package com.thelobsterpot.userauthorization.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

/***
 * Provides JWT tokens
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;

    /***
     * Generate a JWT token based on the username and role
     * @param username user's username
     * @param role user's role (e.x. manager, admin, customer)
     * @return The generated JWT token
     ***/
    public String generateToken(String username, String role) {
        UserDetails userDetails = User.withUsername(username).password("").roles(role).build();
        // Create a UserDetails object with the username, empty password, and role
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        // Build and return the JWT token
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /***
     * Extracts the username from the JWT token
     * @param token The JWT token
     * @return The username extracted from the token
     ***/
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /***
     * Validates the JWT token
     * @param authToken The JWT token to validate
     * @return True if the token is valid, false otherwise
     ***/
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            logger.error("Invalid JWT token", ex);
        }

        return false;
    }
}
