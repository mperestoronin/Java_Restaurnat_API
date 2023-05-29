package com.thelobsterpot.userauthorization.user;

import org.hibernate.validator.constraints.Email;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "`user`")
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private int id;

    /***
     * Unique username column, not nullable
     ***/
    @Column(unique = true, nullable = false)
    private String username;

    /***
     * Unique email column, not nullable, validated as an email
     ***/
    @Column(unique = true, nullable = false)
    @Email
    private String email;

    /***
     * Column to store password hash, not nullable
     ***/
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    /***
     * Column to store the role of the user, not nullable
     ***/
    @Column(nullable = false)
    private String role;

    /***
     * Column to store the creation timestamp
     ***/
    @Column(name = "created_at")
    private Timestamp createdAt;

    /***
     * Column to store the update timestamp
     ***/
    @Column(name = "updated_at")
    private Timestamp updatedAt;


     //Constructors

    public User(int id, String username, String email, String passwordHash, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public User(String username, String email, String passwordHash, String role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public User() {
    }


     // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    /***
     * PrePersist and PreUpdate hooks to set the creation and update timestamps
     ***/
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    /***
     * Override toString() method to provide a string representation of the object
     ***/
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
