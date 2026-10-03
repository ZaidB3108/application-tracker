package com.basri.applicationtracker.profile;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "full_name", nullable = false) private String fullName;
    @Column(nullable = false, unique = true) private String email;
    @Column(name = "password_hash", nullable = false) private String passwordHash;
    @Column(name = "created_at", insertable = false, updatable = false) private LocalDateTime createdAt;
    protected UserAccount() { }
    UserAccount(String fullName, String email, String passwordHash) { this.fullName = fullName; this.email = email; this.passwordHash = passwordHash; }
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
